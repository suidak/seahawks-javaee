package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class AdoptionRequest implements Serializable{
	private int id;
	private String description;
	private AdoptionRequestStatus status;
	private AdoptionOffer offer;
	private Member adopter;
	
	public AdoptionRequest() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	
	@Enumerated(EnumType.STRING)
	public AdoptionRequestStatus getStatus() {
		return status;
	}
	
	@ManyToOne  //unidirect
	//@JsonManagedReference
	public AdoptionOffer getOffer() {
		return offer;
	}
	
	@ManyToOne
	public Member getAdopter() {
		return adopter;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatus(AdoptionRequestStatus status) {
		this.status = status;
	}

	public void setOffer(AdoptionOffer offer) {
		this.offer = offer;
	}
	public void setAdopter(Member adopter) {
		this.adopter = adopter;
	}
	
	
	
}
