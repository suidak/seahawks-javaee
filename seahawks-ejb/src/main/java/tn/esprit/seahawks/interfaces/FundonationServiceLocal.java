package tn.esprit.seahawks.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.stripe.model.Charge;

import tn.esprit.seahawks.persistence.FunDonation;
import tn.esprit.seahawks.persistence.Member;


@Local
public interface FundonationServiceLocal {
	
	public boolean donate(FunDonation f,Map<String, Object> chargeParams, int fid, int mid);  // From my stripe client
	public List<FunDonation> getDonationsByMember(Member m); //	POST
	public Map<Integer,Double> getSumDonationsByMember();	 // GET	  /bymember
	public Map<Integer,Double> getAverageDonationsByFundraiser();  // GET	/avgbyfundraiser
	public Double getAverageDonations();  		//GET	/average
	public Long getNbrDonatorsByCountry(String country);  		//GET /donators?country=Tunisia
	public Double getAverageDonationsByCountry(String country);		 //GET  /avgdonations?country=Tunisia
}
