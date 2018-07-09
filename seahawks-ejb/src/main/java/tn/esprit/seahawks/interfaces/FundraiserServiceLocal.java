package tn.esprit.seahawks.interfaces;


import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

import tn.esprit.seahawks.persistence.Fundraiser;
import tn.esprit.seahawks.persistence.User;

@Local
public interface FundraiserServiceLocal {
	public boolean addFundraiser(Fundraiser f);  //
	public boolean updateFundraiser(Fundraiser f); //
	public boolean removeFundraiser(int id);  //
	public boolean closeFundraiser(Fundraiser f);  //
	
	public double getAverageRaisedMoney();    
	public double getAverageGoal(); 
	
	public List<Fundraiser> getAllFundraisers();     // 
	public List<Fundraiser> getOpenFundraisers();   //
	public List<Fundraiser> getFundraisersByUser(User u); //
	public List<Fundraiser> getReachedFundraisers();   //
	 
	public Map<String, Long> getNbrFundraisersByLocation();  //
	public int getRemainingDays(int id); //
	public Long getNbrReachedFundraisers();		//
	public boolean addFundraiserPhoto(int fid, String imgUrl, String title);
	
	
	public void endFundraisers();  //scheduled - needs uncommenting
	
	//percentAmountRemaining() 
	//AmountRemaining
	//Map raisedMoneyPerUser()

	//countdown(f)
	//markAsImportant(f)
	//List getMostImportant()
	//link fundraiser to the animal
	
    public Fundraiser getFundraiserById(int id);
	
}
