
 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
 
public class GeneratePDF {
	
	private static Font TIME_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
 
	/**
	 * @param args
	 *
	 */
	static	long acc;
	public void execute(HttpServletRequest request)
	{
		HttpSession hs=request.getSession(false);
		acc=(long)hs.getAttribute("acc");
	}
	public static Document createPDF(String file) {
 
		Document document = null;
 
		try {
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
 
			addMetaData(document);
 
			addTitlePage(document);
 
			createTable(document);
 
			document.close();
 
		} catch (FileNotFoundException e) {
 
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
 
	}
 
	private static void addMetaData(Document document) {
		document.addTitle("Generate PDF Histery");
		document.addSubject("Generate PDF Histery");
		document.addAuthor("Bank");
		document.addCreator("JTian");
	}
 
	private static void addTitlePage(Document document)
			throws DocumentException {
 
		Paragraph preface = new Paragraph();
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("PDF Transaction_History", TIME_ROMAN));
		
		Connection cn;
		PreparedStatement ps;
		ResultSet rs=null;
		String name="";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select name from bank where account=(?)");
			ps.setLong(1, acc);
			rs=ps.executeQuery();
			rs.next();
			name=rs.getString(1);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
 
		 
		creteEmptyLine(preface, 1);
		preface.add(new Paragraph("Dear Mr/Mrs:- "+name, TIME_ROMAN));
		
		creteEmptyLine(preface, 1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		preface.add(new Paragraph("Transaction_History created on "
				+ simpleDateFormat.format(new Date()), TIME_ROMAN_SMALL));
		document.add(preface);
 
	}
 
	private static void creteEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
 
	private static void createTable(Document document) throws DocumentException {
		Paragraph paragraph = new Paragraph();
		creteEmptyLine(paragraph, 2);
		document.add(paragraph);
		PdfPTable table = new PdfPTable(6);
		
		//PDF download0long acc;
		
		
		Connection cn;
		PreparedStatement ps;
		ResultSet rs=null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn=DriverManager.getConnection("jdbc:oracle:thin:system/8484448@localhost:1521:XE");
			ps=cn.prepareStatement("select * from Transaction_History where account=(?)");
			ps.setLong(1, acc);
			rs=ps.executeQuery();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
 
		PdfPCell c1 = new PdfPCell(new Phrase("SL.no"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
 
		c1 = new PdfPCell(new Phrase("Date"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
 
		c1 = new PdfPCell(new Phrase("Particulars"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Amt. Withdrawn"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Amt. Deposited"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		c1 = new PdfPCell(new Phrase("Balance"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		
		table.setHeaderRows(1);
		try {
			int i=1;
		while(rs.next()) {
			
			table.setWidthPercentage(100);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(Integer.toString(i++));
			table.addCell(rs.getString(2));
			table.addCell(rs.getString(3));
			table.addCell(Double.toString(rs.getDouble(4)));
			table.addCell(Double.toString(rs.getDouble(5)));
			table.addCell(Double.toString(rs.getDouble(6)));
		}
		}catch(Exception e)
		{e.printStackTrace();}
 
		document.add(table);
	}
 
}
