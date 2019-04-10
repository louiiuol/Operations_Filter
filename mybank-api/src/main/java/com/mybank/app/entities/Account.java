package com.mybank.app.entities;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * <h1> This entity containing all information related to an account</h1>
 *
 *
 * */
public class Account{

	@NotBlank
	@Size(min = 5, max = 5)
	private int bank;

	@NotBlank
	@Size(min = 5, max = 5)
	private int agency;

	@NotBlank
	@Size(min = 6, max = 6)
	private int score;

	@NotBlank
	@Size(min = 3, max = 3)
	private int series;

	@NotBlank
	@Size(min = 2, max = 2)
	private int subAccount;

	public Account() { }

	public Account(int bank, int agency, int score, int series, int underAccount) {
		this.bank = bank;
		this.agency = agency;
		this.score = score;
		this.series = series;
		this.subAccount = underAccount;
	}

	public int getBank() {
		return bank;
	}

	public void setBank(int bank) {
		this.bank = bank;
	}

	public int getAgency() {
		return agency;
	}

	public void setAgency(int agency) {
		this.agency = agency;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSeries() {
		return series;
	}

	public void setSeries(int series) {
		this.series = series;
	}

	public int getSubAccount() {
		return subAccount;
	}

	public void setSubAccount(int subAccount) {
		this.subAccount = subAccount;
	}

	@Override
	public String toString() { return ""+bank + agency + score + series + subAccount; }

}
