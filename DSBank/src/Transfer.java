

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Transfer
 */
@WebServlet("/Transfer")
public class Transfer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	Connection cn;
    PreparedStatement ps;
    ResultSet rs;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		HttpSession hs=request.getSession(false);
		long acc=(long)hs.getAttribute("acc");
		double transfer=Double.parseDouble(request.getParameter("transfer"));
		long acc1=Long.parseLong(request.getParameter("transferac"));
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select balance from bank where account=(?)");
			ps.setLong(1, acc);
			
			ResultSet rs=ps.executeQuery();
			rs.next();
			double balance=rs.getDouble(1);
			
			if(transfer<=balance)
			{
				ps=cn.prepareStatement("update bank set balance=(?) where account=(?) ");
				double bal=(balance-transfer);
				ps.setDouble(1, bal);
				ps.setLong(2, acc);
				ps.executeUpdate();
				
				ps=cn.prepareStatement("update bank set balance=(balance+?) where account=(?)");
				ps.setDouble(1, transfer);
				ps.setLong(2, acc1);
				ps.executeUpdate();
				
				ps=cn.prepareStatement("insert into Transaction_History values(?,?,?,?,?,?)");
				Date dd=Calendar.getInstance().getTime();
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				ps.setLong(1, acc);
				ps.setString(2, df.format(dd));
				ps.setString(3,("U Transfer This"+ Long.toString(acc1)));
				ps.setDouble(4, transfer);
				ps.setDouble(5, (double)0.0);
				ps.setDouble(6, bal);
				ps.executeUpdate();
				
				pw.println("<center><h1>Transaction Successfully done........</h1>");
				pw.println("<h2>Total Remaining amount is : "+bal+" </h2></center>");
				RequestDispatcher rd=request.getRequestDispatcher("Transfer.html");
				rd.include(request, response);
			}else {
				pw.println("<h1 color='red'>Insufficient Balance.!!!!!!!!</h1>");
				RequestDispatcher rd=request.getRequestDispatcher("Transfer.html");
				rd.include(request, response);
			}
			cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
