package tn.esprit.seahawks.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.seahawks.interfaces.AddressServiceLocal;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class AddressService implements AddressServiceLocal {
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public int addAddress(Address a,int id) {
		a.setX(36.9487824);
		a.setY(10.198343099999988);
		em.persist(a);
		User u = em.find(User.class, id);
		u.setAddress(a);
		System.out.println("user is: "+u);
		em.merge(u);
		System.out.println("user merged with new address");
		return a.getId();
	}
	
	@Override
	public boolean modifyAddress(Address oldA, Address newA) {
		// TODO Auto-generated method stub
		
			if (newA.getCity() != null) {
				oldA.setCity(newA.getCity());
			}
			if (newA.getCountry() != null) {
				oldA.setCountry(newA.getCountry());
			}
			if (newA.getStreet() != null) {
				oldA.setStreet(newA.getStreet());
			}
			if (newA.getState() != null) {
				oldA.setState(newA.getState());
			}
			if (newA.getZip() != null) {
				oldA.setZip(newA.getZip());
			}
			
			if(em.merge(oldA) != null)
				return true;
			return false ;

	}
	
	@Override
	public Address findOne(int id) {
		Address u = em.find(Address.class, id);
		return u;
	}


	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		}
		double dist_carre = Math.pow(dist, 2);
		return (Math.sqrt(dist_carre));
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
}
