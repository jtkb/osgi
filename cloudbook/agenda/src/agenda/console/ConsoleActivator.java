package agenda.console;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import agenda.api.Agenda;

public class ConsoleActivator extends DependencyActivatorBase {


	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "agenda");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String [] {"listConferences", "addConference"});
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(AgendaConsole.class)
				.add(createServiceDependency()
						.setService(Agenda.class)
						.setRequired(true)));
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		
	}

}
