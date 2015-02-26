package agenda.console;

import java.util.List;

import org.apache.felix.service.command.CommandProcessor;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import agenda.api.Agenda;
import agenda.api.Conference;

//@Component(provide=Object.class, properties={
//		CommandProcessor.COMMAND_SCOPE + ":String=agenda",
//		CommandProcessor.COMMAND_FUNCTION + ":String=listConferences|addConference"
//})
public class AgendaConsole {
	
	private volatile Agenda agenda;
	
	public void listConferences(){
		List<Conference> conferences = agenda.listConferences();
		for (Conference conference : conferences) {
			System.out.println(conference.getName());
		}
	}
	
	public void addConference(String name, String location){
		agenda.addConference(new Conference(name, location));
	}
	
//	@Reference
//	public void bindAgenda(Agenda agenda){
//		this.agenda = agenda;
//	}
}
