package com.logpresso.example.app;

import java.util.HashMap;
import java.util.Map;

import org.araqne.dom.api.ProgramApi;
import org.araqne.dom.model.Program;
import org.araqne.dom.model.ProgramPack;
import org.araqne.dom.model.ProgramProfile;

public class DemoAppInstaller {
	
	public static void install(ProgramApi programApi) {
		createProgramPack(programApi);
	}
	
	private static void createProgramPack(ProgramApi programApi) {
		ProgramPack pack = programApi.findProgramPack("localhost", "app-tutorial");
		if (pack != null)
			return;

		pack = new ProgramPack();
		pack.setName("app-tutorial");
		pack.setDll("app-tutorial");
		programApi.createProgramPack("localhost", pack);

		ProgramProfile all = programApi.getProgramProfile("localhost", "all");
		ProgramProfile admin = programApi.getProgramProfile("localhost", "admin");
		ProgramProfile member = programApi.getProgramProfile("localhost", "member");

		Program consoleDemo = new Program();
		consoleDemo.setPack("app-tutorial");
		consoleDemo.setName("devconsole");
		Map<String, String> consoleDisplayNames = new HashMap<String, String>();
		consoleDisplayNames.put("en", "Developer Console");
		consoleDisplayNames.put("ko", "개발자 콘솔");
		consoleDemo.setDisplayNames(consoleDisplayNames);
		
		Program logdbDemo = new Program();
		logdbDemo.setPack("app-tutorial");
		logdbDemo.setName("logdb");
		Map<String, String> logdbDisplayNames = new HashMap<String, String>();
		logdbDisplayNames.put("en", "App LogDB Demo");
		logdbDisplayNames.put("ko", "앱 로그DB 예제");
		logdbDemo.setDisplayNames(logdbDisplayNames);
		
		Program crudDemo = new Program();
		crudDemo.setPack("app-tutorial");
		crudDemo.setName("crud");
		Map<String, String> crudDisplayNames = new HashMap<String, String>();
		crudDisplayNames.put("en", "App CRUD Demo");
		crudDisplayNames.put("ko", "앱 CRUD 예제");
		crudDemo.setDisplayNames(crudDisplayNames);

		programApi.createProgram("localhost", consoleDemo);
		programApi.createProgram("localhost", logdbDemo);
		programApi.createProgram("localhost", crudDemo);

		all.getPrograms().add(consoleDemo);
		all.getPrograms().add(logdbDemo);
		all.getPrograms().add(crudDemo);
		admin.getPrograms().add(consoleDemo);
		member.getPrograms().add(consoleDemo);

		programApi.updateProgramProfile("localhost", all);
		programApi.updateProgramProfile("localhost", admin);
		programApi.updateProgramProfile("localhost", member);
	}
}
