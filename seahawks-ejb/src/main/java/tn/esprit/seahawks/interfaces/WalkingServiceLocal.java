package tn.esprit.seahawks.interfaces;



import java.util.List;

import javax.ejb.Local;


import tn.esprit.seahawks.persistence.Walkings;

@Local
public interface WalkingServiceLocal {
	public List<Walkings> getWalkingByDog();//
	

}
