package tn.esprit.seahawks.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.ReviewServiceLocal;
import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.FoundReview;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.LostReview;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.Review;

@Stateless
public class ReviewService implements ReviewServiceLocal{
	@PersistenceContext
	EntityManager em;

	@Override
	public boolean chooseRightAnswerL(int rev) {
	LostReview r =	em.find(LostReview.class, rev);
		try {
		r.setCorrectAnswer(true);
	
			toggleAnimalStatus(r.getReportLost());
			ReportService repServ = new ReportService();
			repServ.closeReport(em.find(LostReport.class,r.getReportLost().getId()));
		
		
		em.merge(r);
		return true;
		}catch(Exception e)
		{
		
			return false ;
		}
	}
	
	@Override
	public boolean chooseRightAnswerF(int rev) {
		FoundReview r =	em.find(FoundReview.class, rev);
		try {
			
				r.setCorrectAnswer(true);	
				ReportService repServ = new ReportService();
				repServ.closeReport(em.find(FoundReport.class,r.getFoundReport().getId()));
			
			
			em.merge(r);
			return true;
			}catch(Exception e)
			{
			
				return false ;
			}
	}

	@Override
	public Map<FoundReport, List<FoundReview>> showFoundReportAlongReviews() {
		
		Map<FoundReport,List<FoundReview>> 	mResult = new HashMap<>();
		TypedQuery<FoundReport> query= em.createQuery("select r from FoundReport r",FoundReport.class);	
		Iterator<FoundReport> itr = query.getResultList().iterator();
		while(itr.hasNext()) {
            FoundReport element = itr.next();
    		TypedQuery<FoundReview> query2= em.createQuery("select r from FoundReview r where r.foundReport=:x",FoundReview.class);	
            query2.setParameter("x", element);		
            mResult.put(element,query2.getResultList());
		}
         //   List<FoundReview> results = query.getResultList();
		return mResult;
	}

	@Override
	public Map<LostReport, List<LostReview>> showLostReportAlongReviews() {
		Map<LostReport,List<LostReview>> 	mResult =  new HashMap<>();
		TypedQuery<LostReport> query= em.createQuery("select r from LostReport r",LostReport.class);	
		Iterator<LostReport> itr = query.getResultList().iterator();
		while(itr.hasNext()) {
			LostReport element = itr.next();
    		TypedQuery<LostReview> query2= em.createQuery("select r from LostReview r where r.reportLost=:x",LostReview.class);	
            query2.setParameter("x", element);		
            mResult.put(element,query2.getResultList());
		}
         //   List<FoundReview> results = query.getResultList();
		return mResult;
	}

	

	@Override
	public boolean removeFoundReview(int i) {
		if((em.find(FoundReview.class, i )!= null)){
			FoundReview r = em.find(FoundReview.class,i);
			try {
				
				em.remove(r);
				return true;
			}
			/*if (r instanceof LostReview)
			{
				em.remove((LostReview)em.find(LostReview.class, r.getId()));
				return true;
				
			}*/
			/*else if (r instanceof FoundReview)
			{
				em.remove((FoundReview)em.find(FoundReview.class, r.getId()));
				return true;
				
			}
			else return false;}*/
			catch(Exception e)
			{
			
				return false ;
			}
		}
		return false;
	}
	

	@Override
	public boolean editLostReview(LostReview r) {
		if((em.find(LostReview.class, r.getId() )!= null)){
			try {
				em.merge(em.find(LostReview.class, r.getId() ));
				return true;}
			catch(Exception e)
			{
			
				return false ;
			}
		}
		return false;
	}
	@Override
	public boolean editFoundReview(FoundReview r) {
		if((em.find(FoundReview.class, r.getId() )!= null)){
			try {
				em.merge(em.find(FoundReview.class, r.getId() ));
				return true;}
			catch(Exception e)
			{
			
				return false ;
			}
		}
		return false;
	}

	@Override
	public boolean toggleAnimalStatus(LostReport l) {
		if((em.find(LostReport.class, l.getId()))!= null){
			try {
				l.getAnimal().setLost(false);
				em.merge(l);
				return true;
				}catch(Exception e)
				{
				
					return false ;
				}}
			else return false;
	}

	@Override
	public boolean addFoundReview(FoundReview review) {
		if((em.find(FoundReport.class, review.getFoundReport().getId()))!= null){
			try {
				em.persist(review);
				return true;
				}catch(Exception e)
				{
				
					return false ;
				}}
			else return false;
	}

	@Override
	public boolean addLostReview(LostReview review) {
		if((em.find(LostReport.class, review.getReportLost().getId()))!= null){
		try {
			em.persist(review);
			return true;
			}catch(Exception e)
			{
			
				return false ;
			}}
		else return false;
	}

	@Override
	public List<FoundReview> showFoundReviewsByReport(FoundReport f) {
		TypedQuery<FoundReview> query= em.createQuery("select r from FoundReview r where r.foundReport= :x",FoundReview.class);	
		query.setParameter("x", f);
		List<FoundReview> results = query.getResultList();
		return results;
	}

	@Override
	public List<LostReview> showLostReviewsByReport(LostReport f) {
		TypedQuery<LostReview> query= em.createQuery("select r from LostReview r where r.reportLost= :x order by r.reviewDate DESC",LostReview.class);	
		query.setParameter("x", f);
		List<LostReview> results = query.getResultList();
		return results;
	}

	@Override
	public boolean removeLostReview(int id) {
		if((em.find(LostReview.class, id )!= null)){
			LostReview r = em.find(LostReview.class,id);
			try {
				
				em.remove(r);
				return true;
			}
			/*if (r instanceof LostReview)
			{
				em.remove((LostReview)em.find(LostReview.class, r.getId()));
				return true;
				
			}*/
			/*else if (r instanceof FoundReview)
			{
				em.remove((FoundReview)em.find(FoundReview.class, r.getId()));
				return true;
				
			}
			else return false;}*/
			catch(Exception e)
			{
			
				return false ;
			}
		}
		return false;
	}
	

}
