package tn.esprit.seahawks.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.BreedingOfferServiceLocal;
import tn.esprit.seahawks.interfaces.WalkingServiceLocal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Walkings;

@Path("walking")
@RequestScoped
public class WalkingResource {
	@EJB(beanName = "WalkingService")
	WalkingServiceLocal walkingService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("dog")
	public Response getAllBreedingOfferLocated() {
		List<Walkings> list = walkingService.getWalkingByDog();
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer Located at the moment")
					.build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

}
