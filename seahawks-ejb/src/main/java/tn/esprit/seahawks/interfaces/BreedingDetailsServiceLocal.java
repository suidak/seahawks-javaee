package tn.esprit.seahawks.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.BreedingDetails;

@Local
public interface BreedingDetailsServiceLocal {
	
	
	public BreedingDetails showBreedingDetails (int id);//
	public void editBreedingDetails (BreedingDetails breedingDetails);
	public void edit2BreedingDetails(int id, Date d);
	//public void editAdressBreeding(BreedingDetails breedingDetails);
	//public void addBabiesAndDate(BreedingDetails breedingDetails, int babies , String date );
	//public void addDateAction(String date);
	//public void editStatutDecline (BreedingDetails breedingDetails);
	
	
	
	

}
