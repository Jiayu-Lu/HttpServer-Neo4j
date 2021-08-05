package ca.utoronto.utm.mcs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.stream.Collectors;

import org.json.JSONObject;

public class Utils {
	
	/**
     * The method parses requestBody into string 
     * 
     * @param inputStream
     * 
     * @return string
     * 
     */
    public static String convert(InputStream inputStream) throws IOException {
 
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
    
    /**
     * The method checks the format of requestBody for addActor     
     * 
     * @param jsonBody
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean addActorCheckBody(JSONObject jsonBody) {
    	 
    	return jsonBody.has("name") && jsonBody.has("actorId");

    }
    
    /**
     * The method checks the format of requestBody for addMovie    
     * 
     * @param jsonBody 
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean addMovieCheckBody(JSONObject jsonBody) {
   	 
    	return jsonBody.has("name") && jsonBody.has("movieId");
    	
    }
    
    /**
     * The method checks the format of requestBody for addRelationship     
     * 
     * @param jsonBody
     * 
     */
    public static boolean addRelationshipCheckBody(JSONObject jsonBody) {
      	 
    	return jsonBody.has("actorId") && jsonBody.has("movieId");
    	
    }
    
    /**
     * The method checks the format of requestBody for getActor     
     * 
     * @param jsonBody
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean getActorCheckBody(JSONObject jsonBody) {
     	 
    	return jsonBody.has("actorId");
    	
    }
    
    /**
     * The method checks the format of requestBody for hasRelationship    
     * 
     * @param jsonBody
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean hasRelationshipCheckBody(JSONObject jsonBody) {
    	 
    	return jsonBody.has("actorId") && jsonBody.has("movieId");
    	
    }
    
    /**
     * The method checks the format of requestBody for computeBaconNumber    
     * 
     * @param jsonBody
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean computeBaconNumberCheckBody(JSONObject jsonBody) {
    	 
    	return jsonBody.has("actorId");
    	
    }
    
    /**
     * The method checks the format of requestBody for computeBaconPath    
     * 
     * @param jsonBody
     * 
     * @return true if the format is valid
     * 		   false if the format is invalid
     * 
     */
    public static boolean computeBaconPathCheckBody(JSONObject jsonBody) {
    	 
    	return jsonBody.has("actorId");
    	
    }
}
