package tn.esprit.seahawks.interfaces;

import java.util.List;
import java.util.Map;

import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.FoundReview;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.LostReview;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.Review;

public interface ReviewServiceLocal {
	public boolean chooseRightAnswerL(int r);//CallForClosing check Instance of Child Upcasting
	public boolean chooseRightAnswerF(int r);//CallForClosing check Instance of Child Upcasting

	public Map<FoundReport,List<FoundReview>> showFoundReportAlongReviews();//?
	public Map<LostReport,List<LostReview>> showLostReportAlongReviews();//?
	///Unrevised
	public boolean removeFoundReview(int id);
	public boolean removeLostReview(int id);
	public boolean editLostReview(LostReview rev);
	public boolean editFoundReview(FoundReview rev);
	//Unrevised
	///SpecifiC FoundReport
	public boolean addFoundReview(FoundReview review);//ToDowncast Done
	///Specific LostReport
	public boolean toggleAnimalStatus(LostReport l); //Next
	public boolean addLostReview(LostReview review);//ToDowncast
	//fetching
	public List<FoundReview> showFoundReviewsByReport(FoundReport f);
	public List<LostReview> showLostReviewsByReport(LostReport f);
	
	
	




}
