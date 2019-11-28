package com.moviehub.ApiClient;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moviehub.DAO.DBConnector;
import com.moviehub.model.Account;

@Path("/Account")
public class AccountService {

	@GET
	@Path("/All")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAllAccount()
	{
		DBConnector db = new DBConnector();
		System.out.println("Get all account");
		
		ArrayList<Account> list = db.getAllAccount();
		
//		System.out.println(new Gson().toJson(list));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(list)).
				build();
	}
	
	@POST
	@Path("/ChangePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changePassword(String request)
	{
		Account ac = new Gson().fromJson(request, Account.class);
		
		DBConnector db = new DBConnector();
		System.out.println("Change password account: "+ac.getUsername());
		String msg = "Password can not change!";
		
		try {
			if(db.changePassword(ac)) {
				msg = "Password was updated!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JsonObject object = new JsonObject();
		object.addProperty("announce", msg);
		
//		System.out.println(new Gson().toJson(list));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(object)).
				build();
	}
	
	@POST
	@Path("/Add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addAccount(String request)
	{
		Account ac = new Gson().fromJson(request, Account.class);
		
		DBConnector db = new DBConnector();
		System.out.println("Add account: "+ac.getUsername());
		String msg = "This username is already in use!";
		
		try {
			if(ac.getRole()==1)
			{
				if(db.addAccountAdmin(ac)) {
					msg = "Account created successfully!";
				}
			}
			else if(ac.getRole()==0)
			{
				if(db.addAccount(ac)) {
					msg = "Account created successfully!";
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JsonObject object = new JsonObject();
		object.addProperty("announce", msg);
		
//		System.out.println(new Gson().toJson(list));
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(object)).
				build();
	}
	
	@POST
	@Path("/Delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteAccount(String request)
	{
		Account ac = new Gson().fromJson(request, Account.class);
		DBConnector db = new DBConnector();
		System.out.println("Delete all account: "+ac.getUsername());
		
		String msg = "Delete "+ac.getUsername()+" fail!";
		try {
			if(db.deleteAccount(ac)) {
				msg = "Delete "+ac.getUsername()+" successfully!";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonObject object = new JsonObject();
		object.addProperty("announce", msg);
		
		return Response.
				status(Response.Status.OK).
				entity(new Gson().toJson(object)).
				build();
	}
	
}
