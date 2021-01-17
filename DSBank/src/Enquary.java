

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Enquary
 */
@WebServlet("/Enquary")
public class Enquary extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection cn;
	PreparedStatement ps;
	ResultSet rs;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		HttpSession hs=request.getSession(false);
		long acc=(long)hs.getAttribute("acc");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost");
			ps=cn.prepareStatement("select balance from bank where account=?");
			ps.setLong(1, acc);
			rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
		if(rs.next())
		{
		
		pw.println("<h1 bgcolor='red'>Total Balance is : "+rs.getDouble(1)+"</h1>");
		RequestDispatcher rd=request.getRequestDispatcher("Enquary.html");
		rd.include(request, response);
		}
		}catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}

}
