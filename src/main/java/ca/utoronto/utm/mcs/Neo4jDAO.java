package ca.utoronto.utm.mcs;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.HashMap;
import java.util.Map;

public class Neo4jDAO {
	Driver driver;
	int status;
	String response;
	
	/**
	 * Class constructor
	 * 
	 */
	public Neo4jDAO(Driver driver)
    {
        this.driver = driver;
    }
	
	/**
	 * The method runs addActor query
	 * 
	 * @param name
	 * @param actorId
	 * 
	 */
	public void addActor(String name, String actorId)
    {
        try (Session session = driver.session())
        {
        	 Map<String, Object> parameters = new HashMap<>();
        	 parameters.put("name", name);
        	 parameters.put("actorId", actorId);
        	 
        	 //check if the actor already exists
        	 Result actorExist = session.run("MATCH (a:Actor) "
        	 		+ "WHERE a.actorId = $actorId "
        	 		+ "RETURN a", 
        			 parameters);
        	 if (actorExist.hasNext()) {
        		 this.status =  400;
        		 session.close();
        		 return;
        	 }
        	 
        	 //run query to add the actor
        	 session.run("MERGE (a:Actor {name: $name, actorId: $actorId})", 
        			 parameters);
        	 this.status = 200;
        	 session.close();
        } catch (Exception e) {
        	this.status = 500;
        }
    }
	
	/**
	 * The method runs addMovie query
	 * 
	 * @param name
	 * @param movieId
	 * 
	 */
	public void addMovie(String name, String movieId)
    {
        try (Session session = driver.session())
        {
        	 Map<String, Object> parameters = new HashMap<>();
        	 parameters.put("name", name);
        	 parameters.put("movieId", movieId);
        	 
        	 //check if the movie already exists 
        	 Result movieExist = session.run("MATCH (m:Movie) "
        	 		+ "WHERE m.movieId = $movieId "
        	 		+ "RETURN m", 
        	 		parameters);
        	 if (movieExist.hasNext()) {
        		 this.status = 400;
        		 session.close();
        		 return;
        	 }
        	 
        	 //run query to add the movie
        	 session.run("MERGE (m:Movie {name: $name, movieId: $movieId})", 
        			 parameters);
        	 this.status = 200;
        	 session.close();
        } catch (Exception e) {
        	this.status = 500;
        }
    }
	
	/**
	 * The method runs addRelationship query
	 * 
	 * @param actorId
	 * @param movieId
	 * 
	 */
	public void addRelationship(String actorId, String movieId)
    {
        try (Session session = driver.session())
        {
        	 Map<String, Object> parameters = new HashMap<>();
        	 parameters.put("actorId", actorId);
        	 parameters.put("movieId", movieId);
        	 
        	 //check if the actor and movie exists
        	 Result actorExist = session.run("MATCH (a:Actor) "
        	 		+ "WHERE a.actorId = $actorId "
        	 		+ "RETURN a", 
        	 		parameters);
        	 Result movieExist = session.run("MATCH (m:Movie) "
        	 		+ "WHERE m.movieId = $movieId "
        	 		+ "RETURN m", 
        	 		parameters);
        	 if (!(actorExist.hasNext() && movieExist.hasNext())) {
        		 this.status = 404;
        		 session.close();
        		 return;
        	 }
        	 
        	 //check if the relationship exists
        	 Result relationshipExist = session.run("MATCH (a:Actor) "
        	 		+ "WHERE a.actorId = $actorId "
        	 		+ "MATCH (m:Movie) "
        	 		+ "WHERE m.movieId = $movieId "
        	 		+ "RETURN exists((a)-[:ACTED_IN]->(m)) AS hasRelationship", 
        	 		parameters);
        	 Record record = relationshipExist.next();
        	 if (record.get("hasRelationship").toString() == "TRUE") {
        		 this.status = 400;
        		 session.close();
        		 return;
        	 }
        	 
        	//run query to add the relationship
        	 session.run("MATCH (a:Actor) "
        	 		+ "WITH a "
        	 		+ "MATCH (m:Movie) "
        	 		+ "WHERE a.actorId = $actorId AND m.movieId = $movieId "
        	 		+ "CREATE (a)-[:ACTED_IN]->(m)", 
        	 		parameters);
        	 this.status = 200;
        	 session.close();
        } catch (Exception e) {
        	this.status = 500;
        }
    }
	
