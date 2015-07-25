package jsf.resourcehandler;

import jsf.resourcehandler.internal.JsfResourceHandlerActivator;
import jsf.resourcehandler.internal.SimpleJsfResourcesRegistry;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

public class JsfOsgiResourceHandler extends ResourceHandlerWrapper{

    private final ResourceHandler wrapped;


    public JsfOsgiResourceHandler(ResourceHandler wrapped){
        this.wrapped = wrapped;
    }

    @Override
    public ResourceHandler getWrapped() {
        return wrapped;
    }

    @Override
    public ViewResource createViewResource(FacesContext context, String resourceName) {
        // check standard first
        ViewResource  resource = super.createViewResource(context, resourceName);
        if(resource == null){
            // lookup resource in jsf-resource bundles
            SimpleJsfResourcesRegistry service = (SimpleJsfResourcesRegistry)JsfResourceHandlerActivator.getInstance().getJsfResourcesRegistry();
            resource = service.createResource(resourceName);
            System.out.println("=================== VIEW-RESOURCE OSGI: " + resource);
        }
        return resource;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        // check standard first
        Resource resource = super.createResource(resourceName, libraryName);
        if(resource == null) {
            // lookup resource in jsf-resource bundles
            SimpleJsfResourcesRegistry service = (SimpleJsfResourcesRegistry)JsfResourceHandlerActivator.getInstance().getJsfResourcesRegistry();
            resource = service.createResource(resourceName, libraryName);
            System.out.println("=================== RESOURCE OSGI: " + resource);
        }
        return resource;
    }

}
