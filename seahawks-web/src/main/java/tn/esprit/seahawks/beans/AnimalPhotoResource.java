package tn.esprit.seahawks.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.primefaces.json.JSONObject;

import tn.esprit.seahawks.interfaces.AnimalPhotoServiceLocal;
import tn.esprit.seahawks.persistence.AdoptionOffer;
import tn.esprit.seahawks.persistence.Animal;
import tn.esprit.seahawks.persistence.AnimalPhoto;
import tn.esprit.seahawks.persistence.BreedingOffer;
import tn.esprit.seahawks.persistence.Member;

@Path("animals/photos")
@RequestScoped
public class AnimalPhotoResource extends HttpServlet {

	@EJB(beanName = "AnimalPhotoService")
	AnimalPhotoServiceLocal photoService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAnimalPics() {
		if (!photoService.getAllAnimalPics().isEmpty())
			return Response.ok(photoService.getAllAnimalPics()).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Consumes("multipart/form-data")
	public Response addPhoto(MultipartFormDataInput input) {
		int petId = 0;
		String fileFinalPath = "";

		List<String> formatFile = new ArrayList<String>();
		formatFile.add("jpeg");
		formatFile.add("jpg");
		formatFile.add("png");

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

		// List<InputPart> inputParts = uploadForm.get("file");
		List<InputPart> animId = uploadForm.get("animal");

		// get id
		String[] contentDisposition = new String[1000];
		for (InputPart inputPart : animId) {
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			contentDisposition = headers.getFirst("Content-Disposition").split(";");
			try {
				String animalId = inputPart.getBodyAsString();
				petId = Integer.parseInt(animalId);
				System.out.println(petId);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		for (String key : uploadForm.keySet()) {
			if (!key.equalsIgnoreCase("application")) {
				List<InputPart> inputParts = uploadForm.get(key);
				addImage(inputParts, petId, formatFile);
			}

			System.out.println(key);
		}

		// photoService.addPhoto(petId, fileFinalPath);

		return Response.status(Status.OK).entity("Photo added.").build();
	}

	private boolean addImage(List<InputPart> inputParts, int petId, List<String> formatFile) {
		// get picture
		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				String filename = getFileName(headers);

				// format file test
				String extension = "";
				int i = filename.lastIndexOf('.');
				if (i >= 0) {
					extension = filename.substring(i + 1);
				}

				if (!formatFile.contains(extension)) {
					// return Response.status(Status.NOT_ACCEPTABLE)
					// .entity("Format not supported please use .jpeg .jpg .png
					// format").build();
					return false;
				}
				// end of file format test

				String fileLocation = "D:\\ESPRIT\\4TWIN2\\Seahawks\\javaee\\seahawks-web\\src\\main"
						+ "\\webapp\\assets\\animals\\" + UUID.randomUUID().toString() + filename;

				int index = fileLocation.lastIndexOf("webapp") + 7;
				String dbPath = fileLocation.substring(index);
				System.out.println(dbPath);

				// create file
				FileOutputStream fileOuputStream = new FileOutputStream(fileLocation);
				fileOuputStream.write(bytes);

				int pid = photoService.addPhoto(petId, dbPath);

				if (pid == 0)
					return false;
			} catch (IOException e) {
				// return Response.status(Status.NOT_ACCEPTABLE).entity("Bad
				// format, error parsing file.").build();
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	private String getFileName(MultivaluedMap<String, String> headers) {
		String[] contentDisposition = headers.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = sanitizeFilename(name[1]);
				return finalFileName;
			}
		}

		return "unknown";
	}

	private String sanitizeFilename(String s) {
		return s.trim().replaceAll("\"", "");
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePhoto(AnimalPhoto p) {
		if (photoService.deletePhoto(p.getId()))
			return Response.status(Status.GONE).build();
		return Response.status(Status.BAD_REQUEST).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getAnimalPicsById(@PathParam("id") int id) {
		if (!photoService.getAnimalPicsById(id).isEmpty())
			return Response.ok(photoService.getAnimalPicsById(id)).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPhotoBase64(AnimalPhoto obj, @QueryParam(value = "id") int id) {
		return Response.status(Status.OK).entity(photoService.addPhotoBase64(id, obj.getPhoto())).build();
	}

	/*
	 * @GET
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public Response test(){ return
	 * Response.ok(System.getProperty("user.dir")).build(); }
	 * 
	 * @GET
	 * 
	 * @Path("test2")
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public Response test2() throws
	 * ServletException, IOException{ String rootPath =
	 * getServletConfig().getServletContext().getRealPath("/");
	 * 
	 * return Response.ok(rootPath).build(); }
	 */
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("membre")
	public Response getPhotosByAnimal(Animal animal) {
		List<AnimalPhoto> list =photoService.getPhotos(animal);
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer for this Member").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//@JWTTokenNeeded
	public Response getAllBreedingOffer() {
		List<AnimalPhoto> list = photoService.getAll();
		if (list.isEmpty())
			return Response.status(Status.NOT_FOUND).entity("There a not a BreedingOffer at the moment").build();
		return Response.status(Status.ACCEPTED).entity(list).build();

	}

	
	
}
