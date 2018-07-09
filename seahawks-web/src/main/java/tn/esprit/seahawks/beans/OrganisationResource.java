package tn.esprit.seahawks.beans;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.OrganisationServiceLocal;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Organisation;

@Path("organisation")
@RequestScoped
public class OrganisationResource {

	@EJB(beanName = "OrganisationService")
	OrganisationServiceLocal organisationService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOrg(Organisation o) {
		int result1 = organisationService.addOrg(o);
		String result2 = JsonConverter.convertUser(o);

		return Response.ok(result1).entity("org id:" + result1 + " => " + result2).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrg(Organisation o) {
		String result2 = JsonConverter.convertUser(o);
		if (organisationService.updateOrg(o))
			return Response.status(Status.ACCEPTED).entity("org updated: => " + result2).build();
		else
			return Response.status(Status.BAD_REQUEST).entity("org not updated: => " + result2).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterOrg(@QueryParam("name") String name, @QueryParam("date") Date d,
			@QueryParam("country") String country, @QueryParam("city") String city,
			@QueryParam("street") String street) {
		if (name != null) {
			if (organisationService.getOrgByName(name) != null)
				return Response.status(Status.ACCEPTED).entity(organisationService.getOrgByName(name)).build();
			return Response.status(Status.NOT_FOUND).build();
		} else if (d != null) {
			if (organisationService.getOrgByFoundDate(d) != null)
				return Response.status(Status.ACCEPTED).entity(organisationService.getOrgByFoundDate(d)).build();
			return Response.status(Status.NOT_FOUND).build();
		}
		else if (country!=null && city!=null && street!=null){
			if (organisationService.getOrgByAddress(country, city, street) != null)
				return Response.status(Status.ACCEPTED).entity(organisationService.getOrgByAddress(country, city, street)).build();
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.UNSUPPORTED_MEDIA_TYPE).build();
	}
	
	@GET
	@Path("getAllorgs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllOrg() {
		if(organisationService.getAllOrg()!= null)
			return Response.status(Status.ACCEPTED).entity(organisationService.getAllOrg()).build();
		else
			return Response.status(Status.NOT_ACCEPTABLE).entity("no orgs").build();
		
	}

	
}
