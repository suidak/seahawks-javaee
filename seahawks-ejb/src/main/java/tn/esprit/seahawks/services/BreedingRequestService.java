package tn.esprit.seahawks.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.seahawks.interfaces.BreedingOfferServiceLocal;
import tn.esprit.seahawks.interfaces.BreedingOfferServiceRemote;
import tn.esprit.seahawks.interfaces.BreedingRequestServiceLocal;
import tn.esprit.seahawks.interfaces.BreedingRequestServiceRemote;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingDetails;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.BreedingRequest;
import tn.esprit.seahawks.persistence.Distance;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.SendEmail;
import tn.esprit.seahawks.persistence.Statut;
import tn.esprit.seahawks.persistence.Walkings;


@Stateless
public class BreedingRequestService implements BreedingRequestServiceLocal, BreedingRequestServiceRemote {
	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public int AddBreedingRequest(int idb, int ida, String des) {
		BreedingOffer breedingOffer = em.find(BreedingOffer.class,idb);
		Animal animal = em.find(Animal.class,ida);
	
		//if((animal.getId()== breedingOffer.getAnimal().getId())
				//|| (AnimalControl(animal) == false))
			//return 0;
		BreedingRequest breedingRequest = new BreedingRequest();
		breedingRequest.setOffer(breedingOffer);
		breedingRequest.setDescription(des);
		breedingRequest.setAnimal(animal);
		breedingRequest.setStatus(Statut.inProgress);
		em.persist(breedingRequest);
		return breedingRequest.getId();
	}

	@Override
	public void deleteBreedingRequest(BreedingRequest breedingRequest) {
		BreedingRequest breedingRequest1 = em.find(BreedingRequest.class,breedingRequest.getId());
		em.remove(breedingRequest1);
	}

	@Override
	public void editBreedingRequest(BreedingRequest breedingRequest) {
		BreedingRequest breedingRequest1 = em.find(BreedingRequest.class,breedingRequest.getId());
		breedingRequest1.setDescription(breedingRequest.getDescription());
		em.merge(breedingRequest1);
		
	}


	@Override
	public List<BreedingRequest> showBreedingRequestByMember(int id) {
		Query query = em.createQuery("select b from BreedingRequest b join b.animal a join a.owner u where u.id=:id");
		query.setParameter( "id", id);
		//query.setParameter( "name", member.getId() );
		
		return query.getResultList();
	}

	
	@Override
	public List<BreedingRequest> GetAllBreedingRequestByOffer(int id) {
		Query query = em.createQuery("select b from BreedingRequest b join b.offer bo  where bo.id=:id ");
		query.setParameter( "id", id);
		//query.setParameter( "name", member.getId() );
		
		return query.getResultList();
	}

	@Override
	public void AcceptBreedingRequest(int id) {
		BreedingRequest breedingRequest1 = em.find(BreedingRequest.class,id);
		BreedingOffer breedingOffer = em.find(BreedingOffer.class,breedingRequest1.getOffer().getId());
		//BreedingOffer breedingOffer1 = em.find(BreedingOffer.class,);
		//Animal animal = em.find(Animal.class,breedingRequest.getAnimal().getId());
		if(breedingOffer != null)
		{
			breedingRequest1.setStatus(Statut.confirmed);
			breedingOffer.setClosed(true);
			removeBreeding(breedingOffer.getId());
			//if(breedingOffer.isLocated())
			//{
			BreedingDetails breedingDetails = new BreedingDetails(new Date(), Statut.inProgress, breedingOffer.getAnimal().getOwner().getAddress(), breedingRequest1);
			em.persist(breedingDetails);
			//}
			//else 
			//{
			//	BreedingDetails breedingDetails = new BreedingDetails(new Date(), Statut.inProgress, null, breedingRequest1);
			//	em.persist(breedingDetails);
				
			//}
			SendEmail mail = new SendEmail();
			String [] to = {breedingOffer.getAnimal().getOwner().getEmail()};
			mail.sendMail("aymen.makhlouf@esprit.tn", "futuraymen", "your request is confirmed",to);
				
		}
		
	}

	@Override
	public void DeclineBreedingRequest(int id) {
		BreedingRequest breedingRequest1 = em.find(BreedingRequest.class,id);
		BreedingOffer breedingOffer = em.find(BreedingOffer.class,breedingRequest1.getOffer().getId());
		if(breedingOffer != null)
		{
			breedingRequest1.setStatus(Statut.failed);
			//removeBreeding(breedingOffer.getId());
			SendEmail mail = new SendEmail();
			String [] to = {breedingOffer.getAnimal().getOwner().getEmail()};
			mail.sendMail("aymen.makhlouf@esprit.tn", "futuraymen", "your request is refused",to);
				
		}
	}



	@Override
	public int DistanceBetweenOfferandRequest(BreedingRequest breedingRequest) {
		BreedingRequest breedingRequest1 = em.find(BreedingRequest.class,breedingRequest.getId());
		double x1 = breedingRequest1.getAnimal().getOwner().getAddress().getX();
		double y1 = breedingRequest1.getAnimal().getOwner().getAddress().getY();
		double x2 = breedingRequest1.getOffer().getAnimal().getOwner().getAddress().getX();
		double y2 = breedingRequest1.getOffer().getAnimal().getOwner().getAddress().getY();
		Distance dist = new Distance();
		return (int) dist.distance(x1, y1, x2, y2, "K");
	}
	

