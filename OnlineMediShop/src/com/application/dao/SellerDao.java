package com.application.dao;

import java.util.List;
import java.util.Map;

import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;

import com.application.model.CancellationRequest;
import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Seller;
import com.application.model.User;

public interface SellerDao {
	public List<Seller> getSeller(final String urlPath, final String queryValue);
	public int confrimOrder(final ConfirmOrderRequest confirmOrderRequest,final User user);
	public int cancelRequest(final DeliverOrderRequest deliverOrderRequest,final User user);
	public int deliverOrder(final DeliverOrderRequest deliverOrderRequest,final User user);
	public List<List<Object>> getSellsReport(final String sellerId,final long fromDate,final long toDate);
	public Map<String,List<Object>> generateSellerReport(final String sellerId,final long fromDate,final long toDate);
	public Map<String,Integer> getOrderCount(final String sellerId,final long fromDate,final long toDate);
	public List<CancellationRequest> getCancelRequest(final String sellerId,final long fromDate,final long toDate,String searchType,User user,List<String> cancelRequestIdList,List<String> cancelRequestStatusList);
}