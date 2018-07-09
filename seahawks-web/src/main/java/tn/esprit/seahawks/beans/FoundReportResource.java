package tn.esprit.seahawks.beans;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import tn.esprit.seahawks.interfaces.UserServiceLocal;
import tn.esprit.seahawks.interfaces.ReportServiceLocal;
import tn.esprit.seahawks.persistence.FoundReport;
import tn.esprit.seahawks.persistence.LostReport;
import tn.esprit.seahawks.persistence.Report;
import tn.esprit.seahawks.persistence.ReportLocalisation;
import tn.esprit.seahawks.persistence.User;

@RequestScoped
@Path("FoundReport")
public class FoundReportResource {

	@EJB(beanName = "ReportService")
	ReportServiceLocal lRs ;
	@EJB(beanName = "UserService")
	UserServiceLocal uS ;
	
	@Path("deleteReport")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteReport(LostReport l)
	{	
		if(lRs.deleteReport((Report)l)) 
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.NOT_MODIFIED).build();
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addFoundReport(FoundReport FoundReport)
	{	
		if(lRs.addFoundReport(FoundReport)) 
		{
			return Response.status(Status.CREATED).entity("created").build();
		}
			return Response.status(Status.EXPECTATION_FAILED).build();
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

	private String addImage(List<InputPart> inputParts, List<String> formatFile) {
		// get picture
		String fname = "";
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
					System.out.println(filename);
					
					extension = filename.substring(i + 1);
				}

				if (!formatFile.contains(extension)) {
					// return Response.status(Status.NOT_ACCEPTABLE)
					// .entity("Format not supported please use .jpeg .jpg .png
					// format").build();
					return "error22";
				}
				// end of file format test
				String fileLocation = "C:\\Users\\Khaled Ouertani\\workspace\\Seahawks\\javaee\\seahawks-web\\src\\main"
						+ "\\webapp\\assets\\reports\\" + UUID.randomUUID().toString() + filename;
				
				int index = fileLocation.lastIndexOf("webapp")+7;
				String dbPath = fileLocation.substring(index);
				System.out.println(dbPath);

				// create file
				FileOutputStream fileOuputStream = new FileOutputStream(fileLocation);
				fileOuputStream.write(bytes);

				fname=dbPath;
				
			} catch (IOException e) {
				// return Response.status(Status.NOT_ACCEPTABLE).entity("Bad
				// format, error parsing file.").build();
				e.printStackTrace();
				return "error33";
			}
		}

		return fname;
	}
	private String sanitizeFilename(String s) {
		return s.trim().replaceAll("\"", "");
	}
	
	@Path("uploadImageData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadBase64(JsonObject x) {
		String im=x.getString("imagedata");
		String filename = UUID.randomUUID().toString()+"imagedata";
		String fileLocation = "C:\\Users\\Khaled Ouertani\\workspace\\Seahawks\\javaee\\seahawks-web\\src\\main"
				+ "\\webapp\\assets\\reports\\" +filename+".jpg";
		try (FileOutputStream imageOutFile = new FileOutputStream(fileLocation)) {
			// Converting a Base64 String into Image byte array
			byte[] imageByteArray = Base64.getDecoder().decode(im.getBytes("UTF-8"));
			imageOutFile.write(imageByteArray);
			return Response.status(Status.OK).entity("assets\\reports\\"+filename+".jpg").build();

		} catch (FileNotFoundException e) {
			return Response.status(Status.EXPECTATION_FAILED).entity("FileNotFound").build();

		} catch (IOException ioe) {
			return Response.status(Status.EXPECTATION_FAILED).entity("can't read image").build();
		}
	}
	
	@Path("uploadImage")
	@POST
	@Consumes("multipart/form-data")
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadImage(MultipartFormDataInput input)
	{	
		
		String fileFinalPath = "";

		List<String> formatFile = new ArrayList<String>();
		formatFile.add("jpeg");
		formatFile.add("jpg");
		formatFile.add("png");
		formatFile.add("JPEG");
		formatFile.add("JPG");
		formatFile.add("PNG");
		formatFile.add("GIF");
		formatFile.add("gif");





		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		
		//List<InputPart> inputParts = uploadForm.get("file");
		
		
		for (String key : uploadForm.keySet()){
			if(!key.equalsIgnoreCase("application"))
			{
				List<InputPart> inputParts = uploadForm.get(key);
				System.out.println(key);
				return Response.status(Status.OK).entity(addImage(inputParts, formatFile) ).build();

				
			}else {
				return Response.status(Status.EXPECTATION_FAILED).entity("error").build();

			}
			
		}
		

		return Response.status(Status.OK).entity("Photo added.").build();
	}
	
	@Path("findFoundReport")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findFoundReport(JsonObject x)
	{	
		FoundReport lr = lRs.findFoundReport(x.getInt("report"));
		if (lr != null) {
			return Response.status(Status.FOUND).entity(lr).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FoundReport showFoundReport()
	{
		FoundReport ls = new FoundReport();
		ls.setIsClosed(false);
		ls.setReporterUser(null);
		return ls ;
	}
	@Path("AllFoundReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllFoundReports()
	{	
		List<FoundReport> lr = lRs.getAllFoundReport();
		
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getAllFoundReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("AllFoundReportscount")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllFoundReportscount()
	{	
		List<FoundReport> lr = lRs.getAllFoundReport();
		List<JsonObject> list =null;
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getAllFoundReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("AllClosedFoundReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllClosedFoundReports()
	{	
		List<FoundReport> lr = lRs.getAllisClosedFoundReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getAllisClosedFoundReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	
	@Path("OpenFoundReports")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showOpenFoundReport()
	{	
		List<FoundReport> lr = lRs.getOpenFoundReport();
		if (!lr.isEmpty()) {
			return Response.status(Status.OK).entity(lRs.getOpenFoundReport()).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).build();

		
	}
	@Path("{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response showUserFoundReports(@PathParam("userId")int userId)
	{		
		//System.out.println(userId);
		User u =  uS.FindUser(userId);
		if(u != null) {
			List<FoundReport> lr = lRs.getUserSpecificFoundReport(u);
			if (!lr.isEmpty()) {
				return Response.status(Status.FOUND).entity(lRs.getUserSpecificFoundReport(u)).build();
			}
			return Response.status(Status.EXPECTATION_FAILED).build();
		}
		return Response.status(Status.NOT_ACCEPTABLE).entity("User: "+userId+" does not exist.").build();

		

		
	}
	
	@Path("searchFoundReport")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchFoundReport (@FormParam("criteria") String x){
		if(!lRs.searchFoundReport(x).isEmpty()) {
			System.out.println(x);
			return Response.status(Status.OK).entity(lRs.searchFoundReport(x)).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();
	}
	
	@Path("AllFoundReport/{criteria}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchFoundReports (@PathParam("criteria") String x){
		if(!lRs.searchFoundReport(x).isEmpty()) {
			System.out.println(x);
			return Response.status(Status.OK).entity(lRs.searchFoundReport(x)).build();
		}
		return Response.status(Status.EXPECTATION_FAILED).entity("List Is Empty").build();
	}
	

	
	@Path("editReport")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response EditReport(FoundReport l)
	{	
		if(lRs.editReport((Report)l)) 
		{
			return Response.status(Status.ACCEPTED).build();
		}
			return Response.status(Status.NOT_MODIFIED).build();
	}
		
	
}
