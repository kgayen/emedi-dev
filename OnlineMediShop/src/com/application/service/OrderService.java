package com.application.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.application.dao.OrderDao;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderSearchDao;
import com.application.model.OrderStatus;
import com.application.model.OrderWrapper;
import com.application.model.RefundOrder;
import com.application.model.RefundOrderRequest;
import com.application.model.RefundOrderStatus;
import com.application.model.ReturnValidationRequest;
import com.application.model.SearchRefundOrder;
import com.application.model.Seller;
import com.application.model.User;
import com.application.utility.OrderUtility;
import com.application.utility.SpringPropertiesUtil;
import com.opensymphony.xwork2.util.TextUtils;

import org.json.simple.JSONObject;

public class OrderService extends  BaseService implements OrderDao {
	
	private JdbcTemplate template;
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	  
	public void setTemplate(JdbcTemplate template) {  
		this.template = template;  
	}
	
	@Autowired
	UserService userService;
	@Autowired
	SellerService sellerService;
	@Autowired
	SimpleDateFormat sdf;
	@Autowired
	RefundOrder refundOrder;
	@Autowired
	RefundOrderStatus refundOrderStatus;
	//@Autowired
	//NotificationService notificationService;

	public int saveOrderDetails(final Order order) {
		final String query="INSERT INTO medishop.order "
				+"(order_creator,order_create_date,order_details,order_pec_image,order_status,order_price,order_last_modify,order_cancellationcmd,order_emergencyFlag,"
				+ "order_address,order_emergency_price,order_pincode,order_discount_amount,order_delivery_price) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps = connection.prepareStatement(query);
		            ps.setString(1, order.getOrdercreator());
		            ps.setString(2, String.valueOf(order.getOrderlastmodify()));
		            ps.setString(3, order.getOrderdetails());
		            ps.setBlob(4, order.getOrderpecimage());
		            ps.setString(5, order.getOrderstatus());
		            //ps.setDouble(6, order.getOrderprice());
		            ps.setBigDecimal(6, order.getOrderprice());
		            ps.setString(7, String.valueOf(order.getOrderlastmodify()));
		            ps.setString(8, order.getCancellationCmd());
		            ps.setInt(9, order.getEmergencyFlag());
		            ps.setString(10, order.getShippingAddress());
		            ps.setBigDecimal(11, order.getEmergencyPrice());
		            ps.setString(12, order.getOrderPincode());
		            ps.setBigDecimal(13, order.getOrderDiscountAmount());
		            ps.setBigDecimal(14, order.getOrderDeliveryAmount());
		            return ps;
		        }
		    },
		    keyHolder);
		return keyHolder.getKey().intValue();
	}

	public void OrderStoreProcService(Notification notification, OrderStatus orderStatus){
		final String procedureCall = "{call procedure_Status_Notifiction(?, ?, ?, ?, ?, ?)}";
		Connection connection = null;
		String statusModifier = orderStatus.getOrdermodifier();
		if(notification.getNotifyuser().equalsIgnoreCase(orderStatus.getOrdermodifier())){
			statusModifier = notification.getNotifyuser();
		}
		try {
			//Get Connection instance from dataSource
			connection = template.getDataSource().getConnection();
			CallableStatement callableSt = connection.prepareCall(procedureCall);
			callableSt.setLong(1, orderStatus.getOrderid());
			callableSt.setString(2, orderStatus.getOrderstatus());
			callableSt.setString(3, notification.getNotifyuser());
			callableSt.setString(4, notification.getNotificationText());
			callableSt.setString(5, orderStatus.getOrderPlacedIP());
			callableSt.setString(6, statusModifier);
			//Call Stored Procedure
			callableSt.executeUpdate();
		}catch (SQLException e) {
			logger.info("Exception throws from OrderStoreProcService function."
					+" Exception Message: "+e.getMessage());
		}
	}

	public List<Order> getOrderList(String urlPath, String queryValue) {
		logger.info("Executing Method getOrderList().");
		String strQuery="";
		if(urlPath.equalsIgnoreCase("getOrderByUser")){
			strQuery = "select * from medishop.order where order_creator = '"+queryValue+"'";
		}
		else if(urlPath.equalsIgnoreCase("getOrderByID")){
			strQuery = "select * from medishop.order where order_id = "+queryValue;
		}
		else if(urlPath.equalsIgnoreCase("getOrderByMobile")){
			strQuery = "select * from medishop.order where order_creator = select user_id from user where user_mobile = '"+queryValue+"'";
		}
		else if(urlPath.equalsIgnoreCase("getOrderBySeller")){
			strQuery = "select * from medishop.order where order_seller_id ="+queryValue+"";
		}
		final String query = strQuery+" order by order_id desc";
		logger.info("Executing Method getOrderList(). Query : "+query);
		return (List<Order>) template.query(query,new ResultSetExtractor<List<Order>>(){  
			public List<Order> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Order> list=new ArrayList<Order>();  
				while(rs.next()){  
					Order orderObj=new Order();
					orderObj.setOrderid(String.valueOf(rs.getLong("order_id")));
					orderObj.setOrdercreator(rs.getString("order_creator"));
					orderObj.setOrderCreateDate(rs.getTimestamp("order_create_date"));
					orderObj.setOrderdetails(rs.getString("order_details"));
					orderObj.setOrderstatus(rs.getString("order_status"));
					//orderObj.setOrderprice(rs.getDouble("order_price"));
					orderObj.setOrderprice(rs.getBigDecimal("order_price"));
					orderObj.setOrderlastmodify(rs.getTimestamp("order_last_modify"));
					orderObj.setOrderSellerId(rs.getLong("order_seller_id"));
					orderObj.setShippingAddress(rs.getString("order_address"));
					orderObj.setEmergencyPrice(rs.getBigDecimal("order_emergency_price"));
					orderObj.setOrderPincode(rs.getString("order_pincode"));
					orderObj.setOrderDiscountAmount(rs.getBigDecimal("order_discount_amount"));
					list.add(orderObj);  
				}  
				return list;  
			}  
		});
	}
	
	public List<Order> getBulkOrderList(String urlPath, List<BigInteger> orders) {
		String strQuery="";
		String queryValue = "";
		if(urlPath.equalsIgnoreCase("getBulkOrder")){
			queryValue = OrderUtility.getOrderQueryString(orders);
			strQuery = "select * from medishop.order where order_id in ("+queryValue+")";
		}
		final String query = strQuery+" order by order_id desc";
		logger.info("Executing Method getBulkOrderList(). Query : "+query);
		return (List<Order>) template.query(query,new ResultSetExtractor<List<Order>>(){  
			public List<Order> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<Order> list=new ArrayList<Order>();  
				while(rs.next()){  
					Order orderObj=new Order();
					orderObj.setOrderid(String.valueOf(rs.getLong("order_id")));
					orderObj.setOrdercreator(rs.getString("order_creator"));
					orderObj.setOrderCreateDate(rs.getTimestamp("order_create_date"));
					orderObj.setOrderdetails(rs.getString("order_details"));
					orderObj.setOrderstatus(rs.getString("order_status"));
					//orderObj.setOrderprice(rs.getDouble("order_price"));
					orderObj.setOrderprice(rs.getBigDecimal("order_price"));
					orderObj.setOrderlastmodify(rs.getTimestamp("order_last_modify"));
					orderObj.setOrderSellerId(rs.getLong("order_seller_id"));
					orderObj.setShippingAddress(rs.getString("order_address"));
					orderObj.setEmergencyPrice(rs.getBigDecimal("order_emergency_price"));
					orderObj.setOrderPincode(rs.getString("order_pincode"));
					orderObj.setOrderDiscountAmount(rs.getBigDecimal("order_discount_amount"));
					list.add(orderObj);  
				}  
				return list;  
			}  
		});
	}
	
	public List<OrderWrapper> getWrapperOrderList(String urlPath, String queryValue) {
		logger.info("Executing Method getWrapperOrderList().");
		String strQuery="";
		if(urlPath.equalsIgnoreCase("getOrderByUser")){
			strQuery = "select * from medishop.order where order_creator = '"+queryValue+"'";
		}
		else if(urlPath.equalsIgnoreCase("getOrderByID")){
			strQuery = "select * from medishop.order where order_id = "+queryValue;
		}
		else if(urlPath.equalsIgnoreCase("getOrderByMobile")){
			strQuery = "select * from medishop.order where order_creator = select user_id from user where user_mobile = '"+queryValue+"'";
		}
		else if(urlPath.equalsIgnoreCase("getOrderBySeller")){
			long rangeTimestamp = System.currentTimeMillis()-Long.valueOf(Long.valueOf(SpringPropertiesUtil.getProperty("sellerDataFoundHours"))*60*60*1000);
			strQuery = "select * from medishop.order where order_seller_id ="+queryValue+" and (order_create_date between '"+sdf.format(rangeTimestamp) +"' and '"+ sdf.format(System.currentTimeMillis()) + "')";
		}
		final String query = strQuery + " order by order_id desc";
		logger.info("Executing Method getWrapperOrderList(). Query : "+query);
		return (List<OrderWrapper>) template.query(query,new ResultSetExtractor<List<OrderWrapper>>(){  
			public List<OrderWrapper> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<OrderWrapper> list = new ArrayList<OrderWrapper>();  
				while(rs.next()){  
					OrderWrapper orderWrapperObj=new OrderWrapper();
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
					User user = new User();
					user = userService.getDataListByUserId(rs.getString("order_creator")).get(0);
					Seller seller = new Seller();
					seller = sellerService.getSeller("getSellerById", String.valueOf(rs.getLong("order_seller_id"))).get(0);
					List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
					orderStatusList = getOrderStatus(String.valueOf(rs.getLong("order_id")));
					orderWrapperObj.setOrder(order);
					orderWrapperObj.setUser(user);
					orderWrapperObj.setSeller(seller);
					orderWrapperObj.setOrderstatus(orderStatusList);
					list.add(orderWrapperObj);
				}  
				return list;  
			}  
		});
	}
	
	public InputStream getImageStream(String queryValue){
		final String query = "select order_pec_image from medishop.order where order_id = "+queryValue;
		logger.info("Executing Method getImageStream(). Query : "+query);
		return (InputStream) template.query(query,new ResultSetExtractor<InputStream>(){  
			public InputStream extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				InputStream image=null;  
				while(rs.next()){
					image = rs.getBinaryStream("order_pec_image");  
				} 
				return image;
			}  
		});
	}
	
	public List<OrderStatus> getOrderStatus(String orderid){
		logger.info("Executing Method getOrderStatus().");
		final String query = "select * from medishop.order_status where order_id = "+orderid+" order by status_id asc";
		logger.info("Executing Method getOrderStatus(). Query : "+query);
		return (List<OrderStatus>) template.query(query,new ResultSetExtractor<List<OrderStatus>>(){  
			public List<OrderStatus> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<OrderStatus> list=new ArrayList<OrderStatus>();  
				while(rs.next()){  
					OrderStatus orderStatusObj=new OrderStatus();
					//order_id, order_action_status, order_action_timestamp, order_modifier_user, status_id
					orderStatusObj.setOrderstatus(rs.getString("order_action_status"));
					orderStatusObj.setOrderActionTimestamp(rs.getTimestamp("order_action_timestamp"));
					list.add(orderStatusObj);  
				}  
				return list;  
			}  
		});
	}
	
	public int updateOrderDetails(Object[] fieldValues, String[] fieldNames,
			int[] fieldDataTypes,String tableName,String whereClause,List<String> whereClauseFieldNames) {
		final String updateQuery = OrderUtility.buildUpdateQuery(fieldNames, tableName, whereClause,whereClauseFieldNames);
		logger.info("Executing Method updateOrderDetails(). UpdateQuery ="+updateQuery);
		int updateFlag = 0;
		updateFlag = template.update(updateQuery, fieldValues, fieldDataTypes);
		return updateFlag;
	}

	public List<OrderWrapper> getSearchOrderList(final OrderSearchDao jsonData, User user) {
		logger.info("Executing Method getSearchOrderList().");
		StringBuffer strBuff = new StringBuffer();
		if(user.getUser_role().equalsIgnoreCase("customer")){
			strBuff.append("select * from medishop.order where (order_creator = '"+user.getUser_id()+"' or order_creator = '"+user.getUser_mobile()+"')");
		}
		else if(user.getUser_role().equalsIgnoreCase("Seller")){
			strBuff.append("select * from medishop.order where (order_seller_id = "+user.getUser_id()+")");
		}
		else if(user.getUser_role().equalsIgnoreCase("Admin")){
			strBuff.append("select * from medishop.order where (order_creator = '"+user.getUser_id()+"' or order_creator = '"+user.getUser_mobile()+"')");
		}
		
		DateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(jsonData.getOrderSearchType().equalsIgnoreCase("ByDateRange")){
			strBuff.append(" and (order_create_date between '"+sdf.format(jsonData.getFromDate()) +"' and '"+ sdf.format(jsonData.getToDate()) + "')");
			strBuff.append(" order by order_create_date desc ");
		}
		else if(jsonData.getOrderSearchType().equalsIgnoreCase("ByOrderID")){
			strBuff.append(" and order_id in ("+TextUtils.join(",", jsonData.getOrderIdList())+")");
			strBuff.append(" order by order_create_date desc ");
		}
		else if(jsonData.getOrderSearchType().equalsIgnoreCase("ByAdvanceSearch")){
			strBuff.append(" and order_status in ('"+TextUtils.join("','", jsonData.getOrderStatusList())+"')");
			if(jsonData.getOrderFetchedBy().get(0).equalsIgnoreCase("ByDateOtion")){
				strBuff.append(" and (order_create_date between '"+sdf.format(jsonData.getFromDate()) +"' and '"+ sdf.format(jsonData.getToDate()) + "')");
			}
			else if(jsonData.getOrderFetchedBy().get(0).equalsIgnoreCase("ByOrderIdOtion")){
				strBuff.append(" and order_id in ("+TextUtils.join(",", jsonData.getOrderIdList())+")");
			}			
			strBuff.append(" order by order_create_date desc ");
		}
		/*else if(jsonData.getOrderSearchType().equalsIgnoreCase("getOrderBySeller")){
			//strQuery = "select * from medishop.order where order_seller_id ="+queryValue+"";
		}*/
		final String query = strBuff.toString();
		logger.info("Executing Method getSearchOrderList(). Query : "+query);
		return (List<OrderWrapper>) template.query(query,new ResultSetExtractor<List<OrderWrapper>>(){  
			public List<OrderWrapper> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<OrderWrapper> list = new ArrayList<OrderWrapper>();  
				while(rs.next()){  
					OrderWrapper orderWrapperObj=new OrderWrapper();
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
					User user = new User();
					user = userService.getDataListByUserId(rs.getString("order_creator")).get(0);
					Seller seller = new Seller();
					seller = sellerService.getSeller("getSellerById", String.valueOf(rs.getLong("order_seller_id"))).get(0);
					List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
					orderStatusList = getOrderStatus(String.valueOf(rs.getLong("order_id")));
					orderWrapperObj.setOrder(order);
					orderWrapperObj.setUser(user);
					orderWrapperObj.setSeller(seller);
					orderWrapperObj.setOrderstatus(orderStatusList);
					list.add(orderWrapperObj);
				}  
				return list;  
			}  
		});
	}
	
	@Autowired
	JSONObject jsonObject;
	@SuppressWarnings("unchecked")
	public JSONObject validateRefundOrder(final ReturnValidationRequest validationRequest, User user) {
		logger.info("Executing Method validateRefundOrder().");
		final String query = "SELECT orderview.*,ordercashmemoview.* FROM `medishop`.`order` as orderview , `medishop`.`order_cashmemo_details` as ordercashmemoview"
				+ " where (orderview.order_creator = '"+user.getUser_id()+"' or orderview.order_creator = '"+user.getUser_mobile()+"') and "
				+ "orderview.order_id = "+validationRequest.getOrderid()+" and ordercashmemoview.cashmemo_no = '"+validationRequest.getBillNo()+"'"
				+ " and orderview.order_id = ordercashmemoview.order_id"
				+ " and (select count(*) from `medishop`.`refun_order` as refunorderview where refunorderview.order_id = orderview.order_id and refunorderview.refund_status!='Cancel') = 0";
		logger.info("Executing Method validateRefundOrder(). Query : "+query);
		jsonObject.clear();
		jsonObject.put("status", "success");
		try{
			return (JSONObject) template.query(query,new ResultSetExtractor<JSONObject>(){  
				public JSONObject extractData(ResultSet rs) throws SQLException,  
					DataAccessException {
					rs.last();
					int rowCount = rs.getRow();
					rs.beforeFirst();
					if(rowCount >0){
						while(rs.next()){
							if(!rs.getString("order_status").equalsIgnoreCase("Deliver")){
								jsonObject.put("status", "invalid");
								jsonObject.put("errormsg", "Order only refund when it will be delivered. Current order status is "+rs.getString("order_status"));
								jsonObject.put("errorcode", "1122");
								return jsonObject;
							}
							long currentTimeStamp = System.currentTimeMillis();
							long ordertimestmp = rs.getTimestamp("order_create_date").getTime();
							int diffWeeks = (int) ((currentTimeStamp-ordertimestmp) / (7*24 * 60 * 60 * 1000));
							if(diffWeeks>0){
								jsonObject.put("status", "invalid");
								jsonObject.put("errormsg", "");
								jsonObject.put("errorcode", "1123");
								return jsonObject;
							}
							jsonObject.put("cashmemoDate", rs.getTimestamp("cashmemo_update_timestamp"));
							jsonObject.put("invoiceno", rs.getString("invoice_no"));
							double orderpriceAmount = rs.getBigDecimal("order_price").doubleValue();
							double discountPercentage = rs.getBigDecimal("order_discount_amount").doubleValue();
							double refundableAmount = orderpriceAmount-(orderpriceAmount*(discountPercentage/100));
							jsonObject.put("price", refundableAmount);
							jsonObject.put("orderid", rs.getLong("order_id"));
							jsonObject.put("cashmemono", rs.getString("cashmemo_no"));
							jsonObject.put("sellerid", rs.getLong("order_seller_id"));
						} 
					}
					else{
						jsonObject.put("status", "invalid");
						jsonObject.put("errormsg", "");
						jsonObject.put("errorcode", "1124");
						return jsonObject;
					}
					return jsonObject;  
				}  
			});
		}
		catch(Exception excep){
			jsonObject.put("status", "error");
			jsonObject.put("errormsg", excep.getMessage());
			if(excep.getMessage().indexOf("SQL syntax")!=-1){
				jsonObject.put("errorcode", "1113");
			}
			else{
				jsonObject.put("errorcode", "1120");
			}
			return jsonObject;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject saveRefundOrderRequest(final RefundOrderRequest refundOrderRequest, User user) {
		logger.info("Executing Method saveRefundOrderRequest().");
		final String query="INSERT INTO medishop.refun_order "
				+"(order_id,cashmemo_no,order_amount,refund_reason,refund_status,refund_initiate_time,refund_request_id,refund_initiate_user)"
				+ " VALUES"
				+ "(?,?,?,?,?,?,?,?)";
		logger.info("Executing Method saveRefundOrderRequest(). Query : "+query);
		jsonObject.clear();
		
		try{
			
			int updateFlag = template.update(
			    new PreparedStatementCreator() {
			        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			            PreparedStatement ps = connection.prepareStatement(query);
			            ps.setLong(1, new Long(refundOrderRequest.getRefundOrderid()));
			            ps.setString(2, refundOrderRequest.getRefundCashmemono());
			            ps.setBigDecimal(3, new BigDecimal(refundOrderRequest.getRefundAmount()));
			            ps.setString(4, refundOrderRequest.getReundReason());
			            ps.setString(5, refundOrderRequest.getRefundOrderStatus());
			            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			            ps.setString(7, refundOrderRequest.getRefundRequestId());
			            ps.setString(8, refundOrderRequest.getRefundInitiateUser());
			            return ps;
			        }
			    });
			if(updateFlag >0){
				jsonObject.put("status", "success");
				jsonObject.put("refundorderid", refundOrderRequest.getRefundOrderid());
				jsonObject.put("refundrequestid", refundOrderRequest.getRefundRequestId());
				
				final String procedureCall = "{call procedure_RefundStatus_Notification(?, ?, ?, ?, ?, ?, ?)}";
				logger.info("Executing Method saveRefundOrderRequest(). Query : "+procedureCall);
				Connection connection = null;
				connection = template.getDataSource().getConnection();
				CallableStatement callableSt = connection.prepareCall(procedureCall);
				callableSt.setLong(1, new Long(refundOrderRequest.getRefundOrderid()));
				callableSt.setString(2, refundOrderRequest.getRefundOrderStatus());
				callableSt.setString(3, refundOrderRequest.getRefundInitiateUser());
				callableSt.setString(4, refundOrderRequest.getRefundNotificattionText());
				callableSt.setString(5, refundOrderRequest.getRequestedHostIP());
				callableSt.setString(6, refundOrderRequest.getRefundRequestId());
				callableSt.setString(7, refundOrderRequest.getSellerID());
				int executeProcFlag = callableSt.executeUpdate();
				if(executeProcFlag !=0){
					jsonObject.put("message", "Notification and statu update successfully.");
				}
				else{
					jsonObject.put("message", "Notification and statu are not updated successfully due to unknown reason.");
				}
			}
			else{
				jsonObject.put("status", "invalid");
				jsonObject.put("errormsg", "Refund is not intiated due to unknown reason.");
				jsonObject.put("errorcode", "1122");
			}
			return jsonObject;
		}
		catch(Exception excep){
			jsonObject.put("status", "error");
			jsonObject.put("errormsg", excep.getMessage());
			if(excep.getMessage().indexOf("SQL syntax")!=-1){
				jsonObject.put("errorcode", "1113");
			}
			else{
				jsonObject.put("errorcode", "1120");
			}
			return jsonObject;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject searchRefundOrders(final SearchRefundOrder searchRefundOrder,final User user) {
		logger.info("Executing Method searchRefundOrders().");
		StringBuffer strBuff = new StringBuffer();
		if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.seller"))){
			strBuff.append("select * from medishop.refun_order as refundview,medishop.order orderview"
					+ " where (orderview.order_seller_id = '"+user.getUser_id()+"')");
		}
		else if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.customer"))){
			strBuff.append("select * from medishop.refun_order as refundview,medishop.order orderview"
					+ " where (orderview.order_creator = '"+user.getUser_id()+"' or"
					+ " orderview.order_creator = '"+user.getUser_mobile()+"')");
		}
		else if(user.getUser_role().equalsIgnoreCase(SpringPropertiesUtil.getProperty("user.admin"))){
			strBuff.append("select * from medishop.refun_order as refundview,medishop.order orderview");
			//strBuff.append(" where (refundview.refund_initiate_time between '"+sdf.format(searchRefundOrder.getFromDate()) +"' and '"+ sdf.format(System.currentTimeMillis()) + "')");
			strBuff.append(" where (refundview.refund_initiate_time between "+SpringPropertiesUtil.getProperty("RefundOrder.Search.Default.Admin")+" and NOW())");
		}
		
		if(searchRefundOrder.getRefundSearchType().equalsIgnoreCase("refundByDateOtion")){
			strBuff.append(" and (refundview.refund_initiate_time between '"+sdf.format(searchRefundOrder.getFromDate()) +"' and '"+ sdf.format(searchRefundOrder.getToDate()) + "')");
			strBuff.append(" and (refundview.refund_status in ('"+TextUtils.join("','", searchRefundOrder.getRefundOrderStatusList())+"'))");
		}
		else if(searchRefundOrder.getRefundSearchType().equalsIgnoreCase("refundByOrderIdOtion")){
			strBuff.append(" and refundview.refund_request_id in ("+TextUtils.join(",", searchRefundOrder.getRefundOrderIdList())+")");
		}
		
		strBuff.append(" and orderview.order_id = refundview.order_id order by refundview.refund_initiate_time");
		final String query=strBuff.toString();
		logger.info("Executing Method searchRefundOrders(). Query : "+query);
		
		jsonObject.clear();
		jsonObject.put("status", "success");
		try{
			return (JSONObject) template.query(query,new ResultSetExtractor<JSONObject>(){  
				public JSONObject extractData(ResultSet rs) throws SQLException,  
					DataAccessException {
						rs.last();
						int rowCount = rs.getRow();
						rs.beforeFirst();
						if(rowCount >0){
							List<RefundOrder> refundOrders = new ArrayList<RefundOrder>();
							while(rs.next()){
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
								refundOrder.setInitiatorMobileNo(user.getUser_mobile());
								refundOrder.setInitiatorAddress(rs.getString("order_address").concat(". Pincode: "+rs.getString("order_pincode")));
								refundOrder.setInitiatorEmailAddress(user.getUser_email());
								
								List<RefundOrderStatus> refundOrderStatusList = getRefundOrderStatus(rs.getString("refund_request_id"));
								refundOrder.setRefundOrderStatusList(refundOrderStatusList);
								refundOrders.add(refundOrder);
							}
							jsonObject.put("refundOrders", (Object)refundOrders);
						}
						else{
							jsonObject.put("status", "error");
							jsonObject.put("errormsg", "");
							jsonObject.put("errorcode", "1116");
						}
					return jsonObject;  
				}  
			});
		}
		catch(Exception excep){
			jsonObject.put("status", "error");
			jsonObject.put("errormsg", excep.getMessage());
			if(excep.getMessage().indexOf("SQL syntax")!=-1){
				jsonObject.put("errorcode", "1113");
			}
			else{
				jsonObject.put("errorcode", "1120");
			}
			return jsonObject;
		}
	}
	
	public List<RefundOrderStatus> getRefundOrderStatus(String refundOrderid){
		logger.info("Executing Method getRefundOrderStatus().");
		final String query = "select * from medishop.refund_status where refund_request_id = '"+refundOrderid+"' order by refund_status_id asc";
		logger.info("Executing Method getRefundOrderStatus(). Query : "+query);
		return (List<RefundOrderStatus>) template.query(query,new ResultSetExtractor<List<RefundOrderStatus>>(){  
			public List<RefundOrderStatus> extractData(ResultSet rs) throws SQLException,  
				DataAccessException {  
				List<RefundOrderStatus> list=new ArrayList<RefundOrderStatus>();  
				while(rs.next()){
					RefundOrderStatus refundOrderStatus=new RefundOrderStatus();
					refundOrderStatus.setRefundOrderstatus(rs.getString("refund_status"));
					refundOrderStatus.setRefundOrderActionTimestamp(rs.getTimestamp("refund_action_time"));
					refundOrderStatus.setRefundOrderid(rs.getLong("order_id"));
					refundOrderStatus.setRefundOrdermodifier(rs.getString("refund_modifier_user"));
					refundOrderStatus.setRefundOrderPlacedIP(rs.getString("refund_ip"));
					refundOrderStatus.setRefundOrderStatusId(rs.getLong("refund_status_id"));
					refundOrderStatus.setRefundRequestId(rs.getString("refund_request_id"));
					list.add(refundOrderStatus);  
				}
				return list;  
			}  
		});
	}
	
	public int insertNotificationAndStatusRefund(final RefundOrderRequest refundOrderRequest) throws Exception {
		logger.info("Executing Method insertNotificationAndStatusRefund().");
		int executeProcFlag = 0;
		try{
				final String procedureCall = "{call procedure_RefundStatus_Notification(?, ?, ?, ?, ?, ?, ?)}";
				logger.info("Executing Method insertNotificationAndStatusRefund(). Query : "+procedureCall);
				Connection connection = null;
				connection = template.getDataSource().getConnection();
				CallableStatement callableSt = connection.prepareCall(procedureCall);
				callableSt.setLong(1, new Long(refundOrderRequest.getRefundOrderid()));
				callableSt.setString(2, refundOrderRequest.getRefundOrderStatus());
				callableSt.setString(3, refundOrderRequest.getRefundInitiateUser());
				callableSt.setString(4, refundOrderRequest.getRefundNotificattionText());
				callableSt.setString(5, refundOrderRequest.getRequestedHostIP());
				callableSt.setString(6, refundOrderRequest.getRefundRequestId());
				callableSt.setString(7, refundOrderRequest.getSellerID());
				executeProcFlag = callableSt.executeUpdate();
		}
		catch(Exception excep){
			logger.info("Executing Method insertNotificationAndStatusRefund(). Exception : "+excep.getMessage());
			throw excep;
		}
		return executeProcFlag;
	}
	
	public int orderStatusBulkInsert(final List<OrderStatus> orderSatatusList) throws Exception{
		int insertStatus = 0;
		try{
			StringBuilder strBuilder = new StringBuilder("insert into `medishop`.`order_status`(order_id,order_action_status,order_modifier_user,ordered_ip) values ");
			for(OrderStatus orderStatus : orderSatatusList){
				strBuilder.append("(");
				strBuilder.append(orderStatus.getOrderid());
				strBuilder.append(",");
				strBuilder.append("'");
				strBuilder.append(orderStatus.getOrderstatus());
				strBuilder.append("'");
				strBuilder.append(",");
				strBuilder.append("'");
				strBuilder.append(orderStatus.getOrdermodifier());
				strBuilder.append("'");
				strBuilder.append(",");
				strBuilder.append("'");
				strBuilder.append(orderStatus.getOrderPlacedIP());
				strBuilder.append("'");
				strBuilder.append("),");
			}
			strBuilder.delete(strBuilder.length()-1, strBuilder.length());
			strBuilder.append(";");
			final String query = strBuilder.toString();
			System.out.println("orderStatusBulkInsert : "+query);
			insertStatus = template.update(query);
		}
		catch(Exception e){
			throw e;
		}
		return insertStatus;
	}
}