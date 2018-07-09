package tn.esprit.seahawks.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Organisation;

@Local
public interface OrganisationServiceLocal {
	
	//subscribe as organisation
	public int addOrg(Organisation o);
	public boolean updateOrg(Organisation o);
	public Organisation getOrgByName(String name);
	public Organisation getOrgByFoundDate(Date d);
	public Organisation getOrgByAddress(String country, String city, String street);
	List<Organisation> getAllOrg();

}
