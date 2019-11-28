package com.moviehub.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.moviehub.model.Account;
import com.moviehub.model.Comment;
import com.moviehub.model.Film;
import com.moviehub.model.FilmSaved;
import com.moviehub.model.FilmWatched;
import com.moviehub.model.Profile;



public class DBConnector {
	
    Statement stm = null;
    ResultSet rs = null;
    Connection cnn = null;
    
    public void getInstance() {
        try {
            String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=APP_FILM;user=sa;password=sa";
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            cnn =  DriverManager.getConnection(dbURL);
//            System.out.println("Đã kết nối database\n");
        }
        catch ( SQLException e) {
            System.out.println("Không thể kết nối database\n" + e);
        }
    }
    
    //*****************************************************************
    //Account DAO
    //*****************************************************************
    //get all account with no password
    public ArrayList<Account> getAllAccount(){
    	
    	ArrayList<Account> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM ACCOUNT";
            rs = stm.executeQuery(query);
            while(rs.next()){
            	
            	Account account = new Account();
            	account.setUsername(rs.getString(1).trim());
            	account.setPassword("");
            	account.setRole(Integer.parseInt(rs.getString(3).trim()));
            	
//            	System.out.println("Profile: "+profile.toString());
                ds.add(account);
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    //change password
    public boolean changePassword(Account account) throws SQLException{
    	ArrayList<Account> listAc = this.getAllAccount();
        getInstance();
        int isSuccess = 0;
        
        stm = (Statement) cnn.createStatement();
        for(Account ac : listAc) {
        	if(ac.getUsername().equals(account.getUsername())) {
        		String update = "update ACCOUNT set password_='"+account.getPassword()+"' where username_='"+account.getUsername()+"'";
        		isSuccess = stm.executeUpdate(update);
        		break;
        	}
        }
       
        stm.close();
        CloseConnect();
       
        return isSuccess>0 ? true : false;
    }
    
    //add account admin
    public boolean addAccountAdmin(Account account) throws SQLException{
        getInstance();
        int isSuccess = 0;
        String insertAccount = "insert into ACCOUNT values "
        		+ "('"+account.getUsername().trim()+"', "
        		+ "'"+account.getPassword().trim()+"', '"+account.getRole()+"')";

        stm = (Statement) cnn.createStatement();
        isSuccess = stm.executeUpdate(insertAccount);
        
        stm.close();
        CloseConnect();
       
        return isSuccess>0 ? true : false;
    }
    
    //get data from table Account
    public ArrayList<Account> getDataAccount(){
        ArrayList<Account> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM ACCOUNT";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new Account(rs.getString(1).trim(),rs.getString(2).trim(),rs.getInt(3)));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    //*****************************************************************
    //Profile DAO
    //*****************************************************************
    
    //get data from table USER_PROFILE
    public ArrayList<Profile> getDataProfile(){
    	
    	ArrayList<Profile> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM USER_PROFILE";
            rs = stm.executeQuery(query);
            while(rs.next()){
            	
            	Profile profile = new Profile();
            	profile.setUsername(rs.getString(1).trim());
            	profile.setFistName(rs.getString(2).trim());
            	profile.setLastName(rs.getString(3).trim());
//            	System.out.println(rs.getString(2).trim()+ " " + rs.getString(3).trim());
            	if(rs.getString(4)==null)
            	{
            		profile.setImage("saitama.png");
            	}
            	else {
            		profile.setImage(rs.getString(4).trim());
				}
            	
//            	System.out.println("Profile: "+profile.toString());
                ds.add(profile);
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    
    //add new user
    public boolean addAccount(Account account) throws SQLException{
        getInstance();
        int isSuccess = 0;
        String insertAccount = "insert into ACCOUNT values "
        		+ "('"+account.getUsername().trim()+"', "
        		+ "'"+account.getPassword().trim()+"', '"+account.getRole()+"')";
        String insertProfile = "insert into USER_PROFILE values ('"
        		+account.getUsername().trim()+"', N'No', N'Fullname', 'null')";
        stm = (Statement) cnn.createStatement();
        
        isSuccess = stm.executeUpdate(insertAccount);
        stm.executeUpdate(insertProfile);
        
        stm.close();
        CloseConnect();
       
        return isSuccess>0 ? true : false;
    }
    
    public boolean deleteAccount(Account account) throws SQLException{
        getInstance();
        int isSuccess = 0;
        String deleteAccount = "delete ACCOUNT where username_='"+account.getUsername()+"'";
        String deleteUserProfile = "delete USER_PROFILE where username_='"+account.getUsername()+"'";
        String deleteFilmSaved = "delete SAVED_FILM where username_='"+account.getUsername()+"'";
        String deleteFilmWatched = "delete WATCHED_FILM where username_='"+account.getUsername()+"'";
        String deleteComment = "delete COMMENT where username_='"+account.getUsername()+"'";
        
        stm = (Statement) cnn.createStatement();
        if(account.getRole()==0) {
        	stm.executeUpdate(deleteComment);
        	stm.executeUpdate(deleteFilmWatched);
        	stm.executeUpdate(deleteFilmSaved);
        	stm.executeUpdate(deleteUserProfile);
        }
        isSuccess = stm.executeUpdate(deleteAccount);
        
        stm.close();
        CloseConnect();
       
        return isSuccess>0 ? true : false;
    }
    
    //Update info of account
    public boolean updateInfoAccount(Profile profile) throws SQLException {
    	
    	getInstance();
        int isSuccess = 0;
      //  FileOutputStream stream = new FileOutputStream();
        
        String updateProfile = "update USER_PROFILE set firstName = N'"+profile.getFistName()+"', "
        		+ "lastName = N'"+profile.getLastName()+"',"
        		+ "avatar = '"+profile.getImage()+"' "
        		+ "where username_ = '"+profile.getUsername()+"'";
       System.out.println(updateProfile);
        stm = (Statement) cnn.createStatement();
        isSuccess = stm.executeUpdate(updateProfile);
        
        stm.close();
        CloseConnect();
    	
        return isSuccess>0 ? true : false;
	}
    
    //*****************************************************************
    //FILM DAO
    //*****************************************************************
    
    //get all film from db
    public ArrayList<Film> getDataFilm(){
    	
    	ArrayList<Film> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM FILM";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new Film(rs.getString(1).trim(),rs.getString(2).trim(),rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5).trim(), rs.getString(6).trim(), rs.getString(7).trim(), rs.getString(8).trim(), rs.getString(9).trim()));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
  //get film by genre from db
    public ArrayList<Film> getDataFilmByGenre(String genre){
    	
    	ArrayList<Film> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM FILM WHERE genre = '"+genre+"'";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new Film(rs.getString(1).trim(),rs.getString(2).trim(),rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5).trim(), rs.getString(6).trim(), rs.getString(7).trim(), rs.getString(8).trim(), rs.getString(9).trim()));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    //get film recommend from db
    public ArrayList<Film> getFilmRecommend(String genre){
    	
    	ArrayList<Film> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT TOP 10 * FROM FILM WHERE genre = '"+genre+"' order by film_views desc";
            //System.out.println("Recommend query: "+query);
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new Film(rs.getString(1).trim(),rs.getString(2).trim(),rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5).trim(), rs.getString(6).trim(), rs.getString(7).trim(), rs.getString(8).trim(), rs.getString(9).trim()));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    //get film watched of user
    public ArrayList<FilmWatched> getDataFilmWatched(String username){
    	
    	ArrayList<FilmWatched> ds = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM WATCHED_FILM WHERE username_ = '"+username+"'";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new FilmWatched(rs.getString(1).trim(),rs.getInt(2),rs.getString(3).trim()));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return ds;
    }
    
    //get film saved of user
    public ArrayList<Film> getDataFilmSaved(String username){
    	
    	ArrayList<FilmSaved> ds = new ArrayList<>();
    	ArrayList<Film> films = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM SAVED_FILM WHERE username_ = '"+username+"'";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new FilmSaved(rs.getString(1).trim(),rs.getString(2)));
            }
            
            for(FilmSaved filmSaved : ds)
            	films.add(this.getDataFilmById(filmSaved.getIdFilm()));
            
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return films;
    }
    
    //get film by id
    public Film getDataFilmById(String id){
    	Film film = new Film();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM FILM WHERE id_film = '"+id+"'";
            rs = stm.executeQuery(query);
            rs.next();
            film = new Film(rs.getString(1).trim(),rs.getString(2).trim(),rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5).trim(), rs.getString(6).trim(), rs.getString(7).trim(), rs.getString(8).trim(), rs.getString(9).trim());
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return film;
    }
    
    //get current progress of film by id
    public int getCurrentProgressFilmById(String id, String username){
    	int progress = 0;
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT * FROM WATCHED_FILM WHERE id_film = '"+id+"' AND username_ = '"+username+"'";
            rs = stm.executeQuery(query);
            if(rs.next())
            	progress = rs.getInt(2);
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return progress;
    }
    
    //update film views
    public boolean updateFilmView(String id, int views)
    {
    	int isUpdated = 0;
        getInstance();
        try {        
            stm = cnn.createStatement();
            String update = "update FILM set film_views = '"+views+"' where id_film = '"+id+"'";
            isUpdated = stm.executeUpdate(update);
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return isUpdated > 0 ? true : false;
    }
    
    //update film watched
    public boolean updateFilmWatched(FilmWatched filmWatched)
    {
    	int isUpdated = 0;
    	boolean isExist = false;
    	ArrayList<FilmWatched> filmWatcheds = this.getDataFilmWatched(filmWatched.getUsername());
    	
    	for(FilmWatched watched : filmWatcheds)
    	{
    		if(filmWatched.getIdFilm().equals(watched.getIdFilm()) && filmWatched.getUsername().equals(watched.getUsername()))
    		{
    			isExist = true;
    			break;
    		}
    	}
    	
    	getInstance();
    	
    	if(isExist)
    	{
	        String update = "update WATCHED_FILM set cur_progress = '"+filmWatched.getCurProgress()+"' where id_film = '"+filmWatched.getIdFilm()+"' AND username_='"+filmWatched.getUsername()+"'";
	        try {
	        	stm = cnn.createStatement();
				isUpdated = stm.executeUpdate(update);
	            stm.close();
			} catch (SQLException ex) {
				System.out.println("Error: " + ex.toString());
			}
	        CloseConnect();
	        
            return isUpdated > 0 ? true : false;
    	}
    	else {
    		String insertFilmWatched = "insert into WATCHED_FILM values ( '"+filmWatched.getIdFilm()+"', "+filmWatched.getCurProgress()+", '"+filmWatched.getUsername()+"')";
	        try {
	        	stm = cnn.createStatement();
				isUpdated = stm.executeUpdate(insertFilmWatched);
	            stm.close();
				
			} catch (SQLException ex) {
				System.out.println("Error: " + ex.toString());
			}
	        CloseConnect();
	        
            return isUpdated > 0 ? true : false;
		}
    	
    }
    
    
  //update film loved
    public boolean updateFilmLoved(String filmId, String username)
    {
    	int isUpdated = 0;
    	boolean isExist = false;
    	ArrayList<Film> filmSaveds = this.getDataFilmSaved(username);
    	
    	for(Film saved : filmSaveds)
    	{
    		if(saved.getId_film().equals(filmId))
    		{
    			isExist = true;
    			break;
    		}
    	}
    	
    	getInstance();
    	
    	if(isExist)
    	{
	        String removeQuery = "Delete from SAVED_FILM where username_='"+username+"' AND id_film='"+filmId+"'";
	        try {
	        	stm = cnn.createStatement();
				isUpdated = stm.executeUpdate(removeQuery);
				
	            stm.close();
			} catch (SQLException ex) {
				System.out.println("Error: " + ex.toString());
			}
	        
	        CloseConnect();
	        
            return isUpdated > 0 ? false : true;
    	}
    	else {
    		String insertFilmWatched = "insert into SAVED_FILM values ( '"+filmId+"', '"+username+"')";
	        try {
	        	stm = cnn.createStatement();
				isUpdated = stm.executeUpdate(insertFilmWatched);
	            stm.close();
			
			} catch (SQLException ex) {
				System.out.println("Error: " + ex.toString());
			}
	        CloseConnect();
	        
            return isUpdated > 0 ? true : false;
		}
    	
    }
    
    //update film thumbnail
    public boolean updateInfoFilm(Film film) throws SQLException {
    	
    	getInstance();
        int isSuccess = 0;
        
        String updateProfile = "update FILM set title_film = N'"+film.getTitle_film()+"', "
        		+ "film_description = N'"+film.getFilm_description()+"',"
        		+ "trailer_url = N'"+film.getTrailer_url()+"',"
        		+ "film_url = N'"+film.getFilm_url()+"',"
        		+ "film_views = N'"+film.getFilm_views()+"',"
        		+ "rate_imdb = N'"+film.getRate_imdb()+"',"
        		+ "genre = N'"+film.getGenre()+"' "
        		+ "where id_film = '"+film.getId_film()+"'";
       
        stm = (Statement) cnn.createStatement();
        System.out.println("DB: "+updateProfile);
        isSuccess = stm.executeUpdate(updateProfile);
        stm.close();
        CloseConnect();
    	
        return isSuccess>0 ? true : false;
	}
    
    //add new film
    public boolean addFilm(Film film) throws SQLException {
        int isSuccess = 0;
        int id = this.getNumberOfFilm()+1;
        getInstance();
        film.setId_film("F0"+id);
        
        String sql = "INSERT INTO FILM values "
        		+ "('"+film.getId_film()+"' ,"
        		+ "N'"+film.getTitle_film()+"', "
        		+ "N'"+film.getThumbnail()+"', "
        		+ "N'"+film.getFilm_description()+"',"
        		+ "N'"+film.getTrailer_url()+"',"
        		+ "N'"+film.getFilm_url()+"',"
        		+ "N'"+film.getFilm_views()+"',"
        		+ "N'"+film.getRate_imdb()+"',"
        		+ "N'"+film.getGenre()+"' )";
	    try {
	    	stm = (Statement) cnn.createStatement();
	        System.out.println("DB: "+sql);
	        isSuccess = stm.executeUpdate(sql);
		}
	    catch (SQLException ex) {
			ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
		}
	    
    	stm.close();
        CloseConnect();
    	
        return isSuccess>0 ? true : false;
	}
    
    //delete film
    public boolean deleteFilm(Film f)
    {
    	int isUpdated = 0;
    	getInstance();
    	
        try {        
        	stm = (Statement) cnn.createStatement();
            String update1 = "delete SAVED_FILM where id_film='"+ f.getId_film() +"'";
            String update2 = "delete COMMENT where id_film='"+ f.getId_film() +"'";
            String update3 = "delete WATCHED_FILM where id_film='"+f.getId_film()+"'";
            String update4 = "delete FILM where id_film='"+f.getId_film()+"'";
            
            stm.execute(update1);
            stm.execute(update2);
            stm.execute(update3);
           	isUpdated = stm.executeUpdate(update4);

            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        return isUpdated > 0 ? true : false;
    }
    
    
    
    public int getNumberOfFilm()
    {
    	ArrayList<Integer> listId = new ArrayList<>();
    	getInstance();
    	
        try {        
            stm = cnn.createStatement();
            String query = "select id_film from FILM";
            rs = stm.executeQuery(query);
            
            while(rs.next())
            {
            	String id = rs.getString(1).trim();
            	id = id.replace("F0", "");
            	listId.add(Integer.parseInt(id));
            }
            
            rs.close();
            stm.close();
            CloseConnect();
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        int maxIndex = 0;
        if(listId.isEmpty())
        {
        	return maxIndex;
        }
        else{
        	return Collections.max(listId);
        }
    }
    
    //*****************************************************************
    //Comment DAO
    //*****************************************************************
    
    //get number of comment to auto increase id of comment ID
    public int getNumberOfComment()
    {
    	ArrayList<Comment> list = new ArrayList<>();
    	ArrayList<Integer> listIdCmt = new ArrayList<>();
    	getInstance();
    	String[] arr = null;
    	
        try {        
            stm = cnn.createStatement();
            String query = "select * from COMMENT";
            rs = stm.executeQuery(query);
            
            while(rs.next())
            {
            	list.add(new Comment(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim()));
            	Comment comment = list.get(list.size()-1);
            	arr = comment.getCommentId().split("M");
            	listIdCmt.add(Integer.parseInt(arr[1]));
            }
            
            rs.close();
            stm.close();
            CloseConnect();
            
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        int maxIndex = 0;
        if(listIdCmt.isEmpty())
        {
        	return maxIndex;
        }
        else{
        	return Collections.max(listIdCmt);
        }
    }
    
    //get comment of film by film ID
    public ArrayList<Comment> getCommentByFilmId(String filmId)
    {
    	ArrayList<Comment> list = new ArrayList<>();
    	getInstance();
    	
        try {        
            stm = cnn.createStatement();
            String query = "select * from COMMENT where id_film='"+filmId+"'";
            rs = stm.executeQuery(query);
            
            while(rs.next())
            {
            	list.add(new Comment(rs.getString(1).trim(), rs.getString(2).trim(), rs.getString(3).trim(), rs.getString(4).trim()));
            }
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        return list;
    }
    
    //update comment of film by comment Id
    public boolean updateCommentByCommentId(Comment comment)
    {
    	int isUpdated = 0;
    	getInstance();
    	
        try {        
            stm = cnn.createStatement();
            String update = "update COMMENT set content = N'"+comment.getContent()+"' where id_comment = '"+comment.getCommentId()+"'";
            System.out.println("updateCommentByCommentId: "+update);
            isUpdated = stm.executeUpdate(update);

            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        return isUpdated > 0 ? true : false;
    }
    
    //delete comment
    public boolean deleteCommentByCommentId(Comment comment)
    {
    	int isUpdated = 0;
    	getInstance();
    	
        try {        
            stm = cnn.createStatement();
            String update = "delete COMMENT where id_comment='"+comment.getCommentId()+"'";
            isUpdated = stm.executeUpdate(update);
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        return isUpdated > 0 ? true : false;
    }
    
    //add new comment
    public String addComment(Comment comment)
    {
    	int isUpdated = 0;
    	int positionNewComment = this.getNumberOfComment() + 1;
    	comment.setCommentId("CM"+positionNewComment);
    	getInstance();
        try {        
            String update = "insert into COMMENT values "
            		+ "('"+comment.getCommentId()+"', "
            		+ "'"+comment.getFilmId()+"', "
    				+ "'"+comment.getUsername()+"', "
					+ "N'"+comment.getContent()+"')";
            System.out.println(update);
            stm = (Statement) cnn.createStatement();
            isUpdated = stm.executeUpdate(update);
            
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
        	ex.printStackTrace();
            System.out.println("Error: " + ex.toString());
        }
        
        
        return isUpdated > 0 ? comment.getCommentId() : "";
    }
    
    ////////////////////////////////////////////////////////////////
    ////						Statistic DAO					////
    ////////////////////////////////////////////////////////////////
    
    public ArrayList<Film> getFilmSavedMost() {
    	ArrayList<FilmSaved> ds = new ArrayList<>();
    	ArrayList<Film> films = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "select top 5 id_film, 'Times'=count(*) from saved_film group by id_film order by 'Times'  desc";
            rs = stm.executeQuery(query);
            while(rs.next()){
                ds.add(new FilmSaved(rs.getString(1).trim(),rs.getString(2)));
            }
            
            for(FilmSaved filmSaved : ds) {
            	Film temp = this.getDataFilmById(filmSaved.getIdFilm());
            	temp.setFilm_url(filmSaved.getUsername()); //replace film url by time saved film
            	films.add(temp);
            }
            	
            
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return films;
	}
///////////////////////////////////////////////////////////////
    public ArrayList<Film> getGenreSavedMost() {
    	ArrayList<Film> films = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT TOP 5 genre, 'Times'=count(*) FROM saved_film, film WHERE saved_film.id_film = film.id_film GROUP BY genre ORDER BY 'Times' DESC";
            rs = stm.executeQuery(query);
            while(rs.next()){
            	Film f = new Film();
            	f.setFilm_description("-");
            	f.setFilm_url("-");
            	f.setRate_imdb("-");
            	f.setTrailer_url("-");
            	f.setThumbnail("-");
            	f.setId_film("-");
            	f.setTitle_film("-");
            
            	f.setGenre(rs.getString(1));
            	f.setFilm_views(rs.getString(2));
            	films.add(f);
            }
            
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return films;
	}
    /////////////////////////////////////////////////////////////////
    public ArrayList<Film> getGenreViewMost() {
    	ArrayList<Film> films = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            
            List<String> genreListRequest = new ArrayList<>();
            genreListRequest.add("Action");//3
            genreListRequest.add("Adventure");//2
            genreListRequest.add("Comedy");//1
            genreListRequest.add("Cartoon");//
            genreListRequest.add("Horror");//5
            genreListRequest.add("Romance");//
            genreListRequest.add("Science & Fiction");//6
            
            
            for(String genre : genreListRequest) {
            	String query = "SELECT film_views FROM film WHERE genre = '"+genre+"'";
            	
            	rs = stm.executeQuery(query);
            	int sum = 0;
                while(rs.next()){
                	int v = Integer.parseInt(rs.getString(1));
                	sum+=v;
                }
            	
            	Film f = new Film();
            	f.setFilm_description("-");
            	f.setFilm_url("-");
            	f.setRate_imdb("-");
            	f.setTrailer_url("-");
            	f.setThumbnail("-");
            	f.setId_film("-");
            	f.setTitle_film("-");
            
            	f.setGenre(genre);
            	f.setFilm_views(sum+"");
            	films.add(f);
            }
            
            
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return films;
	}
///////////////////////////////////////////////////////////////
    public ArrayList<FilmSaved> getAccountWatchedMost() {
    	ArrayList<FilmSaved> films = new ArrayList<>();
        getInstance();
        try {        
            stm = cnn.createStatement();
            String query = "SELECT TOP 5 username_, 'Times'=count(*) FROM watched_film GROUP BY username_ ORDER BY 'Times' DESC";
            rs = stm.executeQuery(query);
            while(rs.next()){
            	FilmSaved f = new FilmSaved();
            	f.setUsername(rs.getString(1).trim());
            	f.setIdFilm(rs.getString(2).trim());
            	films.add(f);
            }
            
            rs.close();
            stm.close();
            CloseConnect();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.toString());
        }
        return films;
	}
///////////////////////////////////////////////////////////////
    //disconnect from server
    public void CloseConnect() {
        if (cnn != null) {
            try {
                cnn.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
