package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class FosteringRequest implements Serializable{
	private int id;
	private boolean isAccepted;
	private FosteringOffer offer;
	
	public FosteringRequest() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	@ManyToOne
	public FosteringOffer getOffer() {
		return offer;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public void setOffer(FosteringOffer offer) {
		this.offer = offer;
	}
	
	
	
}
