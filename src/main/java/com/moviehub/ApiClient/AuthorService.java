package com.moviehub.ApiClient;



import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moviehub.DAO.DBConnector;
import com.moviehub.model.Account;


@Path("/AuthorService")
public class AuthorService {

//	@GET
//	@Path("/authors")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getLogin(@QueryParam("username") String username, @QueryParam("password") String password) throws UnsupportedEncodingException  {
//		DBConnector db = new DBConnector();
//		
//		ArrayList<Account> listAccount = db.getDataAccount();
//		boolean check = false;
//		for (Account account : listAccount) {
//			if(username.equals(account.getUsername().trim()) && password.equals(account.getPassword().trim()))
//			{
//				check = true;
//				break;
//			}
//		}
//		
//		return Response.
//				status(Response.Status.OK).
//				entity(check).
//				build();
//	}
	
	
	@POST
	@Path("/authors")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postLogin(String request) 
			throws UnsupportedEncodingException  {
		System.out.println(request);
		Account ac = new Gson().fromJson(request, Account.class);
		
		DBConnector db = new DBConnector();
		System.out.println(ac.getUsername()+" "+ac.getPassword());
		
		ArrayList<Account> listAccount = db.getDataAccount();
		boolean check = false;
		for (Account account : listAccount) {
			if(ac.getUsername().equals(account.getUsername().trim()) && ac.getPassword().equals(account.getPassword().trim()))
			{
				check = true;
				ac = account;
				break;
			}
		}
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("result", check);
		jsonObject.addProperty("role", ac.getRole());
		
		System.out.println(new Gson().toJson(jsonObject));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(jsonObject)).
				build();
	}
	
	@POST
	@Path("/CheckUsername")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUsername(String request)			
			throws UnsupportedEncodingException  {
		
		Account ac = new Gson().fromJson(request, Account.class);
		
		DBConnector db = new DBConnector();
		System.out.println(ac.getUsername()+" "+ac.getPassword());
		
		ArrayList<Account> listAccount = db.getDataAccount();
		boolean check = true;
		for (Account account : listAccount) {
			if(ac.getUsername().equals(account.getUsername().trim()) )
			{
				check = false;
				ac = account;
				break;
			}
		}
		
		boolean result = check;

		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("result", result);
		 
		System.out.println(new Gson().toJson(jsonObject));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(jsonObject)).
				build();
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postRegister(String request)			
			throws UnsupportedEncodingException  {
		
		Account ac = new Gson().fromJson(request, Account.class);
		
		DBConnector db = new DBConnector();
		System.out.println(ac.getUsername()+" "+ac.getPassword());
		
		ArrayList<Account> listAccount = db.getDataAccount();
		boolean check = true; //check trung user name
		for (Account account : listAccount) {
			if(ac.getUsername().equals(account.getUsername().trim()) )
			{
				check = false;
				ac = account;
				break;
			}
		}
		
		boolean result = false;
		String announce = null;
		if(check)
		{
			 try {
				result = db.addAccount(ac);
				System.out.println("Is Success : "+result);
				if(result)
				{
					announce = "Regist Successfully!";
				}
				else {
					announce = "Regist Failure!";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			announce = "This username has been created!";
		}
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("result", result);
		jsonObject.addProperty("announce", announce);
		 
		System.out.println(new Gson().toJson(jsonObject));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(jsonObject)).
				build();
	}
}
