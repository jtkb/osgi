package jsf.resourcehandler;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class JsfOsgiResource extends Resource {
    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getRequestPath() {
        return null;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return false;
    }
}
