package test;

import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;

import test.filter.LoggingFilter;
import test.resource.ResourceRegistration;
import test.servlet.HelloWorldServlet;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent()
				.setImplementation(ResourceRegistration.class)
				.add(createServiceDependency()
						.setService(HttpService.class)
						.setRequired(true))
				.add(createServiceDependency()
						.setService(LogService.class)
						.setRequired(false)));
		// Whiteboard Servlet
		Properties props = new Properties();
		props.put("alias", "/whiteboard");
		manager.add(createComponent()
				.setInterface(Servlet.class.getName(), props)
				.setImplementation(HelloWorldServlet.class));
		
		// Whiteboard Filter
		props = new Properties();
		props.put("pattern", "/whiteboard");
		manager.add(createComponent()
				.setInterface(Filter.class.getName(), props)
				.setImplementation(LoggingFilter.class));
	}

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1)
			throws Exception {
		
	}

}
