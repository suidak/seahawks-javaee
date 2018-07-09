package tn.esprit.seahawks.services;



import java.util.Random;

import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.seahawks.interfaces.MemberServiceLocal;
import tn.esprit.seahawks.persistence.AccountStatus;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;
import tn.esprit.seahawks.util.SendConfirmationMail;

@Stateless
public class MemberService implements MemberServiceLocal {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;
	
	UserService us = new UserService();

	@Override
	public int addMember(Member m ) {
		String token = us.getSaltString();
		m.setToken(token);
		m.setRole("Member");
		m.setStatus(AccountStatus.waitingForConfirmation);
		String pwd = us.encrypt(m.getPassword());
		m.setPassword(pwd);
		em.persist(m);
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112","oumayma.gader@esprit.tn", "Animal Caring team [Account confirmation code]","This is your confirmation code :"+token);
		//SendConfirmationMail.SendEmail(m.getEmail(), "Account Confirmation", "this is your account activation code: "+token);
//		try {
//			SendConfirmationMail.generateAndSendEmail();
//		} catch (AddressException e) {
//			// TODO Auto-generated catch block
//			System.out.println("address exception !!! mail not sent");
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			System.out.println("messaging exception !!! mail not sent");
//			e.printStackTrace();
//		}
		
//		
		return m.getId();
	}

	@Override
	public int addMemberWithhoto(Member m ) {
		String token = us.getSaltString();
		m.setToken(token);
		m.setRole("Member");
		m.setStatus(AccountStatus.waitingForConfirmation);
		String pwd = us.encrypt(m.getPassword());
		m.setPassword(pwd);
		em.persist(m);
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112","oumayma.gader@esprit.tn", "Animal Caring team [Account confirmation code]","This is your confirmation code :"+token);
		//SendConfirmationMail.SendEmail(m.getEmail(), "Account Confirmation", "this is your account activation code: "+token);
//		try {
//			SendConfirmationMail.generateAndSendEmail();
//		} catch (AddressException e) {
//			// TODO Auto-generated catch block
//			System.out.println("address exception !!! mail not sent");
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			System.out.println("messaging exception !!! mail not sent");
//			e.printStackTrace();
//		}
		
//		
		return m.getId();
	}
	
	@Override
	public boolean updateMember(Member m) {
		
//		Member m_updated = em.find(Member.class,m.getId());
//		m_updated.setLogin(m.getLogin());
//		m_updated.setEmail(m.getEmail());
//		m_updated.setPhoto(m.getPhoto());
//		m_updated.setFirstName(m.getFirstName());
//		m_updated.setLastName(m.getLastName());
		
		em.merge(m);
		return true;
	}
	
	@Override
	public boolean modifyMember(Member oldUser, Member newUser) {
		// TODO Auto-generated method stub
		
			if (newUser.getEmail() != null) {
				oldUser.setEmail(newUser.getEmail());
			}
			if (newUser.getLogin() != null) {
				oldUser.setLogin(newUser.getLogin());
			}
			if (newUser.getPhoneNumber() != null) {
				oldUser.setPhoneNumber(newUser.getPhoneNumber());
			}
			if (newUser.getFirstName() != null) {
				oldUser.setFirstName(newUser.getFirstName());
			}
			if (newUser.getLastName() != null) {
				oldUser.setLastName(newUser.getLastName());
			}
			
			
			if(em.merge(oldUser) != null)
				return true;
			return false ;

	}

	@Override
	public Member findOne(int id) {
		Member u = em.find(Member.class, id);
		return u;
	}
	
	@Override
	public boolean DesactivateAccount(Member u) {
		Member u_desactivated = em.find(Member.class, u.getId());
		u_desactivated.setStatus(AccountStatus.desactivated);
		em.merge(u_desactivated);
		return true;

	}
	
	@Override
	public boolean changePassword(String oldPassword, String newPassword, Member u) {
		Member user = em.find(Member.class, u.getId());
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

	@Override
	public void ForgotPasswordByMail(Member user) {
		// TODO Auto-generated method stub
		String token = getSaltString();
		String mail_content = "Enter the token generated and have the oppotunity to activate your account -->" + token;
		SendConfirmationMail.sendMail("oumayma.gader@esprit.tn", "gader52923112", user.getEmail(), "forgot password",
				mail_content);
		Member u = em.find(Member.class, user.getId());
		u.setToken(token);
		em.merge(u);
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

	
	

}
