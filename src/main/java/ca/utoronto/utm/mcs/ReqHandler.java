package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ReqHandler implements HttpHandler {

	private Neo4jDAO neo4jDAO;
    
    /**
     * Class constructor
     * 
     * @param neo4jDAO
     */
    @Inject
    public ReqHandler(Neo4jDAO neo4jDAO) {
      this.neo4jDAO=neo4jDAO;
    }

    /**
     * The method determines the request method and sends response
     * 
     * @param httpExchange
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    	//Initialize neo4jDAO
    	neo4jDAO.initNeo4jDAO();
    	
    	//Determine the type of request method
    	String requestMethod = httpExchange.getRequestMethod();
    	if("GET".equals(requestMethod) || "POST".equals(requestMethod)) { 
			try {
				handleGetRequest(httpExchange);
			} catch (IOException | JSONException e) {
				neo4jDAO.setStatus(400);
			}
		} else if ("PUT".equals(requestMethod)) { 
			try {
				handlePutRequest(httpExchange);
			} catch (IOException | JSONException e) {
				neo4jDAO.setStatus(400);
			}       
		} else {
			//The method is invalid
			neo4jDAO.setStatus(400);
		}
    	
    	//Send status and response back
		sendResponse(httpExchange, neo4jDAO.getStatus(), neo4jDAO.getResponse());
    }
    
    /**
     * The method handles GET request 
     * 
     * @param httpExchange
     * @throws JSONException 
     * 
     */
    public void handleGetRequest(HttpExchange httpexchange) throws IOException, JSONException {
    	
    	//Get request endpoint
		String endPoint = getEndpoint(httpexchange);
		
		//Convert request body to JSON object
		String requestBody = Utils.convert(httpexchange.getRequestBody());
		JSONObject jsonBody = new JSONObject(requestBody);
		
		//Determine the type of endpoint and execute corresponding query
		if (endPoint.equals("getActor")) {
			if (Utils.getActorCheckBody(jsonBody)) {
				neo4jDAO.getActor(jsonBody.get("actorId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else if (endPoint.equals("hasRelationship")) {
			if (Utils.hasRelationshipCheckBody(jsonBody)) {
				neo4jDAO.hasRelationship(jsonBody.get("actorId").toString(), 
						                 jsonBody.get("movieId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else if (endPoint.equals("computeBaconNumber")) {
			if (Utils.computeBaconNumberCheckBody(jsonBody)) {
				neo4jDAO.computeBaconNumber(jsonBody.get("actorId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else if (endPoint.equals("computeBaconPath")) {
			if (Utils.computeBaconPathCheckBody(jsonBody)) {
				neo4jDAO.computeBaconPath(jsonBody.get("actorId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else {
			//the endpoint is not valid
			neo4jDAO.setStatus(400);
		}
		
	}
    
    /**
     * The method handles PUT request 
     * 
     * @param httpExchange
     * @throws JSONException 
     * 
     */
	public void handlePutRequest(HttpExchange httpexchange) throws IOException, JSONException {
		
		//Get request endpoint
		String endPoint = getEndpoint(httpexchange);
				
		//Convert request body to JSON object
		String requestBody = Utils.convert(httpexchange.getRequestBody());
		JSONObject jsonBody = new JSONObject(requestBody);
		
		//Determine the type of endpoint and execute corresponding query
		if (endPoint.equals("addActor")) {
			if (Utils.addActorCheckBody(jsonBody)) {
				neo4jDAO.addActor(jsonBody.get("name").toString(), 
						          jsonBody.get("actorId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else if (endPoint.equals("addMovie")) {
			if (Utils.addMovieCheckBody(jsonBody)) {
				neo4jDAO.addMovie(jsonBody.get("name").toString(), 
						          jsonBody.get("movieId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else if (endPoint.equals("addRelationship")) {
			if (Utils.addRelationshipCheckBody(jsonBody)) {
				neo4jDAO.addRelationship(jsonBody.get("actorId").toString(),
						                 jsonBody.get("movieId").toString());
			} else {
				neo4jDAO.setStatus(400);
			}
		} else {
			//the endpoint is not valid
			neo4jDAO.setStatus(400);
		}
	}
	
	/**
     * The method sends response 
     * 
     * @param httpExchange
     * @param status
     * @param response
     * 
     */
	public static void sendResponse(HttpExchange httpexchange, int status, String response) throws IOException {
		
		httpexchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpexchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        
	}
	
	/**
     * The method parses the endpoint into string
     * 
     * @param httpExchange
     * 
     * @return string
     */
	public static String getEndpoint(HttpExchange httpExchange) {
		
		return httpExchange.getRequestURI().toString().split("\\/")[3];
		
	}
	
}