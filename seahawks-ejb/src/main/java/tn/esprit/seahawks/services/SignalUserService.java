package tn.esprit.seahawks.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.SignalUserServiceLocal;
import tn.esprit.seahawks.persistence.SignalUser;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class SignalUserService implements SignalUserServiceLocal {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public boolean blockUser(SignalUser su) {
		TypedQuery<User> u_doer = em.createQuery("select u from User u where u.id=:id_doer", User.class);
		TypedQuery<User> u_signaled = em.createQuery("select u from User u where u.id=:id_signaled", User.class);

		u_doer.setParameter("id_doer", su.getDoer().getId());
		u_signaled.setParameter("id_signaled", su.getSignaled().getId());

		User doer = u_doer.getSingleResult();
		System.out.println("doer   " + doer);
		User signaled = u_signaled.getSingleResult();
		System.out.println("signaled  " + signaled);
		su.setDoer(doer);
		su.setSignaled(signaled);
		Date d = new Date();
		su.setSignalDate(d);
		em.persist(su);
		return true;

	}

	@Override
	public boolean unblockUser(SignalUser su) {
		// TODO Auto-generated method stub
		em.remove(em.contains(su) ? su : em.merge(su));
		return true;
	}

	@Override
	public List<SignalUser> isAlreadyBlocked(int id_doer, int id_signaled) {
		// TODO Auto-generated method stub
		System.out.println("dkhal lel already blocked");
		TypedQuery<SignalUser> q = em.createQuery("select s from SignalUser s where s.doer.id=:x and s.signaled.id=:y",
				SignalUser.class);
		q.setParameter("x", id_doer);
		q.setParameter("y", id_signaled);
		List<SignalUser> su = q.getResultList();
		System.out.println(su);
		return su;
		
	}

	@Override
	public int howMuchSignal(User u) {
		// TODO Auto-generated method stub
		TypedQuery<SignalUser> q = em.createQuery("select s from SignalUser s where s.signaled.id=:id",SignalUser.class);
		q.setParameter("id", u.getId());
		List<SignalUser> l = q.getResultList();
		return l.size();
	}

}
