package jsf.itest;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.web.itest.base.HttpTestClient;
import org.ops4j.pax.web.itest.base.WaitCondition;
import org.ops4j.pax.web.itest.base.WebListenerImpl;
import org.ops4j.pax.web.service.spi.WebListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import javax.inject.Inject;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.ops4j.pax.exam.CoreOptions.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JsfResourceHandlerTest {

    protected static final String VERSION_PAX_WEB = "5.0.0-SNAPSHOT";
    protected static final String VERSION_PAX_URL = "2.4.1";
    protected static final String VERSION_XBEAN = "4.1";
    protected static final String VERSION_JETTY = "9.2.10.v20150310";


    @Inject
    protected BundleContext bundleContext;

    protected Bundle installWarBundle;

    protected HttpTestClient httpTestClient;
    protected WebListener webListener;

    @Before
    public void setUp() throws Exception {
        httpTestClient = new HttpTestClient("admin", "admin", "src/main/resources/keystore");
        initWebListener();

        String bundlePath = "reference:file:../jsf-application/target/jsf-application-1.0-SNAPSHOT.jar";
        installWarBundle = installAndStartBundle(bundlePath);

        waitForWebListener();
    }

    @After
    public void tearDown() throws Exception {
        if(httpTestClient != null) {
            httpTestClient.close();
            httpTestClient = null;
        }
        if (installWarBundle != null) {
            installWarBundle.stop();
            installWarBundle.uninstall();
        }
    }


    public Option[] configureBase() {
        return options(
                workingDirectory("target/paxexam"),
                cleanCaches(true),
                // Home
                //systemProperty("org.ops4j.pax.url.mvn.localRepository").value("/home/marc/.m2/repository"),
                // Work
                //systemProperty("org.ops4j.pax.url.mvn.localRepository").value("C:/Development/temp/maven-local-repository"),
                //repository("https://uenexus1.nbg.sdv.spb.de/nexus/content/groups/repo/").id("central"),

                // Framework
                mavenBundle("org.apache.felix", "org.apache.felix.log").version("1.0.1"),
                mavenBundle("org.apache.felix", "org.apache.felix.eventadmin").version("1.4.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin").version("1.8.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.scr").version("1.8.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.metatype").version("1.0.10"),
                // Commons
                mavenBundle("commons-io", "commons-io").version("1.4"),
                mavenBundle("commons-codec", "commons-codec").version("1.10"),
                mavenBundle("commons-beanutils", "commons-beanutils").version("1.8.3"),
                mavenBundle("commons-collections", "commons-collections").version("3.2.1"),
                mavenBundle("commons-digester", "commons-digester").version("1.8.1"),
                // Pax-Web
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web.itest").artifactId("pax-web-itest-base").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").version("4.4.2"),
                // HttpClient for URL-Tests
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.4.1")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.2")),
                // Others
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").version(VERSION_XBEAN),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").version(VERSION_XBEAN),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder-shaded").version(VERSION_XBEAN),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").version(VERSION_XBEAN),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-asm5-shaded").version(VERSION_XBEAN),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                //
                bundle("reference:file:C:/Development/git/osgi/jsf-resourcehandler/jsf-resourcebundle/target/jsf-resourcebundle-1.0-SNAPSHOT.jar"),
                bundle("reference:file:C:/Development/git/osgi/jsf-resourcehandler/jsf-resourcehandler/target/jsf-resourcehandler-1.0-SNAPSHOT.jar"),
                bundle("reference:file:C:/Development/git/osgi/jsf-resourcehandler/jsf-resourcehandler-extender/target/jsf-resourcehandler-extender-1.0-SNAPSHOT.jar"),
                junitBundles(),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                frameworkProperty("felix.log.level").value("2"),
                frameworkProperty("osgi.console").value("6666"),
                frameworkProperty("osgi.console.enable.builtin").value("true"),
                frameworkProperty("felix.bootdelegation.implicit").value("false")
        );
    }

    @Configuration
    public Option[] config() {
        return OptionUtils.combine(
                configureBase(),
                // MyFaces
                mavenBundle("org.apache.myfaces.core", "myfaces-api").version("2.2.7"),
                mavenBundle("org.apache.myfaces.core", "myfaces-impl").version("2.2.7"),
                mavenBundle("javax.annotation", "javax.annotation-api").version("1.2"),
                mavenBundle("javax.interceptor", "javax.interceptor-api").version("1.2"),
                mavenBundle("javax.enterprise", "cdi-api").version("1.2"),
                mavenBundle("javax.validation", "validation-api").version("1.1.0.Final"),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject").version("1_2"),
                // mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version(VERSION_PAX_WEB),
                // Following lines are necessary because pax-web-jetty-bundle doesnt work with jsf
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-runtime").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty").version(VERSION_PAX_WEB),
                // Jetty
                mavenBundle().groupId("javax.servlet").artifactId("javax.servlet-api").version("3.1.0"),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-util").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-io").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-http").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-continuation").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-server").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-client").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-security").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-xml").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-servlet").version(VERSION_JETTY));
    }


    @Test
    public void testJsfBundleActive() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "jsf-application".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }


    @Test
    public void testExtenderBundleActive() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "jsf-resourcehandler-extender".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }

    @Test
    public void testJsf() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-resourcehandler/index.xhtml", "Hello JSF");
    }


    @Test
    public void testJsfResourceHandler() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-resourcehandler/index.xhtml", "Hello Template");
    }


    protected Bundle installAndStartBundle(String bundlePath)
            throws BundleException, InterruptedException {
        final Bundle bundle = bundleContext.installBundle(bundlePath);
        bundle.start();
        new WaitCondition("bundle startup") {
            @Override
            protected boolean isFulfilled() {
                return bundle.getState() == Bundle.ACTIVE;
            }
        }.waitForCondition();
        return bundle;
    }

    protected void initWebListener() {
        webListener = new WebListenerImpl();
        bundleContext.registerService(WebListener.class, webListener, null);
    }

    protected void waitForWebListener() throws InterruptedException {
        new WaitCondition("webapp startup") {
            @Override
            protected boolean isFulfilled() {
                return ((WebListenerImpl) webListener).gotEvent();
            }
        }.waitForCondition();
    }

}
