package tn.esprit.seahawks.util;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class DBPopulator {


	
	public DBPopulator() {
	}
	
	@PostConstruct
	public void init(){
		
	}
}
