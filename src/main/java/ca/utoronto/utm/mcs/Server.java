package ca.utoronto.utm.mcs;

import javax.inject.Inject;

import com.sun.net.httpserver.HttpServer;

public class Server {
	private HttpServer httpServer;
	  
	/**
	 * Class constructor
	 * 
	 * @param httpServer
	 */
	@Inject
	public Server(HttpServer httpServer) { 
		this.httpServer = httpServer;
	}
	  
	  
	/**
	 * This method will get the class member httpserver.
	 * 
	 * @return HttpServer
	 */
	public HttpServer getHttpServer() {
		return this.httpServer;
	}
	  
}
	  
	  

