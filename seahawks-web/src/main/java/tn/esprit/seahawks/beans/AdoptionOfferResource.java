package tn.esprit.seahawks.beans;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.AdoptionOfferServiceLocal;
import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.User;

@Path("adopoffers")
@RequestScoped
public class AdoptionOfferResource {
	
	@PersistenceContext(unitName = "seahawks-ejb")
	private EntityManager em;
	
	@EJB(beanName="AdoptionOfferService")
	AdoptionOfferServiceLocal adoptionOfferService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAdoptionOffer(AdoptionOffer a){
		if (adoptionOfferService.addAdoptionOffer(a) == -1)
			return Response.status(Status.NOT_FOUND)
					.entity("Animal Not Found!").build();
		/*if (adoptionOfferService.addAdoptionOffer(a) == -2)
			return Response.status(Status.BAD_REQUEST)
					.entity("Only organizations can make adoption offers!").build();*/
		//if (adoptionOfferService.addAdoptionOffer(a) > 0)
			return Response.ok(a).build();
		/*return Response.status(Status.BAD_REQUEST)
				.entity("You already put this pet for adoption.").build();*/
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAdoptionOffer(AdoptionOffer a){
		if (adoptionOfferService.updateAdoptionOffer(a))
			return Response.status(Status.OK).entity(a).build();
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAdoptionOffer(AdoptionOffer a){
		if (adoptionOfferService.deleteAdoptionOffer(a))
			return Response.status(Status.GONE).build();
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAdoptionOffers(){
		if (!adoptionOfferService.getAllAdoptionOffers().isEmpty())
			return Response.ok(adoptionOfferService.getAllAdoptionOffers()).build();
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("pending/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPendingAdoptionOffers(@PathParam("uid") int uid){
		if (!adoptionOfferService.getPendingAdoptionOffers(uid).isEmpty())
			return Response.ok(adoptionOfferService.getPendingAdoptionOffers(uid)).build();
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("specie")
	public Response getPendingAdoptionOffersBySpecie(@QueryParam("specie") String specie){
		if (!adoptionOfferService.getPendingAdoptionOffersBySpecie(specie).isEmpty())
			return Response.ok(adoptionOfferService.getPendingAdoptionOffersBySpecie(specie)).build();
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("/speciebreed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPendingAdoptionOffersBySpecieBreed(@QueryParam("specie") String specie, 
			@QueryParam("breed")String breed){
		if (!adoptionOfferService.getPendingAdoptionOffersBySpecieBreed(specie, breed).isEmpty())
			return Response.ok(adoptionOfferService.getPendingAdoptionOffersBySpecieBreed(specie, breed)).build();
		return Response.status(Status.NO_CONTENT).build();
	}
	
	// for testing purposes
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("datetest")
	public Response dateDiff(){
		Query i =  em.createNativeQuery("SELECT DATEDIFF('2017-06-25','2017-06-15')");
	    System.out.println(i.getSingleResult());
	    return Response.ok(i.getSingleResult()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("avgprices")
	public Response averagePricePerSpecie(){
		if (!adoptionOfferService.averagePricePerSpecie().isEmpty())
			return Response.ok(adoptionOfferService.averagePricePerSpecie()).build();
		return Response.status(404).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("byuser")
	public Response getUserAdoptionOffers(@QueryParam("user") int userId){
		if (!adoptionOfferService.getUserAdoptionOffers(userId).isEmpty())
			return Response.ok(adoptionOfferService.getUserAdoptionOffers(userId)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("byuser/pending")
	public Response getUserPendingAdoptionOffers(@QueryParam("user") int userId){
		if (!adoptionOfferService.getUserPendingAdoptionOffers(userId).isEmpty())
			return Response.ok(adoptionOfferService.getUserPendingAdoptionOffers(userId)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("species")
	public Response getSpecies(){
		if (!adoptionOfferService.getSpecies().isEmpty())
			return Response.ok(adoptionOfferService.getSpecies()).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("breeds")
	public Response getBreeds(@QueryParam("specie") String specie){
		if (!adoptionOfferService.getBreeds(specie).isEmpty())
			return Response.ok(adoptionOfferService.getBreeds(specie)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("byid")
	public Response getOfferById(@QueryParam("id") int id){
		if (adoptionOfferService.getOfferById(id) != null)
			return Response.ok(adoptionOfferService.getOfferById(id)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
}
