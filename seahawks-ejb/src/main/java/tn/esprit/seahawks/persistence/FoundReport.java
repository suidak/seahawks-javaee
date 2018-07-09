package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class FoundReport extends Report implements Serializable{
	//possibleAnimalClone.
	@Column(nullable=true)
	private String profilePic;
	private String status;//isWithMe+notWithMe+(WithOrganization=21DaysCronn&CloseReport).
	private Organisation careTaker=null;//Trigger+TimerService after 21Days.Done

	
	public FoundReport() {
	//	super();
	}
	
	


	public String getProfilePic() {
		return profilePic;
	}


	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@ManyToOne
	public Organisation getCareTaker() {
		return careTaker;
	}

	public void setCareTaker(Organisation careTaker) {
		this.careTaker = careTaker;
	}
/*
	@OneToMany(mappedBy="foundReport",fetch=FetchType.EAGER)
	public List<FoundReview> getReviews() {
		return reviews;
	}
	*/
	/*
	public void setReviews(List<FoundReview> reviews) {
		this.reviews = reviews;
	}*/
	
	@Override
	public String toString() {
		return "{ \"id\" : "+getId()+'}';
	}
	
	
	
}
