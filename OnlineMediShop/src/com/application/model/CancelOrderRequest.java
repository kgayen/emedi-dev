package com.application.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class CancelOrderRequest implements Serializable{

	private static final long serialVersionUID = -5858093496531082954L;
	private List<BigInteger> cancelOrders;
	private String status;
	private String cancellationNote;
	public List<BigInteger> getCancelOrders() {
		return cancelOrders;
	}
	public void setCancelOrders(List<BigInteger> cancelOrders) {
		this.cancelOrders = cancelOrders;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCancellationNote() {
		return cancellationNote;
	}
	public void setCancellationNote(String cancellationNote) {
		this.cancellationNote = cancellationNote;
	}
}
