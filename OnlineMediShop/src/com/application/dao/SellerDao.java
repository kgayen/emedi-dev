package com.application.dao;

import java.util.List;

import com.application.model.ConfirmOrderRequest;
import com.application.model.DeliverOrderRequest;
import com.application.model.Seller;
import com.application.model.User;

public interface SellerDao {
	public List<Seller> getSeller(final String urlPath, final String queryValue);
	public int confrimOrder(final ConfirmOrderRequest confirmOrderRequest,final User user);
	public int cancelRequest(final DeliverOrderRequest deliverOrderRequest,final User user);
	public int deliverOrder(final DeliverOrderRequest deliverOrderRequest,final User user);
}