package com.application.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.application.exception.CustomGenericException;
import com.application.model.Invoice;
import com.application.model.TaxInfo;
import com.itextpdf.text.pdf.Barcode128;

public class InvoiceHTML {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceHTML.class);
	
	@Autowired
	private BarCodeGenerator barCodeGenerator;
	@Autowired
	Barcode128 code128Model;
	@Autowired
	TaxInfo taxInfo;
	@Autowired
	TaxInfoUtility taxInfoUtility;
	
	public static DecimalFormat newFormat = new DecimalFormat("#.##");
	
	//@SuppressWarnings("unused")
	public String generateInvoiceHTML(Invoice invoice) throws CustomGenericException{
		code128Model = barCodeGenerator.generateBarCode(invoice.getCashmemodetail().getInvoiceid());
		java.awt.Image awtImage = code128Model.createAwtImage(Color.BLACK, Color.WHITE);
		BufferedImage bImage= toBufferedImage(awtImage);
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    String imageString = null;
	    StringBuilder exceptionStr = new StringBuilder();
	    try {
            ImageIO.write(bImage, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = Base64Converter.encode(imageBytes);
            //System.out.println(imageString);
            bos.close();
        } catch (IOException e) {
        	exceptionStr = new StringBuilder();
        	exceptionStr.append(SpringPropertiesUtil.getProperty("5002"));
			exceptionStr.append("###");
			exceptionStr.append(e.getMessage());
			throw  new CustomGenericException("5002",exceptionStr.toString(),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					"");
        }
	    
	    StringBuilder invoiceHtmlBuilder = new StringBuilder();
	    if(imageString != null && !imageString.isEmpty() && imageString.length()>0 ){
	    	invoiceHtmlBuilder.append("<html> <header> </header> <body>");
	    	invoiceHtmlBuilder.append("<table border='0' style='width: 100%'> <tr> <td colspan='5' align='center' style='background-color: lightblue'>INVOICE</td> </tr> <tr height='30'> <td colspan='5' align='left'><img src='data:image/png;base64, "+imageString+"'/></td> </tr> <tr> <td colspan='2' rowspan='3' style='background-color: white'><b>"+SpringPropertiesUtil.getProperty("invoice.companyName")+"</b><br />"+SpringPropertiesUtil.getProperty("invoice.companyAddress")+"</td> <td>INVOICE</td> <td>"+invoice.getCashmemodetail().getInvoiceid()+"</td> </tr> <tr> <td>Order</td> <td>"+invoice.getOrder().getOrderid()+"</td> </tr> <tr rowspan='3' style=''> <td>Order Date</td> <td>"+invoice.getOrder().getOrderCreateDate()+"</td> </tr> <tr> <td colspan='2' rowspan='4' style='background-color: white'><p><b >"+invoice.getUser().getUser_name()+"</b></p>"+invoice.getOrder().getShippingAddress()+"</td> <td>Customer Mobile No:</td> <td colspan='2'>"+invoice.getUser().getUser_mobile()+"</td> </tr> <tr> <td>Email Id</td> <td colspan='2'>"+invoice.getUser().getUser_email()+"</td> </tr> </table>");
	    	invoiceHtmlBuilder.append("<div> </div>");
	    	
	    	taxInfo = taxInfoUtility.loadTaxInfo();
	    	Map<String, Double> deliveryTaxInfoData = taxInfo.getTaxInfoMap().get("DeliveryTax");
	    	Map<String, Double> emergencyTaxInfoData = taxInfo.getTaxInfoMap().get("EmergencyTax");
	    	Map<String, Double> sellerTaxInfoData = taxInfo.getTaxInfoMap().get(invoice.getSeller().getTaxCategory());
	    	if(invoice.getOrder().getOrderprice().doubleValue() > 0.0){
	    		invoiceHtmlBuilder.append("<table border='1' style='font-size: 12px !important;width: 100%' class='table table-bordered'><thead><tr height='20'><th>Product Name</th><th>Tax Type(Tax %)</th><th>Amount</th></tr></thead><tbody>");
	    		double totalGross = 0.00;
	    		double sum = 0.00;
		    	if(invoice.getSeller().getTaxType().equalsIgnoreCase("exclude")){
		    		double amountAfterDiscount = getAmountAfterDiscount(invoice.getOrder().getOrderprice().doubleValue(),invoice.getOrder().getOrderDiscountAmount().doubleValue());
		    		sum = amountAfterDiscount;
		    		invoiceHtmlBuilder.append("<tr><td><b>Medicine Price(Exclude Tax)</b></td><td>Net Amount</td><td>"+roundOfTwoDigitDecimal(amountAfterDiscount)+"</td></tr>");
		    		int i = 0;
		    		for(String taxTypeName : sellerTaxInfoData.keySet()){
		    			String rowSpanTD = "";
		    			if(i == 0){
		    				rowSpanTD = "<td rowspan='"+(sellerTaxInfoData.size()+1)+"'></td>";
		    			}
		    			double taxValue = getTaxAmount(sellerTaxInfoData.get(taxTypeName),amountAfterDiscount);
		    			invoiceHtmlBuilder.append("<tr>"+rowSpanTD+"<td>"+taxTypeName+" ("+sellerTaxInfoData.get(taxTypeName)+"%)</td><td>"+roundOfTwoDigitDecimal(taxValue)+"</td></tr>");
		    			sum = sum+taxValue;
		    			i ++;
		    		}
		    		totalGross = sum+totalGross;
		    		invoiceHtmlBuilder.append("<tr><td><b>Gross Amount</b></td><td>"+roundOfTwoDigitDecimal(sum)+"</td></tr>");
		    		//invoiceHtmlBuilder.append("<tr><td colspan = '3'></td></tr>");
		    	}
		    	else if(invoice.getSeller().getTaxType().equalsIgnoreCase("include")){
		    		double amountAfterDiscount = getAmountAfterDiscount(invoice.getOrder().getOrderprice().doubleValue(),invoice.getOrder().getOrderDiscountAmount().doubleValue());
		    		double netAmount = getNetAmount(amountAfterDiscount,sellerTaxInfoData);
		    		sum = netAmount;
		    		invoiceHtmlBuilder.append("<tr><td><b>Medicine Price(Exclude Tax)</b></td><td>Net Amount</td><td>"+roundOfTwoDigitDecimal(netAmount)+"</td></tr>");
		    		int i = 0;
		    		for(String taxTypeName : sellerTaxInfoData.keySet()){
		    			String rowSpanTD = "";
		    			if(i == 0){
		    				rowSpanTD = "<td rowspan='"+(sellerTaxInfoData.size()+1)+"'></td>";
		    			}
		    			double taxValue = getTaxAmount(sellerTaxInfoData.get(taxTypeName),netAmount);
		    			invoiceHtmlBuilder.append("<tr>"+rowSpanTD+"<td>"+taxTypeName+" ("+sellerTaxInfoData.get(taxTypeName)+"%)</td><td>"+roundOfTwoDigitDecimal(taxValue)+"</td></tr>");
		    			sum = sum+taxValue;
		    			i++ ;
		    		}
		    		totalGross = sum+totalGross;
		    		invoiceHtmlBuilder.append("<tr><td><b>Gross Amount</b></td><td>"+roundOfTwoDigitDecimal(sum)+"</td></tr>");
		    		//invoiceHtmlBuilder.append("<tr><td colspan = '3'></td></tr>");
		    		//alert();
		    		
		    		// delivery and emergency tax pending
		    	}
		    	
		    	double deliveryPriceNet = getNetAmount(invoice.getOrder().getOrderDeliveryAmount().doubleValue(),deliveryTaxInfoData);
		    	sum = deliveryPriceNet;
		    	invoiceHtmlBuilder.append("<tr><td><b>Delivery Price(Exclude Tax)</b></td><td>Net Amount</td><td>"+roundOfTwoDigitDecimal(deliveryPriceNet)+"</td></tr>");
		    	int i= 0;
		    	for(String taxTypeName : deliveryTaxInfoData.keySet()){
		    		String rowSpanTD = "";
		    		if(i == 0){
		    			rowSpanTD = "<td rowspan='"+(deliveryTaxInfoData.size()+1)+"'></td>";
		    		}
		    		double taxValue = getTaxAmount(deliveryTaxInfoData.get(taxTypeName),deliveryPriceNet);
		    		invoiceHtmlBuilder.append("<tr>"+rowSpanTD+"<td>"+taxTypeName+" ("+deliveryTaxInfoData.get(taxTypeName)+"%)</td><td>"+roundOfTwoDigitDecimal(taxValue)+"</td></tr>");
		    		sum = sum+taxValue;
		    		i++;
		    	}
		    	
		    	invoiceHtmlBuilder.append("<tr><td><b>Gross Amount</b></td><td>"+roundOfTwoDigitDecimal(sum)+"</td></tr>");
		    	totalGross = sum+totalGross;
		    	if(invoice.getOrder().getEmergencyFlag() == 1 && invoice.getOrder().getEmergencyPrice().doubleValue()>0.00){
		    		//invoiceHtmlBuilder.append("<tr><td colspan = '3'></td></tr>");
		    		double emergencyPriceNet = getNetAmount(invoice.getOrder().getEmergencyPrice().doubleValue(),emergencyTaxInfoData);
		    		sum = emergencyPriceNet;
		    		invoiceHtmlBuilder.append("<tr><td><b>Emergency Price(Exclude Tax)</b></td><td>Net Amount</td><td>"+roundOfTwoDigitDecimal(emergencyPriceNet)+"</td></tr>");
		    		i = 0;
		    		for(String taxTypeName : emergencyTaxInfoData.keySet()){
		    			String rowSpanTD = "";
		    			if(i == 0){
		    				rowSpanTD = "<td rowspan='"+(emergencyTaxInfoData.size()+1)+"'></td>";
		    			}
		    			double taxValue = getTaxAmount(emergencyTaxInfoData.get(taxTypeName),emergencyPriceNet);
		    			invoiceHtmlBuilder.append("<tr>"+rowSpanTD+"<td>"+taxTypeName+" ("+emergencyTaxInfoData.get(taxTypeName)+"%)</td><td>"+roundOfTwoDigitDecimal(taxValue)+"</td></tr>");
		    			sum = sum+taxValue;
		    			i++;
		    		}
		    		totalGross = sum+totalGross;
		    		invoiceHtmlBuilder.append("<tr><td><b>Gross Amount</b></td><td>"+roundOfTwoDigitDecimal(sum)+"</td></tr>");
		    	}
		    	invoiceHtmlBuilder.append("<tr><td colspan = '2'><b>Total Gross Amount (Payable Amount)</b></td><td><b>"+roundOfTwoDigitDecimal(totalGross)+"</b></td></tr></tbody></table>");
		    	//System.out.println("Round Value: "+roundOfTwoDigitDecimal(totalGross));
		    	//totalAmountHtml = "<div class='form-group'> <label for='cname' class='control-label col-lg-2'>Payable Amount:</label> <div class='col-lg-10'> <strong> <span class='form-control' id='order_view_price'>'+roundToTwo(totalGross)+'</span><a title='View Tax Breakup' data-toggle='modal' href='#taxbreakup'+orderObject.order.orderid+''>Tax Breakup</a> </strong> </div> </div>";
	    	}
	    	else{
	    		throw  new CustomGenericException("5004",SpringPropertiesUtil.getProperty("5004"),
						Thread.currentThread().getStackTrace()[1].getClassName(),
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						"");
	    	}
	    	//invoiceHtmlBuilder.append("<table border='1' style='font-size: 12px !important;width: 100%' class='table table-bordered'><thead><tr height='20'><th>Product Name</th><th>Tax Type(Tax %)</th><th>Amount</th></tr></thead><tbody><tr height='20'><td><b>Medicine Price(Exclude Tax)</b></td><td>Net Amount</td><td>506.85</td></tr><tr height='20'><td rowspan='3'></td><td>VAT (12%)</td><td>60.82</td></tr><tr height='20'><td>Service Tax (14%)</td><td>70.96</td></tr><tr height='20'><td><b>Gross Amount</b></td><td>638.63</td></tr><tr height='20'><td><b>Delivery Price(Exclude Tax)</b></td><td>Net Amount</td><td>8.70</td></tr><tr height='20'><td rowspan='4'></td><td>Swachh Bharat Cess (0.5%)</td><td>0.04</td></tr><tr height='20'><td>Krishi Kalyan Cess (0.5%)</td><td>0.04</td></tr><tr height='20'><td>Service Tax (14%)</td><td>1.22</td></tr><tr height='20'><td><b>Gross Amount</b></td><td>10.00</td></tr><tr height='20'><td><b>Emergency Price(Exclude Tax)</b></td><td>Net Amount</td><td>26.91</td></tr><tr height='20'><td rowspan='3'></td><td>Swachh Bharat Cess (1.5%)</td><td>0.40</td></tr><tr height='20'><td>Service Tax (10%)</td><td>2.69</td></tr><tr height='20'><td><b>Gross Amount</b></td><td>30.00</td></tr><tr height='20'><td colspan='2'><b>Total Gross Amount (Payable Amount)</b></td><td><b>678.63</b></td></tr></tbody></table>");
	    	invoiceHtmlBuilder.append("<p><b>Get Medicine easyly on your door step</b></p>");
	    	invoiceHtmlBuilder.append("</body> </html>");
	    }
	    else{
	    	throw  new CustomGenericException("5003",SpringPropertiesUtil.getProperty("5003").concat(invoice.getOrder().getOrderid()),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					"");
	    }
	    
	    //logger.info("HTML : "+invoiceHtmlBuilder.toString());
		return invoiceHtmlBuilder.toString();
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static double getAmountAfterDiscount(double amount,double discountPercentage){
		double amountAfterDiscount = (amount-(amount*(discountPercentage/100)));
		return amountAfterDiscount;
	}
	
	public static double getTaxAmount(double taxPercentage,double amount){
		double taxamount = (amount*(taxPercentage/100));
		return taxamount;
	}
	
	public static double getNetAmount(double grossAmount,Map<String,Double>taxArray){
		double taxsum = 100.00;
		for(String taxTypeName : taxArray.keySet()){
			taxsum = taxsum+taxArray.get(taxTypeName);
		}
		double netAmount = ((grossAmount*100)/taxsum);
		return netAmount;
	}
	
	public static String roundOfTwoDigitDecimal(double value){
		double roundvalue = Double.valueOf(newFormat.format(value));
		String convertTwoDigit = String.valueOf(roundvalue);
		convertTwoDigit = (convertTwoDigit.split("\\.")[1].length() == 1)? convertTwoDigit.concat("0"): convertTwoDigit; 
		return convertTwoDigit;
	}
}