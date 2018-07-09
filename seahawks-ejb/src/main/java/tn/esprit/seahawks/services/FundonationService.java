package tn.esprit.seahawks.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import tn.esprit.seahawks.interfaces.FundonationServiceLocal;
import tn.esprit.seahawks.persistence.FunDonation;
import tn.esprit.seahawks.persistence.Fundraiser;
import tn.esprit.seahawks.persistence.Member;

@Stateless
public class FundonationService implements FundonationServiceLocal {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public boolean donate(FunDonation donation, Map<String, Object> chargeParams, int fid, int mid) {
		Stripe.apiKey = "sk_test_WYtj3NrHI5ToRkXwrEvm4H67";

		/*
		 * RequestOptions options = RequestOptions .builder()
		 * .setIdempotencyKey("WheZruwAEKf6MQ7C") .build();
		 */

		Charge charge = null;
		try {
			charge = Charge.create(chargeParams);
			Fundraiser f = em.find(Fundraiser.class, fid);
			Member m = em.find(Member.class, mid);
			donation.setDonator(m);
			donation.setFundraiser(f);
			em.persist(donation);
			f.setCollectedDonations(f.getCollectedDonations() + donation.getAmount());
			em.merge(f);
			em.flush();

			return true;

		} catch (Exception e) {
			System.out.println("Erreur lors du paiement!"+e.toString());
			return false;
		}

	}

	@Override
	public List<FunDonation> getDonationsByMember(Member m) {
		try {
			TypedQuery<FunDonation> query = em.createQuery("select f from FunDonation f where f.donator = :member",
					FunDonation.class);
			query.setParameter("member", em.find(Member.class, m.getId()));
			List<FunDonation> results = query.getResultList();
			return results;

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<Integer, Double> getSumDonationsByMember() {
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		try {
			Query query = em.createQuery("select f.donator,SUM(f.amount) from FunDonation f " + "GROUP BY f.donator");

			List<Object[]> list = query.getResultList();
			for (Object[] result : list) {
				map.put(((Member) result[0]).getId(), (Double) result[1]);
			}

			return map;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Double getAverageDonations() {
		try {
			TypedQuery<Double> query = em.createQuery("select AVG(f.amount) from FunDonation f", Double.class);
			Double moy = query.getSingleResult();
			return moy;
			

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Map<Integer, Double> getAverageDonationsByFundraiser() {
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		try {
			Query query = em
					.createQuery("select f.fundraiser,AVG(f.amount) from FunDonation f " + "GROUP BY f.fundraiser");

			List<Object[]> list = query.getResultList();
			for (Object[] result : list) {
				map.put(((Fundraiser) result[0]).getId(), (Double) result[1]);
			}

			return map;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Double getAverageDonationsByCountry(String country) {
		try {
			TypedQuery<Double> query = em.createQuery("SELECT AVG(f.amount) FROM FunDonation f join f.donator d join d.address a "
					+ "WHERE a.country = :country", Double.class);

			query.setParameter("country", country);
			return (Double) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Long getNbrDonatorsByCountry(String country) {
		try {
			TypedQuery<Long> query = em.createQuery("SELECT COUNT(distinct f.donator) FROM FunDonation f " + "join f.donator d "
					+ "join d.address a" + " WHERE a.country = :country", Long.class);

			query.setParameter("country", country);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			return 0L;
		}
	}

	/*
	 * @Override public Double getDonationsToday() { Date date = new Date();
	 * System.out.println(date); LocalDate localDate =
	 * date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); int year =
	 * localDate.getYear(); int month = localDate.getMonthValue(); int day =
	 * localDate.getDayOfMonth();
	 * 
	 * TypedQuery<Double> query = em.createQuery(
	 * "select SUM(f.amount) from FunDonation f where DATE(f.donationDate)= :today"
	 * ,Double.class);
	 * 
	 * query.setParameter("today", year+"-"+month+"-"+day);
	 * 
	 * return (Double)query.getSingleResult(); }
	 */

}
