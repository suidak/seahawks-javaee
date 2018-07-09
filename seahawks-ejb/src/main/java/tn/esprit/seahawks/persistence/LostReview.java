package tn.esprit.seahawks.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class LostReview extends Review implements Serializable{
	
	protected LostReport reportLost;
	protected ReviewStatus status;

	@ManyToOne
	public LostReport getReportLost() {
		return reportLost;
	}

	public void setReportLost(LostReport reportLost) {
		this.reportLost = reportLost;
	}

	public ReviewStatus getStatus() {
		return status;
	}

	public void setStatus(ReviewStatus status) {
		this.status = status;
	}
	
	 

}
