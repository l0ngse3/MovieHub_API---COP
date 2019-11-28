package com.moviehub.ApiClient;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moviehub.DAO.DBConnector;
import com.moviehub.model.Profile;

@Path("/Profile")
public class UserProfileService {

//	private static String HOST_STORAGE = "E:/work-space-android/film_test/ImageProfile/";
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getProfile(@PathParam("username") String username)
	{
		DBConnector db = new DBConnector();
		System.out.println("Get user: "+username);
		
		ArrayList<Profile> list = db.getDataProfile();
		Profile check = new Profile();
		for (Profile profile : list) {
			if(profile.getUsername().equals(username))
			{
				check = profile;
				break;
			}
		}
		
		System.out.println(new Gson().toJson(check));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(check)).
				build();
	}
	
	
	@POST
	@Path("/updateInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response postUpdateInfoProfile(String resquest)
	{
		Profile profile = new Gson().fromJson(resquest, Profile.class);
		DBConnector db = new DBConnector();
		System.out.println("Save infor of profile "+ profile.getUsername());
		
		JsonObject result = new JsonObject();
		
		try {
			if(db.updateInfoAccount(profile))
			{
				result.addProperty("announce", "Saved");
			}
			else {
				result.addProperty("announce", "Can not save");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: can not save infor of profile "+ profile.toString());
		}
		
		
		System.out.println(new Gson().toJson(result));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(result)).
				build();
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response postUpdateImage(@FormDataParam("file") InputStream file, @FormDataParam("name") String name)
	{
		System.out.println("Change image profile: " + name);
		String mgs = "failed";
		if(file!=null)
		{
			String uploadedFileLocation = "E:\\work-space-android\\film_test\\ImageProfile\\" + name;
			DBConnector dbConnector = new DBConnector();
			ArrayList<Profile> listProfile = dbConnector.getDataProfile();
			
			String arr[]  = name.split("\\.");
			Profile changeImg = new Profile();
			for(Profile p : listProfile) {
				if(p.getUsername().trim().equals(arr[0])) {
					changeImg = p;
					changeImg.setImage(name);
					break;
				}
			}
			
			try {
				dbConnector.updateInfoAccount(changeImg);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			mgs = "success";
			writeToFile(file, uploadedFileLocation);
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Change image profile: " + mgs);

		JsonObject obj = new JsonObject();
		obj.addProperty("result", mgs);
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(obj)).
				build();
	}
	
	
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
			try {
				OutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
				
				uploadedInputStream.close();
			} catch (IOException e) {e.printStackTrace();}

	}
}
