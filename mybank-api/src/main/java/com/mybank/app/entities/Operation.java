package com.mybank.app.entities;

import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mybank.app.validator.DoubleDigit;

/**
 *
 * <h1> This entity containing all information related to an operation</h1>
 *
 * @see com.mybank.app.validator.DoubleDigit
 *
 * */

public class Operation {

	private LocalDateTime date;

	private String id;

	@NotBlank(message = "The label is a required field")
	@Size(min = 1, max = 20)
	private String label;

	@DoubleDigit
	@NotNull(message = "The amount is a required field")
	private Double amount;

	@NotNull(message = "the direction is a required field")
	private Boolean isCredential;

	@NotNull(message = "The account is a required field")
	@Size(min = 21, max = 21)
	private String account;

	@NotNull(message = "The type of operation is a required field")
	private OperationType type;

	public Operation() {  }

	public Operation(String label, double amount, boolean isCredential, String account, String type, String id,
			LocalDateTime date) {
		this.label = label;
		this.amount = amount;
		this.isCredential = isCredential;
		this.account = account;
		this.type = OperationType.valueOf(type.toUpperCase());
		this.id = id;
		this.date = date;
	}

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public Double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Boolean getIsCredential() {
		return isCredential;
	}
	public void setIsCredential(boolean isCredential) {
		this.isCredential = isCredential;
	}

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	public OperationType getType() {
		return type;
	}
	public void setType(OperationType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Operation [  label=" + label + ", amount=" + amount + ", isCredential=" + isCredential + ", account="
				+ account + ", type=" + type + ", date=" + date + "]";
	}
}
