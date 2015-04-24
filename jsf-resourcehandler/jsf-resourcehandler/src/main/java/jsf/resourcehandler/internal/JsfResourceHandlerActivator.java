package jsf.resourcehandler.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class JsfResourceHandlerActivator implements BundleActivator{

    private static JsfResourceHandlerActivator INSTANCE;

    private ServiceTracker<JsfResourceHandlerService, JsfResourceHandlerService> tracker;

    @Override
    public void start(BundleContext context) throws Exception {
        INSTANCE = this;
        tracker = new ServiceTracker<JsfResourceHandlerService, JsfResourceHandlerService>(context, JsfResourceHandlerService.class.getName(), null);
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        tracker.close();
        INSTANCE = null;
    }


    public JsfResourceHandlerService getJsfResourceHandlerService(){
        return tracker.getService();
    }

    public static JsfResourceHandlerActivator getInstance(){
        return INSTANCE;
    }
}
