package jsf.resourcebundle;

import jsf.resourcehandler.JsfResourceHandler;
import org.osgi.service.component.annotations.Component;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.io.InputStream;

@Component
public class BundleResourceHandler implements JsfResourceHandler{

    @Override
    public Resource createResource(String resourceName, String libraryName) {
//        InputStream is =this.getClass().getResourceAsStream(libraryName + "/" + resourceName);
//        new ResourceImpl(
//                new ResourceMetaImpl(),
//                new ResourceLoaderWrapper() {
//                    @Override
//                    public ResourceLoader getWrapped() {
//                        return null;
//                    }
//                }
//        )
        return null;
    }
}
