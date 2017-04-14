package com.application.utility;

import com.itextpdf.text.pdf.Barcode128;

public class BarCodeGenerator {
	
	private Barcode128 code128;
	public void setCode128(Barcode128 code128) {
		this.code128 = code128;
	}
	
	public Barcode128 generateBarCode(String barCodeValue){
	    code128.setGenerateChecksum(true);
	    code128.setCode(barCodeValue);
	    return code128;
	}

}
