package com.application.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.application.dao.InvoiceDao;
import com.application.model.CashmemoDetail;
import com.application.model.Invoice;
import com.application.model.Order;
import com.application.model.Seller;
import com.application.model.User;

public class InvoiceService extends  BaseService implements InvoiceDao {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
	
	@Autowired
	Invoice invoice;
	@Autowired
	Order newOrder;
	@Autowired
	Seller seller;
	@Autowired
	CashmemoDetail cashmemodetail;
	@Autowired
	User user;
	
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}

	public Invoice generateInvoice(String orderno) {
		final String query = "Select orderview.order_id,orderview.order_create_date,orderview.order_status,orderview.order_price,orderview.order_last_modify,orderview.order_address,orderview.order_pincode,orderview.order_discount_amount,orderview.order_delivery_price,orderview.order_emergency_price,orderview.order_emergencyFlag,userview.user_name,userview.user_email,userview.user_mobile,cashmemoview.cashmemo_no,cashmemoview.invoice_id,sellerview.seller_shop_name,sellerview.seller_tax_category, sellerview.seller_tax_type,sellerview.seller_address "
				+"FROM `medishop`.`order` as orderview INNER JOIN `medishop`.`order_cashmemo_details` as cashmemoview ON orderview.order_id = cashmemoview.order_id and cashmemoview.invoice_id = CONCAT("+orderno+",'_',(Select cashmemo_no from `medishop`.`order_cashmemo_details` where `medishop`.`order_cashmemo_details`.order_id = "+orderno+")) "
				+"INNER JOIN `medishop`.`user` as userview ON (userview.user_id = orderview.order_creator or userview.user_mobile = orderview.order_creator) "
				+"INNER JOIN `medishop`.`seller_details` as sellerview ON sellerview.seller_id = orderview.order_seller_id WHERE orderview.order_id ="+orderno;
		logger.info("Executing Method generateInvoice(). Query : "+query);		
		return (Invoice) template.query(query,new ResultSetExtractor<Invoice>(){  
			public Invoice extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				invoice = new Invoice();  
				if(rs.next()){  
					newOrder = new Order();
					seller = new Seller();
					cashmemodetail = new CashmemoDetail();
					user = new User();
					newOrder.setOrderid(String.valueOf(rs.getLong("order_id")));
					newOrder.setOrderCreateDate(rs.getTimestamp("order_create_date"));
					newOrder.setOrderstatus(rs.getString("order_status"));
					newOrder.setOrderprice(rs.getBigDecimal("order_price"));
					newOrder.setOrderlastmodify(rs.getTimestamp("order_last_modify"));
					newOrder.setShippingAddress(rs.getString("order_address"));
					newOrder.setEmergencyPrice(rs.getBigDecimal("order_emergency_price"));
					newOrder.setOrderPincode(rs.getString("order_pincode"));
					newOrder.setOrderDiscountAmount(rs.getBigDecimal("order_discount_amount"));
					newOrder.setOrderDeliveryAmount(rs.getBigDecimal("order_delivery_price"));
					newOrder.setEmergencyFlag(rs.getInt("order_emergencyFlag"));
					invoice.setOrder(newOrder);
					seller.setSellerShopName(rs.getString("seller_shop_name"));
					seller.setSellerAddress(rs.getString("seller_address"));
					seller.setTaxCategory(rs.getString("seller_tax_category"));
					seller.setTaxType(rs.getString("seller_tax_type"));
					invoice.setSeller(seller);
					cashmemodetail.setCashmemono(rs.getString("cashmemo_no"));
					cashmemodetail.setInvoiceid(rs.getString("invoice_id"));
					invoice.setCashmemodetail(cashmemodetail);
					user.setUser_name(rs.getString("user_name"));
					user.setUser_email(rs.getString("user_email"));
					user.setUser_mobile(rs.getString("user_mobile"));
					invoice.setUser(user);
				}
				return invoice;  
			}  
		});
	}
}
