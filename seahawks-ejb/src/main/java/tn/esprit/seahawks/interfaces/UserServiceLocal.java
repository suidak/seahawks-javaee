package tn.esprit.seahawks.interfaces;

import java.util.List;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Local
public interface UserServiceLocal {
	public boolean confirmAccount(String token, User u);
	public boolean changePassword(String oldPassword, String newPassword, User u);
	public void ForgotPasswordByPhone();
	public void ForgotPasswordByMail(User user);
	public boolean changeForgotPassword(String token, String newPwd,User user);
	public boolean DesactivateAccount(User u);
	public User FindUser(int id);//Added by Khaled
	public User getFullUser(User u);
	public User Authenticate(String email, String pwd);
	public List<User> getAllUsers();
	public List<User> getAllVets();
	public List<User> getLocalVets(User u);
	public int isLoggedIn24H();
	public void addUserPhoto(int user_id, String photo);

	public User getUserByEmail(String email); //added by Sejir
}
