package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
public class FoundReview extends Review implements Serializable{
	protected FoundReport foundReport;
	protected LostReport existingLostReportMatch;
	protected FoundStatus status;
	public FoundReview() {
		// TODO Auto-generated constructor stub
	}
	@ManyToOne
	public FoundReport getFoundReport() {
		return foundReport;
	}
	public void setFoundReport(FoundReport foundReport) {
		this.foundReport = foundReport;
	}
	@ManyToOne
	public LostReport getExistingLostReportMatch() {
		return existingLostReportMatch;
	}
	public void setExistingLostReportMatch(LostReport existingLostReportMatch) {
		this.existingLostReportMatch = existingLostReportMatch;
	}
	
	public FoundStatus getStatus() {
		return status;
	}
	public void setStatus(FoundStatus status) {
		this.status = status;
	}
	
}
