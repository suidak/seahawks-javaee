package tn.esprit.seahawks.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.OrganisationServiceLocal;
import tn.esprit.seahawks.persistence.AccountStatus;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Organisation;
import tn.esprit.seahawks.persistence.User;
import tn.esprit.seahawks.util.SendConfirmationMail;

@Stateless
public class OrganisationService implements OrganisationServiceLocal {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	UserService us = new UserService();

	@Override
	public int addOrg(Organisation o) {
		o.setRole("Organisation");
		String str = us.getSaltString();
		o.setToken(str);
		o.setStatus(AccountStatus.waitingForConfirmation);
		String pwd = us.encrypt(o.getPassword());
		o.setPassword(pwd);
		o.setRole("Organisation");
		em.persist(o);
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112","oumayma.gader@esprit.tn", "Animal Caring team [Account confirmation code]","This is your confirmation code :"+str);

		return o.getId();
	}

	@Override
	public boolean updateOrg(Organisation o) {
		Organisation o_updated = em.find(Organisation.class, o.getId());
		o_updated.setLogin(o.getLogin());
		o_updated.setEmail(o.getEmail());
		o_updated.setPhoto(o.getPhoto());
		o_updated.setFoundDate(o.getFoundDate());
		o_updated.setOrgName(o.getOrgName());

		em.merge(o_updated);
		return true;
	}

	@Override
	public Organisation getOrgByName(String name) {
		TypedQuery<User> q = em.createQuery("select o from User o where o.orgName=:name", User.class);
		q.setParameter("name", name);
		Organisation org = (Organisation) q.getSingleResult();
		return org;
	}

	@Override
	public Organisation getOrgByFoundDate(Date d) {
		TypedQuery<User> q = em.createQuery("select o from User o where o.foundDate=:date", User.class);
		q.setParameter("date", d,TemporalType.DATE);
		Organisation org = (Organisation) q.getSingleResult();
		return org;
	}

	@Override
	public Organisation getOrgByAddress(String country, String city, String street) {
		TypedQuery<User> q = em.createQuery("select o from User o join Address a" + "in o.address_id = a.id "
				+ "where a.country =:country and a.city=:city and a.street=:street", User.class);
		q.setParameter("country", country);
		q.setParameter("city", city);
		q.setParameter("street", street);
		
		Organisation org = (Organisation) q.getSingleResult();
		return org;
	}
	
	@Override
	public List<Organisation> getAllOrg() {
		Query q = em.createQuery("select o from User o "
				+ "where o.role =:role ", User.class);
		q.setParameter("role", "Organisation");

		
		
		return q.getResultList();
	}

}
