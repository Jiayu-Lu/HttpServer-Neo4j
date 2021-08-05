package ca.utoronto.utm.mcs;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class ReqHandlerModule {
	
	@Provides
	public Neo4jDAO provideNeo4jDAO() {
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", 
        		AuthTokens.basic("neo4j", "1234"));
		return new Neo4jDAO(driver);
	}
	
}
