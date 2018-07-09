package tn.esprit.seahawks.beans;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.SignalUserServiceLocal;
import tn.esprit.seahawks.persistence.SignalUser;
import tn.esprit.seahawks.persistence.User;

@Path("SignalUser")
@RequestScoped
public class SignalUserResource {
	@EJB(beanName = "SignalUserService")
	SignalUserServiceLocal signaluserService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response blockUser(SignalUser su) {

		System.out.println(su);
		if (su.getDoer().getId() != su.getSignaled().getId()) {
			if (signaluserService.isAlreadyBlocked(su.getDoer().getId(), su.getSignaled().getId()).isEmpty()) {
				if (signaluserService.blockUser(su))
					return Response.status(Status.CREATED).entity(su).build();
				return Response.status(Status.BAD_REQUEST).build();
			}

			else {
				System.out.println("already blocked");
				return Response.status(Status.FORBIDDEN).entity("You have already block this user").build();
			}
		} else
			System.out.println("don't block yourself");
			return Response.status(Status.FORBIDDEN).entity("You have already block this user").build();

	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response unblockUser(SignalUser su) {
		if (signaluserService.unblockUser(su))
			return Response.status(Status.ACCEPTED).entity("User unblocked").build();
		return Response.status(Status.BAD_REQUEST).build();
	}

}
