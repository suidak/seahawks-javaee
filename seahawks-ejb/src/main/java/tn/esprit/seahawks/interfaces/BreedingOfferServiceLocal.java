package tn.esprit.seahawks.interfaces;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Member;

@Local
public interface BreedingOfferServiceLocal {
	
	
	public BreedingOffer addBreedingOffer(int id, String des,String titre);//
	public void closeBreedingOffer(BreedingOffer breedingOffer);//
	public void locateBreedingOffer(BreedingOffer breedingOffer);//
	public void deleteBreedingOffer(int id);//
	public void editBreedingOffer(BreedingOffer breedingOffer);// update bizarre
	public BreedingOffer showBreedingOffer(int breedingOffer);//
	public List<BreedingOffer> getAllBreedingOfferClossed();//
	public List<BreedingOffer> getAllBreedingOfferLocated();//
	public List<BreedingOffer> getAllBreedingOffer();	//
	public List<BreedingOffer> getBreedingOfferByMembre(Member member);//
	public List<BreedingOffer> getBreedingOfferByAnimal(Animal animal); // 
	public List<BreedingOffer> getBreedingOfferBySpeciesAndSexM(Animal animal);//
	public List<BreedingOffer> getBreedingOfferBySpeciesAndSexF(Animal animal);//
	public List<BreedingOffer> getBreedingOfferByCountry(String country);//
	public List<BreedingOffer> getBreedingOfferByStreet(String street);//
	public List<BreedingOffer> getBreedingOfferByCity(String city);//
	public List<BreedingOffer> getBreedingOfferBybreed(String breed);//
	public List<BreedingOffer> getBreedingOfferBySpecies(String species);//
	public List<BreedingOffer> getBreedingOfferByDate(String date);// 
	public List<Member> getMemberByBreedingOffer(BreedingOffer breedingOffer);//
	public List<Animal> getAnimalByBreedingOffer(BreedingOffer breedingOffer);//
	//public List<BreedingOffer> GetAllBreedingbyDateDSEC ();
	
	    //stat
		public Boolean haveOldBreeding(Animal animal);//
		public Long NumberBreeding(int id);//
		public Long NumberBreedingSucces(int id);//
		public Long NumberBreedingFailed(int id);//
		public String LastdateOfBreeding(Animal animal);//
		//public Animal BreedingPartner (BreedingOffer breedingOffer);
		public int NumberofBabies(Animal animal);//
		public int ChanceHaveBreeding(int id);//
		public int DistanceBetweenOfferandRequest(Double x1,Double y1, Double x2,Double y2);
		
		//angular
		
		public List<BreedingOffer> getBreedingOfferBySpecies3Days(Animal animal);//
		public List<BreedingOffer> getBreedingBydistance(int dis,Animal animal);//
		public BreedingOffer getBreedingOfferByb(int id);//
		public int days(Date date);//
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
