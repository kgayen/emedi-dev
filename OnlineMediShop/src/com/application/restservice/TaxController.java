package com.application.restservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.TaxInfo;
import com.application.utility.TaxInfoUtility;

@RestController
@RequestMapping(value = "/tax")
public class TaxController {
	
	@Autowired
	TaxInfo taxInfo;
	@Autowired
	TaxInfoUtility taxInfoUtility;
	
	private static final Logger logger = LoggerFactory.getLogger(TaxController.class);
	@RequestMapping(value = "/getTaxDetails", method = RequestMethod.GET)
    public ResponseEntity<TaxInfo> getTaxDetails() {
		
		logger.info("Executing Method getTaxDetails().");
		logger.info("Executing Method getTaxDetails(). taxInfo.getTaxInfoMap().size() = "+taxInfo.getTaxInfoMap().size());
		if(taxInfo.getTaxInfoMap().size() == 0){
			taxInfo = taxInfoUtility.loadTaxInfo();
		}
		return new ResponseEntity<TaxInfo>(taxInfo, HttpStatus.OK);
    }
}