	/**
	 * The method runs getActor query
	 * 
	 * @param actorId
	 * 
	 */
	public void getActor(String actorId)
    {
        try (Session session = driver.session())
        {
        	Map<String, Object> parameters = new HashMap<>();
       	 	parameters.put("actorId", actorId);
       	 	
       	 	//check if the actor exists
        	Result actorExist = session.run("MATCH (a:Actor) "
        			+ "WHERE a.actorId = $actorId "
        			+ "RETURN a.name AS name, a.actorId AS actorId", 
        			parameters);
        	if (!actorExist.hasNext()) {
        		this.status = 404;
        		session.close();
        		return;
       	 	}
        	
        	//get the name and actorId of the actor for response
        	Record actorInfo = actorExist.next();
        	String actorIdResponse = actorInfo.get("actorId").asString();
        	String nameResponse = actorInfo.get("name").asString();
        	
        	//run query to get the actor
            Result result = session.run("MATCH (a:Actor) "
            		+ "WHERE a.actorId = $actorId "
            		+ "MATCH (m:Movie) "
            		+ "WHERE (a)-[:ACTED_IN]->(m:Movie) "
            		+ "RETURN m.movieId AS movieId", 
            		parameters);
 
            String movieList = "";
            Record record;
            if (result.hasNext()) {
            	while(result.hasNext()){
            		record = result.next();
            		if (result.hasNext()) {
            			movieList = movieList + "\n        " + 
            		record.get("movieId").toString() + ",";
            		} else {
            			movieList = movieList + "\n        " + 
            		record.get("movieId").toString() + "\n    ]";
            		}
			    }
            	response = "{\n    \"actorId\": \""+ actorIdResponse 
            			+ "\",\n    \"name\": \"" + nameResponse 
            			+ "\",\n    \"movies\": [" + movieList + "\n}";
            } else {
            	response = "{\n    \"actorId\": \""+ actorIdResponse 
            			+ "\",\n    \"name\": \"" + nameResponse 
            			+ "\",\n    \"movies\": []" + "\n}";
            }
            this.status = 200;
            session.close();

        } catch (Exception e) {
        	this.status = 500;
        }
    }
	
	/**
	 * The method runs hasRelationship query
	 * 
	 * @param actorId
	 * @param movieId
	 * 
	 */
	public void hasRelationship(String actorId, String movieId)
    {
        try (Session session = driver.session())
        {
        	Map<String, Object> parameters = new HashMap<>();
       	 	parameters.put("actorId", actorId);
       	 	parameters.put("movieId", movieId);
       	 	
       	 	//check if the actor and movie exist
        	Result actorExist = session.run("MATCH (a:Actor) "
        			+ "WHERE a.actorId = $actorId "
        			+ "RETURN a", 
        			parameters);
       	 	Result movieExist = session.run("MATCH (a:Actor) "
       	 			+ "WHERE a.actorId = $actorId "
       	 			+ "RETURN a", 
       	 			parameters);
       	 	if (!(actorExist.hasNext() && movieExist.hasNext())) {
       	 		this.status = 404;
       	 		session.close();
       	 		return;
       	 	}
       	 	
       	 	//run query to check if there is relationship
            Result result = session.run("MATCH (a:Actor) "
            		+ "WHERE a.actorId = $actorId "
            		+ "MATCH (m:Movie) "
            		+ "WHERE m.movieId = $movieId "
            		+ "RETURN a.actorId AS actorId, m.movieId AS movieId, "
            		+ "exists((a)-[:ACTED_IN]->(m)) AS hasRelationship", 
            		parameters);
            Record record = result.next();
            response = "{\n    \"actorId\": \""+ actorId 
            		+ "\",\n    \"movieId\": \"" + movieId 
            		+ "\",\n    \"hasRelationship\": " 
            		+ record.get("hasRelationship").toString().toLowerCase() + "\n}";
            this.status = 200;
            session.close();
        } catch (Exception e) {
        	this.status = 500;
        }
    }
	
