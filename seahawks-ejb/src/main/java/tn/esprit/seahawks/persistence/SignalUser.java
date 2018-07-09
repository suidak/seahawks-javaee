package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class SignalUser implements Serializable {

	private int id;
	private User doer;
	private User signaled;
	private MotiveEnum motive;
	private String otherContent;
	private Date  signalDate;

	//Getters
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	@OneToOne
	//@XmlTransient
	public User getDoer() {
		return doer;
	}

	@OneToOne
	//@XmlTransient
	public User getSignaled() {
		return signaled;
	}

	
	@Enumerated(EnumType.STRING)
	public MotiveEnum getMotive() {
		return motive;
	}

	public String getOtherContent() {
		return otherContent;
	}
	
	public Date getSignalDate() {
		return signalDate;
	}

	
	//Setters

	
	public void setSignalDate(Date signalDate) {
		this.signalDate = signalDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoer(User doer) {
		this.doer = doer;
	}

	public void setSignaled(User signaled) {
		this.signaled = signaled;
	}

	public void setMotive(MotiveEnum motive) {
		this.motive = motive;
	}

	public void setOtherContent(String otherContent) {
		this.otherContent = otherContent;
	}

	@Override
	public String toString() {
		return "SignalUser [id=" + id + ", doer=" + doer + ", signaled=" + signaled + ", motive=" + motive
				+ ", otherContent=" + otherContent + ", signalDate=" + signalDate + "]";
	}
	
	
	
}
