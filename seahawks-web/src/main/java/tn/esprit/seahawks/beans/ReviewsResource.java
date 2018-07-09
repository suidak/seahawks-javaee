package tn.esprit.seahawks.beans;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.bean.RequestScoped;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.seahawks.interfaces.ReportServiceLocal;
import tn.esprit.seahawks.interfaces.ReviewServiceLocal;
import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.FoundReview;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.LostReview;
import tn.esprit.seahawks.persistence.Member;
import tn.esprit.seahawks.persistence.Review;

@Path("reviews")
@RequestScoped
public class ReviewsResource {

	@EJB(beanName = "ReportService")
	ReportServiceLocal lRs ;
	@EJB(beanName = "UserService")
	UserServiceLocal uS ;
	@EJB(beanName = "ReviewService")
	ReviewServiceLocal rs;
	
	@Path("FoundReportRightAnswer")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response FoundReportRightAnswer(JsonObject x)
	{			if(rs.chooseRightAnswerF(x.getInt("review")))
		return Response.status(Status.OK).build();	
	return Response.status(Status.EXPECTATION_FAILED).build();
	}
	
	@Path("LostReportRightAnswer")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response LostReportRightAnswer(JsonObject x )
	{	
		if(rs.chooseRightAnswerL(x.getInt("review")))
		return Response.status(Status.OK).build();	
		
		return Response.status(Status.EXPECTATION_FAILED).build();
	}
	
	@Path("revf")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FoundReview showReviewF()
	{
		FoundReview ls = new FoundReview();
		ls.setContent("ee");
		
		return ls ;
	}
	//ObjectJson
	@Path("revl")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LostReview showReviewL()
	{
		LostReview ls = new LostReview();
		ls.setContent("ee");
		
		return ls ;
	}
	@Path("addFoundReview")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFoundReview(JsonObject x) {
		FoundReview f = new FoundReview();
		f.setContent(x.getString("content"));
		//f.setExistingLostReportMatch(lRs.findLostReport(x.getInt("existingLostReportMatch")));
		f.setReviewer(uS.FindUser(x.getInt("reviewer")));
		f.setFoundReport(lRs.findFoundReport(x.getInt("foundReport")));
		System.out.println(x);
		if(rs.addFoundReview(f))
		
			return Response.status(Status.CREATED).build();
		
			return Response.status(Status.EXPECTATION_FAILED).build();
	}
	@Path("addLostReview")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLostReview(LostReview f) {
		//LostReview f = new LostReview ();
		/*f.setContent(x.getString("content"));
		//f.setExistingLostReportMatch(lRs.findLostReport(x.getInt("existingLostReportMatch")));
		f.setReviewer(uS.FindUser(x.getInt("reviewer")));
		f.setReportLost(lRs.findLostReport(x.getInt("reportLost")));
		System.out.println(x);*/
		if(rs.addLostReview(f))
		{
			return Response.status(Status.CREATED).build();
		}
			return Response.status(Status.EXPECTATION_FAILED).build();
	}
	@Path("editLostReview")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putLostReview(JsonObject x) {
		LostReview f = new LostReview ();
		f.setId(x.getInt("id"));
		f.setContent(x.getString("content"));
		f.setReviewer((Member)uS.FindUser(x.getInt("reviewer")));
		f.setReportLost(lRs.findLostReport(x.getInt("lostreport")));

		if(rs.editLostReview(f))
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.EXPECTATION_FAILED).build();
	}
	@Path("editFoundReview")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putFoundReview(JsonObject x) {
		FoundReview f = new FoundReview ();
		f.setId(x.getInt("id"));
		f.setContent(x.getString("content"));
		f.setReviewer((Member)uS.FindUser(x.getInt("reviewer")));
	//	f.setExistingLostReportMatch(lRs.findLostReport(x.getInt("lostreport")));
		f.setFoundReport(lRs.findFoundReport(x.getInt("foundreport")));

		if(rs.editFoundReview(f))
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.EXPECTATION_FAILED).build();
	}
	
	@Path("FoundReviews")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showFoundReviews()
	{	
		Map<FoundReport, List<FoundReview>> lr = rs.showFoundReportAlongReviews();
		if (!lr.isEmpty()) {
			return Response.status(Status.FOUND).entity(rs.showFoundReportAlongReviews()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("LostReviews")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showLostReviews()
	{	
		Map<LostReport, List<LostReview>> lr = rs.showLostReportAlongReviews();
		if (!lr.isEmpty()) {
			return Response.status(Status.FOUND).entity(rs.showLostReportAlongReviews()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	
	@Path("removeFoundReview")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeFoundReview(FoundReview f)
	{
		if (rs.removeFoundReview(f.getId())) {
			return Response.status(Status.GONE).entity("reviewDeleted").build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

	}
	@Path("removeLostReview")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeLostReview(LostReview l)//Simple review InJson
	{
		if (rs.removeLostReview(l.getId())) {
			return Response.status(Status.GONE).entity("reviewDeleted").build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

	}
	
	@Path("ShowLostReviews")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ShowLostReview (JsonObject x){
		if(rs.showLostReviewsByReport(lRs.findLostReport(x.getInt("review")))!= null) {
			List<LostReview> list = rs.showLostReviewsByReport(lRs.findLostReport(x.getInt("review")));
			return Response.status(Status.OK).entity(list).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();

		

	}
	

	
	
	
	@Path("ShowFoundReview")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ShowFoundReview (JsonObject x){
		if(rs.showFoundReviewsByReport(lRs.findFoundReport(x.getInt("review")))!= null) {
			List<FoundReview> list = rs.showFoundReviewsByReport(lRs.findFoundReport(x.getInt("review")));
			return Response.status(Status.FOUND).entity(list).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();

		

	}
	
	@Path("ShowFoundReview/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ShowFoundReviews (@PathParam("id")int id){
		if(rs.showFoundReviewsByReport(lRs.findFoundReport(id))!= null) {
			List<FoundReview> list = rs.showFoundReviewsByReport(lRs.findFoundReport(id));
			return Response.status(Status.OK).entity(list).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();

		

	}
	
	@Path("ShowLostReview/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ShowLostReviews (@PathParam("id")int id){
		if(rs.showLostReviewsByReport(lRs.findLostReport(id))!= null) {
			List<LostReview> list = rs.showLostReviewsByReport(lRs.findLostReport(id));
			return Response.status(Status.OK).entity(list).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();

		

	}
	
	@Path("statC")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response statC (){
		if(lRs.statActivityPerCountry() != null) {
			return Response.status(Status.OK).entity(lRs.statActivityPerCountry()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	
	@Path("tunisie")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response statT (){
		if(lRs.stattn() !=null) {
			return Response.status(Status.OK).entity(lRs.stattn()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	@Path("algerie")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response statA(){
		if(lRs.statalg() !=null) {
			return Response.status(Status.OK).entity(lRs.statalg()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	@Path("france")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response statf (){
		if(lRs.statfr() !=null) {
			return Response.status(Status.OK).entity(lRs.statfr()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	@Path("china")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response statchina (){
		if(lRs.statchina() !=null) {
			return Response.status(Status.OK).entity(lRs.statchina()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	@Path("australia")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response stataus (){
		if(lRs.stataus()!=null) {
			return Response.status(Status.OK).entity(lRs.stataus()).build();

		}
		return Response.status(Status.EXPECTATION_FAILED).entity("map").build();

		

	}
	
	
	
}
