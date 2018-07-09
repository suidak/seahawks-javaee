package tn.esprit.seahawks.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.BreedingOfferServiceLocal;
import tn.esprit.seahawks.interfaces.BreedingOfferServiceRemote;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingDetails;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.BreedingRequest;
import tn.esprit.seahawks.persistence.Distance;
import tn.esprit.seahawks.persistence.Member;


@Stateless
public class BreedingOfferService implements BreedingOfferServiceRemote, BreedingOfferServiceLocal {

	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;
	
	
	@Override
	public BreedingOffer addBreedingOffer(int id, String des,String titre) {
		
		Animal animal = em.find(Animal.class,id);
		//  System.out.println(animal.getSex());
		 // System.out.println(animal.getAge());
		
      BreedingOffer breedingOffer = new BreedingOffer();
        
		//  if (AnimalControl(animal))
			//	{
			  
        	  breedingOffer.setAnimal(animal);
        	  breedingOffer.setDescription(des);
        	  breedingOffer.setTitre(titre);
        	  breedingOffer.setDate(new Date());
        	breedingOffer.setIdAnimal(animal.getId());
		       em.persist(breedingOffer);
		       return breedingOffer;
              //}
       // else 
     //  {
        	// System.out.println(animal.getSex());
   		 // System.out.println(animal.getAge());
        	//return null;
       // }
		//return true;
        //}
       // return false;
	}


	@Override
	public void closeBreedingOffer(BreedingOffer breedingOffer) {
		BreedingOffer breedingOffer1 = em.find(BreedingOffer.class,breedingOffer.getId());
		breedingOffer1.setClosed(true);
		em.merge(breedingOffer1);
	}

	@Override
	public void locateBreedingOffer(BreedingOffer breedingOffer) {
		BreedingOffer breedingOffer1 = em.find(BreedingOffer.class,breedingOffer.getId());
		breedingOffer1.setLocated(true);
		em.merge(breedingOffer1);
	}

	@Override
	public void deleteBreedingOffer(int id) {
		
		BreedingOffer breedingOffer1 = em.find(BreedingOffer.class,id);
		em.remove(breedingOffer1);
		
	}


	@Override
	public void editBreedingOffer(BreedingOffer breedingOffer) {
		BreedingOffer breedingOffer1 = em.find(BreedingOffer.class,breedingOffer.getId());
		if(breedingOffer.getTitre() != null)
		breedingOffer1.setTitre(breedingOffer.getTitre());
		if(breedingOffer.getDescription() != null)
		breedingOffer1.setDescription(breedingOffer.getDescription());
		if(breedingOffer.getDate()!= null)
			breedingOffer1.setDate(breedingOffer.getDate());
		//if(breedingOffer.isLocated() != null)
			//breedingOffer1.set(breedingOffer.getDescription());
		
	    em.merge(breedingOffer1);
	   // em.flush();
	    
	}


	@Override
	public BreedingOffer showBreedingOffer(int breedingOffer) {
	BreedingOffer o= em.find(BreedingOffer.class,breedingOffer);
		
		 
			  return o;
	}


	@Override
	public List<BreedingOffer> getAllBreedingOfferClossed() {
		Query query = em.createQuery("select b from BreedingOffer b where b.closed = true");
		return query.getResultList();
		
				}


	@Override
	public List<BreedingOffer> getAllBreedingOfferLocated() {
		Query query = em.createQuery("select b from BreedingOffer b where b.located = true");
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getAllBreedingOffer() {
		Query query = em.createQuery("select b from BreedingOffer b ");
		return query.getResultList();
				}


	@Override
	public List<BreedingOffer> getBreedingOfferByMembre(Member member) {
		 
	
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a join a.owner u where u.id=:id");
		query.setParameter( "id", member.getId());
		//query.setParameter( "name", member.getId() );
		
		return query.getResultList();
	}
	
	

	@Override
	public BreedingOffer getBreedingOfferByb(int id) {
		
		//Animal animal1= em.find(Animal.class,3);
		Query query = em.createQuery("select b from BreedingOffer b where b.id=:id",BreedingOffer.class);
		query.setParameter("id", id );
		
		return (BreedingOffer)query.getSingleResult();
	}
	
	
	
	
	@Override
	public List<BreedingOffer> getBreedingOfferByAnimal(Animal animal) {
		
		//Animal animal1= em.find(Animal.class,3);
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a  where a.id=:id");
		query.setParameter("id", animal.getId() );
		
		return query.getResultList();
	}
	


	@Override
	public List<BreedingOffer> getBreedingOfferBySpeciesAndSexF(Animal animal) {
		String etat  ;
	    Animal animal1 = em.find(Animal.class, animal.getId());
	    System.out.println(animal1.getSex());
	      if(animal1.getSex().equals("M"))
	         etat = "F";
	      else{etat="M";}
		 
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.specie=:s and a.sex=:sex and b.closed=0");
		
		query.setParameter("s", animal1.getSpecie() );
		query.setParameter("sex", etat );

		
		return query.getResultList();
	}

	
	@Override
	public List<BreedingOffer> getBreedingOfferBySpeciesAndSexM(Animal animal) {
		
	    Animal animal1 = em.find(Animal.class, animal.getId());
		 
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.specie=:s and a.sex=:sex");
		
		query.setParameter("s", animal1.getSpecie() );
		query.setParameter("sex", "M");

		
		return query.getResultList();
	}

	@Override
	public List<BreedingOffer> getBreedingOfferByCountry(String country) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a join a.owner u join u.address adr where adr.country=:cs");
		query.setParameter("cs", country );
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getBreedingOfferByStreet(String street) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a join a.owner u join u.address adr where adr.street=:c");
		query.setParameter("c", street );
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getBreedingOfferByCity(String city) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a join a.owner u join u.address adr where adr.city=:c");
		query.setParameter("c", city);
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getBreedingOfferBybreed(String breed) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.breed=:s ");
		query.setParameter("s", breed);
		//query.setParameter("sex", sex);
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getBreedingOfferBySpecies(String species) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.specie=:s");
		query.setParameter("s", species );
		return query.getResultList();
	}


