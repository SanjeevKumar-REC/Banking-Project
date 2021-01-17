

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Deposit
 */
@WebServlet("/Deposit")
public class Deposit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    

	Connection cn;
	PreparedStatement ps;
	ResultSet rs;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		
		HttpSession hs=request.getSession(false);
		long acc=(long)hs.getAttribute("acc");
		double deposit=Double.parseDouble(request.getParameter("deposit"));
		double balance=0.0;
		int i=0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select balance from bank where account=(?)");
			ps.setLong(1, acc);
			rs=ps.executeQuery();
			rs.next();
			balance=rs.getDouble(1);
			
			
			
			
			
			ps=cn.prepareStatement("update bank set balance=(balance+?) where account=(?)");
			
			ps.setDouble(1,deposit);
			ps.setLong(2, acc);
			i=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(i==1)
		{
			pw.println("<h1>Deposit Successfully......</h1>");
			try {
				ps=cn.prepareStatement("insert into Transaction_History values(?,?,?,?,?,?)");
				Date dd=Calendar.getInstance().getTime();
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				ps.setLong(1, acc);
				ps.setString(2, df.format(dd));
				ps.setString(3, "BY CASH Deposited");
				ps.setDouble(4, (double)0.0);
				ps.setDouble(5, deposit);
				ps.setDouble(6, balance+deposit);
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		RequestDispatcher rd=request.getRequestDispatcher("Deposit.html");
		rd.include(request, response);
		}else
		{
			pw.println("<h1>Deposit Faild</h1>");
			pw.println("<h1>"+acc+" </h1>");
			RequestDispatcher rd=request.getRequestDispatcher("Deposit.html");
			rd.include(request, response);
		}
		
	}

}
