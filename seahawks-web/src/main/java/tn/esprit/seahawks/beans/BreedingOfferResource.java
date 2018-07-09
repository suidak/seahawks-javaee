package tn.esprit.seahawks.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.enterprise.context.RequestScoped;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.BreedingOfferServiceLocal;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Path("breedingOffer")
@RequestScoped
public class BreedingOfferResource {

	@EJB(beanName = "BreedingOfferService")
	BreedingOfferServiceLocal breedingOfferService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/{des}/{titre}")
	public Response addBreedingOffer(@PathParam("id")int id, @PathParam("des")String des,@PathParam("titre")String titre) {
		// int resultat = breedingOfferService.addBreedingOffer(breedingOffer);
		// String result2 = JsonConverter.convertBreedingOffer(breedingOffer);
		if (breedingOfferService.addBreedingOffer(id,des,titre) == null)
			return Response.status(Status.CONFLICT).entity("Your animal is not ready for a breeding").build();

		return Response.status(Status.CREATED).entity(id).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("closeBreeding")
	public Response closeBreedingOffer(BreedingOffer breedingOffer) {
		breedingOfferService.closeBreedingOffer(breedingOffer);
		return Response.status(Status.ACCEPTED).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("locateBreeding")
	public Response locateBreedingOffer(BreedingOffer breedingOffer) {
		breedingOfferService.locateBreedingOffer(breedingOffer);
		return Response.status(Status.ACCEPTED).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response deleteBreedingOffer(@PathParam("id")int id) {
		breedingOfferService.deleteBreedingOffer(id);
		return Response.status(Status.ACCEPTED).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response editBreedingOffer(BreedingOffer breedingOffer) {

		breedingOfferService.editBreedingOffer(breedingOffer);
		// String result2 = JsonConverter.convertBreedingOffer(breedingOffer);
		return Response.status(Status.ACCEPTED).entity("BreedingOffer id:" + breedingOffer.getId()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded
	public Response getAllBreedingOffer() {
		List<BreedingOffer> list = breedingOfferService.getAllBreedingOffer();
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer at the moment").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("closed")
	public Response getAllBreedingOfferClossed() {
		List<BreedingOffer> list = breedingOfferService.getAllBreedingOfferClossed();
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer Clossed at the moment")
					.build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("located")
	public Response getAllBreedingOfferLocated() {
		List<BreedingOffer> list = breedingOfferService.getAllBreedingOfferLocated();
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer Located at the moment")
					.build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("membre")
	public Response getBreedingOfferByMembre(Member member) {
		List<BreedingOffer> list = breedingOfferService.getBreedingOfferByMembre(member);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer for this Member").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("animal")
	public Response getBreedingOfferByAnimal(Animal animal) {
		List<BreedingOffer> list = breedingOfferService.getBreedingOfferByAnimal(animal);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer for this Animal").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("spice&sex")
	public Response getBreedingOfferBySpeciesAndSex(Animal animal) {
	
		//if (animal.getSex()=="M")
			// return
			// breedingOfferService.getBreedingOfferBySpeciesAndSexF(animal);
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.getBreedingOfferBySpeciesAndSexF(animal)).build();
		//return Response.status(Status.ACCEPTED).entity(breedingOfferService.getBreedingOfferBySpeciesAndSexM(animal))
				//.build();
		// return breedingOfferService.getBreedingOfferBySpeciesAndSexF(animal);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("aymen")
	public Response logementParDelegation(@QueryParam(value = "city") String city,
			@QueryParam(value = "street") String street, @QueryParam(value = "spices") String species,
			@QueryParam(value = "breed") String breed, @QueryParam(value = "date") String date,
			@QueryParam(value = "country") String country) {

		List<BreedingOffer> list = null;
		if (city != null) {
			list = breedingOfferService.getBreedingOfferByCity(city);
		}
		if (breed != null) {
			list = breedingOfferService.getBreedingOfferBybreed(breed);
		}
		if (country != null) {
			list = breedingOfferService.getBreedingOfferByCountry(country);
		}
		if (date != null) {
			list = breedingOfferService.getBreedingOfferByDate(date);
		}
		if (species != null) {
			list = breedingOfferService.getBreedingOfferBySpecies(species);
		}

		if (street != null) {
			list = breedingOfferService.getBreedingOfferByStreet(street);
		}
		return Response.status(Status.OK).entity(list).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("search")
	public Response showBreedingOffer(JsonObject criteria) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		BreedingOffer breedingOffer2 = breedingOfferService.showBreedingOffer(criteria.getInt("criteria"));
		if(breedingOffer2 != null)
			
			return Response.status(Status.ACCEPTED).entity(breedingOffer2).build();
		return Response.status(Status.NOT_FOUND).entity("BreedingOffer Not found").build();
		//else
			

		

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("memberB")
	public Response getMemberByBreedingOffer(BreedingOffer breedingOffer) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.ACCEPTED)
				.entity(breedingOfferService.getMemberByBreedingOffer(breedingOffer)).build();
		

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("animalB")
	public Response getAnimalByBreedingOffer(BreedingOffer breedingOffer) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.ACCEPTED)
				.entity(breedingOfferService.getAnimalByBreedingOffer(breedingOffer)).build();
		

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("test")
	public Response haveOldBreeding(Animal animal) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		if(breedingOfferService.haveOldBreeding(animal))
		return Response.status(Status.CREATED).entity("Yes! this animal had a old Breeding").build();
		return Response.status(Status.CREATED).entity("No, this animal had not a old breeding").build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("numberBreeding/{id}")
	public Response NumberBreeding(@PathParam("id")int id) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.CREATED).entity(breedingOfferService.NumberBreeding(id)).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("numberBreedingSucces/{id}")
	public Response NumberBreedingSucces(@PathParam("id")int id) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.CREATED).entity(breedingOfferService.NumberBreedingSucces(id)).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("numberBreedingFailed/{id}")
	public Response NumberBreedingFailed(@PathParam("id")int id) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.CREATED).entity(breedingOfferService.NumberBreedingFailed(id)).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("lastDate")
	public Response lastdateOfBreeding(Animal animal) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.CREATED).entity(breedingOfferService.LastdateOfBreeding(animal)).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("numberBaby")
	public Response numberofBabies(Animal animal) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.CREATED).entity("this animal had"+breedingOfferService.NumberofBabies(animal)+"babies").build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("chance/{id}")
	public Response ChanceHaveBreeding(@PathParam("id")int id) {
		// String result2 =
		// JsonConverter.convertBreedingOfferList(breedingOfferService.getAllBreedingOffer());
		return Response.status(Status.ACCEPTED)
				.entity(breedingOfferService.ChanceHaveBreeding(id)).build();

	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("days")
	public Response getBreedingOfferBy3(Animal animal) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.getBreedingOfferBySpecies3Days(animal)).build();
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("distance/{id}")
	public Response getBreedingBydistance(@PathParam("id")int dis,Animal animal) {
	
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.getBreedingBydistance(dis,animal)).build();
		
	}
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("b/{id}")
	public Response getBreedingByb(@PathParam("id")int id) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.getBreedingOfferByb(id)).build();
		
	}
	
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("distance2/{x1}/{y1}/{x2}/{y2}")
	public Response distance(@PathParam("x1")Double x1,@PathParam("y1")Double y1,@PathParam("x2")Double x2,@PathParam("y2")Double y2) {
	
		
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.DistanceBetweenOfferandRequest(x1, y1, x2, y2)).build();
		
	}
	
	@GET

	@Produces(MediaType.APPLICATION_JSON)
	@Path("days/{date}")
	public Response days(@PathParam("date")String date) throws ParseException {
		
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = format.parse(date);
			return Response.status(Status.ACCEPTED)
					.entity(breedingOfferService.days(d)).build();
		
	}
	
	
	

}
