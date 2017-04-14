package com.application.service;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.application.dao.SellerDao;
import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Seller;
import com.application.model.User;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;

public class SellerService extends  BaseService implements SellerDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);
	
	private JdbcTemplate template;
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	public static Calendar calendar = Calendar.getInstance();

	public List<Seller> getSeller(String urlPath, String queryValue) {
		logger.info("Executing Method getSeller().");
		String strQuery="";
		if(urlPath.equalsIgnoreCase("getSellerById")){
			strQuery = "select * from seller_details where seller_id = "+queryValue+"";
		}
		else if(urlPath.equalsIgnoreCase("getSellerByPincode")){
			strQuery = "select * from seller_details where seller_delivery_pincode = '"+queryValue+"'";
		}
		else if(urlPath.equalsIgnoreCase("getOrderByShopName")){
			strQuery = "select * from seller_details where seller_shop_name like '%"+queryValue+"%'";
		}
		final String query = strQuery;
		logger.info("Executing Method getDataList(). Query : "+query);
		return (List<Seller>) template.query(query,new ResultSetExtractor<List<Seller>>(){  
			public List<Seller> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Seller> list=new ArrayList<Seller>();  
				while(rs.next()){  
					Seller sellerObj=new Seller();
					sellerObj.setSellerid(rs.getLong("seller_id"));
					sellerObj.setSellerShopName(rs.getString("seller_shop_name"));
					sellerObj.setSellerComission(rs.getDouble("seller_comission"));
					sellerObj.setSellerPriority(rs.getInt("seller_priority"));
					sellerObj.setSellerStatus(rs.getString("seller_status"));
					sellerObj.setSellerDeliveryPincode(rs.getString("seller_delivery_pincode"));
					sellerObj.setSellerCreateDate(rs.getTimestamp("seller_create_date"));
					sellerObj.setSellerLastModifyDate(rs.getTimestamp("seller_last_modify_date"));
					sellerObj.setSellerExpiryDate(rs.getTimestamp("seller_expiry_date"));
					sellerObj.setSellerAddress(rs.getString("seller_address"));
					sellerObj.setSellerRegistrationNo(rs.getString("seller_registration_no"));
					sellerObj.setSellerDiscount(rs.getDouble("seller_discount"));
					sellerObj.setSellerEmergencyPrice(rs.getBigDecimal("seller_emergency_price"));
					sellerObj.setSellerDeliveryPrice(rs.getBigDecimal("seller_delivery_price"));
					sellerObj.setSellerEmailId(rs.getString("seller_email_id"));
					sellerObj.setSellerMobileNo(rs.getString("seller_mobileno"));
					sellerObj.setTaxType(rs.getString("seller_tax_type"));
					sellerObj.setTaxCategory(rs.getString("seller_tax_category"));
					list.add(sellerObj);  
				}  
				return list;  
			}  
		});
	}

	@Override
	public int confrimOrder(final ConfirmOrderRequest confirmOrderRequest,final User user) {
		// TODO Auto-generated method stub
		logger.info("Execute confrimOrder service func : "+confirmOrderRequest.toString());
		int insertStatus = 0;
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall (template);
		simpleJdbcCall.withProcedureName (SpringPropertiesUtil.getProperty("confirmOrder")).withoutProcedureColumnMetaDataAccess().withReturnValue(); 
		// Declare parameter to save Stored Procedure return value 
		simpleJdbcCall.addDeclaredParameter (new SqlParameter ("orderId", Types.BIGINT)); 
		simpleJdbcCall.addDeclaredParameter (new SqlParameter ("cashmemoNo", Types.VARCHAR));
		simpleJdbcCall.addDeclaredParameter (new SqlParameter ("invoiceNo", Types.VARCHAR));
		simpleJdbcCall.addDeclaredParameter (new SqlParameter ("invoiceId", Types.VARCHAR));
		simpleJdbcCall.addDeclaredParameter (new SqlParameter ("userId", Types.VARCHAR));
		simpleJdbcCall.declareParameters (new SqlOutParameter ("insertStatus", Types.INTEGER)); 
		Map<String, Object> inParamsValue = new HashMap<String, Object> (); 
		inParamsValue.put ("orderId", new BigInteger(confirmOrderRequest.getOrderid()));
		inParamsValue.put ("cashmemoNo", confirmOrderRequest.getCashmemoNo());
		inParamsValue.put ("invoiceNo", confirmOrderRequest.getInvoiceNo());
		inParamsValue.put ("invoiceId", confirmOrderRequest.getInvoiceId());
		inParamsValue.put ("userId", user.getUser_id());

		simpleJdbcCall.getJdbcTemplate().setResultsMapCaseInsensitive (true); 
		simpleJdbcCall.getJdbcTemplate().setSkipUndeclaredResults (true); 
		Map <String, Object> resultMap = simpleJdbcCall.execute(inParamsValue); 
		insertStatus = Integer.parseInt (resultMap.get("insertStatus").toString());
		return insertStatus;
	}
	
	public int confrimOrderNew(final ConfirmOrderRequest confirmOrderRequest,final User user) {
		// TODO Auto-generated method stub
		logger.info("Execute confrimOrder service func : "+confirmOrderRequest.toString());
		int insertStatus = 0;
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall (template);
		simpleJdbcCall.withProcedureName(SpringPropertiesUtil.getProperty("confirmOrder")); 
		// Declare parameter to save Stored Procedure return value 
		SqlParameterSource in = new MapSqlParameterSource().addValue("orderId", new BigInteger(confirmOrderRequest.getOrderid()), Types.BIGINT)
				.addValue("cashmemoNo", confirmOrderRequest.getCashmemoNo(), Types.VARCHAR)
				.addValue("invoiceNo", confirmOrderRequest.getInvoiceNo(), Types.VARCHAR)
				.addValue("invoiceId", confirmOrderRequest.getInvoiceId(), Types.VARCHAR)
				.addValue("userId", user.getUser_id(), Types.VARCHAR)
				.addValue("orderPrice", confirmOrderRequest.getOrderprice(), Types.DECIMAL);

		Map<String,Object> resultMap = simpleJdbcCall.execute(in);
		insertStatus = (int) resultMap.get("insertstatus");
		return insertStatus;
	}

	@Override
	public int cancelRequest(DeliverOrderRequest deliverOrderRequest, User user) {
		logger.info("Executing Method cancelRequest().");
		int insertStatus = 0;
		StringBuilder strBuilder = new StringBuilder("insert into `medishop`.`order_cancel_request` (order_id,cancel_request_id,cancel_requester,cancel_requested_date,cancellation_reason,cancellation_request_status,cancel_request_approver,request_last_update) values");
		for(BigInteger orderid : deliverOrderRequest.getConfirmOrders()){
			long currentTimestamp = System.currentTimeMillis();
			strBuilder.append("(");
			strBuilder.append(orderid);
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append(orderid.toString().concat("_").concat(String.valueOf(currentTimestamp)));
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append(user.getUser_id());
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("NOW()");
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append(deliverOrderRequest.getCancellationNote());
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append(SpringPropertiesUtil.getProperty("cancelRequest.cancelRequestStatus"));
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("'");
			strBuilder.append("");
			strBuilder.append("'");
			strBuilder.append(",");
			strBuilder.append("NOW()");
			strBuilder.append(")");
			strBuilder.append(",");
		}
		strBuilder.delete(strBuilder.length()-1, strBuilder.length());
		strBuilder.append(";");
		final String query = strBuilder.toString();
		System.out.println("cancelRequest : "+query);
		insertStatus = template.update(query);
		return insertStatus;
	}

	@Override
	public int deliverOrder(DeliverOrderRequest deliverOrderRequest, User user) {
		// TODO Auto-generated method stub
		int updateStatus = 0;
		StringBuilder strBuilder = new StringBuilder("UPDATE `medishop`.`order` SET order_status = CASE order_id ");
		for(BigInteger orderId : deliverOrderRequest.getConfirmOrders()){
			strBuilder.append(" WHEN "+orderId+" THEN '"+SpringPropertiesUtil.getProperty("deliver")+"'");
		}
		strBuilder.append(" ELSE order_status END, order_last_modify = CASE order_id");
		for(BigInteger orderId : deliverOrderRequest.getConfirmOrders()){
			//strBuilder.append(" WHEN "+orderId+" THEN "+new java.sql.Timestamp(calendar.getTime().getTime())+"");
			strBuilder.append(" WHEN "+orderId+" THEN NOW()");
		}
		strBuilder.append(" ELSE order_last_modify END WHERE order_id IN ("+OrderUtility.getOrderQueryString(deliverOrderRequest.getConfirmOrders())+") and order_status='"+SpringPropertiesUtil.getProperty("confirm")+"'");
		strBuilder.append(";");
		final String query = strBuilder.toString();
		updateStatus = template.update(query);
		return updateStatus;
	}
}
