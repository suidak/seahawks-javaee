package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
public class FosteringOffer implements Serializable{
	private int id;
	private Date startDate;
	private Date endDate;
	private String description;
	private Animal animal;
	private List<FosteringRequest> requests;
	
	public FosteringOffer() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getDescription() {
		return description;
	}

	@ManyToOne
	public Animal getAnimal() {
		return animal;
	}
	
	@OneToMany(mappedBy="offer")
	public List<FosteringRequest> getRequests() {
		return requests;
	}

	

	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	
	public void setRequests(List<FosteringRequest> requests) {
		this.requests = requests;
	}
	
	

}
