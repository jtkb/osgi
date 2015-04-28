package jsf.resourcehandler;


import org.osgi.framework.Bundle;

import java.util.Collection;

public interface JsfResourcesRegistry {

    void register(Bundle bundle);

    void unregister(Bundle bundle);

    Collection<ClassLoader> getBundleClassloaders();
}
