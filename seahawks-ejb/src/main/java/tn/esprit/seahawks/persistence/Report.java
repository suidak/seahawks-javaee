package tn.esprit.seahawks.persistence;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Inheritance(strategy = InheritanceType.JOINED)
public class Report {
	protected int id;
	protected User reporterUser;
	protected String title;
	protected String Description;
	protected Date date;
	protected Boolean isClosed=false;
	protected String reportCategory;
	protected ReportLocalisation localisation;
	
	public Report() {
		//super();
		this.date = new Date();
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne
	public User getReporterUser() {
		return reporterUser;
	}
	public void setReporterUser(User reporterUser) {
		this.reporterUser = reporterUser;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Boolean getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	public String getReportCategory() {
		return reportCategory;
	}
	public void setReportCategory(String reportCategory) {
		this.reportCategory = reportCategory;
	}
	@OneToOne(cascade= {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
	public ReportLocalisation getLocalisation() {
		return localisation;
	}
	public void setLocalisation(ReportLocalisation localisation) {
		this.localisation = localisation;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
}
