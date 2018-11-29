package com.qa.account.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Prize {

	@Id
	@GeneratedValue
	private Long prizeId;
	
	private int prizeAmount;
	
	private String time;
	
	public Prize() {
		
	}
	
	public Prize(int prizeAmount, String time) {
		this.prizeAmount = prizeAmount;
		this.time = time;
	}
	
	public Long getId() {
		return prizeId;
	}
	
	public int getPrizeAmount() {
		return prizeAmount;
	}
	
	public void setPrizeAmount(int prizeAmount) {
		this.prizeAmount = prizeAmount;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
}
