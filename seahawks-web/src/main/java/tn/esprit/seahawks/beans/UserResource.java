package tn.esprit.seahawks.beans;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("users")
@RequestScoped
public class UserResource {

	@Context
	private UriInfo uriInfo;

	@Context
	SecurityContext securityContext;

	@EJB(beanName = "UserService")
	UserServiceLocal userService;

	/*
	 * @POST
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response addMember(Member
	 * m){ int result1 = userService.addMember(m); String result2 =
	 * JsonConverter.convertUser(m);
	 * 
	 * return Response.status(Status.CREATED).entity("User id:" + result1 +
	 * " => " + result2).build(); }
	 * 
	 * @POST
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("addOrg") public Response addOrg(Organisation o){ int result1 =
	 * userService.addOrg(o); String result2 = JsonConverter.convertUser(o);
	 * 
	 * return Response.status(Status.CREATED).entity("User id:" + result1 +
	 * " => " + result2).build(); }
	 * 
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{token}")
	public Response confirmAccount(@PathParam("token") String token, User u) {
		if (userService.confirmAccount(token, u)) {
			return Response.ok(u).build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{oldpwd}/{newpwd}")
	public Response changePassword(@PathParam("oldpwd") String oldPassword, @PathParam("newpwd") String newPassword,
			User u) {
		if (userService.changePassword(oldPassword, newPassword, u)) {
			return Response.status(Status.ACCEPTED).entity("success").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
	}

	@PUT
	@Path("/forgotByMail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ForgotPasswordByMail(User u) {
		userService.ForgotPasswordByMail(u);
		return Response.status(Status.ACCEPTED).build();
	}

	@PUT
	@Path("forgot/{newtoken}/{newPwd}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeForgotPassword(@PathParam("newtoken") String token, @PathParam("newPwd") String newPwd,
			User u) {
		if (userService.changeForgotPassword(token, newPwd, u)) {
			return Response.status(Status.ACCEPTED).build();
		} else
			return Response.status(Status.NOT_ACCEPTABLE).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response desactivateAccount(User u) {
		if (userService.DesactivateAccount(u)) {
			System.out.println("Account desactivated !");
			return Response.status(Status.ACCEPTED).build();
		} else {
			System.out.println("Account desactivation failed");
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
	}

	//@JWTTokenNeeded
	@POST
	@Path("/getFull")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFullUser(User user) {
		User u = userService.getFullUser(user);
		if (u != null)
			return Response.status(Status.ACCEPTED).entity(u).build();
		return Response.status(Status.NOT_FOUND).build();
	}

	// @GET
	// @Produces(MediaType.APPLICATION_JSON)
	// @Path("/authentificate/{username}/{password}")
	// public Response Authenticate(@PathParam("username") String username,
	// @PathParam("password") String password) {
	// Member u = userService.Authenticate(username, password);
	// if (u != null) {
	// System.out.println(u);
	// return Response.status(Status.ACCEPTED).entity(u).build();
	// }
	//
	// else {
	// System.out.println("no user !!!!!!!!");
	// return Response.status(Status.NOT_ACCEPTABLE).build();
	// }
	// }
/*
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/authentificate")
	public Response Authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		if (userService.Authenticate(username, password) !=null) {
			try {
				// Issue a token for the user
				String token = issueToken(username);
				User u = userService.Authenticate(username, password);
				// Return the token on the response
				return Response.ok(u).entity(u).build();

			} catch (NoResultException e) {
				e.printStackTrace();
				return Response.status(Response.Status.NOT_ACCEPTABLE).entity("no user").build();
			}
		}

		else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("user not connected").build();
		}
	}

	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	//@Path("/local")
	public Response getLocalVets(User u){
		System.out.println(u);
		List<User> l = userService.getLocalVets(u);
		if(l!=null){
			System.out.println("there's "+l.size()+" Local vets");
			return Response.status(Status.ACCEPTED).entity(l).build();
		}
		System.out.println("no local vets");
		return Response.status(Status.NOT_ACCEPTABLE).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@QueryParam("email") String email){
		if (userService.getUserByEmail(email) != null)
			return Response.ok(userService.getUserByEmail(email)).build();
		return Response.status(Status.NOT_FOUND).build();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response loggedinLast24(){
		return Response.status(Status.ACCEPTED).entity(userService.isLoggedIn24H()).build();
	}
	
	
	private String issueToken(String username) {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		// The issued token must be associated to a user
		// Return the issued token

		String keyString = "simplekey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		System.out.println("the key is : " + key);

		String jwtToken = Jwts.builder().setSubject(username).setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
				.signWith(SignatureAlgorithm.HS512, key).compact();

		System.out.println("the returned token is : " + jwtToken);
		return jwtToken;

		/*
		 * Random random = new SecureRandom(); String token = new
		 * BigInteger(130,random).toString(32); return token;
		 */

