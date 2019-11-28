package com.moviehub.ApiClient;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataParam;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moviehub.DAO.DBConnector;
import com.moviehub.model.Film;
import com.moviehub.model.FilmWatched;

@Path("/Film")
public class FilmService {
	
	//get all film 
	@GET
	@Path("/GetAll")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAllFilm()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get all film from DB");
		
		ArrayList<Film> list = db.getDataFilm();
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	
	// get film by genre
	@POST
	@Path("/Genre")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFilmByGenre(@FormParam("genre") String genre)
	{
		DBConnector db = new DBConnector();
		System.out.println("Get film by genre from DB: "+genre);
		
		ArrayList<Film> list = db.getDataFilmByGenre(genre);
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("genre", genre);
		jsonObject.addProperty("list", new Gson().toJson(list));
		
		return  Response.
				status(Response.Status.OK).
				entity(jsonObject.toString()).
				build();
	}
	
	@POST
	@Path("/Recommend")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFilmRecommend(String request)
	{
		System.out.println("Get film recommend from DB: "+request);
		JsonArray arr = new Gson().fromJson(request, JsonArray.class);
		Film f = new Gson().fromJson(arr.get(0), Film.class);
		
		System.out.println("Get film recommend from DB: "+arr.get(0));
		String genre = f.getGenre();
		String id = f.getId_film();
		
		DBConnector db = new DBConnector();
		
		ArrayList<Film> list = db.getFilmRecommend(genre);
		
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getId_film().equals(id)) {
				list.remove(i);
				break;
			}
		}
		System.out.println(new Gson().toJson(list).toString());

		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list).toString()).
				build();
	}

	
