package tn.esprit.seahawks.services;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.FundraiserServiceLocal;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.Fundraiser;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class FundraiserService implements FundraiserServiceLocal{
	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public boolean addFundraiser(Fundraiser f) {
		//verify if the user has an already running fundraiser, or if he is entering invalid input
		endFundraisers();
		em.flush();
		Query query = em.createQuery("SELECT f from Fundraiser f where f.launcher= :u AND f.state=true");
		query.setParameter("u", f.getLauncher());
		List<Fundraiser> runningFundraisers = query.getResultList();
		
		//if(runningFundraisers.isEmpty() && !f.getStartDate().after(f.getEndDate()) ){
			em.persist(f);
			return true;	
		/*}
		else 
			return false;*/
	}

	@Override
	public boolean updateFundraiser(Fundraiser f) {
		try {
			Fundraiser toUpdate = em.find(Fundraiser.class, f.getId());

			toUpdate.setDescription(f.getDescription());
			toUpdate.setTitle(f.getTitle());
			toUpdate.setGoal(f.getGoal());
			
			em.merge(toUpdate);
			
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean removeFundraiser(int id) {
		
		em.remove(em.find(Fundraiser.class, id));
		return true;
	}


	@Override
	public List<Fundraiser> getFundraisersByUser(User u) {
		try {

			TypedQuery<Fundraiser> query = em.createQuery("select f from Fundraiser f where f.launcher = :u", Fundraiser.class);
			query.setParameter("u", u);
			List<Fundraiser> results = query.getResultList();
			return results;

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Fundraiser> getAllFundraisers() {
		Query query = em.createQuery("SELECT f FROM Fundraiser f order by f.endDate DESC");
		return  query.getResultList();
	}

	@Override
	public List<Fundraiser> getOpenFundraisers() {
		try {
			TypedQuery<Fundraiser> query = em.createQuery("select f from Fundraiser f where f.state = :s order by f.endDate DESC", Fundraiser.class);
			query.setParameter("s", true);
			List<Fundraiser> results = query.getResultList();
			return results;

		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public boolean closeFundraiser(Fundraiser f) {
		try{
			Query query = em.createQuery("UPDATE Fundraiser f SET f.state=false");
			int updated = query.executeUpdate();
			if(updated!=0){
				return true;
			}
			else return false;
		}catch(Exception e){
			return false;
		}
		
	}


	@Override
	public List<Fundraiser> getReachedFundraisers() {
		try{
		Query q = em.createQuery("SELECT f FROM Fundraiser f "
				+ "where (f.goal <= f.collectedDonations AND f.endDate < :today)");
		q.setParameter("today",new Date(),TemporalType.DATE);
		return (List<Fundraiser>) q.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public Long getNbrReachedFundraisers() {
		try{
			TypedQuery<Long> query = em.createQuery("SELECT COUNT(f.id) FROM Fundraiser f "
					+ "where (f.goal <= f.collectedDonations AND f.endDate < :today)",Long.class);
			query.setParameter("today",new Date(),TemporalType.DATE);
			return (Long)query.getSingleResult();
			}catch(Exception e){
				return 0L;
			}
	}

	
	@Override
	public double getAverageRaisedMoney() {
		try{
			TypedQuery<Double> query = em.createQuery("SELECT AVG(f.collectedDonations) FROM Fundraiser f "
					+ "where f.state=false AND f.endDate < :today",Double.class)
					.setParameter("today",new Date(),TemporalType.DATE);
			return query.getSingleResult();
			}catch(Exception e){
				return -1;
			}
	}

	@Override
	public double getAverageGoal() {
		try{
			TypedQuery<Double> query = em.createQuery("SELECT AVG(f.goal) FROM Fundraiser f where f.state=false",Double.class);
			return query.getSingleResult();
			}catch(Exception e){
				return -1;
			}
	}

	@Override
	public Map<String, Long> getNbrFundraisersByLocation() {
		try{
			Map<String, Long> map = new HashMap<String,Long>();
			Query query = em.createQuery("SELECT f.location,COUNT(f.id) FROM Fundraiser f "
					+ "GROUP BY f.location");
			
			List<Object[]> list = query.getResultList();
			
			for (Object[] result : list) {
				map.put(result[0].toString(), (Long)result[1]);
			
			}
			return map;
			
			}catch(Exception e){
				return null;
		}
	}


	@Override

	//@Schedule(second="*/85", minute="*",hour="*", persistent=false)
	public void endFundraisers() {
		
		try{
			
			Query query = em.createQuery("UPDATE Fundraiser f SET f.state=false "
					+ "WHERE f.endDate <= :today OR f.goal <= f.collectedDonations");
			query.setParameter("today",new Date(),TemporalType.TIMESTAMP);
			
			int updated = query.executeUpdate();
			if(updated!=0){
			
				System.out.println(updated+" Fundraisers have been CLOSED by the server schedule system."
						+ " They either has reached its goal of raised money, or reached the end date");
			}

		}catch(Exception e){}
		
	}

	@Override
	public int getRemainingDays(int id) {
			try{
			Query query = em.createQuery("SELECT datediff(f.endDate, :today) from Fundraiser f "
					+ "where f.id= :id AND f.state= :state");
			query.setParameter("today",new Date(),TemporalType.TIMESTAMP);
			query.setParameter("id",id);
			query.setParameter("state",true);
			int result = Math.abs((int)query.getSingleResult());
			return result;
				/*Fundraiser f = em.find(Fundraiser.class, id);
				Query query = em.createNativeQuery("SELECT TIMESTAMPDIFF(SECOND,?1,?2)");
				query.setParameter("1",f.getEndDate());
				query.setParameter("2",new Date().getTime());
				
				int result = Math.abs((int)query.getSingleResult());
				return result;*/
			}catch(Exception e)
				{
				return -100;
				}
			
			
	}
	
	public Fundraiser getFundraiserById(int id){
		//TypedQuery<Fundraiser> query = em.createQuery("SELECT f FROM Fundraiser f where f.id="+id,Fundraiser.class);
		//return query.getSingleResult();
		return em.find(Fundraiser.class, id);
	}
	
	@Override
	public boolean addFundraiserPhoto(int fid, String imgUrl, String title){
		boolean result = false;
		 if (imgUrl.length()>100)
		 {
			 BufferedImage image = null;
			
			 byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(imgUrl);

			 try {
				System.out.println(imgUrl);
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
				File outputfile = new File(title);
				
				ImageIO.write(img, "jpg", new File("D:/4TWIN2/Java EE 7/JBoss Workspace/seahawks/seahawks-web/src/main/webapp/WEB-INF/fundraisers"+title+".jpg"));
				System.out.print(outputfile.toURI().toString());
				//article.setImageURL(title+".jpg");
				Fundraiser f = em.find(Fundraiser.class, fid);
				f.setLocation(imgUrl);
				em.merge(f);
				result= true;
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result= false;
			}
		 }
		 
		 return result;
		
		
	}

	
}

