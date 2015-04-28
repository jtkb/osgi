package jsf.resourcehandler.internal;

import jsf.resourcehandler.JsfResourcesRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class JsfResourceHandlerActivator implements BundleActivator{

    private static JsfResourceHandlerActivator INSTANCE;

    private ServiceTracker<JsfResourcesRegistry, JsfResourcesRegistry> tracker;

    @Override
    public void start(BundleContext context) throws Exception {
        INSTANCE = this;
        tracker = new ServiceTracker<JsfResourcesRegistry, JsfResourcesRegistry>(context, JsfResourcesRegistry.class.getName(), null);
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        tracker.close();
        INSTANCE = null;
    }


    public JsfResourcesRegistry getJsfResourcesRegistry(){
        return tracker.getService();
    }

    public static JsfResourceHandlerActivator getInstance(){
        return INSTANCE;
    }
}