	@Override
	public List<Animal> showAnimalinBreedingRequest(BreedingRequest breedingRequest) {
		Query query = em.createQuery("select b.animal from BreedingRequest b  where b.id=:id");
		query.setParameter( "id", breedingRequest.getId());
		//query.setParameter( "name", member.getId() );
		
		return query.getResultList();
	}

	@Override
	public List<Member> showMemberinBreedingRequest(BreedingRequest breedingRequest) {
		Query query = em.createQuery("select b.animal.owner from BreedingRequest b   where  b.id=:id");
		query.setParameter( "id", breedingRequest.getId());
		//query.setParameter( "name", member.getId() );
		
		return query.getResultList();
	}

	@Override
	public Address showAdressRequest(BreedingRequest breedingRequest) {
		Query query = em.createQuery("select b.animal.owner.address from BreedingRequest b   where  b.id=:id",Address.class);
		query.setParameter( "id", breedingRequest.getId());
		//query.setParameter( "name", member.getId() );
		
		return  (Address) query.getSingleResult();
	}

	
	
	public void removeBreeding(int id)
	{
		Query query = em.createQuery("select b from BreedingRequest b where b.status= 'inProgress' or b.status= 'failed'  and b.offer.id=:id");
		query.setParameter("id",id);
		List<BreedingRequest> lr = query.getResultList();
		for(Iterator it=lr.iterator(); it.hasNext();) 
		{   
			BreedingRequest breedingRequest1 = (BreedingRequest) it.next();
			em.remove(breedingRequest1);
		}
		
		
		
	}
	

	public boolean AnimalControl(Animal animal) {
	    
	  	   SimpleDateFormat dateFormat=new  SimpleDateFormat("yyyy-MM-dd");
		       if (haveOldBreeding(animal)){
		Query i =  em.createNativeQuery("SELECT DATEDIFF(?1,?2)");
		i.setParameter("2", dateFormat.format(new Date()));
		i.setParameter("1", LastdateOfBreeding(animal));
	     System.out.println(i.getSingleResult().toString());
     int	mois = Integer.parseInt(i.getSingleResult().toString()); 
    // System.out.println(mois+1);  
                       if(Math.abs(mois)<31)
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
	public int DistanceBetweenOfferandRequest1(Walkings walking) {
		
		Walkings walking1 = em.find(Walkings.class,walking.getId());
		
		Address a1 = em.find(Address.class, walking1.getArriveId());
		Address a2 = em.find(Address.class, walking1.getDepartId());
		
		double x1 = a1.getX();
		double y1 = a1.getY();
		double x2 = a2.getX();
		double y2 = a2.getY();
		Distance dist = new Distance();
		return (int) dist.distance(x1, y1, x2, y2, "K");
	}
	
	@Override
	public List<Walkings> getAllBreedingOffer(Walkings walking) {
		Query query = em.createQuery("select b from Walkings b where b.id=:id");
		  query.setParameter("id", walking.getId());
		return query.getResultList();
				}
	@Override
   public void mail (Member member)
   {
        Member member2 = em.find(Member.class,member.getId());	
	   SendEmail mail = new SendEmail();
		String [] to = {"aymen.makhlouf5@gmail.com"};
		mail.sendMail("aymen.makhlouf@esprit.tn", "futuraymen", "Walking",to);
   }
	
	@Override
	public BreedingRequest getBreedingRequestBybr(int id) {
		
		//Animal animal1= em.find(Animal.class,3);
		Query query = em.createQuery("select br from BreedingRequest br where br.id=:id",BreedingRequest.class);
		query.setParameter("id", id );
		
		return (BreedingRequest)query.getSingleResult();
	}
	
	
	@Override
	public BreedingRequest GetAllBreedingRequestByOfferD(int id) {
		Query query = em.createQuery("select b from BreedingRequest b join b.offer bo  where bo.id=:id and b.status='confirmed' ");
		query.setParameter( "id", id);
		//query.setParameter( "name", member.getId() );
		
		return (BreedingRequest)query.getSingleResult();
	}
	
	/*
	public boolean test(int id, int offer ){
		return true;
	}
	*/
	@Override
	public boolean verif(int ido){
	
	Query query = em.createQuery("select b from BreedingRequest b join b.offer bo  where bo.id=:id ");
	query.setParameter( "id", ido);
	//query.setParameter( "name", member.getId() );
	
	List<BreedingRequest> lr = query.getResultList();
	     
	for(Iterator it=lr.iterator(); it.hasNext();) 
	{   
		BreedingRequest b = (BreedingRequest) it.next();
		if(b.getAnimal().getId() == 1)
		
			return true ;
		   // Dmax.toLocaleString();
	
	
	}
	return  false;
	}
	
	
	@Override
	public BreedingOffer GetofferbyRequest(int id) {
		Query query = em.createQuery("select b.offer from BreedingRequest b where b.id =:id  ");
		query.setParameter( "id", id);
		//query.setParameter( "name", member.getId() );
		
		return (BreedingOffer)query.getSingleResult();
	}
	
		
	
	

}
