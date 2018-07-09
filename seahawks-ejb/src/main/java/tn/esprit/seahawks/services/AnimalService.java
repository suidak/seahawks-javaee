package tn.esprit.seahawks.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.AnimalServiceLocal;
import tn.esprit.seahawks.interfaces.AnimalServiceRemote;
import tn.esprit.seahawks.persistence.AdoptionRequestStatus;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class AnimalService implements AnimalServiceLocal, AnimalServiceRemote {


	// you can find the persistence unit name in persistence.xml
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public int addAnimal(Animal a) {
		User user = em.find(User.class, a.getOwner().getId());
		a.setOwner(user);
		em.persist(a);
		em.flush();
		em.refresh(a);
		return a.getId();
	}

	@Override
	public boolean deleteAnimal(int id) {
		Animal a = em.find(Animal.class, id);
		try {
			em.remove(a);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean updateAnimal(Animal a) {
		try {
			Animal toUpdate = em.find(Animal.class, a.getId());

			toUpdate.setAge(a.getAge());
			toUpdate.setBreed(a.getBreed());
			toUpdate.setCastrated(a.isCastrated());
			toUpdate.setFostered(a.isFostered());
			toUpdate.setHeight(a.getHeight());
			toUpdate.setWeight(a.getWeight());
			toUpdate.setLost(a.isLost());
			toUpdate.setSex(a.getSex());
			toUpdate.setSpecie(a.getSpecie());
			toUpdate.setStatus(a.getStatus());
			
			em.merge(toUpdate);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<Animal> getAllAnimals(){
		return em.createQuery("select a from Animal a", Animal.class)
				.getResultList();
	}
	
	@Override
	public List<Animal> getUserAnimals(int id){
		return em.createQuery("select a from Animal a "
				+ "where a.owner.id=:id", Animal.class)
				.setParameter("id", id)
				.getResultList();
	}
	
	@Override
	public List<Animal> getUserUnlistedAnimals(int id){
		return em.createQuery("select a from Animal a "
				+ "where "
				+ "a.owner.id=:id "
				+ "and "
				+ "a not in "
				+ "(select o.animal from AdoptionOffer o "
				+ "where "
				+ "o.animal.owner.id=:id)", Animal.class)
				.setParameter("id", id)
				.getResultList();
	}
	
	@Override
	public List<Animal> getAnimalsBySpecie(String specie) {
		return em.createQuery("select a from Animal a where "
				+ "a.specie=:specie", Animal.class)
				.setParameter("specie", specie).getResultList();
	}
	
	@Override
	public List<Animal> getAnimalsByUser(Member member) {

		Query query = em.createQuery("select a from Animal a join a.owner u where u.id=:id");
		//query.setParameter( "id", id);
		query.setParameter( "id", member.getId() );
		
		return query.getResultList();
	}
	
	@Override
	public List<Animal> getAnimalsBySpecieAndBreed(String specie, String breed){
		return em.createQuery("select a from Animal a where "
				+ "a.specie=:specie and a.breed=:breed", Animal.class)
				.setParameter("specie", specie)
				.setParameter("breed", breed).getResultList();
	}
	
	@Override
	public long averageAnimalsNbrPerUser(){
		TypedQuery<Long> animalCount = em
				.createQuery("select count(a) from Animal a", Long.class);
		TypedQuery<Long> userCount = em
				.createQuery("select count(distinct a.owner) from Animal a", Long.class);
		
		return Math.round(Double.valueOf
				(animalCount.getSingleResult()) / Double.valueOf(userCount.getSingleResult()));
	}
	
	@Override
	public long nbrOfAnimalsPerSpecie(String specie){
		try {
			TypedQuery<Long> count = em
					.createQuery("select count(a) from Animal a where specie=:specie", Long.class);
			
			count.setParameter("specie", specie);
			
			return count.getSingleResult();
		} catch (NoResultException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	@Override
	public long nbrOfAnimalsPerSpecieBreed(String specie, String breed){
		try {
			TypedQuery<Long> count = em
					.createQuery("select count(a) from Animal a where specie=:specie "
							+ "and breed=:breed", Long.class);
			
			count.setParameter("specie", specie);
			count.setParameter("breed", breed);
			
			return count.getSingleResult();
		} catch (NoResultException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
	    List<T> r = new ArrayList<T>(c.size());
	    for(Object o: c)
	      r.add(clazz.cast(o));
	    return r;
	}
	
	@Override
	public HashMap<String, Long> nbrOfAdoptionsPerSpecie(){
		HashMap<String, Long> adoptionsPerSpecies = new HashMap<>();
		
		try {
			List<Object[]> queryResults = castList(Object[].class, 
					em.createQuery("select a.specie, count(r.id) from AdoptionRequest r "
					+ "join r.offer o "
					+ "join o.animal a "
					+ "where r.status= :status group by a.specie")
					.setParameter("status", AdoptionRequestStatus.accepted)
					.getResultList());
			
			for (Object[] entry : queryResults){
				String specie = (String) entry[0];
				long count = (long) entry[1];
				adoptionsPerSpecies.put(specie,count);
			}
			
			return adoptionsPerSpecies; 
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public HashMap<String, Long> getMostAdoptedAnimal(){
		try {
			HashMap<String, Long> adopStats = nbrOfAdoptionsPerSpecie();
			if (adopStats.isEmpty())
				return null;
			
			HashMap.Entry<String, Long> mostAdopted = null;
			
			for (HashMap.Entry<String, Long> entry : adopStats.entrySet()){
				if (mostAdopted == null || entry.getValue().compareTo(mostAdopted.getValue()) > 0)
			    {
					mostAdopted = entry;
			    }
			}
			
			HashMap<String, Long> result = new HashMap<>();
			result.put(mostAdopted.getKey(), mostAdopted.getValue());
			
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int lastIndex(){
		try {
			TypedQuery<Integer> index = em
					.createQuery("select max(a.id) from Animal a", Integer.class);
			
			return index.getSingleResult();
		} catch (NoResultException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	@Override
	public List<Animal> getAnimalsByUser(User u) {

		Query query = em.createQuery("select a from Animal a join a.owner u where u.id=:id");
		//query.setParameter( "id", id);
		query.setParameter( "id", u.getId() );
		
		return query.getResultList();
	}


	@Override
	public List<Animal> getAnimalsPerMember(int memberid) {
		Query query = em.createQuery("SELECT a from Animal a where a.owner=:owner");
		Member member= em.find(Member.class, memberid);
		query.setParameter("owner", member);
		
		return query.getResultList();
	}

	@Override
	public Animal getSingleAnimal(int id) {
		Animal a = em.find(Animal.class, id);
		return a;
	}
	
	
}
