package tn.esprit.seahawks.services;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.AdoptionOfferServiceLocal;
import tn.esprit.seahawks.interfaces.AdoptionOfferServiceRemote;
import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.AdoptionRequestStatus;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Organisation;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class AdoptionOfferService implements AdoptionOfferServiceLocal, AdoptionOfferServiceRemote {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override // add throws to method and according exceptions: animalnotfound,
				// animalalreadyenlisted
	public int addAdoptionOffer(AdoptionOffer a) {
		Animal pet = em.find(Animal.class, a.getAnimal().getId());
		
		// check if animal is listed in the DB
		if (pet == null)
			return -1;
		/*
		 * changed my mind, members and orgs can make adoption offers // check
		 * if adoption maker is an organization if (em.find(Member.class,
		 * a.getAnimal().getOwner().getId()) != null){ return -2; }
		 */

		try {
			TypedQuery<AdoptionOffer> query = em
					.createQuery("select a from AdoptionOffer a where a.animal.id=:animal", 
							AdoptionOffer.class)
					.setParameter("animal", a.getAnimal().getId());

			a.setAnimal(pet);

			em.persist(a);
			em.flush();
			em.refresh(a);
			
			System.out.println(a.getId());
			
			return a.getId();
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Override
	public boolean deleteAdoptionOffer(AdoptionOffer a) {
		AdoptionOffer offer = em.find(AdoptionOffer.class, a.getId());
		
		try {
			em.remove(offer);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean updateAdoptionOffer(AdoptionOffer a) {
		AdoptionOffer toUpdate = em.find(AdoptionOffer.class, a.getId());
		Animal animal = em.find(Animal.class, a.getAnimal().getId());

		// check if both animals have the same owner.
		if (toUpdate.getAnimal().getOwner().getId() != animal.getOwner().getId())
			return false;

		try {
			toUpdate.setPrice(a.getPrice());
			toUpdate.setAnimal(animal);
			em.merge(toUpdate);

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<AdoptionOffer> getAllAdoptionOffers() {
		return em.createQuery("select a from AdoptionOffer a", AdoptionOffer.class).getResultList();
	}

	@Override
	public List<AdoptionOffer> getPendingAdoptionOffers(int uid) {
		return em
				.createQuery("select a from AdoptionOffer a where (a in "
						+ "(select b.offer from AdoptionRequest b where b.status=:status) or "
						+ "(select count(b.offer) from AdoptionRequest b where b.offer=a)=0) "
						+ "and a.animal.owner.id!=:uid "
						+ "order by offerDate DESC", AdoptionOffer.class)
				.setParameter("uid", uid)
				.setParameter("status", AdoptionRequestStatus.pending).getResultList();
	}

	@Override
	public List<AdoptionOffer> getPendingAdoptionOffersBySpecie(String specie) {
		return em
				.createQuery("select a from AdoptionOffer a where "
						+ "(a in (select b.offer from AdoptionRequest b where b.status=:status) or "
						+ "(select count(b.offer) from AdoptionRequest b where b.offer=a)=0) "
						+ "and a.animal.specie=:specie order by a.offerDate DESC", AdoptionOffer.class)
				.setParameter("specie", specie).setParameter("status", AdoptionRequestStatus.pending).getResultList();
	}

	@Override
	public List<AdoptionOffer> getPendingAdoptionOffersBySpecieBreed(String specie, String breed) {
		return em
				.createQuery("select a from AdoptionOffer a where "
						+ "(a in (select b.offer from AdoptionRequest b where b.status=:status) or "
						+ "(select count(b.offer) from AdoptionRequest b where b.offer=a)=0) "
						+ "and a.animal.specie=:specie and a.animal.breed=:breed " + "order by a.offerDate DESC",
				AdoptionOffer.class).setParameter("specie", specie).setParameter("breed", breed)
				.setParameter("status", AdoptionRequestStatus.pending).getResultList();
	}

	@Override
	public HashMap<String, Double> averagePricePerSpecie() {
		HashMap<String, Double> avgPrices = new HashMap<>();

		try {
			List<Object[]> queryResults = AnimalService.castList(Object[].class, em.createQuery(
					"select a.specie, avg(o.price) from AdoptionOffer o " + "join o.animal a " + "group by a.specie")
					.getResultList());

			for (Object[] entry : queryResults) {
				String specie = (String) entry[0];
				double price = (double) entry[1];
				price = Math.floor(price * 100) / 100;
				avgPrices.put(specie, price);
			}

			return avgPrices;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AdoptionOffer> getUserAdoptionOffers(int id) {
		return em
				.createQuery("select a from AdoptionOffer a " + "where " + "a.animal.owner.id=:id", AdoptionOffer.class)
				.setParameter("id", id).getResultList();
	}

	@Override
	public List<AdoptionOffer> getUserPendingAdoptionOffers(int id) {
		return em
				.createQuery("select a from AdoptionOffer a " + "where a.animal.owner.id=:id " + "and " + "(a in "
						+ "(select b.offer from AdoptionRequest b where b.status=:status) or "
						+ "(select count(b.offer) from AdoptionRequest b where b.offer=a)=0) "
						+ "order by offerDate DESC", AdoptionOffer.class)
				.setParameter("id", id).setParameter("status", AdoptionRequestStatus.pending).getResultList();
	}

	@Override
	public List<String> getSpecies() {
		return em.createQuery("select DISTINCT(a.animal.specie) from AdoptionOffer a", String.class).getResultList();
	}

	@Override
	public List<String> getBreeds(String specie) {
		return em.createQuery(
				"select DISTINCT(a.animal.breed) from AdoptionOffer a " + "where " + "a.animal.specie=:specie",
				String.class).setParameter("specie", specie).getResultList();
	}

	@Override
	public AdoptionOffer getOfferById(int id) {
		return em.createQuery("select a from AdoptionOffer a " + "where a.id=:id", AdoptionOffer.class)
				.setParameter("id", id).getSingleResult();
	}
}