		/*
		 * Date creationDate = new Date();
		 * 
		 * String key = UUID.randomUUID().toString().toUpperCase() + "|" +
		 * username + "|" + format.format(creationDate);
		 * 
		 * // this is the authentication token user will send in order to use
		 * the web // service String authenticationToken = jasypt.encrypt(key);
		 * 
		 * return authenticationToken;
		 */
	//}

	// ======================================
	// = Private methods =
	// ======================================

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/authentificate")
	public Response Authenticate(@FormParam("username") String username, @FormParam("password") String password) {
		if (userService.Authenticate(username, password)!=null) {
			try {
				// Issue a token for the user
				//String token = issueToken(username);
				User u = userService.Authenticate(username, password);
				// Return the token on the response
				return Response.ok(u).entity(u).build();

			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		}

		else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
	}


	
	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response addPhoto(MultipartFormDataInput input) {
		int uid = 0;
		String finalPath = "";

		// les formats de données
		List<String> formatFile = new ArrayList<String>();
		formatFile.add("jpeg");
		formatFile.add("jpg");
		formatFile.add("png");

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		//List<InputPart> userParts = uploadForm.get("user");

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
					return Response.status(Status.NOT_ACCEPTABLE)
							.entity("Format not supported  please use .jpeg .jpg .png  format").build();
				}
				// end of file format test
//				String fileLocation = "C:\\Users\\oumayma gader\\Documents\\workspace java EE\\javaee\\seahawks-web\\src\\main\\webapp\\"
//						+ "assets\\users\\" + UUID.randomUUID().toString() + filename;
				//String fileLocation = "C:\\wamp64\\wamp64\\www\\uploads\\users\\" + filename;
				String fileLocation = "C:\\Users\\oumayma gader\\Documents\\workspace Angular 2\\app-user\\src\\assets\\uploads\\users\\"  + filename;
				int index = fileLocation.lastIndexOf("users") + 6;
				finalPath = fileLocation.substring(index);

				// create file
				FileOutputStream fileOuputStream = new FileOutputStream(fileLocation);
				fileOuputStream.write(bytes);
				
				String fileLocation1 = "C:\\ionic-user\\src\\assets\\uploads\\users\\"  + filename;
				int index1 = fileLocation.lastIndexOf("users") + 6;
				finalPath = fileLocation.substring(index1);

				// create file
				FileOutputStream fileOuputStream1 = new FileOutputStream(fileLocation1);
				fileOuputStream.write(bytes);

			} catch (IOException e) {
				return Response.status(Status.NOT_ACCEPTABLE).entity("Bad format, error parsing file.").build();
			}
		}

		// get id
		String[] contentDisposition = new String[1000];
		/*for (InputPart inputPart : userParts) {
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			contentDisposition = headers.getFirst("Content-Disposition").split(";");
			try {
				String userid = inputPart.getBodyAsString();
				uid = Integer.parseInt(userid);
				System.out.println(uid);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}*/

		// Call service (persist)
		//userService.addUserPhoto(uid, finalPath);

		return Response.status(Status.OK).entity("Photo added.").build();
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
	
	

}