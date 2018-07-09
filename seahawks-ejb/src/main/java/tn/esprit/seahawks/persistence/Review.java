package tn.esprit.seahawks.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Inheritance(strategy = InheritanceType.JOINED)
public class Review implements Serializable{
	protected int id;
	protected String content;
	protected Date reviewDate;
	protected boolean isCorrectAnswer=false;
	protected User reviewer;
	
	public Review() {
		// TODO Auto-generated constructor stub
		this.reviewDate=new Date();	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public boolean isCorrectAnswer() {
		return isCorrectAnswer;
	}

	@ManyToOne
	public User getReviewer() {
		return reviewer;
	}
	


	//setters
	public void setId(int id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public void setCorrectAnswer(boolean isCorrectAnswer) {
		this.isCorrectAnswer = isCorrectAnswer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	
	
	
}
