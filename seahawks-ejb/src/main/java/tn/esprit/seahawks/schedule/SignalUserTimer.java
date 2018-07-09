package tn.esprit.seahawks.schedule;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import tn.esprit.seahawks.interfaces.SignalUserServiceLocal;
import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.persistence.User;

@Stateless
public class SignalUserTimer {

	@EJB(beanName = "SignalUserService")
	SignalUserServiceLocal su;

	@EJB(beanName = "UserService")
	UserServiceLocal userService;

//	@Schedule(second = "*/45", minute = "*", hour = "*", persistent = false)
//	public void ban() {
//		List<User> list_user = userService.getAllUsers();
//		Iterator<User> itr = list_user.iterator();
//		while (itr.hasNext()) {
//			User element_user = itr.next();
//			if(su.howMuchSignal(element_user)>2){
//				userService.DesactivateAccount(element_user);
//				System.out.println("done");
//			}
//
//		}
//		
//	}
}
