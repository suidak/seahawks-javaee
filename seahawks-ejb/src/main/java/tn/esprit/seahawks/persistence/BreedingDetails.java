package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class BreedingDetails implements Serializable {

	private int id ;
	private Date dateConfirmed ;
	private Date dateAction ;
	private Date dateBreeding ;
	private Statut statut ;
	private int BabiesNumber ;
	private Address addressBreeding;
	private BreedingRequest breedingRequest ;
	
	/*
	public BreedingDetails(Date dateConfirmed, Statut statut, BreedingRequest breedingRequest) {
		super();
		this.dateConfirmed = dateConfirmed;
		this.statut = statut;
		this.breedingRequest = breedingRequest;
	}
	*/
	
	public BreedingDetails(Date dateConfirmed, Statut statut, Address addressBreeding,
			BreedingRequest breedingRequest) {
		super();
		this.dateConfirmed = dateConfirmed;
		this.statut = statut;
		this.addressBreeding = addressBreeding;
		this.breedingRequest = breedingRequest;
	}


	public BreedingDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	public Date getDateConfirmed() {
		return dateConfirmed;
	}
	public void setDateConfirmed(Date dateConfirmed) {
		this.dateConfirmed = dateConfirmed;
	}
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	public Date getDateAction() {
		return dateAction;
	}
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}
	@Enumerated(EnumType.STRING)
	public Statut getStatut() {
		return statut;
	}
	public void setStatut(Statut statut) {
		this.statut = statut;
	}
	public int getBabiesNumber() {
		return BabiesNumber;
	}
	public void setBabiesNumber(int babiesNumber) {
		BabiesNumber = babiesNumber;
	}
	@OneToOne
	public Address getAddressBreeding() {
		return addressBreeding;
	}
	public void setAddressBreeding(Address addressBreeding) {
		this.addressBreeding = addressBreeding;
	}
	@OneToOne
	@JsonBackReference
	//@XmlTransient
	public BreedingRequest getBreedingRequest() {
		return breedingRequest;
	}
	public void setBreedingRequest(BreedingRequest breedingRequest) {
		this.breedingRequest = breedingRequest;
	}
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	public Date getDateBreeding() {
		return dateBreeding;
	}
	public void setDateBreeding(Date dateBreeding) {
		this.dateBreeding = dateBreeding;
	}
	
	
	
	
	
	
	
	
}
