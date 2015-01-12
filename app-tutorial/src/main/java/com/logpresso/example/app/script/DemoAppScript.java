package com.logpresso.example.app.script;

import org.araqne.api.Script;
import org.araqne.api.ScriptContext;
import org.araqne.api.ScriptUsage;
import org.araqne.dom.api.ProgramApi;

import com.logpresso.example.app.DemoAppInstaller;

public class DemoAppScript implements Script {
	private ProgramApi programApi;
	
	public DemoAppScript(ProgramApi programApi) {
		this.programApi = programApi;
	}

	@Override
	public void setScriptContext(ScriptContext context) {
	}
	
	@ScriptUsage(description = "install")
	public void install(String[] args) throws InterruptedException {
		DemoAppInstaller.install(programApi);
	}


}
