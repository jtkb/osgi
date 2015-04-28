package jsf.resourcehandler.internal;

import jsf.resourcehandler.JsfResourcesRegistry;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import javax.faces.application.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SimpleJsfResourcesRegistry implements JsfResourcesRegistry {
    private static final String JSF_DEFAULT_RESOURCE_FOLDER = "/META-INF/resources/";

    private LogService log;
    private Set<Bundle> activated = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void register(Bundle bundle) {
        synchronized (activated) {
            if (activated.contains(bundle)) return;
            log.log(LogService.LOG_INFO, String.format("Registering JSF-Resource-Bundle: %s", bundle.getSymbolicName()));
            activated.add(bundle);
        }
    }

    @Override
    public void unregister(Bundle bundle) {
        synchronized (activated) {
            if (!activated.contains(bundle)) return;
            log.log(LogService.LOG_INFO, String.format("De-Registering JSF-Resource-Bundle: %s", bundle.getSymbolicName()));
            activated.remove(bundle);
        }
    }

    @Override
    public Collection<ClassLoader> getBundleClassloaders() {
        return activated.stream().map(b -> b.getClass().getClassLoader()).collect(Collectors.toList());
    }

    @Deactivate
    void deactivate() {
        activated.forEach(this::unregister);
    }


    @Reference
    void bindLogService(LogService log) {
        this.log = log;
    }


    /**
     * Searches all registered bundles for a given resource. The first match will be created as
     * javax.faces.application.Resource.
     * In order to conform to the Servlet 3.0 resources, the lookup will start at META-INF/resources/
     * @param resourceName name or path of the resource to create
     * @return Resource for given resource
     */
    public Resource createResource(String resourceName) {
        if(resourceName == null){
            log.log(LogService.LOG_ERROR,
                    "Method called with invalid argument!",
                    new IllegalArgumentException("resourceName for lookup must not be null!"));
        }
        if (resourceName.charAt(0) == '/')
        {
            // If resourceName starts with '/', remove that character because it
            // does not have any meaning (with and without should point to the
            // same resource).
            resourceName = resourceName.substring(1);
        }
        final String lookupString = JSF_DEFAULT_RESOURCE_FOLDER + resourceName;

        System.out.println("=================== SEARCHING: " + lookupString + " in " + activated.size() + " bundles.");
        Optional<Bundle> searchResult = activated.stream().filter(b -> b.getResource(lookupString) != null).findFirst();
        if(searchResult.isPresent()){
            return new JsfOsgiResource(searchResult.get().getResource(lookupString), resourceName, null);
        }
        return null;
    }

}
