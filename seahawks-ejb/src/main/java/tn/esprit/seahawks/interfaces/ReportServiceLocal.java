package tn.esprit.seahawks.interfaces;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Organisation;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.User;

public interface ReportServiceLocal {
	public boolean closeReport(Report Report);//done
	public List<Report> getAllReport();//à redef
	public List<Report> getAllisClosedReport();//à redef
	public List<Report> searchReport(String criteria);//à redef
	public List<Report> getUserSpecificReports(User User);//à redef
	public List<Report> getOpenReport();//à redef
	public Report showReport(int id);//à redef
	public boolean editReport(Report Report);//à redef in Lost
	public boolean deleteReport(Report Report);//done
	public boolean addLostReport(LostReport lostreport);//done
	public List<LostReport> getRewardingLostReport();	//done
	public List<LostReport> getAllLostReport();//à redef
	public List<LostReport> getAllisClosedLostReport();//à redef
	public List<LostReport> searchLostReport(String criteria);//à redef
	public List<LostReport> getUserSpecificLostReports(User User);//à redef
	public List<LostReport> getOpenLostReport();//à redef
	public LostReport showLostReport(int id);//à redef
	//FoundReport//
	public boolean addFoundReport(FoundReport f);//done
	public List<FoundReport> getAllFoundReport();//à redef
	public List<FoundReport> getAllisClosedFoundReport();//à redef
	public List<FoundReport> searchFoundReport(String criteria);//à redef
	public List<FoundReport> getUserSpecificFoundReport(User User);//à redef
	public List<FoundReport> getOpenFoundReport();//à redef
	public FoundReport showFoundReport(int id);//à redef
	public Organisation passToCareTaker(FoundReport f);///get org to be care Taker
	public boolean CloseFoundReport(Organisation org,FoundReport f);//Close report And Choose Org to be Care Taker
	public LostReport findLostReport(int id);
	public FoundReport findFoundReport(int id);
	public Map<String, Long> statActivityPerCountry();
	public Long stattn();
	public Long statalg();
	public Long statfr();
	public Long stataus();
	public Long statchina();



}
