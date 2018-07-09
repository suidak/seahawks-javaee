package tn.esprit.seahawks.beans;



import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.BreedingDetailsServiceLocal;

import tn.esprit.seahawks.persistence.BreedingDetails;


@Path("breedingDetails")
@RequestScoped
public class BreedingDetailsResource {
	
	@EJB(beanName="BreedingDetailsService")
	BreedingDetailsServiceLocal breedingDetailsService ;
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get/{id}")
	public Response showBreedingDetails(@PathParam("id")int id){
	
		return Response.status(Status.ACCEPTED).entity(breedingDetailsService.showBreedingDetails(id)).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editBreedingDetails(BreedingDetails breedingDetails){
	//	String result2 = JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
	 breedingDetailsService.editBreedingDetails(breedingDetails);
		return Response.status(Status.ACCEPTED).build();
	}
	
	

}
