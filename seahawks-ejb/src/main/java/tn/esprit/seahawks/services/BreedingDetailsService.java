package tn.esprit.seahawks.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.BindingType;

import tn.esprit.seahawks.interfaces.BreedingDetailsServiceLocal;
import tn.esprit.seahawks.interfaces.BreedingDetailsServiceRemote;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.BreedingDetails;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.BreedingRequest;
import tn.esprit.seahawks.persistence.Statut;
@Stateless
public class BreedingDetailsService implements BreedingDetailsServiceLocal , BreedingDetailsServiceRemote{
	
	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public BreedingDetails showBreedingDetails(int id) {
		//BreedingRequest breedingRequest = em.find(BreedingRequest.class, id);
		Query query = em.createQuery("select b from BreedingDetails b join b.breedingRequest br  where br.id=:id",BreedingDetails.class);
		query.setParameter( "id",id);
		//query.setParameter( "name", member.getId() );
		
		return (BreedingDetails)query.getSingleResult();
		
	}

	@Override
	public void editBreedingDetails(BreedingDetails breedingDetails) {
		BreedingDetails breedingDetails2 = em.find(BreedingDetails.class,breedingDetails.getId());
	
		if(breedingDetails.getAddressBreeding() != null)
		{
			
		breedingDetails2.setAddressBreeding(breedingDetails.getAddressBreeding());
		}
		
		if(breedingDetails.getBabiesNumber() != 0)
		{
		    breedingDetails2.setBabiesNumber(breedingDetails.getBabiesNumber());
		    breedingDetails2.setStatut(Statut.confirmed);		
		}
		if(breedingDetails.getDateAction() != null)
		breedingDetails2.setDateAction(breedingDetails.getDateAction());
		
		if(breedingDetails.getDateBreeding() != null)
			breedingDetails2.setDateBreeding(breedingDetails.getDateBreeding());
		
		if(breedingDetails.getDateConfirmed() != null)
			breedingDetails2.setDateConfirmed(breedingDetails.getDateConfirmed());
		if(breedingDetails.getStatut() != null)
			breedingDetails2.setStatut(breedingDetails.getStatut());
		
		
		em.merge(breedingDetails2);
	}
	
	
	@Override
	public void edit2BreedingDetails(int id, Date d) {
		BreedingDetails breedingDetails2 = em.find(BreedingDetails.class,id);
		BreedingDetails breedingDetails = em.find(BreedingDetails.class,id);
		
		
		if(breedingDetails.getDateConfirmed() != null)
			breedingDetails2.setDateConfirmed(d);
	
		
		
		em.merge(breedingDetails2);
	}

	//if(breedingOffer.getTitre() != null)
	//	breedingOffer1.setTitre(breedingOffer.getTitre());
	

}
