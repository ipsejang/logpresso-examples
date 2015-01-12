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

		Program program = new Program();
		program.setPack("app-tutorial");
		program.setName("devconsole");
		Map<String, String> displayNames = new HashMap<String, String>();
		displayNames.put("en", "Developer Console");
		displayNames.put("ko", "개발자 콘솔");
		program.setDisplayNames(displayNames);

		programApi.createProgram("localhost", program);

		all.getPrograms().add(program);
		admin.getPrograms().add(program);
		member.getPrograms().add(program);

		programApi.updateProgramProfile("localhost", all);
		programApi.updateProgramProfile("localhost", admin);
		programApi.updateProgramProfile("localhost", member);
	}
}
