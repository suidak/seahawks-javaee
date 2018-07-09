package tn.esprit.seahawks.beans;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.AddressServiceLocal;
import tn.esprit.seahawks.persistence.Address;
import tn.esprit.seahawks.persistence.Member;

@Path("address")
@RequestScoped
public class AddressResource {
	@EJB(beanName = "AddressService")
	AddressServiceLocal addressService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAddress(Address a,@QueryParam("id") int id) {
		int result = addressService.addAddress(a,id);
		String result2 = JsonConverter.convertAddress(a);
		return Response.status(Status.CREATED).entity("Address id:" + result+ " => " + result2).build();
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifyAddress(Address newA) {
		Address oldA = addressService.findOne(newA.getId());
		if(addressService.modifyAddress(oldA, newA)==true)
			return Response.status(Status.OK).entity(newA).build();
		else 
			return Response.status(Status.CREATED).entity("not modified ").build();
	}

}
