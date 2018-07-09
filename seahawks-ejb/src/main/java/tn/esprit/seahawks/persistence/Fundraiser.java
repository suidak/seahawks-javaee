package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@XmlRootElement
public class Fundraiser implements Serializable{
	private int id;
	private Date startDate;
	private Date endDate;
	private String title;
	private double goal;
	private String description;
	private boolean state;
	private double collectedDonations;
	private User launcher;
	private List<FunDonation> donations;
	private String location;

	
	public Fundraiser() {
		this.state=true;
		this.startDate= new Date();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	
	//@Temporal(TemporalType.TIMESTAMP)
	public Date getStartDate() {
		return startDate;
	}

	//@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}


	public String getTitle() {
		return title;
	}

	public double getGoal() {
		return goal;
	}

	public String getDescription() {
		return description;
	}


	public boolean getState() {
		return state;
	}
	
	

	public double getCollectedDonations() {
		return collectedDonations;
	}

	
	
	@ManyToOne
	public User getLauncher() {
		return launcher;
	}
	
	//@XmlTransient
	@JsonBackReference
	@OneToMany(mappedBy="fundraiser", cascade= CascadeType.REMOVE/*, fetch=FetchType.EAGER*/)
	public List<FunDonation> getDonations() {
		return donations;
	}
	
	public String getLocation() {
		return location;
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



	public void setTitle(String title) {
		this.title = title;
	}
	

	public void setGoal(double goal) {
		this.goal = goal;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}
	

	public void setState(boolean state) {
		this.state = state;
	}

	public void setCollectedDonations(double collectedDonations) {
		this.collectedDonations = collectedDonations;
	}
	
	public void setLauncher(User launcher) {
		this.launcher = launcher;
	}
	

	public void setDonations(List<FunDonation> donations) {
		this.donations = donations;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fundraiser other = (Fundraiser) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