	/**
	 * The method runs computeBaconNumber query
	 * 
	 * @param actorId
	 * 
	 */
	public void computeBaconNumber(String actorId) {
		int count = 0;
		try(Session session = driver.session()){
			if(actorId.equals("nm0000102")){
				response = String.format("{\n    \"baconNumber\":%d\n}",count);
			}else {
			    String KevinB = "Kevin Bacon";
			    Map<String, Object> parameters = new HashMap<>();
			    parameters.put("actorId",actorId);
			    parameters.put("KevinB",KevinB);

			    Result result = session.run("MATCH (a:Actor{actorId:$actorId}),"
			    		+ "(KevinB:Actor{name:$KevinB}), "
			    		+ "p = shortestPath((a)-[:ACTED_IN*]-(KevinB)) "
			    		+ "RETURN size(nodes(p)) AS length", 
			    		parameters);
			    if(result.hasNext()) {
			    	Record record = result.next();
			    	String length_string = record.get("length").toString();
			    	int length_int = Integer.parseInt(length_string);
			    	count = length_int / 2;
			    	response = String.format("{\n    \"baconNumber\":%d\n}",count);
					this.status = 200;
					session.close();
			    }
			    else{
			    	this.status = 404;
			    	session.close();
			    }
		    }	
		} catch(Exception e) {
			this.status=500;
		}
	}
	
	/**
	 * The method runs computeBaconPath query
	 * 
	 * @param actorId
	 * 
	 */
	public void computeBaconPath(String actorId){
		try(Session session = driver.session()){
			if(actorId.equals("nm0000102")) {
				response = "{\n    \"baconPath\": [\n        \"actorId\":\"nm0000102\"\n    ]}";
			}else{
				String KevinB = "Kevin Bacon";
			    Map<String, Object> parameters = new HashMap<>();
			    parameters.put("actorId",actorId);
			    parameters.put("KevinB",KevinB);

			    Result result = session.run("MATCH (a:Actor{actorId:$actorId}),"
			    		+ "(KevinB:Actor{name:$KevinB}), "
			    		+ "p = shortestPath((a)-[:ACTED_IN*]-(KevinB)) "
			    		+ "RETURN nodes(p) AS nodes,size(nodes(p)) AS length", 
			    		parameters);		    
			    if(result.hasNext()) {
			    	Record record = result.next();
			    	String length_string = record.get("length").toString();
			    	int length_int = Integer.parseInt(length_string);
			    	int i = 0;
				    int is_actor = 1;
				    String path="";
			    	while(i<length_int) {
					    if(is_actor==1) {
					    	path = path + "\n        " + 
					    record.get("nodes").get(i).get("actorId").toString()+",";
				        	 is_actor=1-is_actor;
					    }
					    else if(is_actor==0) {
					    	 path = path + "\n        " + 
					    record.get("nodes").get(i).get("movieId").toString()+",";
				        	 is_actor=1-is_actor;
					    }
					    i++;
			    	}
			    	path = path.substring(0,path.length()-1);
			    	response = "{\n    \"baconPath\": ["+ path + "\n    ]\n}";
			    }else {
			    	this.status=404;
			    	session.close();
			    }    
			}	    
		}catch(Exception e) {
			this.status=500;
		}
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getResponse() {
		return this.response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public void initNeo4jDAO() {
		this.status = 200;
        this.response = "";
	}
	
	public void close()
    {
        driver.close();
    }
}
