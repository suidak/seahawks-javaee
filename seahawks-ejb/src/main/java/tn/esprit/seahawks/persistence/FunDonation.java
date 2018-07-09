package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
public class FunDonation implements Serializable{
	private int id;
	private double amount;
	private Date donationDate;
	private Fundraiser fundraiser;
	private Member donator;
	
	
	public FunDonation() {
		this.donationDate=new Date();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}
	
	
	public Date getDonationDate() {
		return donationDate;
	}

	
	@ManyToOne
	public Fundraiser getFundraiser() {
		return fundraiser;
	}
	
	@ManyToOne
	public Member getDonator() {
		return donator;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}


	public void setFundraiser(Fundraiser fundraiser) {
		this.fundraiser = fundraiser;
	}

	public void setDonator(Member donator) {
		this.donator = donator;
	}
	
	

}
