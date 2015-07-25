package jsf.itest;

import org.hamcrest.CustomTypeSafeMatcher;
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
import org.osgi.framework.Bundle;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.OptionUtils.combine;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class MyfacesJsfResourceHandlerTest extends BaseTest {

	@Before
	public void setUp() throws Exception {
		httpTestClient = new HttpTestClient("admin", "admin", "src/main/resources/keystore");
		initWebListener();

		String bundlePath = "reference:file:../jsf-application-myfaces/target/jsf-application-myfaces-1.0-SNAPSHOT.jar";
		installWarBundle = installAndStartBundle(bundlePath);

		waitForWebListener();
	}

	@Configuration
	public Option[] config() {
		return combine(combine(configureBase(), configureJetty()),
				// MyFaces
				mavenBundle("org.apache.myfaces.core", "myfaces-api").version("2.2.7"),
				mavenBundle("org.apache.myfaces.core", "myfaces-impl").version("2.2.7"),
				// mavenBundle("javax.el", "javax.el-api").version("2.2.1"),
				mavenBundle("javax.annotation", "javax.annotation-api").version("1.2"),
				mavenBundle("javax.interceptor", "javax.interceptor-api").version("1.2"),
				mavenBundle("javax.enterprise", "cdi-api").version("1.2"),
				mavenBundle("javax.validation", "validation-api").version("1.1.0.Final"),
				mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject")
						.version("1_2"),
				// mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version(VERSION_PAX_WEB),
				// Following lines are necessary because pax-web-jetty-bundle
				// doesnt work with jsf
				mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version(VERSION_PAX_WEB),
				mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version(VERSION_PAX_WEB),
				mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-runtime").version(VERSION_PAX_WEB),
				mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty").version(VERSION_PAX_WEB));
	}

	/**
	 * Does multiple assertions in one test since container-startup is quite
	 * long
	 * 
	 * <pre>
	 * <ul>
	 * 	<li>Check if jsf-resourcehandler-extender is started</li>
	 * 	<li>Check if application under test (jsf-application-myfaces) is started
	 * 	<li>Test actual resource-handler
	 * 		<ul>
	 * 			<li>Test for occurence of 'Hello JSF' (jsf-application-myfaces)</li>
	 * 			<li>Test for occurence of 'Standard Header' (jsf-resource-bundle-one)</li>
	 * 			<li>Test for occurence of 'iceland.jpg' (jsf-resourcebundle-one)</li>
	 * 			<li>Test for occurence of 'Customized Footer' (jsf-resourcebundle-two)</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 * </pre>
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJsfResourceHandler() throws Exception {
		assertThat(Arrays.asList(bundleContext.getBundles()),
				hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
					@Override
					protected boolean matchesSafely(Bundle item) {
						return "jsf-resourcehandler-extender".equals(item.getSymbolicName())
								&& item.getState() == Bundle.ACTIVE;
					}
				}));
		assertThat(Arrays.asList(bundleContext.getBundles()),
				hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
					@Override
					protected boolean matchesSafely(Bundle item) {
						return "jsf-application-myfaces".equals(item.getSymbolicName())
								&& item.getState() == Bundle.ACTIVE;
					}
				}));
		String response = httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-resourcehandler-myfaces/index.xhtml",
				"Hello JSF");
		assertThat("Standard header shall be loaded from resourcebundle-one", response,
				containsString("Standard Header"));
		assertThat("Images shall be loaded from resourcebundle-one", response, containsString("iceland.jpg"));
		assertThat("Customized footer shall be loaded from resourcebundle-two", response,
				containsString("Customized Foote"));
	}

}
