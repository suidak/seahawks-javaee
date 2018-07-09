package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.*;
import javax.websocket.ClientEndpoint;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class BreedingRequest implements Serializable{
	private int id;
	private Statut status;
	private String Description ;
	private BreedingOffer offer;
	private Animal animal ;

	
	public BreedingRequest() {
		// TODO Auto-generated constructor stub
	}

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}


	
	@ManyToOne
	@JsonBackReference
	public BreedingOffer getOffer() {
		return offer;
	}
	
	
	

	public void setId(int id) {
		this.id = id;
	}


	public void setOffer(BreedingOffer offer) {
		this.offer = offer;
	}

	

    @Column(columnDefinition = "varchar(32) default 'inProgress'")
    @Enumerated(EnumType.STRING)
	public Statut getStatus() {
		return status;
	}


	public void setStatus(Statut status) {
		this.status = status;
	}

    @Column(nullable=true)
	public String getDescription() {
		return Description;
	}


	public void setDescription(String description) {
		Description = description;
	}

	@OneToOne
	public Animal getAnimal() {
		return animal;
	}


	public void setAnimal(Animal animal) {
		this.animal = animal;
	}


	
	
	
	
	
	
	
	

}
