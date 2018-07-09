package tn.esprit.seahawks.services;

import java.util.Random;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.interfaces.UserServiceRemote;
import tn.esprit.seahawks.persistence.AccountStatus;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Organisation;
import tn.esprit.seahawks.persistence.User;
import tn.esprit.seahawks.util.SendConfirmationMail;

@Stateless
public class UserService implements UserServiceLocal, UserServiceRemote {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;

	@Override
	public boolean confirmAccount(String token, User user) {

		User u = em.find(User.class, user.getId());

		String token_db = u.getToken();
		System.out.println(token_db);
		if (token.equals(token_db)) {
			u.setStatus(AccountStatus.activated);
			em.merge(u);
			System.out.println("token is valid ! Account is confirmed.");
			return true;
		} else {
			System.out.println("token not valid");
			return false;
		}

	}

	@Override
	public boolean changePassword(String oldPassword, String newPassword, User u) {
		User user = em.find(User.class, u.getId());
		String decrypted = decrypt(oldPassword);
		if (decrypted.equals(user.getPassword())) {
			String crypted_new = encrypt(newPassword);
			user.setPassword(crypted_new);
			em.merge(user);
			System.out.println("Password changed: old pwd is " + oldPassword + " new password is " + newPassword);
			return true;
		} else {
			System.out.println("password is wrong");
			return false;
		}

	}

	@Override
	public void ForgotPasswordByPhone() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ForgotPasswordByMail(User user) {
		// TODO Auto-generated method stub
		String token = getSaltString();
		String mail_content = "Enter the token generated and have the oppotunity to activate your account -->" + token;
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112", user.getEmail(), "forgot password",
				mail_content);
		User u = em.find(User.class, user.getId());
		u.setToken(token);
		em.merge(u);
	}

	@Override
	public boolean changeForgotPassword(String token, String newPwd, User user) {
		// TODO Auto-generated method stub
		User u = em.find(User.class, user.getId());
		String crypted = encrypt(newPwd);
		if (isTokenValid(token, user.getId())) {
			u.setPassword(crypted);
			em.merge(u);
			return true;
		}
		return false;
	}

	@Override
	public boolean DesactivateAccount(User u) {
		String token = getSaltString();
		String mail_content = "Enter the token generated and have the oppotunity to reactivate your account -->" + token;
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112", u.getEmail(), "Account reactivation",
				mail_content);
		User u_desactivated = em.find(User.class, u.getId());
		u_desactivated.setStatus(AccountStatus.desactivated);
		em.merge(u_desactivated);
		return true;

	}

	@Override
	public User getFullUser(User u) {
		User user = em.find(User.class, u.getId());
		return user;
	}

	// Added By Khaled
	@Override
	public User FindUser(int id) {
		User u = em.find(User.class, id);
		System.out.println(u);
		return u;
	}

	public String encrypt(String password) {
		String crypte = "";
		for (int i = 0; i < password.length(); i++) {
			int c = password.charAt(i) ^ 48;
			crypte = crypte + (char) c;
		}
		// System.out.println("token: " + crypte);
		return crypte;
	}

	public static String decrypt(String password) {
		String aCrypter = "";
		for (int i = 0; i < password.length(); i++) {
			int c = password.charAt(i) ^ 48;
			aCrypter = aCrypter + (char) c;
		}
		// System.out.println("token: " + aCrypter);
		return aCrypter;
	}

	public String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

/*	@Override
	public User Authenticate(String email, String pwd) {
		User user = null;
		String crypted_pwd = encrypt(pwd);
		Query tq = em.createQuery(
				"select e from User e where e.email=:email and e.password=:password");

		tq.setParameter("email", email);
		System.out.println("email: " + email);
		tq.setParameter("password", crypted_pwd);
		System.out.println("password:   " + crypted_pwd);
		user = (User) tq.getSingleResult();
		System.out.println(user);
		if (user != null)
			return null;
		
		return user;
	}
*/
	@Override
	public User Authenticate(String email, String pwd) {
		try{
			User user = null;
			String crypted_pwd = encrypt(pwd);
			Query tq = em.createQuery(
					"select e from User e where e.email=:email and e.password=:password");
		

			tq.setParameter("email", email);
			System.out.println("email: " + email);
			tq.setParameter("password", crypted_pwd);
			System.out.println("password:   " + crypted_pwd);
			user = (User) tq.getSingleResult();
			User u = em.find(User.class, user.getId());
			u.setLastLogin(new Date());
			em.merge(u);
			System.out.println(user);
			if (user != null)
				return user;
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean isTokenValid(String token, int id) {
		User u = em.find(User.class, id);

		String token_db = u.getToken();
		System.out.println(token_db);
		if (token.equals(token_db)) {
			System.out.println("token is valid ! Account is confirmed.");
			return true;
		} else {
			System.out.println("token not valid");
			return false;
		}

	}

	public static void main(String[] args) {
		// System.out.println(decrypt("U^QXYQ"));
	}

	@Override
	public List<User> getAllUsers() {
		Query q = em.createQuery("select u from User u");
		List<User> l = q.getResultList();
		return l;
	}

	@Override
	public List<User> getAllVets() {
		TypedQuery<User> q = em.createQuery("select u from User u where u.vet = true", User.class);
		List<User> l = q.getResultList();
		return l;
	}

	@SuppressWarnings("null")
	@Override
	public List<User> getLocalVets(User u) {
		User user = em.find(User.class, u.getId());
		List<User> localVets = new ArrayList<>();
		System.out.println(user);
		List<User> vets = getAllVets();
		System.out.println(vets);
		Address a = user.getAddress();
		Iterator<User> itr = vets.iterator();
		while (itr.hasNext()) {
			User element = itr.next();
			System.out.println(element);
			System.out.println("A User");
			System.out.println("x:" + a.getX());
			System.out.println("y:" + a.getY());
			System.out.println("A element");
			System.out.println("x:" + element.getAddress().getX());
			System.out.println("y:" + element.getAddress().getY());

			double distance = AddressService.distance(a.getX(), a.getY(), element.getAddress().getX(),
					element.getAddress().getY(), "K");
			System.out.println(distance); // 0.0
			double distance_min = 5;
			int difference = Double.compare(distance, distance_min);
			System.out.println(difference);
			//if (difference == 1) {
			if (difference == -1) {
				System.out.println("in if");
				if (localVets.add(element))
					System.out.println("added" + localVets);
				System.out.println("not added");
			}
			System.out.println("in while");
		}
		System.out.println("out while");
		return localVets;
	}

	@Override
	public int isLoggedIn24H() {

		List<User> all = getAllUsers();
		int total = 0;
		for (User u : all) {
			Date loggedin = u.getLastLogin();
			Date dt = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DATE, -1);
			dt = c.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Query query = em.createNativeQuery("SELECT DATEDIFF(?1,?2)");
			query.setParameter("1", loggedin);
			query.setParameter("2", dateFormat.format(dt));
			System.out.println(query.getSingleResult().toString());
			int difference = Integer.parseInt(query.getSingleResult().toString());
			if (difference <= 1 && difference > 0) {
				total = total + 1;
			}
		}
		return total;
	}
	
	@Override
	public void addUserPhoto(int user_id, String photo){
		User u = em.find(User.class, user_id);
		u.setPhoto(photo);
		em.merge(u);
	}
	
	

	

	
	@Override
	public User getUserByEmail(String email){
		return em.createQuery("select u from User u where email=:email", User.class)
				.setParameter("email", email).getSingleResult();
	}
}
