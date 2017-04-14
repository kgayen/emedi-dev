package com.application.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class DeliverOrderRequest implements Serializable {

	private static final long serialVersionUID = -5058721488513470062L;
	private List<BigInteger> confirmOrders;
	private String status;
	private String cancellationNote;
	
	public String getCancellationNote() {
		return cancellationNote;
	}
	public void setCancellationNote(String cancellationNote) {
		this.cancellationNote = cancellationNote;
	}
	public List<BigInteger> getConfirmOrders() {
		return confirmOrders;
	}
	public void setConfirmOrders(List<BigInteger> confirmOrders) {
		this.confirmOrders = confirmOrders;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{confirmOrders:"+confirmOrders+",status:"+status+",cancellationNote:"+cancellationNote+"}";
	}
}
