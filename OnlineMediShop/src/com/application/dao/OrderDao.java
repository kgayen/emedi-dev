package com.application.dao;

import java.util.List;

import org.json.simple.JSONObject;

import com.application.exception.CustomGenericException;
import com.application.model.Notification;
import com.application.model.Order;
import com.application.model.OrderSearchDao;
import com.application.model.OrderStatus;
import com.application.model.OrderWrapper;
import com.application.model.RefundOrderRequest;
import com.application.model.RefundOrderStatus;
import com.application.model.ReturnValidationRequest;
import com.application.model.SearchRefundOrder;
import com.application.model.User;

public interface OrderDao {
	public int saveOrderDetails(Order order);
	public void OrderStoreProcService(Notification notification, OrderStatus orderStatus);
	public List<Order> getOrderList(String urlPath, String queryValue);
	public List<OrderWrapper> getWrapperOrderList(String urlPath, String queryValue);
	public int updateOrderDetails(Object[] fieldValues,String[] fieldNames,int[] fieldDataTypes,String tableName
			,String whereClause,List<String> whereClauseFieldNames);
	public List<OrderWrapper> getSearchOrderList(final OrderSearchDao jsonData, User user);
	public JSONObject validateRefundOrder(final ReturnValidationRequest validationRequest, User user);
	public JSONObject saveRefundOrderRequest(final RefundOrderRequest refundOrderRequest, User user);
	public JSONObject searchRefundOrders(final SearchRefundOrder searchRefundOrder, User user);
	public List<RefundOrderStatus> getRefundOrderStatus(String refundOrderid);
	public int insertNotificationAndStatusRefund(final RefundOrderRequest refundOrderRequest) throws Exception;
	public int orderStatusBulkInsert(final List<OrderStatus> orderSatatusList) throws Exception;
}