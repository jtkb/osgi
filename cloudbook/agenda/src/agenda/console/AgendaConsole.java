package agenda.console;

import java.util.List;

import agenda.api.Agenda;
import agenda.api.Conference;

//@Component(provide=Object.class, properties={
//		CommandProcessor.COMMAND_SCOPE + ":String=agenda",
//		CommandProcessor.COMMAND_FUNCTION + ":String=listConferences|addConference"
//})
public class AgendaConsole {

	private volatile Agenda agenda;

	public void listConferences() {
		List<Conference> conferences = agenda.listConferences();
		if (conferences != null) {
			for (Conference conference : conferences) {
				System.out.println(conference.getName());
			}
		} else {
			System.out.println("Agenda not available");
		}
	}

	public void addConference(String name, String location) {
		agenda.addConference(new Conference(name, location));
	}

	// @Reference
	// public void bindAgenda(Agenda agenda){
	// this.agenda = agenda;
	// }
}
