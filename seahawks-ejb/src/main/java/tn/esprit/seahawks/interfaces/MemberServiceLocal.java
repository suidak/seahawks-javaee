package tn.esprit.seahawks.interfaces;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Member;

@Local
public interface MemberServiceLocal {
	
	//subscribe as member
	public int addMember(Member m);
	public boolean updateMember(Member m);
	boolean modifyMember(Member oldUser, Member newUser);
	public Member findOne(int id);
	boolean DesactivateAccount(Member u);
	boolean changePassword(String oldPassword, String newPassword, Member u);
	void ForgotPasswordByMail(Member user);
	int addMemberWithhoto(Member m);
}
