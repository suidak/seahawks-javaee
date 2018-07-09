package tn.esprit.seahawks.beans;
 
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.ReportServiceLocal;
import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.User;

@RequestScoped
@Path("LostReport")
public class LostReportResource {

	@EJB(beanName = "ReportService")
	ReportServiceLocal lRs ;
	@EJB(beanName = "UserService")
	UserServiceLocal uS ;
	
	@Path("searchLostReport")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchLostReport (@FormParam("criteria") String x){
		if(!lRs.searchLostReport(x).isEmpty()) {
			return Response.status(Status.OK).entity(lRs.searchLostReport(x)).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();

		

	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLostReport(LostReport lostreport)
	{	
		if(lRs.addLostReport(lostreport)) 
		{
			return Response.status(Status.CREATED).build();
		}
			return Response.status(Status.EXPECTATION_FAILED).build();
	}
	
	@GET
	//@JWTTokenNeeded
	@Produces(MediaType.APPLICATION_JSON)
	public LostReport showLostReport()
	{
		LostReport ls = new LostReport();
		ls.setAnimal(null);
		ls.setIsClosed(false);
		ls.setReporterUser(null);
		return ls ;
	}
	@Path("AllLostReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllLostReports()
	{	
		List<LostReport> lr = lRs.getAllLostReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getAllLostReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	
	@Path("findLostReport/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findLostReport(@PathParam("id")int id)
	{	
		LostReport lr = lRs.findLostReport(id);
		if (lr != null) {
			return Response.status(Status.OK).entity(lr).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	
	@Path("AllClosedLostReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllClosedLostReports()
	{	
		List<LostReport> lr = lRs.getAllisClosedLostReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getAllisClosedLostReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("RewardingLostReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showRewardingLostReport()
	{	
		List<LostReport> lr = lRs.getRewardingLostReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getRewardingLostReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}

	@Path("OpenLostReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showOpenLostReport()
	{	
		List<LostReport> lr = lRs.getOpenLostReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getOpenLostReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showUserLostReports(@PathParam("userId")int userId)
	{		
		//System.out.println(userId);
		User u =  uS.FindUser(userId);
		if(u != null) {
			List<LostReport> lr = lRs.getUserSpecificLostReports(u);
			if (!lr.isEmpty()) {
				return Response.status(Status.OK).entity(lRs.getUserSpecificLostReports(u)).build();
			}
			return Response.status(Status.EXPECTATION_FAILED).build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("User: "+userId+" does not exist.").build();

		

		
	}
	@Path("editReport")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response EditReport(LostReport l)
	{	
		if(lRs.editReport((Report)l)) 
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.NOT_MODIFIED).build();
	}
	
	@Path("deleteReport")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteReport(LostReport l)
	{	
		if(lRs.deleteReport((Report)l)) 
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.NOT_MODIFIED).build();
	}
	
	
}
