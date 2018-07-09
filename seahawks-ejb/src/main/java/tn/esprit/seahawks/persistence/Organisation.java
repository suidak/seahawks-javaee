package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement

@DiscriminatorValue("organisation")


public class Organisation extends User implements Serializable{
	private String orgName;
	private Date foundDate;
	
	public Organisation() {
		
	}

	public String getOrgName() {
		return orgName;
	}

	
	@Temporal(TemporalType.DATE)
	public Date getFoundDate() {
		return foundDate;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}
	
	
}
