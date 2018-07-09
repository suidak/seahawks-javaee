package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class AdoptionOffer implements Serializable {
	private int id;
	private Date offerDate;
	private double price;
	private Animal animal;
	private List<AdoptionRequest> requests;
	
	public AdoptionOffer() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	@Temporal(TemporalType.DATE)
	public Date getOfferDate() {
		return offerDate;
	}

	public double getPrice() {
		return price;
	}


	@ManyToOne
	public Animal getAnimal() {
		return animal;
	}
	
	
	@OneToMany(mappedBy="offer", cascade = {CascadeType.REMOVE}, fetch=FetchType.EAGER)
	@JsonBackReference
	public List<AdoptionRequest> getRequests() {
		return requests;
	}

	

	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	public void setRequests(List<AdoptionRequest> requests) {
		this.requests = requests;
	}
	
}
