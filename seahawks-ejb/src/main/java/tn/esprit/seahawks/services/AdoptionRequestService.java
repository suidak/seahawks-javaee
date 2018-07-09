package tn.esprit.seahawks.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.seahawks.interfaces.AdoptionRequestServiceLocal;
import tn.esprit.seahawks.interfaces.AdoptionRequestServiceRemote;
import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.AdoptionRequest;
import tn.esprit.seahawks.persistence.AdoptionRequestStatus;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Organisation;

@Stateless
public class AdoptionRequestService implements AdoptionRequestServiceLocal, AdoptionRequestServiceRemote {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public int addAdoptionRequest(AdoptionRequest a) {
		// orgs can't adopt pets
		if (em.find(Organisation.class, a.getAdopter().getId()) != null)
			return -1;

		AdoptionOffer offer = em.find(AdoptionOffer.class, a.getOffer().getId());
		Member adopter = em.find(Member.class, a.getAdopter().getId());
		
		// owner can't adopt his own pet
		if (offer.getAnimal().getOwner().equals(adopter))
			return -2;
		
		// check if a request has already been accepted for this offer
		if (!em.createQuery("select a from AdoptionRequest a where a.status = :status "
				+ "and a.offer = :offer", AdoptionRequest.class)
				.setParameter("status", AdoptionRequestStatus.accepted)
				.setParameter("offer", offer)
				.getResultList().isEmpty())
			return -3;
		
		// check if adopter has already made a request for this offer
		if (!em.createQuery("select a from AdoptionRequest a where a.adopter=:adopter "
				+ "and a.offer=:offer",
				AdoptionRequest.class)
				.setParameter("adopter", adopter)
				.setParameter("offer", offer)
				.getResultList()
				.isEmpty())
			return 0;

		a.setAdopter(adopter);
		a.setOffer(offer);

		em.persist(a);
		em.flush();
		em.persist(a);
		return a.getId();
	}

	@Override
	public boolean deleteAdoptionRequest(AdoptionRequest a) {
		AdoptionRequest request = em.find(AdoptionRequest.class, a.getId());

		try {
			em.remove(request);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public boolean updateAdoptionRequest(AdoptionRequest a) {
		AdoptionRequest toUpdate = em.find(AdoptionRequest.class, a.getId());

		try {
			toUpdate.setDescription(a.getDescription());
			em.merge(toUpdate);

			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public List<AdoptionRequest> getAllAdoptionRequestsByMember(Member member) {
		Member m = em.find(Member.class, member.getId());

		return em.createQuery("select a from AdoptionRequest a where " + "a.adopter=:adopter", AdoptionRequest.class)
				.setParameter("adopter", m).getResultList();
	}

	@Override
	public List<AdoptionRequest> getAllAdoptionRequestsByOffer(AdoptionOffer a) {
		AdoptionOffer offer = em.find(AdoptionOffer.class, a.getId());

		return em.createQuery("select a from AdoptionRequest a where a.offer=:offer", AdoptionRequest.class)
				.setParameter("offer", offer).getResultList();
	}

	@Override
	public int processAdoptionRequest(AdoptionRequest a, boolean decision) {
		AdoptionRequest request = em.find(AdoptionRequest.class, a.getId());
		em.refresh(request);
		int declined = 0;

		// check if the request has already been accepted or declined
		if (request.getStatus().equals(AdoptionRequestStatus.accepted)
				|| request.getStatus().equals(AdoptionRequestStatus.declined))
			return 0;
		
		if (decision) {
			try {
				// accept req
				request.setStatus(AdoptionRequestStatus.accepted);
				em.merge(request);

				// change animal's owner
				Animal adopted = em
						.createQuery("select a.animal from AdoptionOffer a " + "where a=:offer", Animal.class)
						.setParameter("offer", request.getOffer()).getSingleResult();

				adopted.setOwner(request.getAdopter());
				em.merge(adopted);

				// auto decline others
				Query query = em.createQuery(
						"update AdoptionRequest a set a.status=:status where " + "a.id!=:id and a.offer=:offer");

				query.setParameter("status", AdoptionRequestStatus.declined);
				query.setParameter("id", request.getId());
				query.setParameter("offer", request.getOffer());
				declined = query.executeUpdate();

				System.out.println("# of declined requests for this offer: " + declined);

				if (declined == 0)
					return -5;
				
				return declined;
			} catch (Exception e) {
				System.out.println(e);
				return -1;
			}
		} else {
			try {
				// decline req
				request.setStatus(AdoptionRequestStatus.declined);
				em.merge(request);
				return -2;
			} catch (Exception e) {
				System.out.println(e);
				return -1;
			}
		}
	}
}
