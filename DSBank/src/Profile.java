

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
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
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
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select * from bank where account=(?)");
			ps.setLong(1, acc);
			
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				pw.println("<p>Name is : '"+rs.getString(3)+"'</p>");
				pw.println("<p>Account Number is : '"+rs.getLong(2)+"'</p>");
				pw.println("<p>Aadher Number is : '"+rs.getLong(4)+"'</p>");
				pw.println("<p>PAN Card Number is : '"+rs.getString(5)+"'</p>");
				pw.println("<p>Mobile Number is : '"+rs.getString(6)+"'</p>");
				pw.println("<p>Email ID is : '"+rs.getString(7)+"'</p>");
				pw.println("<p>Date of Birth is : '"+rs.getDate(8)+"'</p>");
				pw.println("<p>Gender is : '"+rs.getString(9)+"'</p>");
				pw.println("<p>Address is : '"+rs.getString(10)+"'</p>");
				pw.println("<p>Countery is : '"+rs.getString(11)+"'</p>");
				pw.println("<p>State is : '"+rs.getString(12)+"'</p>");
				pw.println("<p>City is : '"+rs.getString(13)+"'</p>");
				pw.println("<p>PIN Code is : '"+rs.getLong(14)+"'</p>");
				RequestDispatcher rd=request.getRequestDispatcher("Profile.html");
				rd.include(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
