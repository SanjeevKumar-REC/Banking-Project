

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    Connection cn;
    PreparedStatement ps;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw=response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost");
			ps=cn.prepareStatement("insert into bank values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try
		{
			String pass=request.getParameter("pass");
			String name=request.getParameter("name");
			long aad=Long.parseLong(request.getParameter("aad"));
			String pan=request.getParameter("pan");
			String phone=request.getParameter("phone");
			String email=request.getParameter("email");
			String dob=request.getParameter("dob");
			System.err.println(dob);
			String gender=request.getParameter("gender");
			String add=request.getParameter("add");
			String countery=request.getParameter("countery");
			String state=request.getParameter("state");
			String city=request.getParameter("city");
			double balance=0.00;
			long pin=Long.parseLong(request.getParameter("pin"));
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date udob=sdf.parse(dob);
			java.sql.Date sdob=new java.sql.Date(udob.getTime());
			System.err.println(sdob);
			Random r=new Random();
	        String s="0123456789";
	        StringBuffer sb= new StringBuffer();
	        for(int i=1;i<=7;i++)
	        {
	            sb.append(s.charAt(r.nextInt(s.length())));
	        }
	        String acc1=4687+sb.toString();
	        
	        long acc=Long.parseLong(acc1);
			
	        ps.setString(1,pass);
	        ps.setLong(2,acc);
	        ps.setString(3,name);
	        ps.setLong(4, aad);
	        ps.setString(5, pan);
	        ps.setString(6, phone);
	        ps.setString(7, email);
	        ps.setDate(8, sdob);
	        ps.setString(9, gender);
	        ps.setString(10, add);
	        ps.setString(11, countery);
	        ps.setString(12, state);
	        ps.setString(13, city);
	        ps.setLong(14, pin);
	        ps.setDouble(15, (double) balance);
	        
	        
	        
	        
	      int i=ps.executeUpdate();
	      
	      if(i==1)
	      {
	    	  pw.println("<html><body bgcolor='cyan'><font color='black' size='5'><center>");
	    	  pw.println("<h1><b>Mr./Mrs'"+name+"' Successfully registered....</b></h1>");
	    	  pw.println("<br><h3 bgcolor='black'>Account Number is: '"+acc+"' And Password is: '"+pass+"'</h3>");
	    	  pw.println("<br><h2 color='yellow'> Your account number is your USER ID</h2>");
	    	  pw.println("<table border='1' width='40%' height='20%'>");
	    	  pw.println("<tr><td align='center'><font color='red' size='5'>");
				pw.println("<p>Go to the login page click?<a href='index.html'>here</a></p>");
				pw.println("</font></td></tr>");

				pw.println("<tr><td align='center'><font color='red' size='5'>");
				pw.println("<p>New Account Opening click?<a href='Register.html'>here</a></p>");
				pw.println("</font></td></tr>");
				pw.println("</center></font></body></html>");
				
	    	  
	      }
		}catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}

}