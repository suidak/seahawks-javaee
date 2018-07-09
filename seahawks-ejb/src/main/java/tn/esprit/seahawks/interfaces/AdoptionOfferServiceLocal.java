package tn.esprit.seahawks.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.User;

@Local
public interface AdoptionOfferServiceLocal {
	
	public int addAdoptionOffer(AdoptionOffer a);
	public boolean deleteAdoptionOffer(AdoptionOffer a);
	public boolean updateAdoptionOffer(AdoptionOffer a);
	public List<AdoptionOffer> getAllAdoptionOffers();
	public List<AdoptionOffer> getUserAdoptionOffers(int id);
	public List<AdoptionOffer> getUserPendingAdoptionOffers(int id);
	public List<AdoptionOffer> getPendingAdoptionOffers(int uid);
	public List<AdoptionOffer> getPendingAdoptionOffersBySpecie(String specie);
	public List<AdoptionOffer> getPendingAdoptionOffersBySpecieBreed(String specie, String breed);
	public Map<String, Double> averagePricePerSpecie();
	public List<String> getSpecies();
	public List<String> getBreeds(String specie);
	public AdoptionOffer getOfferById(int id);
}
