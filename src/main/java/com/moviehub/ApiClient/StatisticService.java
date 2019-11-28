package com.moviehub.ApiClient;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.moviehub.DAO.DBConnector;
import com.moviehub.model.Film;
import com.moviehub.model.FilmSaved;

@Path("/Statistic")
public class StatisticService {
	
	//get the top 5 film like the most
	@GET
	@Path("/FilmSavedMost")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getFilmSavedMost()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get FilmSavedMost  from DB");
		
		ArrayList<Film> list = db.getFilmSavedMost();
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	//get the top 5 genre like the most
	@GET
	@Path("/GenreSavedMost")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getGenreSavedMost()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get GenreSavedMost  from DB");
		
		ArrayList<Film> list = db.getGenreSavedMost();
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	//get the top 5 genre view the most
	@GET
	@Path("/GenreViewMost")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getGenreViewMost()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get GenreViewMost  from DB");
		
		ArrayList<Film> list = db.getGenreViewMost();
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	//get the top 5 account watched the most
	@GET
	@Path("/AccountWatchedMost")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAccountWatchedMost()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get FilmSavedMost  from DB");
		
		ArrayList<FilmSaved> list = db.getAccountWatchedMost();
		
		return  Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
}
