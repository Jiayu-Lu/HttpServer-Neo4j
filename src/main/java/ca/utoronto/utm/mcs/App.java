package ca.utoronto.utm.mcs;

import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

public class App
{
    static int port = 8080;

    public static void main(String[] args) throws IOException
    {   
    	ServerComponent serverComponent = DaggerServerComponent.create();
        Server server = serverComponent.buildServer();
       
        ReqHandlerComponent reqComponent = DaggerReqHandlerComponent.create();
        ReqHandler reqHandler = reqComponent.buildHandler();
        
        HttpServer httpServer = server.getHttpServer();
        if (httpServer == null) {
        	System.out.printf("Server failed to start on port %d\n", port);
        	throw new IOException();
        }
        httpServer.createContext("/api/v1",reqHandler);
        httpServer.start();
    	System.out.printf("Server started on port %d\n", port);
    }
}
