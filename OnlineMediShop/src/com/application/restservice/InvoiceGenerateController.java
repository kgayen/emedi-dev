package com.application.restservice;

import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.application.exception.CustomGenericException;
import com.application.model.Invoice;
import com.application.model.User;
import com.application.service.InvoiceService;
import com.application.utility.InvoiceHTML;
import com.application.utility.PDFGenerator;
import com.application.utility.SpringPropertiesUtil;

@RestController
@RequestMapping(value = "/invoicegenerator")
public class InvoiceGenerateController {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceGenerateController.class);
	@Autowired
	JSONObject jsonObject;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	InvoiceHTML invoiceHTML;
	@Autowired
	User user;
	
	@RequestMapping(value = "/generateInvoice", method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> generateInvoice(@RequestParam(value = "orderno", required = true) String orderno,
    		HttpServletRequest request) throws CustomGenericException {
		
		logger.info("Generate Invoice For : "+orderno);
		HttpSession session = request.getSession();
		User user = ((User)session.getAttribute("user"));
		String userId = (user != null) ? user.getUser_id() : null;
		String invoiceHTMLData = "";
		StringBuilder exceptionStr = new StringBuilder();
		try{
			Invoice invoice = invoiceService.generateInvoice(orderno);
			if(invoice.getOrder() == null){
				throw  new CustomGenericException("5001",SpringPropertiesUtil.getProperty("5001").concat(orderno),
						Thread.currentThread().getStackTrace()[1].getClassName(),
						Thread.currentThread().getStackTrace()[1].getMethodName(),
						userId);
			}
			
			invoiceHTMLData = invoiceHTML.generateInvoiceHTML(invoice);
			//logger.info(invoiceHTMLData);
			PDFGenerator pdfg = new PDFGenerator();
			//Document document = pdfg.createInvoice(invoiceHTMLData, invoice);
			ByteArrayOutputStream byteArrayOutputStream = pdfg.createInvoice(invoiceHTMLData, invoice);
			HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.parseMediaType("application/pdf"));
		    String filename = "Invoice_"+invoice.getOrder().getOrderid()+".pdf";
		    headers.setContentDispositionFormData(filename, filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    byte [] array = byteArrayOutputStream.toByteArray();
		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(array, headers, HttpStatus.OK);
	        return response;
		}
		catch(CustomGenericException customExcp){
			throw customExcp;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
			exceptionStr.append(SpringPropertiesUtil.getProperty("5002"));
			exceptionStr.append("###");
			exceptionStr.append(e.getMessage());
			throw  new CustomGenericException("5002",exceptionStr.toString(),
					Thread.currentThread().getStackTrace()[1].getClassName(),
					Thread.currentThread().getStackTrace()[1].getMethodName(),
					userId);
		}
    }
}