	@Override
	public List<BreedingOffer> getBreedingOfferByDate(String date) {
		Query i =  em.createNativeQuery("SELECT DATEDIFF('2017-06-25','2017-06-15')");
	     System.out.println("e&&"+i.getSingleResult());
	     /*
	     if(i.getSingleResult().equals("10"))
	     {
	    	 System.out.println("aaaaaa"+i.getSingleResult());
	     }
	     */
		SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
		Date dtf = null;
		try {
			dtf = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Query query = em.createQuery("select b from BreedingOffer b where b.date=:date");
		query.setParameter("date",dtf);
		return query.getResultList();
	}






	@Override
	public List<Member> getMemberByBreedingOffer(BreedingOffer breedingOffer) {
		Query query = em.createQuery("select b.animal.owner from BreedingOffer b where b.id =:m");
		//query.setParameter("date",new Date(),TemporalType.DATE );
		query.setParameter("m",breedingOffer.getId() );
		return query.getResultList();
	}


	@Override
	public List<Animal> getAnimalByBreedingOffer(BreedingOffer breedingOffer) {
		Query query = em.createQuery("select b.animal from BreedingOffer b where b.id =:a");
		//query.setParameter("date",new Date(),TemporalType.DATE );
		query.setParameter("a",breedingOffer.getId() );
		return query.getResultList();
	}
	
	



	@Override
	public Boolean haveOldBreeding(Animal animal) {
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.id=:id and b.closed=1");
		query.setParameter("id", animal.getId());
		if(query.getResultList().isEmpty())
           {
                  return false	;
           }
		return true;
	}


	@Override
	public Long NumberBreeding(int id) {
		Query query = em.createQuery("select COUNT(b) from BreedingOffer b join b.animal a where a.id=:id and b.closed=1");
		query.setParameter("id", id);
		return (Long) query.getSingleResult();
	}


	@Override
	public Long NumberBreedingSucces(int id) {
		Query query = em.createQuery("select COUNT(b) from BreedingDetails b "
				+ "join b.breedingRequest br join br.offer bo join bo.animal"
				+ " a where a.id=:id and bo.closed=1 and b.statut ='confirmed'");
		query.setParameter("id",id);
		return (Long) query.getSingleResult();
	}


	@Override
	public Long NumberBreedingFailed(int id) {
		Query query = em.createQuery("select COUNT(b) from BreedingDetails b "
				+ "join b.breedingRequest br join br.offer bo join bo.animal"
				+ " a where a.id=:id and bo.closed=1 and b.statut ='failed'");
		query.setParameter("id", id);
		return (Long) query.getSingleResult();
	}
	
	


	@Override
	public String LastdateOfBreeding(Animal animal) {
		Query query = em.createQuery("select b.dateAction from BreedingDetails b "
				+ "join b.breedingRequest br join br.offer bo join bo.animal"
				+ " a where a.id=:id");
		  query.setParameter("id", animal.getId());
		  query.getResultList();
			List<Date> lr = query.getResultList();
			Date Dmax = lr.get(0) ;
			for(Iterator it=lr.iterator(); it.hasNext();) 
			{   
				Date d = (Date) it.next();
				if(d.after(Dmax))
					Dmax = d;
					System.out.println(Dmax.toString());
				   // Dmax.toLocaleString();
			}
			return  Dmax.toString();
			
	}





	@Override
	public int NumberofBabies(Animal animal) {
		System.out.println("d");
		if(haveOldBreeding(animal))
		{
			Query query = em.createQuery("select b from BreedingDetails b "
					+ "join  b.breedingRequest br  join br.offer bo join bo.animal"
					+ " a where a.id=:id",BreedingDetails.class);
			query.setParameter("id", animal.getId());
			int total = 0 ;
			List<BreedingDetails> lr = query.getResultList();
			for(Iterator it=lr.iterator(); it.hasNext();) 
			{
				BreedingDetails breedingDetails = (BreedingDetails) it.next();
				total = total + breedingDetails.getBabiesNumber();
			
		}
			return total;
		}
		return 0;
	}


	
	public boolean AnimalControl(Animal animal) {
		    
	  	   SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
		       if (haveOldBreeding(animal)){
		Query i =  em.createNativeQuery("SELECT DATEDIFF(?1,?2)");
		i.setParameter("2", dateFormat.format(new Date()));
		i.setParameter("1", LastdateOfBreeding(animal));
	     System.out.println(i.getSingleResult().toString());
        int	days = Integer.parseInt(i.getSingleResult().toString()); 
       // System.out.println(mois+1);  
                          if(Math.abs(days)<31)
                          {
                        	  return false;
                          }
		      }
		       
		       
		 if (( (animal.getSex().equals("M")) && (animal.getAge() > 18))  || 
				 ((animal.getSex().equals("F")) && (animal.getAge() > 6)) )
				 {
	                  return true ;
	                 //  System.out.println(animal.getSex());
				 } 
		return false;
		
	}


	@Override
	public int ChanceHaveBreeding(int id) {
		return  Math.round((((float)NumberBreedingSucces(id)) / ((float)NumberBreeding(id))*100))  ;
	}

	
	
	
	

	@Override
	public List<BreedingOffer> getBreedingOfferBySpecies3Days(Animal animal) {
		String etat  ;
		   SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
	    Animal animal1 = em.find(Animal.class, animal.getId());
	    System.out.println(animal1.getSex());
	      if(animal1.getSex().equals("M"))
	         etat = "F";
	      else{etat="M";}
		 
		Query query = em.createQuery("select b from BreedingOffer b join b.animal a where a.specie=:s and a.sex=:sex");
		
		query.setParameter("s", animal1.getSpecie() );
		query.setParameter("sex", etat );
		
		
		List<BreedingOffer> lr = query.getResultList();
		ArrayList<BreedingOffer> arrList = new ArrayList<BreedingOffer>(); 
		for(Iterator it=lr.iterator(); it.hasNext();) 
 		{
			BreedingOffer breedingOffer = (BreedingOffer) it.next();
			Query i =  em.createNativeQuery("SELECT DATEDIFF(?1,?2)");
			i.setParameter("2", dateFormat.format(new Date()));
			i.setParameter("1", breedingOffer.getDate());
		    // System.out.println(i.getSingleResult().toString());
	        int	days = Integer.parseInt(i.getSingleResult().toString()); 
	        if(Math.abs(days)<4)
            {
          	  arrList.add(breedingOffer);
            }
	        
		
	}
		
		

		
		return arrList;
	}
	
	
	
	@Override
	public List<BreedingOffer> getBreedingBydistance(int dis,Animal a) {
		List<BreedingOffer> l1 = this.getBreedingOfferBySpeciesAndSexF(a) ;
		   Animal animal = em.find(Animal.class, a.getId());
		ArrayList<BreedingOffer> arrList = new ArrayList<BreedingOffer>(); 
		for(Iterator it=l1.iterator(); it.hasNext();) 
 		{
			BreedingOffer breedingOffer = (BreedingOffer) it.next();
		
			double x1 = breedingOffer.getAnimal().getOwner().getAddress().getX();
			double y1 = breedingOffer.getAnimal().getOwner().getAddress().getY();
	
			Distance dist = new Distance();
			  int test =  (int) dist.distance(x1,y1,animal.getOwner().getAddress().getX(),animal.getOwner().getAddress().getY(), "K");
			  
			if ( test <= dis)
			{
				arrList.add(breedingOffer);
			}
	        
		
	}
		return arrList;
		
		
	}
	
	
	@Override
	public int DistanceBetweenOfferandRequest(Double x1,Double y1, Double x2,Double y2) {
	
		Distance dist = new Distance();
		return (int) dist.distance(x1, y1, x2, y2, "K");
	}
	
	
	
	
	
	@Override
	public int days(Date date)
	{
		 SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
		//Date d = (Date) SimpleDateFormat.parse(date);
	Query i =  em.createNativeQuery("SELECT DATEDIFF(?1,?2)");
	i.setParameter("2", dateFormat.format(new Date()));
	i.setParameter("1",dateFormat.format(date));
   System.out.println(i.getSingleResult().toString());
 return Integer.parseInt(i.getSingleResult().toString()); 
	}
	
	

	

}
