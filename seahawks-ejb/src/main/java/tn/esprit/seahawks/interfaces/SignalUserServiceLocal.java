package tn.esprit.seahawks.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.SignalUser;
import tn.esprit.seahawks.persistence.User;

@Local
public interface SignalUserServiceLocal {
	public boolean blockUser(SignalUser su);
	public boolean unblockUser(SignalUser su);
	public List<SignalUser> isAlreadyBlocked(int id_doer, int id_signaled);
	public int howMuchSignal(User u);
}
