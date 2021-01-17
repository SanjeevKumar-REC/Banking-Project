

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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection cn;
	PreparedStatement ps;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		
		
		String pass=request.getParameter("pass");

	long user=Long.parseLong(request.getParameter("user"));
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select * from bank where account=(?) and password=(?)");
			ps.setString(2, pass);
			ps.setLong(1, user);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{  HttpSession hs=request.getSession();
				hs.setAttribute("acc", rs.getLong(2));
				RequestDispatcher rd=request.getRequestDispatcher("Menu.html");
				rd.forward(request, response); 
		        
			
			}else {
				pw.println("<b style='color:red'>You are unauthorized user</b>");
				RequestDispatcher rd=request.getRequestDispatcher("Home.html");
				rd.include(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
	}
		
		

	}
	
}
