package tn.esprit.seahawks.beans;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;	
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.AdoptionRequestServiceLocal;
import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.AdoptionRequest;
import tn.esprit.seahawks.persistence.Member;

@Path("adoprequests")
@RequestScoped
public class AdoptionRequestResource {

	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;
	
	@EJB(beanName = "AdoptionRequestService")
	AdoptionRequestServiceLocal adoptionRequestService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdoptionRequest(AdoptionRequest a) {
		if (adoptionRequestService.addAdoptionRequest(a) > 0)
			return Response.status(Status.CREATED).entity(a).build();
		if (adoptionRequestService.addAdoptionRequest(a) == -1)
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("You can't make an adoption request as an organisation!").build();
		if (adoptionRequestService.addAdoptionRequest(a) == -2)
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("You can't request an adoption for your own pet!").build();
		if (adoptionRequestService.addAdoptionRequest(a) == -3)
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("A request has already been accepted for this offer!").build();
		return Response.status(Status.BAD_REQUEST).entity("You already requested an adoption "
				+ "for this offer!").build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAdoptionRequest(AdoptionRequest a) {
		if (adoptionRequestService.updateAdoptionRequest(a))
			return Response.status(Status.OK).entity(a).build();
		return Response.status(Status.BAD_REQUEST).entity(a).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAdoptionRequest(AdoptionRequest a) {
		if (adoptionRequestService.deleteAdoptionRequest(a))
			return Response.status(Status.GONE).build();
		return Response.status(Status.BAD_REQUEST).build();
	}

	@POST
	@Path("/userrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdoptionRequestsByMember(Member m) {
		if (!adoptionRequestService.getAllAdoptionRequestsByMember(m).isEmpty())
			return Response.ok(adoptionRequestService.getAllAdoptionRequestsByMember(m)).build();
		return Response.status(Status.NO_CONTENT).build();
	}

	@POST
	@Path("/offerrequests")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdoptionRequestsByOffer(AdoptionOffer a) {
		if (!adoptionRequestService.getAllAdoptionRequestsByOffer(a).isEmpty())
			return Response.ok(adoptionRequestService.getAllAdoptionRequestsByOffer(a)).build();
		return Response.status(Status.NO_CONTENT).build();
	}

	@PUT
	@Path("/acceptreq")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processAdoptionRequest(AdoptionRequest a, @QueryParam("decision") boolean decision) {
		int requests = adoptionRequestService.processAdoptionRequest(a, decision);
		if (requests == -2) {
			return Response.ok("Request #" + a.getId() + " declined.").build();
		}
		
		// check error output according to user action, accept or decline
		if (requests == 0) {
			AdoptionRequest req = em.find(AdoptionRequest.class, a.getId());
			
			if (decision) {
				return Response.status(Status.NOT_ACCEPTABLE)
						.entity("You can't accept already " + req.getStatus() + " requests.")
						.build();
			} else {
				return Response.status(Status.NOT_ACCEPTABLE)
						.entity("You can't decline already " + req.getStatus() + " requests.")
						.build();
			}
		}
		
		if (requests == -5)
			return Response.ok("Request #" + a.getId() + " accepted, no other requests to decline were found.")
					.build();
			
		if (requests != -1) {
			return Response.ok("Request #" + a.getId() + " accepted, auto declined " + requests + " requests.")
					.build();
		}
		
		return Response.status(Status.BAD_REQUEST).build();
	}
}
