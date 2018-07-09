package tn.esprit.seahawks.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.seahawks.interfaces.WalkingServiceLocal;
import tn.esprit.seahawks.persistence.Walkings;

@Stateless
public class WalkingService implements WalkingServiceLocal {
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public List<Walkings> getWalkingByDog() {
		Query query = em.createQuery("select w from Walking w where w.breed = dog");
		return query.getResultList();
	}

}
