package tn.esprit.seahawks.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.AdoptionRequest;
import tn.esprit.seahawks.persistence.Member;

@Local
public interface AdoptionRequestServiceLocal {
	
	public int addAdoptionRequest(AdoptionRequest a);
	public boolean deleteAdoptionRequest(AdoptionRequest a);
	public boolean updateAdoptionRequest(AdoptionRequest a);
	public List<AdoptionRequest> getAllAdoptionRequestsByMember(Member m);
	public List<AdoptionRequest> getAllAdoptionRequestsByOffer(AdoptionOffer a);
	public int processAdoptionRequest(AdoptionRequest a, boolean decision);
}
