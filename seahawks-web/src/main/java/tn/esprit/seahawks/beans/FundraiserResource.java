package tn.esprit.seahawks.beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.primefaces.json.JSONObject;

import tn.esprit.seahawks.filters.JWTTokenNeeded;
import tn.esprit.seahawks.interfaces.FundraiserServiceLocal;
import tn.esprit.seahawks.persistence.Fundraiser;
import tn.esprit.seahawks.persistence.User;


@Path("fundraiser")
@RequestScoped
public class FundraiserResource {
	
	@EJB(beanName = "FundraiserService")
	FundraiserServiceLocal fundraiserService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFundraiser(Fundraiser f){

		if(fundraiserService.addFundraiser(f))
			return Response.status(Status.CREATED).entity("Fundraiser successfully added!").build();
		return Response.status(Status.NOT_ACCEPTABLE).entity("Fundraiser failed to be added..".toString()).build();
		
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateFundraiser(Fundraiser f){

		if(fundraiserService.updateFundraiser(f))
			return Response.status(Status.OK).build();
		return Response.status(Status.NOT_MODIFIED).build();
		
		
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/photo")
	public Response addFundraiserPhoto(JSONObject obj){
		int fid = obj.getInt("fid");
		String imgUrl = obj.getString("imgUrl");
		String title = obj.getString("title");
		
		
		if(fundraiserService.addFundraiserPhoto(fid, imgUrl, title))
			return Response.status(Status.OK).build();
		return Response.status(Status.NOT_ACCEPTABLE).build();
		
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response removeFundraiser(Fundraiser f){
		
		if(fundraiserService.removeFundraiser(f.getId()))
			return Response.status(Status.GONE).build();
		return Response.status(Status.NOT_ACCEPTABLE).build();
		
		
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/byuser")
	public Response getFundraisersByUser(User u){
		List<Fundraiser> fundraisers = fundraiserService.getFundraisersByUser(u);
		System.out.println(fundraisers.toString());
		if(!fundraisers.isEmpty())
			return Response.status(Status.OK).entity(fundraisers).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	
	//@JWTTokenNeeded
	@GET
	@Produces(MediaType.APPLICATION_JSON)

	public Response getAllFundraisers(){
		List<Fundraiser> fundraisers = fundraiserService.getAllFundraisers();
		if(!fundraisers.isEmpty())
			return Response.status(Status.OK).entity(fundraisers).build();
		else
			return Response.status(Status.NOT_FOUND).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/open")
	public Response getOpenFundraisers(){
		List<Fundraiser> fundraisers = fundraiserService.getOpenFundraisers();
		if(!fundraisers.isEmpty())
			return Response.status(Status.OK).entity(fundraisers).build();
		else
			return Response.status(Status.NOT_FOUND).build();
		
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/close")
	public Response closeFundraiser(Fundraiser f){
		if(fundraiserService.closeFundraiser(f)){
			return Response.status(Status.OK).entity("Fundraiser closed".toString()).build();
		}
		else
			return Response.status(Status.NOT_MODIFIED).entity("Fundraiser not closed".toString()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reached")
	public Response getReachedFundraisers(){
		List<Fundraiser> fundraisers = fundraiserService.getReachedFundraisers();
		if(!fundraisers.isEmpty())
			return Response.status(Status.OK).entity(fundraisers).build();
		else
			return Response.status(Status.NO_CONTENT).entity("There is no fundraiser that has reached its goal, "
					+ "or they are all still running".toString()).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reached/count")
	public Response getNbrReachedFundraisers(){
		Long nbr = fundraiserService.getNbrReachedFundraisers();
		if(nbr>0L)
			return Response.status(Status.OK).entity(nbr).build();
		else
			return Response.status(Status.NO_CONTENT).entity(nbr).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/raisedavg")
	public Response getAverageRaisedMoney(){
		double moy = fundraiserService.getAverageRaisedMoney();
		if(moy!=-1)
			return Response.status(Status.OK).entity(moy).build();
		else
			return Response.status(Status.NO_CONTENT).entity(moy).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/goalavg")
	public Response getAverageGoal(){
		double moy = fundraiserService.getAverageGoal();
		if(moy!=-1)
			return Response.status(Status.OK).entity(moy).build();
		else
			return Response.status(Status.NO_CONTENT).entity(moy).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/bylocation")
	public Response getNbrFundraisersByLocation(){
		Map<String, Long> list = fundraiserService.getNbrFundraisersByLocation();
		if(!list.isEmpty())
			return Response.status(Status.OK).entity(list).build();
		else
			return Response.status(Status.NO_CONTENT).entity("No content found".toString()).build();
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remaining/{id}")
	public Response getRemainingDays(@PathParam(value="id") int id){
		
		int diff = fundraiserService.getRemainingDays(id);
		if(diff!=-100)
			return Response.status(Status.OK).entity(diff).build();
		else
			return Response.status(Status.NOT_ACCEPTABLE).entity(diff).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/byid/{id}")
	public Response getFundraisersById(@PathParam(value="id") int id){
		Fundraiser fundraiser = fundraiserService.getFundraiserById(id);
		if(fundraiser!=null)
			return Response.status(Status.OK).entity(fundraiser).build();
		else
			return Response.status(Status.NO_CONTENT).build();
		
		
	}
	
	
	
	
	

}
