package jsf.resourcehandler;


import javax.faces.application.Resource;

public interface JsfResourceHandler {

    Resource createResource(String resourceName, String libraryName);

}
