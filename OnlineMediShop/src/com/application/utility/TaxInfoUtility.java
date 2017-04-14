package com.application.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.application.model.TaxInfo;
import com.application.service.BaseService;

public class TaxInfoUtility extends BaseService {
	
	private JdbcTemplate template;
	@Autowired
	TaxInfo taxInfo;
	private static final Logger logger = LoggerFactory.getLogger(TaxInfoUtility.class);
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	public TaxInfo loadTaxInfo()
	{
		logger.info("Initializing taxInfo field value");
		final String query = "select * from medishop.tax_info";
		try{
			if(taxInfo.getTaxInfoMap().size() == 0){
				taxInfo =  template.query(query,new ResultSetExtractor<TaxInfo>(){
					Map<String,Map<String,Double>> taxDetails =new HashMap<String,Map<String,Double>>(); 
					public TaxInfo extractData(ResultSet rs) throws SQLException,  
						DataAccessException {
						while(rs.next()){
							Map<String,Double> taxBreakup =new HashMap<String,Double>();
							String tax_Category=rs.getString("Tax_Category");
							String[] tax_TypeList=rs.getString("Tax_Type").split("#");
							String[] tax_AmountList=rs.getString("Tax_Amount").split("#");
							for(int taxCount = 0;taxCount<tax_TypeList.length;taxCount++){
								taxBreakup.put(tax_TypeList[taxCount], Double.valueOf(tax_AmountList[taxCount]));
							}
							taxDetails.put(tax_Category,taxBreakup);
						}  
						taxInfo.setTaxInfoMap(taxDetails);
						return taxInfo;  
					}  
				});
				logger.info("TaxInfoList Map Size:: "+taxInfo.getTaxInfoMap().size());
			}
		}
		catch(Exception e){
			logger.info("TaxInfo exception:: "+e.getStackTrace());
		}
		return taxInfo;
	}	
}