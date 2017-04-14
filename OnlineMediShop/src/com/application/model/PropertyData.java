package com.application.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyData  extends PropertyPlaceholderConfigurer{

	@Value("${invoice.image.source}")
	String invoiceImage;
	@Value("${invoice.companyName}")
	String companyName;
	@Value("${invoice.companyAddress}")
	String companyFullAddress;

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyFullAddress() {
		return companyFullAddress;
	}

	public String getInvoiceImage() {
		return invoiceImage;
	}
	
	@SuppressWarnings("rawtypes")
	private static Map propertiesMap;
	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;
	 
   @SuppressWarnings({ "unchecked" })
   @Override
   protected void processProperties(ConfigurableListableBeanFactory beanFactory,
             Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
 
        propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            /*propertiesMap.put(keyStr, parseStringValue(props.getProperty(keyStr),
                props, new HashSet()));*/
            propertiesMap.put(keyStr, resolvePlaceholder(props.getProperty(keyStr),
                    props, springSystemPropertiesMode));
        }
    }
 
    public static String getProperty(String name) {
        return (String) propertiesMap.get(name);
    }
}
