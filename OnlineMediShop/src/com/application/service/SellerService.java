package com.application.service;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.web.bind.annotation.PathVariable;

import com.application.dao.SellerDao;
import com.application.model.CancellationRequest;
import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Order;
import com.application.model.OrderStatus;
import com.application.model.OrderWrapper;
import com.application.model.RefundOrder;
import com.application.model.Seller;
import com.application.model.User;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;
import com.opensymphony.xwork2.util.TextUtils;

public class SellerService extends  BaseService implements SellerDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);
	@Autowired
	SimpleDateFormat sdf;
	
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
					sellerObj.setSellerShopCloseDay(rs.getString("seller_shop_close_day"));
					sellerObj.setShopCloseTime(rs.getString("seller_shop_open_time"));
					sellerObj.setShopOpenTime(rs.getString("seller_shop_close_time"));
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

	@Override
	public List<List<Object>> getSellsReport(String sellerId, long fromDate,
			long toDate) {

		String	strQuery = "SELECT count(*) as 'order_count', a.order_status"
		+" FROM `medishop`.`order` as a, `medishop`.`refun_order` b"
		+" where a.order_seller_id = '"+sellerId+"'"
		+" and a.order_create_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'"
		+" and  a.order_id not in (SELECT aa.order_id"
		+" FROM `medishop`.`refun_order` as aa,`medishop`.`order` bb"
		+" where aa.order_id = bb.order_id"
		+" and bb.order_seller_id = '"+sellerId+"'"
		+" and aa.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')"
		+" group by a.order_status";
		logger.info("Get Sells Order Report Query : "+strQuery);
		
		List<List<Object>> finalDataList = new ArrayList<List<Object>>();
		List<List<Object>> mainDataList = new ArrayList<List<Object>>();
		List<Object> datalist =new ArrayList<Object>();
		datalist.add("Task");
		datalist.add("Sell Details");
		finalDataList.add(datalist);
		final String query = strQuery;
		
		mainDataList =  (List<List<Object>>) template.query(query,new ResultSetExtractor<List<List<Object>>>(){  
			public List<List<Object>> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<List<Object>> data=new ArrayList<List<Object>>();  
				while(rs.next()){  
					List<Object> dataList =new ArrayList<Object>();
					dataList.add(String.valueOf(rs.getString("order_status")));
					dataList.add(rs.getInt("order_count"));
					data.add(dataList);					
				}  
				return data;  
			}  
		});
		
		for(List<Object> data : mainDataList){
			finalDataList.add(data);
		}
		
		strQuery = "SELECT count(*) as 'refund_order_count', a.refund_status"
					+" FROM `medishop`.`refun_order` as a,`medishop`.`order` b"
					+" where a.order_id = b.order_id"
					+" and b.order_seller_id = '"+sellerId+"'"
					+" and a.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'"
					+" group by a.refund_status";
		
		logger.info("Get Sells Refud Query : "+strQuery);
		final String queryStr = strQuery;
				
		mainDataList =  (List<List<Object>>) template.query(queryStr,new ResultSetExtractor<List<List<Object>>>(){  
			public List<List<Object>> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<List<Object>> data=new ArrayList<List<Object>>();  
				while(rs.next()){  
					List<Object> dataList =new ArrayList<Object>();
					dataList.add(String.valueOf(rs.getString("refund_status")));
					dataList.add(rs.getInt("refund_order_count"));
					data.add(dataList);					
				}  
				return data;  
			}  
		});
		
		for(List<Object> data : mainDataList){
			finalDataList.add(data);
		}
		return finalDataList;
	}
	
	public Map<String,List<Object>> generateSellerReport(String sellerId, long fromDate, long toDate){
		
		Map<String,List<Object>> dataMap = new HashMap<String,List<Object>>();
		
		final String getOrderQuery = "SELECT * "
				+" FROM `medishop`.`order` as a, `medishop`.`refun_order` b"
				+" where a.order_seller_id = '"+sellerId+"'"
				+" and a.order_create_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'"
				+" and  a.order_id not in (SELECT aa.order_id"
				+" FROM `medishop`.`refun_order` as aa,`medishop`.`order` bb"
				+" where aa.order_id = bb.order_id"
				+" and bb.order_seller_id = '"+sellerId+"'"
				+" and aa.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')";
				//+" group by a.order_status";
		logger.info("Get Sells Order Report Query : "+getOrderQuery);
		
		List<Object> mainDataList = new ArrayList<Object>();
		mainDataList =  (List<Object>) template.query(getOrderQuery,new ResultSetExtractor<List<Object>>(){  
			public List<Object> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Object> orderList=new ArrayList<Object>();  
				while(rs.next()){  
					Order order = new Order();
					order.setOrderid(String.valueOf(rs.getLong("order_id")));
					order.setOrdercreator(rs.getString("order_creator"));
					order.setOrderCreateDate(rs.getTimestamp("order_create_date"));
					order.setOrderdetails(rs.getString("order_details"));
					order.setOrderstatus(rs.getString("order_status"));
					order.setOrderprice(rs.getBigDecimal("order_price"));
					order.setOrderlastmodify(rs.getTimestamp("order_last_modify"));
					order.setOrderSellerId(rs.getLong("order_seller_id"));
					order.setEmergencyFlag(rs.getInt("order_emergencyFlag"));
					order.setShippingAddress(rs.getString("order_address"));
					order.setEmergencyPrice(rs.getBigDecimal("order_emergency_price"));
					order.setOrderPincode(rs.getString("order_pincode"));
					order.setOrderDiscountAmount(rs.getBigDecimal("order_discount_amount"));
					order.setOrderDeliveryAmount(rs.getBigDecimal("order_delivery_price"));
					order.setCancellationCmd(rs.getString("order_cancellationcmd"));
					orderList.add(order);					
				}  
				return orderList;  
			}  
		});
		dataMap.put("Order", mainDataList);
		
		final String getRefundQuery = "SELECT * "
				+" FROM `medishop`.`refun_order` as a,`medishop`.`order` b"
				+" where a.order_id = b.order_id"
				+" and b.order_seller_id = '"+sellerId+"'"
				+" and a.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'";
				//+" group by a.refund_status";
	
		logger.info("Get Sells Refud Query : "+getRefundQuery);
		mainDataList = null;		
		mainDataList =  (List<Object>) template.query(getRefundQuery,new ResultSetExtractor<List<Object>>(){  
			public List<Object> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Object> data=new ArrayList<Object>();  
				while(rs.next()){
					RefundOrder refundOrder = new RefundOrder();
					refundOrder.setRefundAmount(rs.getBigDecimal("order_amount"));
					refundOrder.setCashmemoNo(rs.getString("cashmemo_no"));
					refundOrder.setRefunRequestNo(rs.getString("refund_request_id"));
					refundOrder.setRefundInitiateTime(rs.getTimestamp("refund_initiate_time"));
					refundOrder.setRefundOrderlastmodify(rs.getTimestamp("refund_detail_last_modified"));
					refundOrder.setRefundInitiateUser(rs.getString("refund_initiate_user"));
					refundOrder.setRefundOrderid(String.valueOf(rs.getLong("order_id")));
					refundOrder.setRefundOrderSellerComt(rs.getString("refund_seller_comments"));
					refundOrder.setRefundOrderStatus(rs.getString("refund_status"));
					refundOrder.setRefundReason(rs.getString("refund_reason"));
					refundOrder.setSellerId(String.valueOf(rs.getLong("order_seller_id")));
					refundOrder.setInitiatorAddress(rs.getString("order_address").concat(". Pincode: "+rs.getString("order_pincode")));
					data.add(refundOrder);
				}  
				return data;  
			}  
		});
		dataMap.put("RefundOrder", mainDataList);
		mainDataList = null;
		return dataMap;
	}

	@Override
	public List<CancellationRequest> getCancelRequest(String requester,
			long fromDate, long toDate,String searchType,User user,List<String> cancelRequestIdList,List<String> cancelRequestStatusList) {
		StringBuffer strBuff = new StringBuffer();
		sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(fromDate == 0 || toDate == 0){
			toDate = System.currentTimeMillis();
			fromDate = (toDate - Long.valueOf(SpringPropertiesUtil.getProperty("CancellationRequest.DAY.DIFFR")));
		}
		
		if(searchType.equalsIgnoreCase("simple")){
			if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.seller"))){
				strBuff.append("select * from medishop.order_cancel_request where (cancel_requester = '"+requester+"')");
				strBuff.append(" and (cancel_requested_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')");
			}
			else if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.admin"))){
				strBuff.append("select * from medishop.order_cancel_request ");
				strBuff.append(" where (cancel_requested_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')");
			}
			strBuff.append(" order by cancel_requested_date desc ");			
			
		}
		else if(searchType.equalsIgnoreCase("advance")){
			if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.seller"))){
				strBuff.append("select * from medishop.order_cancel_request where (cancel_requester = '"+requester+"')");
				strBuff.append(" and (cancellation_request_status in ('"+TextUtils.join("','", cancelRequestStatusList)+"'))");
			}
			else if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.admin"))){
				strBuff.append("select * from medishop.order_cancel_request ");
				strBuff.append(" where (cancellation_request_status in ('"+TextUtils.join("','", cancelRequestStatusList)+"'))");
			}
			
			if(cancelRequestIdList != null && cancelRequestIdList.size()>0){
				strBuff.append(" and cancel_request_id in ('"+TextUtils.join("','", cancelRequestIdList)+"')");
			}
			else{
				strBuff.append(" and (cancel_requested_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')");
			}
			strBuff.append(" order by cancel_requested_date desc");
		}
		final String query = strBuff.toString();
		logger.info("Executing Method getCancelRequest(). Query : "+query);
		
		return (List<CancellationRequest>) template.query(query,new ResultSetExtractor<List<CancellationRequest>>(){  
			public List<CancellationRequest> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<CancellationRequest> list = new ArrayList<CancellationRequest>();  
				while(rs.next()){  
					CancellationRequest cancelOrderRequestObj=new CancellationRequest();
					cancelOrderRequestObj.setOrderid(String.valueOf(rs.getLong("order_id")));
					cancelOrderRequestObj.setCancellationRequestId(rs.getString("cancel_request_id"));
					cancelOrderRequestObj.setRequestInitiateTime(rs.getTimestamp("cancel_requested_date"));
					cancelOrderRequestObj.setLastModifiedTime(rs.getTimestamp("request_last_update"));
					cancelOrderRequestObj.setRequestStatus(rs.getString("cancellation_request_status"));
					cancelOrderRequestObj.setRequestReason(rs.getString("cancellation_reason"));
					cancelOrderRequestObj.setInitiateUser(rs.getString("cancel_requester"));
					cancelOrderRequestObj.setRefundRequestId(rs.getString("order_id"));
					cancelOrderRequestObj.setRequestApproverId(rs.getString("cancel_request_approver"));
					cancelOrderRequestObj.setRequestedHostIP(rs.getString("request_Ip_address"));
					list.add(cancelOrderRequestObj);
				}  
				return list;  
			}  
		});
	}

	@Override
	public Map<String, Integer> getOrderCount(String sellerId, long fromDate,
			long toDate) {
		// TODO Auto-generated method stub
		
		/*final String orderQuery = "SELECT count(*) as 'order_count', a.order_status"
				+" FROM `medishop`.`order` as a, `medishop`.`refun_order` b"
				+" where a.order_seller_id = '"+sellerId+"'"
				+" and a.order_create_date between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'"
				+" and  a.order_id not in (SELECT aa.order_id"
				+" FROM `medishop`.`refun_order` as aa,`medishop`.`order` bb"
				+" where aa.order_id = bb.order_id"
				+" and bb.order_seller_id = '"+sellerId+"')"
				//+" and aa.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "')"
				+" group by a.order_status";*/
		
		final String orderQuery = "SELECT count(*) as 'order_count', a.order_status FROM `medishop`.`order` as a, `medishop`.`refun_order` b where a.order_seller_id = '1000' and a.order_create_date between '2015-09-15 10:33:26' and '2017-10-15 10:33:26' and  a.order_id not in (SELECT aa.order_id FROM `medishop`.`refun_order` as aa,`medishop`.`order` bb where aa.order_id = bb.order_id and bb.order_seller_id = '1000') group by a.order_status";
		logger.info("Get Sells Order Report Query : "+orderQuery);
		
		Map<String, Integer> dataMap = new HashMap<String, Integer>();
		Map<String, Integer> finalDataMap = new HashMap<String, Integer>();		
		
		dataMap =  (Map<String, Integer>) template.query(orderQuery,new ResultSetExtractor<Map<String, Integer>>(){  
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				Map<String, Integer> data=new HashMap<String, Integer>();  
				while(rs.next()){
					String status = "";
					if(rs.getString("order_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("pending"))){
						status = "ORDER_PENDING";
					}
					else if(rs.getString("order_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("confirm"))){
						status = "ORDER_CONFIRM";
					}
					else if(rs.getString("order_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("canel"))){
						status = "ORDER_CANCEL";
					}
					else if(rs.getString("order_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("deliver"))){
						status = "ORDER_DELIVER";					
					}
					data.put(status, rs.getInt("order_count"));
				}  
				return data;  
			}  
		});
		finalDataMap.putAll(dataMap);
		
		/*final String refundOrderQuery = "SELECT count(*) as 'refund_order_count', a.refund_status"
				+" FROM `medishop`.`refun_order` as a,`medishop`.`order` b"
				+" where a.order_id = b.order_id"
				+" and b.order_seller_id = '"+sellerId+"'"
				+" and a.refund_initiate_time between '"+sdf.format(fromDate) +"' and '"+ sdf.format(toDate) + "'"
				+" group by a.refund_status";*/
		final String refundOrderQuery = "SELECT count(*) as 'refund_order_count', a.refund_status FROM `medishop`.`refun_order` as a,`medishop`.`order` b where a.order_id = b.order_id and b.order_seller_id = '1000' and a.refund_initiate_time between '2015-09-15 10:33:26' and '2017-10-15 10:33:26' group by a.refund_status";
		logger.info("Get Sells Refud Query : "+refundOrderQuery);
				
		dataMap =  (Map<String, Integer>) template.query(refundOrderQuery,new ResultSetExtractor<Map<String, Integer>>(){  
			public Map<String, Integer> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				Map<String, Integer> data=new HashMap<String, Integer>();  
				while(rs.next()){  
					String status = "";
					if(rs.getString("refund_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("pending"))){
						status = "REFUND_PENDING";
					}
					else if(rs.getString("refund_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("confirm"))){
						status = "REFUND_CONFIRM";
					}
					else if(rs.getString("refund_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("canel"))){
						status = "REFUND_CANCEL";
					}
					else if(rs.getString("refund_status").equalsIgnoreCase(SpringPropertiesUtil.getProperty("approve"))){
						status = "REFUND_REFUNDED";					
					}	
					data.put(status, rs.getInt("refund_order_count"));
				}  
				return data;  
			}  
		});
		finalDataMap.putAll(dataMap);
		return finalDataMap;
	}
}
