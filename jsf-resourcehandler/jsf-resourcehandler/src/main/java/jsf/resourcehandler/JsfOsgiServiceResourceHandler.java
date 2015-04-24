package jsf.resourcehandler;

import jsf.resourcehandler.internal.JsfResourceHandlerActivator;
import jsf.resourcehandler.internal.JsfResourceHandlerService;
import org.apache.myfaces.application.ResourceHandlerImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

public class JsfOsgiServiceResourceHandler extends ResourceHandlerImpl{


    @Override
    public Resource createResource(String resourceName, String libraryName) {
        JsfResourceHandlerService service = JsfResourceHandlerActivator.getInstance().getJsfResourceHandlerService();
        if(service != null) {
            Resource resourceFromOtherBundle = service.createResource(resourceName, libraryName);
            if (resourceFromOtherBundle != null) {
                return resourceFromOtherBundle;
            }
        }
        // fallback to current bundle
        return super.createResource(resourceName, libraryName);
    }

    @Override
    public boolean libraryExists(String libraryName) {
        if(super.libraryExists(libraryName)){
            return true;
        }else {
            JsfResourceHandlerService service = JsfResourceHandlerActivator.getInstance().getJsfResourceHandlerService();
            return service != null ? service.libraryExists(libraryName) : false;
        }
    }

}
