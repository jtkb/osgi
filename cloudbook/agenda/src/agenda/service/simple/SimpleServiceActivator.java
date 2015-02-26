package agenda.service.simple;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import agenda.api.Agenda;

public class SimpleServiceActivator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent()
				.setInterface(Agenda.class.getName(), null)
				.setImplementation(SimpleAgendaService.class));
	}
	
	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		
	}
}
