package tn.esprit.seahawks.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.BreedingRequest;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Walkings;

@Local
public interface BreedingRequestServiceLocal {
	
	//memberRequest
	public int AddBreedingRequest(int idb, int ida, String des);//
	public void deleteBreedingRequest(BreedingRequest breedingRequest);//
	public void editBreedingRequest(BreedingRequest breedingRequest);//
	//public List<BreedingRequest> showBreedingRequest(BreedingRequest breedingRequest);
	public List<BreedingRequest> showBreedingRequestByMember(int id);//

	//public List<BreedingRequest> getAllBreedingRequest(Member memberRequest);
	
	////MembreOffer 
	public List<BreedingRequest> GetAllBreedingRequestByOffer(int id);//
	public void AcceptBreedingRequest( int id);//
	public void DeclineBreedingRequest(int id);//
	//public BreedingRequest showBreedingRequestByStatut(Member memberOffer);
	public List<Animal> showAnimalinBreedingRequest(BreedingRequest breedingRequest);//
	public List<Member> showMemberinBreedingRequest(BreedingRequest breedingRequest);//
	public int DistanceBetweenOfferandRequest(BreedingRequest breedingRequest);//
	public int DistanceBetweenOfferandRequest1(Walkings walking);//
	public Address showAdressRequest(BreedingRequest breedingRequest);//
	public List<Walkings> getAllBreedingOffer(Walkings walking);
	   public void mail (Member member);
		public BreedingRequest getBreedingRequestBybr(int id);
		
		public BreedingRequest GetAllBreedingRequestByOfferD(int id);
		public boolean verif( int ido);
		
		public BreedingOffer GetofferbyRequest(int id) ;
		
	//public Boolean haveOldBreeding(Animal animal);
	
	
	
	
	
	
	
	
	
	
	
	
	
	//stat 
	
	
		
		
		
	
	
	
	
	
	
	
	

}
