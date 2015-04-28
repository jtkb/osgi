package jsf.resourcehandler.extender;

import jsf.resourcehandler.JsfResourcesRegistry;
import org.osgi.framework.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Arrays;

public class JsfResourceHandlerExtender implements BundleActivator, BundleListener {

    private static final String HEADER_JSF_RESOURCE = "Jsf-Resource";

    BundleContext context;
    JsfResourcesRegistry registry;

    ServiceTracker<JsfResourcesRegistry, JsfResourcesRegistry> tracker;

    @Override
    public void bundleChanged(BundleEvent event) {
        if(isJsfBundleForExtender(event.getBundle())) {
            switch (event.getType()) {
                case BundleEvent.STARTED:
                    registry.register(event.getBundle());
                    break;
                case BundleEvent.STOPPED:
                    registry.unregister(event.getBundle());
                    break;
            }
        }
    }


    private boolean isJsfBundleForExtender(Bundle bundle){
        if(bundle.getState() == Bundle.STARTING || bundle.getState() == Bundle.ACTIVE){
            return bundle.getHeaders().get(HEADER_JSF_RESOURCE) != null;
        }
        return false;
    }


    private void fullBundleScan(){
        Arrays.stream(context.getBundles())
                .filter(this::isJsfBundleForExtender)
                .forEach(b -> registry.register(b));
    }

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        tracker = new ServiceTracker<>(context, JsfResourcesRegistry.class.getName(), new ServiceTrackerCustomizer<JsfResourcesRegistry, JsfResourcesRegistry>() {
            @Override
            public JsfResourcesRegistry addingService(ServiceReference reference) {
                registry = (JsfResourcesRegistry)context.getService(reference);
                context.addBundleListener(JsfResourceHandlerExtender.this);
                fullBundleScan();
                return registry;
            }

            @Override
            public void modifiedService(ServiceReference reference, JsfResourcesRegistry service) {}

            @Override
            public void removedService(ServiceReference reference, JsfResourcesRegistry service) {
                context.removeBundleListener(JsfResourceHandlerExtender.this);
                registry = null;
            }
        });
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        tracker.close();
        context.removeBundleListener(this);
    }
}
