package tn.esprit.seahawks.interfaces;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Address;

@Local
public interface AddressServiceLocal {
	
	//user subscription releated
	public int addAddress(Address a,int id);

	boolean modifyAddress(Address oldA, Address newA);

	Address findOne(int id);
}
