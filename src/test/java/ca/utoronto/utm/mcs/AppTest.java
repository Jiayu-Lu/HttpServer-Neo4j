package ca.utoronto.utm.mcs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import com.sun.net.httpserver.HttpServer;

// TODO Please Write Your Tests For CI/CD In This Class. You will see
// these tests pass/fail on github under github actions.
public class AppTest {
	static HttpServer httpServer;
	/**
	 * Before testing, set up the server and start the server.
	 */
	@BeforeAll
	public static void setUp() {
		int port = 8080;
		Driver serverDriver = GraphDatabase.driver("bolt://localhost:7687", 
	    		AuthTokens.basic("neo4j", "1234"));
		Neo4jDAO neo4jDAO = new Neo4jDAO(serverDriver);
		ReqHandler reqHandler = new ReqHandler(neo4jDAO);
		try {
			httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
			httpServer.createContext("/api/v1", reqHandler);
		    httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
	}
	/**
	 * After testing, stop the server.
	 */
	@AfterAll
	public static void tearDown() {
		httpServer.stop(0);
	}
	/**
	 * Add a valid actor to the database and check whether the response code is 200.
	 */
    @Test
    public void addActor_addOnce() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/addActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"actor1\", \"actorId\": \"a1\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Add an actor twice and check whether the response code is 400.
     */
    @Test
    public void addActor_addTwice() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
        	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
        	 	neo4jDAO.addActor("actor2", "a2");
        	 	neo4jDAO.close();
        	 	
	        	URL url = new URL("http://localhost:8080/api/v1/addActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"actor2\", \"actorId\": \"a2\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * When adding an actor to database, the format of the request body is not valid.Check whether the response code is 400.     
     */
    @Test
    public void addActor_incorrectFormat() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/addActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"actor3\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
	/**
	 * Add a valid movie to the database and check whether the response code is 200.
	 */
    @Test
    public void addMovie_addOnce() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/addMovie");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"movie1\", \"movieId\": \"m1\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Add a movie twice and check whether the response code is 400.
     */
    @Test
    public void addMovie_addTwice() {
         try {
	        	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
	     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addMovie("movie2", "m2");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/addMovie");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"movie2\", \"movieId\": \"m2\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * When adding a movie to database, the format of the request body is not valid.Check whether the response code is 400.     
     */   
    @Test
    public void addMovie_incorrectFormat() {
         try {	
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
	        	URL url = new URL("http://localhost:8080/api/v1/addMovie");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"name\": \"movie3\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
	/**
	 * Add a valid relationship between an actor and a movie and check whether the response code is 200.
	 */
    @Test
    public void addRelationship_addonce() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
        	 	neo4jDAO.addActor("actor4", "a4");
	     	 	neo4jDAO.addMovie("movie4", "m4");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/addRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"actorId\": \"a4\", \"movieId\": \"m4\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
	/**
	 * Add a relationship that has already exists and check whether the response code is 200.
	 */
    @Test
    public void addRelationship_addtwice() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
        	 	neo4jDAO.addActor("actor5", "a5");
	     	 	neo4jDAO.addMovie("movie5", "m5");
	     	 	neo4jDAO.addRelationship("a5", "m5");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/addRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"actorId\": \"a5\", \"movieId\": \"m5\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * When adding a relationship, the format of the request body is not valid.Check whether the response code is 400.     
     */  
    @Test
    public void addRelationship_incorrectFormat() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/addRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"actorId\": \"a6\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Adding a relationship between an actor and a movie, but the actor does not exist. Check whether the response code is 404.    
     */  
    @Test
    public void addRelationship_actorNotExist() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addMovie("movie7", "m7");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/addRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("PUT");
	 			
	 	        String test = "{\"actorId\": \"a7\", \"movieId\": \"m7\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 404);
         } catch (Exception e) {
    	
         }
    }
    /**
     * To get an actor that exists in the database and check whether the response code is 200.
     */
    @Test
    public void getActor_actorExist() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addActor("actor8", "a8");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/getActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a8\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * To get an actor that does not exist in the database and check whether the response code is 404.
     */
    @Test
    public void getActor_actorNotExist() {
         try {	 	
	        	URL url = new URL("http://localhost:8080/api/v1/getActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a9\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 404);
         } catch (Exception e) {
    	
         }
    }
    /**
     * When getting an actor, the format of the request body is not valid. Check whether the response code is 400.
     */
    @Test
    public void getActor_incorrectFormat() {
         try {	 	
	        	URL url = new URL("http://localhost:8080/api/v1/getActor");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"name\": \"actor10\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * To check whether there is a relationship between an certain actor and a certain movie. 
     * Check the whether the response code is 200.
     */
    @Test
    public void hasRelationship_relationshipExist() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addActor("actor10", "a10");
	     	 	neo4jDAO.addMovie("movie10", "m10");
	     	 	neo4jDAO.addRelationship("a10", "m10");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/hasRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a10\", \"movieId\": \"m10\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * To check whether there is a relationship between an certain actor and a certain movie, but the actor does not exist. 
     * Check the whether the response code is 404.
     */
    @Test
    public void hasRelationship_actorNotExist() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addMovie("movie11", "m11");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/hasRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a11\", \"movieId\": \"m11\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 404);
         } catch (Exception e) {
    	
         }
    }
    /**
     * To check whether there is a relationship between an certain actor and a certain movie.
     * But the format of the request body is not valid.Check whether the response code is 400.
     */
    @Test
    public void hasRelationship_incorrectFormat() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/getRelationship");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"\", \"movieId\": \"\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Compute the Bacon number when there is a path from an actor to Kevin Bacon.Check whether the response code is 200.
     */
    @Test
    public void computeBaconNumber_hasPath() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
        	 	neo4jDAO.addActor("Kevin Bacon", "nm1001231");
	     	 	neo4jDAO.addActor("actor12", "a12");
	     	 	neo4jDAO.addMovie("movie12", "m12");
	     	 	neo4jDAO.addRelationship("nm1001231", "m12");
	     	 	neo4jDAO.addRelationship("a12", "m12");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a12\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Compute the Bacon number when there is no path from an actor to Kevin Bacon.Check whether the response code is 404.
     */
    @Test
    public void computeBaconNumber_noPath() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
     	    		AuthTokens.basic("neo4j", "1234"));
        	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
	     	 	neo4jDAO.addActor("actor13", "a13");
	     	 	neo4jDAO.close();
	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a13\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 404);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Want to compute the Bacon number but the format of the request body is not valid.Check whether the response code is 400.
     */
    @Test
    public void computeBaconNumber_incorrectFormat() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"name\": \"actor13\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Find a Bacon path when there is a path from an actor to Kevin Bacon.Check whether the response code is 200.
     */
    @Test
    public void computeBaconPath_hasPath() {
         try {
        	 	Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
      	    		AuthTokens.basic("neo4j", "1234"));
         	 	Neo4jDAO neo4jDAO = new Neo4jDAO(driver);
 	     	 	neo4jDAO.addActor("actor14", "a14");
 	     	 	neo4jDAO.addMovie("movie14", "m14");
 	     	 	neo4jDAO.addRelationship("nm1001231", "m14");
 	     	 	neo4jDAO.addRelationship("a14", "m14");
 	     	 	neo4jDAO.close();
 	     	 	
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a14\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 200);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Find a Bacon path when there is no path from an actor to Kevin Bacon.Check whether the response code is 404.
     */
    @Test
    public void computeBaconPath_noPath() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"actorId\": \"a13\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 404);
         } catch (Exception e) {
    	
         }
    }
    /**
     * Want to findn the Bacon path but the format of the request body is not valid.Check whether the response code is 400.
     */
    @Test
    public void computeBaconPath_incorrectFormat() {
         try {
	        	URL url = new URL("http://localhost:8080/api/v1/computeBaconNumber");
	            HttpURLConnection con;
	        	con = (HttpURLConnection) url.openConnection();
	 			con.setRequestMethod("GET");
	 			
	 	        String test = "{\"name\": \"actor13\"}";	
	 	        con.setDoOutput(true);
	 	        DataOutputStream out = new DataOutputStream(con.getOutputStream());
	 	        out.writeBytes(test);
	 	        out.flush();
	 	        out.close();

	 	        int status = con.getResponseCode();   
	 	        con.disconnect();
	 	        assertTrue(status == 400);
         } catch (Exception e) {
    	
         }
    }

}
