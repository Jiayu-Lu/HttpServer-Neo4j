package ca.utoronto.utm.mcs;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import dagger.Module;
import dagger.Provides;

@Module
public class ServerModule {
	
	@Provides
	  public HttpServer provideHttpServer() {
	      try {
	        return HttpServer.create(new InetSocketAddress(8080), 0);
	      } catch (IOException e) {
	        return null;
	      }
	  }
}
