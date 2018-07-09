package tn.esprit.seahawks.beans;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.MemberServiceLocal;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.User;

@Path("member")
@RequestScoped
public class MemberResource {

	@EJB(beanName = "MemberService")
	MemberServiceLocal memberService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMember(Member m) {
		int result1 = memberService.addMember(m);
		String result2 = JsonConverter.convertUser(m);
		return Response.ok(m).entity(m).build();
	}
	
	@POST
	@Path("photo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addMemberwithPhoto(Member m) {
		int result1 = memberService.addMember(m);
		String result2 = JsonConverter.convertUser(m);
		return Response.ok(m).entity(m).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateMember(Member m) {
		String result2 = JsonConverter.convertUser(m);
		if (memberService.updateMember(m))
			return Response.status(Status.ACCEPTED).entity("member updated: => " + result2).build();
		else return Response.status(Status.BAD_REQUEST).entity("member not updated: => " + result2).build();
	}
	
	@PUT
	@Path("modifyMember")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modifyMember(Member newMember) {
		Member oldMember = memberService.findOne(newMember.getId());
		if(memberService.modifyMember(oldMember, newMember)==true)
			return Response.status(Status.OK).entity(newMember).build();
		else 
			return Response.status(Status.CREATED).entity("not modified ").build();
	}
	
	@PUT
	@Path("desactivate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response desactivateAccount(Member u) {
		if (memberService.DesactivateAccount(u)) {
			System.out.println("Account desactivated !");
			return Response.status(Status.ACCEPTED).build();
		} else {
			System.out.println("Account desactivation failed");
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("change/{oldpwd}/{newpwd}")
	public Response changePassword(@PathParam("oldpwd") String oldPassword, @PathParam("newpwd") String newPassword,
			Member u) {
		try{
			if (memberService.changePassword(oldPassword, newPassword, u)) {
				return Response.status(Status.ACCEPTED).entity(u).build();
			} else {
				return Response.status(Status.NOT_ACCEPTABLE).build();
			}
		}
		
		catch (Exception e) {
			return Response.ok().build();
		}
	}
	
	@PUT
	@Path("/forgotByMail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ForgotPasswordByMail(Member u) {
		memberService.ForgotPasswordByMail(u);
		return Response.status(Status.ACCEPTED).entity(u).build();
	}
	
}
