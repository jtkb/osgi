package jsf.resourcehandler.internal;

import jsf.resourcehandler.JsfResourceHandler;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component(service = ResourceHandler.class)
public class JsfResourceHandlerService extends ResourceHandler{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<ServiceReference, JsfResourceHandler> resourceHandlerServices = new ConcurrentHashMap<ServiceReference, JsfResourceHandler>();

    @Reference(unbind = "serviceRemoved")
    void serviceAdded(ServiceReference<JsfResourceHandler> ref, JsfResourceHandler handler){
        resourceHandlerServices.put(ref, handler);
    }

    void serviceRemoved(ServiceReference<JsfResourceHandler> ref, JsfResourceHandler handler){
        resourceHandlerServices.remove(ref);
    }

    @Override
    public Resource createResource(String resourceName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        JsfResourceHandler handler = getHandlerByLibrary(libraryName);
        if(handler != null) {
            return handler.createResource(resourceName, libraryName);
        }
        return null;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRendererTypeForResourceName(String resourceName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleResourceRequest(FacesContext context) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isResourceRequest(FacesContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean libraryExists(String libraryName) {
        return getHandlerByLibrary(libraryName) != null;
    }

    private JsfResourceHandler getHandlerByLibrary(String libraryName){
        Optional<ServiceReference> optional = resourceHandlerServices.keySet().stream().filter(f -> libraryName.equals(f.getProperty("libraryName"))).findAny();
        if(optional.isPresent()) {
            return resourceHandlerServices.get(optional.get());
        }
        return null;
    }

    public Collection<ClassLoader> getClassloaders(){
        logger.error("============================ Classloaders ========================");
        Collection<ClassLoader> result = resourceHandlerServices.values()
                .stream()
                .map(t -> t.getClass().getClassLoader())
                .collect(Collectors.toList());
        logger.error("============================ Classloaders={} ========================", result.size());
        return result;
    }
}
