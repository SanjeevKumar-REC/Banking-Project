

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
 * Servlet implementation class TransactionHistory
 */
@WebServlet("/TransactionHistory")
public class TransactionHistory extends HttpServlet {
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
		
		HttpSession session=request.getSession(false);
		long acc=(long)session.getAttribute("acc");
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select * from Transaction_History where account=(?)");
			ps.setLong(1, acc);
			rs=ps.executeQuery();
			pw.println("<center><h1 color='green'>Your Transaction History is:</h1>");
			pw.println("<table text-align='center'><tr><th>Date</th><th>Particulars</th><th>Amt. Withdrawn</th><th>Amt. Deposited</th><th>Balance</th></tr>");
				while(rs.next()) {
				pw.println("<tr><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getDouble(4)+"</td><td>"+rs.getDouble(5)+"</td><td>"+rs.getDouble(6)+"</td><tr>");
				}
			
			pw.println("</table></center>");
			RequestDispatcher rd=request.getRequestDispatcher("TransactionHistory.html");
			rd.include(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
