package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity //aymen //aymen2

public class BreedingOffer implements Serializable{
	private int id;
	private String titre ;
	private String description;	
	private Animal animal;
	private Date date ;
	private boolean isClosed;
	private boolean isLocated ;
	private int idAnimal ;
//	private Date date ;
	private List<BreedingRequest> requests;
	
	
	public BreedingOffer() {
		// TODO Auto-generated constructor stub
	}
	
	
	//aymens
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	

	@OneToOne
	//@XmlTransient
	//a@JsonBackReference
	public Animal getAnimal() {
		return animal;
	}
	
	@OneToMany(mappedBy="offer",cascade=CascadeType.REMOVE, fetch=FetchType.EAGER )
	@XmlTransient
	public List<BreedingRequest> getRequests() {
		return requests;
	}

	
	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public void setRequests(List<BreedingRequest> requests) {
		this.requests = requests;
	}
    
	@Column(nullable=true)
	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Column(nullable=true)
	public boolean isClosed() {
		return isClosed;
	}


	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	@Column(nullable=true)
	public boolean isLocated() {
		return isLocated;
	}


	public void setLocated(boolean isLocated) {
		this.isLocated = isLocated;
	}

	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getIdAnimal() {
		return idAnimal;
	}


	public void setIdAnimal(int idAnimal) {
		this.idAnimal = idAnimal;
	}

	
	

	
	
	

	
	
	
	
	
	
	
	
}
