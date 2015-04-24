package jsf.resourcehandler;

import jsf.resourcehandler.internal.JsfResourceHandlerActivator;
import jsf.resourcehandler.internal.JsfResourceHandlerService;
import org.apache.myfaces.application.ResourceHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Resource;

public class JsfOsgiClassloaderResourceHandler extends ResourceHandlerImpl{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        logger.error("============================ createResource ========================");
        JsfResourceHandlerService service = JsfResourceHandlerActivator.getInstance().getJsfResourceHandlerService();
        if(service != null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                for (ClassLoader cl : service.getClassloaders()) {
                    Thread.currentThread().setContextClassLoader(cl);
                    super.createResource(resourceName, libraryName);
                }
            }finally {
                Thread.currentThread().setContextClassLoader(loader);
            }
        }
        // fallback to current bundle
        return super.createResource(resourceName, libraryName);
    }

    @Override
    public boolean libraryExists(String libraryName) {
        logger.error("============================ libraryExists ========================");
        JsfResourceHandlerService service = JsfResourceHandlerActivator.getInstance().getJsfResourceHandlerService();
        if(service != null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                for (ClassLoader cl : service.getClassloaders()) {
                    Thread.currentThread().setContextClassLoader(cl);
                    super.libraryExists(libraryName);
                }
            }finally {
                Thread.currentThread().setContextClassLoader(loader);
            }
        }
        // fallback to current bundle
        return super.libraryExists(libraryName);
    }

}
