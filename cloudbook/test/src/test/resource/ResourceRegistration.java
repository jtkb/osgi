package test.resource;

import javax.servlet.ServletException;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.service.log.LogService;

import test.servlet.HelloWorldServlet;

public class ResourceRegistration {

	private volatile HttpService httpService;
	private volatile LogService logService;
	
	public void start(){
		try{
			httpService.registerResources("/", "/html", null);
			httpService.registerResources("/images", "/images", null);
			httpService.registerServlet("/hello", new HelloWorldServlet(), null, null);
		}catch(NamespaceException|ServletException e){
			if(logService != null){
				logService.log(LogService.LOG_WARNING, "Failed to register resources!", e);
			}else{
				e.printStackTrace();
			}
		} 
	}
}
