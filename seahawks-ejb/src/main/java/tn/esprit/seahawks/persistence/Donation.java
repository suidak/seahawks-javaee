package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
public class Donation implements Serializable{
	private int id;
	private Date donationDate;
	private Double amount;
	private Double tips;
	private String description;
	private Member donator;
	private Organisation organisation;
	
	public Donation() {
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDonationDate() {
		return donationDate;
	}

	public Double getAmount() {
		return amount;
	}

	public Double getTips() {
		return tips;
	}

	public String getDescription() {
		return description;
	}

	@ManyToOne
	public Member getDonator() {
		return donator;
	}
	
	@ManyToOne
	public Organisation getOrganisation() {
		return organisation;
	}
	
	//setters

	public void setId(int id) {
		this.id = id;
	}

	public void setDonationDate(Date donationDate) {
		this.donationDate = donationDate;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setTips(Double tips) {
		this.tips = tips;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDonator(Member donator) {
		this.donator = donator;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	
	
	
	
}
