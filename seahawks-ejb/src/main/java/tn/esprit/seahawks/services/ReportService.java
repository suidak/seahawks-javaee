package tn.esprit.seahawks.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import tn.esprit.seahawks.interfaces.ReportServiceLocal;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.FoundReview;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Organisation;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class ReportService implements ReportServiceLocal{
	@PersistenceContext
	EntityManager em;
	@Override
	public boolean closeReport(Report Report) {
		try {
			Report.setIsClosed(true);
			em.merge(Report);
			return true;
		}catch(Exception e)
		{
		
			return false ;
		}
		
	}
	
	//Distance
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2) {

	    final int R = 6371; // radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters
	    distance = Math.pow(distance, 2);
	    return Math.sqrt(distance);
	}
	//Distance

	@Override
	public List<Report> getAllReport() {
		TypedQuery<Report> query= em.createQuery("select r from Report",Report.class);	
		List<Report> results = query.getResultList();
		return results;
	}

	@Override
	public List<Report> getAllisClosedReport() {
		TypedQuery<Report> query= em.createQuery("select r from Report r where r.isClosed=true",Report.class);	
		List<Report> results = query.getResultList();
		return results;
	}

	@Override
	public Report showReport(int id) {
		TypedQuery<Report> query= em.createQuery("select r from Report r where r.id = :x",Report.class);	
		query.setParameter("x",id );
		
		Report result = query.getSingleResult();
		return result;
	}

	@Override
	public boolean editReport(Report report) {
		try {
			em.merge(report);
			return true;
		}catch(Exception e)
		{
		
			return false ;
		}
	}

	@Override
	public boolean deleteReport(Report Report) {
		try {
			//addCascading
			em.remove(Report);			
			return true;
		}catch(Exception e)
		{
		
			return false ;
		}	
	}

	@Override
	public List<Report> searchReport(String criteria) {
		
		TypedQuery<Report> query= em.createQuery("select r from Report r  where (r.reporterUser= :u OR r.title= :u OR r.description= :u)",Report.class);	
		query.setParameter("u", criteria);
		List<Report> results = query.getResultList();
		return results;
	}

	@Override
	public List<Report> getUserSpecificReports(User user) {
		TypedQuery<Report> query= em.createQuery("select r from Report r where r.reporterUser=u",Report.class);	
		query.setParameter("u", user);
		List<Report> results = query.getResultList();
		return results;
	}

	@Override
	public List<Report> getOpenReport() {
		TypedQuery<Report> query= em.createQuery("select r from Report r where r.isClosed=false",Report.class);	
		List<Report> results = query.getResultList();
		return results;
	}

	@Override
	public boolean addLostReport(LostReport lostreport) {
		try {
			
			Animal a =em.find(Animal.class,lostreport.getAnimal().getId());
			if(a.isLost()) {
				return false;	
			}
			a.setLost(true);
			em.persist(lostreport);
			return true;
			
		}catch(Exception e)
		{
		
			return false ;
		}
	}

	@Override
	public List<LostReport> getRewardingLostReport() {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where r.reward > 0",LostReport.class);	
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReport> getAllLostReport() {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r",LostReport.class);	
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReport> getAllisClosedLostReport() {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where r.isClosed=true",LostReport.class);	
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReport> searchLostReport(String criteria) {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where (r.title LIKE :u OR r.description LIKE :u OR r.localisation.country LIKE :u OR r.localisation.city LIKE :u OR r.localisation.street LIKE :u OR r.reporterUser.login LIKE :u)",LostReport.class);	
		query.setParameter("u", '%'+ criteria +'%');
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReport> getUserSpecificLostReports(User u) {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where r.reporterUser =:x",LostReport.class);	
		query.setParameter("x",u );
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReport> getOpenLostReport() {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where r.isClosed=false",LostReport.class);	
		List<LostReport> results = query.getResultList();
		return results;
	}

	@Override
	public LostReport showLostReport(int id) {
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r where r.id = :x",LostReport.class);	
		query.setParameter("x",id );
		
		LostReport result = query.getSingleResult();
		return result;
	}

	@Override
	public boolean addFoundReport(FoundReport f) {
		try {
			em.persist(f);
			return true;
		}catch(Exception e)
		{
		
			return false ;
		}
	}

	
	@Override
	public List<FoundReport> getAllFoundReport() {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r order by r.date DESC",FoundReport.class);	
		List<FoundReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<FoundReport> getAllisClosedFoundReport() {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r where r.isClosed=true",FoundReport.class);	
		List<FoundReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<FoundReport> searchFoundReport(String criteria) {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r where (r.title LIKE :u OR r.description LIKE :u)",FoundReport.class);	
		query.setParameter("u", '%'+ criteria +'%');
		List<FoundReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<FoundReport> getUserSpecificFoundReport(User u) {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r where r.reporterUser =:x",FoundReport.class);	
		query.setParameter("x",u );
		List<FoundReport> results = query.getResultList();
		return results;
	}

	@Override
	public List<FoundReport> getOpenFoundReport() {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r where r.isClosed=false",FoundReport.class);	
		List<FoundReport> results = query.getResultList();
		return results;
	}

	@Override
	public FoundReport showFoundReport(int id) {
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r where r.id = :x",FoundReport.class);	
		query.setParameter("x",id );
		
		FoundReport result = query.getSingleResult();
		return result;
	}

	@Override
	public boolean CloseFoundReport(Organisation org,FoundReport f) {
		try {
			f.setCareTaker(passToCareTaker(f));
			f.setIsClosed(true);
			f.setTitle(f.getTitle()+"[Closed- Passed to "+org.getOrgName()+"]");
			em.merge(f);
			return true;
		}catch(Exception e)
		{
		
			return false ;
		}
	}

	@Override
	public Organisation passToCareTaker(FoundReport f) {
		Map<Integer,Double> 	orgDistance = new HashMap<>();
		double latF= f.getLocalisation().getX();
		double lonF= f.getLocalisation().getY();	
		TypedQuery<Organisation> queryD= em.createQuery("select o from Organisation o",Organisation.class);
		Iterator<Organisation> itr = queryD.getResultList().iterator();
		while(itr.hasNext()) {
			Organisation element = itr.next();
			orgDistance.put(element.getId(),distance(latF, lonF, element.getAddress().getX(), element.getAddress().getY()));
         }
		 Entry<Integer, Double> min = null;
		 for (Entry<Integer, Double> entry : orgDistance.entrySet()) {
		     if (min == null || min.getValue() > entry.getValue()) {
		         min = entry;
		     }
		 }
		 //check if Org exist
		 if (min != null){
			 return em.find(Organisation.class, min.getKey());
		 }
		 else {
			 TypedQuery<Organisation> query= em.createQuery("select o from Organisation o inner Join o.address a on a.id=o.address where (a.country= :c AND a.city= :s) order by o.id DESC ",Organisation.class);	
			 query.setParameter("c",f.getLocalisation().getCountry() );
			 query.setParameter("s", f.getLocalisation().getCity());
			 if (query.getSingleResult() != null) 
			 {
				 Organisation result = query.setMaxResults(1).getSingleResult();
				 return result;
			 }
			 else 
			 {
				 TypedQuery<Organisation> queryb= em.createQuery("select o from Organisation o inner Join o.address a on a.id=o.address where a.country= :c order by o.id DESC",Organisation.class);	
				 queryb.setParameter("c",f.getLocalisation().getCountry());
				 if(query.getSingleResult() != null)
				 {
					 Organisation result = queryb.setMaxResults(1).getSingleResult();
					 return result;
				 }
				 else 
				 {
					 TypedQuery<Organisation> queryc= em.createQuery("select o from Organisation o order by o.foundDate DESC",Organisation.class);	
					 Organisation result = queryc.setMaxResults(1).getSingleResult();
					 return result;
				 }
		
			 }
		 }

	}

	@Override
	public LostReport findLostReport(int id) {
		
		return em.find(LostReport.class, id);
	}

	@Override
	public FoundReport findFoundReport(int id) {
		return em.find(FoundReport.class, id);
	}

	@Override
	public Map<String, Long> statActivityPerCountry() {
		Map<String,Long> 	mResult = new HashMap<>();

		TypedQuery<String> queryc= em.createQuery("select DISTINCT r.reporterUser.address.country from Report r",String.class);	
		List <String> result = queryc.getResultList();
		Iterator<String> itr = result.iterator();
		while(itr.hasNext()) {
            String country = itr.next();
    		TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country =:x",Long.class);	
            query2.setParameter("x", country);	
            TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.reporterUser.address.country =:x",Long.class);	
            query3.setParameter("x", country);
            TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country =:x",Long.class);	
            query4.setParameter("x", country);
            TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country =:x",Long.class);	
            query5.setParameter("x", country);
            Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
            mResult.put(country,count);
		}
		return mResult;
	}

	@Override
	public Long stattn() {
 		//TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country = 'tunisia'",Long.class);	
         TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.localisation.country  = 'tunisia'",Long.class);	
        // TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country  = 'tunisia'",Long.class);	
        // TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country = 'tunisia'",Long.class);	
      //   Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
        
		return query3.getSingleResult();
	}

	@Override
	public Long statalg() {
		//TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country = 'algeria'",Long.class);	
        TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.localisation.country  = 'algeria'",Long.class);	
        //TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country  = 'algeria'",Long.class);	
       // TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country = 'algeria''",Long.class);	
       // Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
        return query3.getSingleResult();

	}

	@Override
	public Long statfr() {
		//TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country = 'france'",Long.class);	
        TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.localisation.country  = 'france'",Long.class);	
      //  TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country  = 'france'",Long.class);	
       // TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country = 'france'",Long.class);	
       // Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
       return query3.getSingleResult();
	}

	@Override
	public Long stataus() {
		//TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country = 'australia'",Long.class);	
       TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.localisation.country  = 'australia'",Long.class);	
       // TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country  = 'australia'",Long.class);	
       // TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country = 'australia'",Long.class);	
       // Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
        return query3.getSingleResult();

	}

	@Override
	public Long statchina() {
		//TypedQuery<Long> query2= em.createQuery("select Count(r) from FoundReview r where r.reviewer.address.country = china",Long.class);	
        TypedQuery<Long> query3= em.createQuery("select Count(r) from LostReport r  where r.localisation.country = china",Long.class);	
        //TypedQuery<Long> query4= em.createQuery("select Count(r) from FoundReport r where r.reporterUser.address.country  = china",Long.class);	
       // TypedQuery<Long> query5= em.createQuery("select Count(r) from LostReview r where r.reviewer.address.country = china",Long.class);	
        //Long count = query2.getSingleResult()+query3.getSingleResult()+query3.getSingleResult()+query4.getSingleResult()+query5.getSingleResult();
        return query3.getSingleResult();

	}
	
	/* public boolean SendMail(Mail mail){ 
			
			  try{
		          String host ="smtp.gmail.com" ;
		          String user = mail.getSenderMail();
		          String pass = mail.getMailPassword();
		          String to = "customercare@universalhaven.com";
		          String from = mail.getSenderMail();
		          String subject = mail.getSubject();
		          String messageText = mail.getContent();
		          boolean sessionDebug = false;
		          Properties props = System.getProperties();
		          props.put("mail.smtp.starttls.enable", "true");
		          props.put("mail.smtp.host", host);
		          props.put("mail.smtp.port", "587");
		          props.put("mail.smtp.auth", "true");
		          props.put("mail.smtp.starttls.required", "true");

		          //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		          Session mailSession = Session.getDefaultInstance(props, null);
		          mailSession.setDebug(sessionDebug);
		          Message msg = new MimeMessage(mailSession);
		          msg.setFrom(new InternetAddress(from));
		          InternetAddress[] address = {new InternetAddress(to)};
		          msg.setRecipients(Message.RecipientType.TO, address);
		          msg.setSubject(subject); msg.setSentDate(new Date());
		          msg.setText(messageText);

		         Transport transport=mailSession.getTransport("smtp");
		         transport.connect(host, user, pass);
		         transport.sendMessage(msg, msg.getAllRecipients());
		         transport.close();
		         System.out.println("message send successfully");
		         return true;		      }catch(Exception ex)
		      {
		          System.out.println(ex);
		          return false;
		      }
		  }*/



}
