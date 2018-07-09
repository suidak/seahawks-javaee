package tn.esprit.seahawks.beans;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.stripe.model.Charge;

import tn.esprit.seahawks.interfaces.FundonationServiceLocal;
import tn.esprit.seahawks.persistence.FunDonation;
import tn.esprit.seahawks.persistence.Fundraiser;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Path("donation")
@RequestScoped
public class FundonationResource {
	
	@EJB(beanName="FundonationService")
	FundonationServiceLocal fundonationService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Path("/donate")
	public Response donate(
			@FormParam("token") String token,
			@FormParam("name") String name,
			@FormParam("amount") int amount,
			@FormParam("fid") int fid,
			@FormParam("mid") int mid
			){
		
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (amount*100)); 
		chargeParams.put("currency", "usd");
		chargeParams.put("description", "Donation received from "+name+" :)");
		chargeParams.put("source", token);
		
		System.out.println("token :"+token);
		System.out.println("amount :"+amount);
		System.out.println("name :"+name);
		System.out.println("fundraiser id :"+fid);
		System.out.println("member id :"+mid);
		
		FunDonation donation = new FunDonation();
		donation.setAmount(amount);
		
		//boolean test = fundonationService.donate(donation,chargeParams);
		
		if(fundonationService.donate(donation,chargeParams,fid,mid))
			return Response.status(Status.OK).entity(donation).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDonationsByMember(Member m){
		List<FunDonation> donations = fundonationService.getDonationsByMember(m);
		//System.out.println(fundraisers.toString());
		if(!donations.isEmpty())
			return Response.status(Status.OK).entity(donations).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bymember")
	public Response getSumDonationsByMember(Member m){
		Map<Integer,Double> donations = fundonationService.getSumDonationsByMember();
		
		if(!donations.isEmpty())
			return Response.status(Status.OK).entity(donations).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/average")
	public Response getAverageDonations(){
		Double avg = fundonationService.getAverageDonations();

		//DecimalFormat df = new DecimalFormat("#.##");      

		
		if(avg!=null)
			return Response.status(Status.OK).entity(avg).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/avgbyfundraiser")
	public Response getAverageDonationsByFundraiser(){
		Map<Integer, Double> avg = fundonationService.getAverageDonationsByFundraiser();
		
		if(avg!=null)
			return Response.status(Status.OK).entity(avg).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/donators")
	public Response getNbrDonatorsByCountry(@QueryParam(value="country") String country){
		Long nbr = fundonationService.getNbrDonatorsByCountry(country);

		if(nbr!=null)
			return Response.status(Status.OK).entity(nbr).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/avgdonations")
	public Response getAvgDonationsByCountry(@QueryParam(value="country") String country){
		Double nbr = fundonationService.getAverageDonationsByCountry(country);

		if(nbr!=null)
			return Response.status(Status.OK).entity(nbr).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	

}
