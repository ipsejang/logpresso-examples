package com.logpresso.example.dashboard.asset;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.httpd.BundleResourceServlet;
import org.araqne.httpd.HttpContext;
import org.araqne.httpd.HttpService;
import org.araqne.webconsole.AppProvider;
import org.araqne.webconsole.AppRegistry;
import org.json.JSONConverter;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@Component(name = "demo-asset-provider")
@Provides
public class DemoAssetProvider implements AppProvider {
	private static final String MANIFEST = "/WEB-INF/manifest.json";

	@Requires
	private AppRegistry appRegistry;

	@Requires
	private HttpService httpd;

	private BundleContext bc;

	private String appId;
	private Map<String, Object> manifest;

	public DemoAssetProvider(BundleContext bc) {
		this.bc = bc;
	}

	@Override
	public String getId() {
		String id = (String) manifest.get("id");
		return id;
	}

	@Validate
	public void start() {
		loadManifest();
		loadServlet();
		appRegistry.register(this);
	}

	@Invalidate
	public void stop() {
		if (appRegistry != null)
			appRegistry.unregister(this);

		unloadServlet();
	}

	private void loadManifest() {
		InputStream is = null;
		try {
			is = DemoAssetProvider.class.getResourceAsStream(MANIFEST);
			JSONTokener tokenizer = new JSONTokener(new InputStreamReader(is, Charset.forName("utf-8")));
			JSONObject json = (JSONObject) tokenizer.nextValue();

			this.manifest = JSONConverter.parse(json);
			this.appId = (String) manifest.get("id");

		} catch (Throwable t) {
			throw new IllegalStateException("cannot load demo app manifest", t);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void loadServlet() {
		HttpContext ctx = httpd.ensureContext("webconsole");
		Bundle bundle = bc.getBundle();
		ctx.addServlet(appId, new BundleResourceServlet(bundle, "/WEB-INF"), "/apps/" + appId + "/*");
	}

	private void unloadServlet() {
		if (httpd != null) {
			HttpContext ctx = httpd.ensureContext("webconsole");
			ctx.removeServlet(appId);
		}
	}

	@Override
	public Map<String, Object> getManifest() {
		return manifest;
	}

}
