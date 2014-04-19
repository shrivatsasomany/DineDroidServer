package com.main.dinedroid.models;

import java.io.Serializable;

public class Restore implements Serializable {
	
	private boolean isOccupied;
	private Order o;
	private Integer waiterId;
	
	public Restore(boolean isOccupied, Order o, Integer waiterId) {
		this.isOccupied = isOccupied;
		this.o = o;
		this.waiterId = waiterId;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public Order getOrder() {
		return o;
	}

	public void setOrder(Order o) {
		this.o = o;
	}

	public Integer getWaiterId() {
		return waiterId;
	}

	public void setWaiterId(Integer waiterId) {
		this.waiterId = waiterId;
	}
	
}
