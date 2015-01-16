package com.logpresso.example.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.httpd.BundleResourceServlet;
import org.araqne.httpd.HttpContext;
import org.araqne.httpd.HttpService;
import org.araqne.webconsole.AppManifest;
import org.araqne.webconsole.AppProgram;
import org.araqne.webconsole.AppProvider;
import org.araqne.webconsole.AppRegistry;
import org.osgi.framework.BundleContext;

@Component(name = "demo-app-provider")
@Provides
public class DemoAppProvider implements AppProvider {
	@Requires
	private AppRegistry appRegistry;

	@Requires
	private HttpService httpd;

	private BundleContext bc;

	public DemoAppProvider(BundleContext bc) {
		this.bc = bc;
	}

	@Validate
	public void start() {
		loadServlet();
		appRegistry.register(this);
	}

	@Invalidate
	public void stop() {
		if (appRegistry != null)
			appRegistry.unregister(this);

		unloadServlet();
	}

	@Override
	public AppManifest getManifest() {
		AppManifest m = new AppManifest();
		m.setId("app-tutorial");
		m.setVersion("1.0");
		m.setDisplayNames(t("App Tutorial"));
		m.setDescriptions(t("Logpresso App Tutorial"));

		AppProgram logdbDemo = new AppProgram();
		logdbDemo.setId("logdb");
		logdbDemo.setDisplayNames(t("App LogDB Demo"));
		logdbDemo.setScriptFiles(Arrays.asList("app.js"));
		logdbDemo.setHtmlFile("index.html");

		AppProgram crudDemo = new AppProgram();
		crudDemo.setId("crud");
		crudDemo.setDisplayNames(t("App CRUD Demo"));
		crudDemo.setScriptFiles(Arrays.asList("app.js"));
		crudDemo.setHtmlFile("index.html");

		AppProgram consoleDemo = new AppProgram();
		consoleDemo.setId("devconsole");
		consoleDemo.setDisplayNames(t("Developer Console"));
		consoleDemo.setScriptFiles(Arrays.asList("app.js"));
		consoleDemo.setHtmlFile("index.html");
		
		m.getPrograms().add(logdbDemo);
		m.getPrograms().add(crudDemo);
		m.getPrograms().add(consoleDemo);

		return m;
	}

	private Map<Locale, String> t(String text) {
		Map<Locale, String> m = new HashMap<Locale, String>();
		m.put(Locale.ENGLISH, text);
		m.put(Locale.KOREAN, text);
		return m;
	}

	private void loadServlet() {
		HttpContext ctx = httpd.ensureContext("webconsole");
		String appId = getManifest().getId();
		ctx.addServlet(appId, new BundleResourceServlet(bc.getBundle(), "/WEB-INF"), "/apps/" + appId + "/*");
	}

	private void unloadServlet() {
		if (httpd != null) {
			HttpContext ctx = httpd.ensureContext("webconsole");
			String appId = getManifest().getId();
			ctx.removeServlet(appId);
		}
	}

}
