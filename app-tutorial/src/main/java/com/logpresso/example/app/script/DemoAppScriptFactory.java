package com.logpresso.example.app.script;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.araqne.api.Script;
import org.araqne.api.ScriptFactory;
import org.araqne.dom.api.ProgramApi;

@Component(name = "demo-app-script-factory")
@Provides
public class DemoAppScriptFactory implements ScriptFactory {
	@Requires
	private ProgramApi programApi;
	
	@ServiceProperty(name = "alias", value = "apptutorial")
	private String alias;

	@Override
	public Script createScript() {
		return new DemoAppScript(programApi);
	}

}
