package com.application.utility;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import com.application.model.Invoice;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PDFGenerator {
	//public static final String DEST = "D:/Gangtok/html_table_4.pdf";
    //public static final String HTML = "D:/Gangtok/table2_css.html";
    
    @Autowired
	private BarCodeGenerator barCodeGenerator;
	@Autowired
	Barcode128 code128Model;
 
    /*public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PDFGenerator().createInvoice(DEST);
    }*/
 
    /**
     * Creates a PDF with the words "Hello World"
     * @param file
     * @throws IOException
     * @throws DocumentException
     */
    public ByteArrayOutputStream createInvoice(String html,Invoice invoice) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);//new FileOutputStream(DEST)
        // step 3
        document.open();
        Barcode128 code128 = new Barcode128();
	    code128.setGenerateChecksum(true);
	    System.out.println(invoice.getCashmemodetail().getInvoiceid());
	    code128.setCode(invoice.getCashmemodetail().getInvoiceid()); 
	    
	    //Barcode128 code128Model = barCodeGenerator.generateBarCode(invoice.getCashmemodetail().getInvoiceid());
	    
	    //java.awt.Image awtImage = code128.createAwtImage(Color.BLACK, Color.WHITE);
        document.add(code128.createImageWithBarcode(writer.getDirectContent(), BaseColor.BLACK, BaseColor.WHITE));
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        // step 4
        //XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(HTML));
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new  ByteArrayInputStream(html.getBytes()));
        // step 5
        Image companyLogo = Image.getInstance(SpringPropertiesUtil.getProperty("invoice.image.source"));
        companyLogo.setAbsolutePosition(480,770);
        companyLogo.scalePercent(25);
        document.add(companyLogo);
        document.close();
        //return document;
        return byteArrayOutputStream;
    }
}