//	get film saved of user
	@GET
	@Path("/Saved/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSavedFilm(@PathParam("username") String username)
	{
		DBConnector db = new DBConnector();
		System.out.println("Get film saved of user");
		ArrayList<Film> list = db.getDataFilmSaved(username);
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
//	get film by id
	@GET
	@Path("/id/{filmId}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFilmById(@PathParam("filmId") String filmId)
	{
		DBConnector db = new DBConnector();
		
		Film film = db.getDataFilmById(filmId);
		System.out.println("Get film watched by Id" + new Gson().toJson(film));
			return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(film)).
				build();
	}
	
//	get film watched of user
	@GET
	@Path("/Watched/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getWathcedFilm(@PathParam("username") String username)
	{
		DBConnector db = new DBConnector();
		System.out.println("Get film watched of user");
		ArrayList<FilmWatched> list = db.getDataFilmWatched(username);
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	// get progress of film watched
	@GET
	@Path("Watched/GetCurrentProgress/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFilmWatchedById(@QueryParam("filmId") String filmId, @QueryParam("username") String username)
	{
		DBConnector db = new DBConnector();
		
		int currentProgress = db.getCurrentProgressFilmById(filmId, username);
		System.out.println("Get film progress by Id: " + new Gson().toJson(currentProgress));
			return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(currentProgress)).
				build();
	}
	
	@PUT
	@Path("Update/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public void updateFilmViews(@PathParam("id") String filmId)
	{
		DBConnector db = new DBConnector();
		
		Film film = db.getDataFilmById(filmId);
		int view = Integer.parseInt(film.getFilm_views());
		view+=1;
		boolean isUpdated = db.updateFilmView(filmId, view);
		String s = isUpdated ? "Views updated in "+filmId : "Cannot update film views in "+filmId;
		System.out.println(s);
	}
	
	@PUT
	@Path("UpdateWatchedFilm/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFilmWatched(String filmWatched)
	{
		FilmWatched watched = new Gson().fromJson(filmWatched,FilmWatched.class);
		
//		FilmWatched filmWatched = new FilmWatched();
		System.out.println(watched.getIdFilm());
		
		boolean response= false;
		//update data when it was read
		DBConnector db = new DBConnector();
		boolean isUpdated = db.updateFilmWatched(watched);
		//log some thing in consolse and response to client
		String s = isUpdated ? "Time progress updated" : "Cannot update Time progress";
		response = isUpdated ? true : false;
		System.out.println(s);
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(response)).
				build();
	}
	
	@PUT
	@Path("/Save/Loved")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateFilmLoved(@QueryParam("filmId") String filmId, @QueryParam("username") String username)
	{
//		FilmWatched filmWatched = new FilmWatched();
		System.out.println("Film loved " + filmId);
		
		boolean response= false;
		//update data when it was read
		DBConnector db = new DBConnector();
		boolean isUpdated = db.updateFilmLoved(filmId, username);
		//log some thing in consolse and response to client
		response = isUpdated ? true : false;
		System.out.println(response ? "Add film saved "+filmId : "Delete film saved "+filmId);
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(response)).
				build();
	}
	
	@GET
	@Path("/Save/IsSaved")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isFilmSaved(@QueryParam("filmId") String filmId, @QueryParam("username") String username)
	{
//		FilmWatched filmWatched = new FilmWatched();
		System.out.println("Film loved " + filmId);
		
		boolean response= false;
		//update data when it was read
		DBConnector db = new DBConnector();
		ArrayList<Film> listSaved = db.getDataFilmSaved(username);
		//log some thing in consolse and response to client
		for(Film film : listSaved)
		{
			if(film.getId_film().equals(filmId))
			{
				response = true;
			}
		}
		
		System.out.println("Film saved id "+filmId+" of "+username+" : "+response);
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(response)).
				build();
	}
	
	//update thumbnail và thông tin phim
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response update(String request)
	{
		DBConnector db = new DBConnector();
		Film film = new Gson().fromJson(request, Film.class);
		System.out.println("Id film: "+film.getId_film()+" - "+film.getFilm_url());
		
		JsonObject result = new JsonObject();
		try {
			if(db.updateInfoFilm(film))
			{
				result.addProperty("result", true);
				result.addProperty("announce", "Saved");
			}
			else {
				result.addProperty("result", false);
				result.addProperty("announce", "Can not save");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error: can not save infor of profile "+ film.toString());
		}
		
		System.out.println(new Gson().toJson(result));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(result)).
				build();
	}
	
	@POST
	@Path("/Add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response add(String film) throws SQLException
	{
		DBConnector db = new DBConnector();
		Film f = new Gson().fromJson(film, Film.class);
		String msg = "Add film failed!";
		if(db.addFilm(f))
		{
			System.out.println("Add film: " + film);
			msg = "Add film successfully!";
		}

		JsonObject obj = new JsonObject();
		obj.addProperty("result", msg);
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(obj)).
				build();
	}
	
	@POST
	@Path("/Delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response delete(String film) throws SQLException
	{
		DBConnector db = new DBConnector();
		Film f = new Gson().fromJson(film, Film.class);
		System.out.println("Delete film: " + film);
		String msg = "Delete film failed!";
		if(db.deleteFilm(f))
		{
			msg = "Delete film successfully!";
			deleteFile(f.getFilm_url());
			deleteFile(f.getTrailer_url());
			deleteFile(f.getThumbnail());
		}

		JsonObject obj = new JsonObject();
		obj.addProperty("result", msg);
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(obj)).
				build();
	}



	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response upload(@FormDataParam("file") InputStream file, @FormDataParam("name") String name)
	{
		System.out.println("Add film: " + name);
		String mgs = "failed";
		if(file!=null)
		{
			String uploadedFileLocation = "E:\\work-space-android\\film_test\\" + name;	
			mgs = "success";
			writeToFile(file, uploadedFileLocation);
		}

		JsonObject obj = new JsonObject();
		obj.addProperty("result", mgs);
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(obj)).
				build();
	}
	
	
	public void deleteFile(String file_name) {
		String file_path = "E:\\work-space-android\\film_test\\"+file_name;
        try {
            File file = new File(file_path);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

