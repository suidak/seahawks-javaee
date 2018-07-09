package tn.esprit.seahawks.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.BreedingRequestServiceLocal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.BreedingRequest;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Walkings;
import tn.esprit.seahawks.services.BreedingRequestService;

@Path("breedingRequest")
@RequestScoped
public class BreedingRequestResource {
	
	@EJB(beanName="BreedingRequestService")
	BreedingRequestServiceLocal breedingRequestService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{idb}/{ida}/{des}")
	public Response addBreedingRequest(@PathParam("idb")int idb, @PathParam("ida")int ida,@PathParam("des") String des)
	{
		int resultat = breedingRequestService.AddBreedingRequest(idb,ida,des);
		if(resultat == 0) 
		return Response.status(Status.NOT_ACCEPTABLE).entity("Vous avez envoyer un BreedingRequest a votre propore BreedingOffre").build();
		
		//if(resultat.)
		//	return Response.status(Status.NOT_ACCEPTABLE).build();
		return Response.status(Status.CREATED).entity("BreedingRequest id:"+resultat).build();
		
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteBreedingRequest(BreedingRequest breedingRequest) {
		breedingRequestService.deleteBreedingRequest(breedingRequest);
		return Response.status(Status.ACCEPTED).build();
	}
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editbreedingRequest(BreedingRequest breedingRequest){
	//	String result2 = JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		breedingRequestService.editBreedingRequest(breedingRequest);
		return Response.status(Status.ACCEPTED).entity("BreedingRequest modfier").build();
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	@Path("member/{id}")
	public Response showBreedingRequestByMember(@PathParam("id")int id) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.showBreedingRequestByMember(id)).build();
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	
	@Path("offer/{id}")
	public Response GetAllBreedingRequestByOffer(@PathParam("id")int id) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.GetAllBreedingRequestByOffer(id)).build();
		
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("accept/{id}")
	public Response AcceptBreedingRequest(@PathParam("id") int id) {
		breedingRequestService.AcceptBreedingRequest(id);
		return Response.status(Status.ACCEPTED).build();
		
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("decline/{id}")
	public Response DeclineBreedingRequest(@PathParam("id") int id) {
		breedingRequestService.DeclineBreedingRequest(id);
		return Response.status(Status.ACCEPTED).build();
		
	}
	
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("animal")
	public Response showAnimalinBreedingRequest(BreedingRequest breedingRequest) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.showAnimalinBreedingRequest(breedingRequest)).build();
		
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("member2")
	public Response  showMemberinBreedingRequest(BreedingRequest breedingRequest) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				. showMemberinBreedingRequest(breedingRequest)).build();
		
	}
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("adress")
	public Response showAdressRequest(BreedingRequest breedingRequest) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.showAdressRequest(breedingRequest)).build();
		
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("dist")
	public Response distanceBetweenOfferandRequest(BreedingRequest breedingRequest) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.DistanceBetweenOfferandRequest(breedingRequest)+"Kilometers\n").build();
		
	}
	
	
	//@GET
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("walking")
	public Response DistanceBetweenOfferandRequest1(Walkings walking) {
		
		return Response.status(Status.ACCEPTED).entity(breedingRequestService
				.DistanceBetweenOfferandRequest1( walking)).build();
		
	}
	
	@POST
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded
	public Response getAllBreedingOffer(Walkings walking) {
		List<Walkings> list = breedingRequestService.getAllBreedingOffer(walking);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer at the moment").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("mail")
	public Response mail(Member member) {
	breedingRequestService.mail(member);
		
		return Response.status(Status.ACCEPTED).entity("Check your mail").build();
		
	}
	
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("br/{id}")
	public Response getBreedingByb(@PathParam("id")int id) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingRequestService.getBreedingRequestBybr(id)).build();
		
	}
	
	

	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("bd/{id}")
	public Response getBreedingBybd(@PathParam("id")int id) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingRequestService.GetAllBreedingRequestByOfferD(id)).build();
		
	}
	
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("verif/{id}")
	public Response verif(@PathParam("id")int id) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingRequestService.verif(id)).build();
		
	}
	
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("offerbyR/{id}")
	public Response offerbyR(@PathParam("id")int id) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingRequestService.GetofferbyRequest(id)).build();
		
	}
	
	
	

}
