package jsf.resourcehandler.internal;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JsfOsgiResource extends Resource {

    private final URL bundleResourceUrl;

    public JsfOsgiResource(URL bundleResourceUrl, String resourceName, String libraryName){
        if(bundleResourceUrl == null){
            throw new IllegalArgumentException("URL for resource must not be null");
        }
        setResourceName(resourceName);
        setLibraryName(libraryName);
        this.bundleResourceUrl = bundleResourceUrl;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return bundleResourceUrl.openConnection().getInputStream();
    }

    @Override
    public String getRequestPath() {
        final FacesContext context = FacesContext.getCurrentInstance();

        StringBuilder sb = new StringBuilder(30)
                .append(ResourceHandler.RESOURCE_IDENTIFIER)
                .append('/')
                .append(getResourceName());
        if(getLibraryName() != null){
            sb.append("?ln=")
                    .append(getLibraryName());
        }


        return context
                .getApplication()
                .getViewHandler()
                .getResourceURL(
                        context,
                        sb.toString());
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return new HashMap<>(0);
    }

    @Override
    public URL getURL() {
        return bundleResourceUrl;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        // FIXME probably something more needed here
        return true;
    }
}
