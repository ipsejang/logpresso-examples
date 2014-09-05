package com.logpresso.example.dashboard;

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
import org.araqne.webconsole.AppDashboardAsset;
import org.araqne.webconsole.AppManifest;
import org.araqne.webconsole.AppProgram;
import org.araqne.webconsole.AppProvider;
import org.araqne.webconsole.AppRegistry;
import org.osgi.framework.BundleContext;

@Component(name = "demo-asset-provider")
@Provides
public class DemoAssetProvider implements AppProvider {
	@Requires
	private AppRegistry appRegistry;

	@Requires
	private HttpService httpd;

	private BundleContext bc;

	public DemoAssetProvider(BundleContext bc) {
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
		m.setId("dashboard-asset-tutorial");
		m.setVersion("1.0");
		m.setDisplayNames(t("Logpresso Dashboard Asset Tutorial"));
		m.setDescriptions(t("Logpresso Dashboard Asset Tutorial"));
		
		AppDashboardAsset contentboxAsset = new AppDashboardAsset();
		contentboxAsset.setId("contentsbox");
		contentboxAsset.setDisplayNames(t("Example Widget"));
		contentboxAsset.setScriptFiles(Arrays.asList("app.js"));
		contentboxAsset.setPropertyFile("property.html");

		m.getDashboardAssets().add(contentboxAsset);
		
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
