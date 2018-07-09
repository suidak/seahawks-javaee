package tn.esprit.seahawks.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
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

import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.AnimalPhotoServiceLocal;
import tn.esprit.seahawks.interfaces.AnimalServiceLocal;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.User;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Member;

@Path("animals")
@RequestScoped
public class AnimalResource {
	
	@EJB(beanName = "AnimalService")
	AnimalServiceLocal animalService;
	
	@EJB(beanName = "AnimalPhotoService")
	AnimalPhotoServiceLocal photoService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAnimal(Animal a) {
		animalService.addAnimal(a);
		return Response.status(Status.CREATED).entity(a).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAnimal(Animal a) {
		if (animalService.updateAnimal(a))
			return Response.status(Status.OK).entity(a).build();
		return Response.status(Status.BAD_REQUEST).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAnimal(Animal animal) {
		if (!photoService.deleteAnimalPhotos(animal.getId()))
			return Response.status(Status.BAD_REQUEST).build();
		if (animalService.deleteAnimal(animal.getId()))
			return Response.status(Status.OK).build();
		return Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAnimals() {
		if (!animalService.getAllAnimals().isEmpty())
			return Response.ok(animalService.getAllAnimals()).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("byuser")
	public Response getUserAnimals(@QueryParam("uid") int uid) {
		if (!animalService.getUserAnimals(uid).isEmpty())
			return Response.ok(animalService.getUserAnimals(uid)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("unlisted")
	public Response getUserUnlistedAnimals(@QueryParam("uid") int uid){
		if (!animalService.getUserUnlistedAnimals(uid).isEmpty())
			return Response.ok(animalService.getUserUnlistedAnimals(uid)).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{specie}")
	public Response getAnimalsBySpecie(@PathParam("specie") String specie) {
		if (!animalService.getAnimalsBySpecie(specie).isEmpty())
			return Response.ok(animalService.getAnimalsBySpecie(specie)).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{specie}/{breed}")
	public Response getAnimalsBySpecieAndBreed(@PathParam("specie") String specie, @PathParam("breed") String breed) {
		if (!animalService.getAnimalsBySpecieAndBreed(specie, breed).isEmpty())
			return Response.ok(animalService.getAnimalsBySpecieAndBreed(specie, breed)).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stats/useravgpets")
	// for publicity, encourage people to adopt
	public Response averageAnimalsNbrPerUser() {
		if (animalService.averageAnimalsNbrPerUser() != 0)
			return Response.ok(animalService.averageAnimalsNbrPerUser()).build();
		return Response.status(404).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stats")
	public Response nbrOfAnimalsPerSpecieOrBreed(@NotNull @QueryParam("specie") String specie,
			@QueryParam("breed") String breed) {
		if (breed == null) {
			if (animalService.nbrOfAnimalsPerSpecie(specie) != 0)
				return Response.ok(animalService.nbrOfAnimalsPerSpecie(specie)).build();
			return Response.status(404).build();
		}

		if (animalService.nbrOfAnimalsPerSpecieBreed(specie, breed) != 0)
			return Response.ok(animalService.nbrOfAnimalsPerSpecieBreed(specie, breed)).build();
		return Response.status(404).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stats/adopsperspecie")
	public Response nbrOfAdoptionsPerSpecie(){
		if (!animalService.nbrOfAdoptionsPerSpecie().isEmpty())
			return Response.ok(animalService.nbrOfAdoptionsPerSpecie()).build();
		return Response.status(404).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("stats/mostadopted")
	public Response getMostAdoptedAnimal(){
		if (animalService.nbrOfAdoptionsPerSpecie().isEmpty())
			return Response.status(404).build();
		if (!animalService.getMostAdoptedAnimal().isEmpty())
			return Response.ok(animalService.getMostAdoptedAnimal()).build();
		return Response.status(404).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("membre")
	public Response getBreedingOfferByMembre(Member member) {
		List<Animal> list = animalService.getAnimalsByUser(member);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer for this Member").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAnimalsByUser")
	public Response getBreedingOfferByMembre(User u) {
		List<Animal> list = animalService.getAnimalsByUser(u);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("0").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("index")
	public Response getLastIndex(){
		return Response.ok(animalService.lastIndex()).build();
	}
	
	
}