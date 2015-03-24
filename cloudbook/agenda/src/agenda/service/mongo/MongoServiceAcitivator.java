package agenda.service.mongo;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import agenda.api.Agenda;

public class MongoServiceAcitivator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
				.setInterface(Agenda.class.getName(), null)
				.setImplementation(MongoAgendaService.class)
				.add(createServiceDependency()
						.setService(MongoDBService.class)
						.setRequired(true)));
	}
	
	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {

	}

}
