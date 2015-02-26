package agenda.service.simple;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import agenda.api.Agenda;
import agenda.api.Conference;

public class SimpleAgendaService implements Agenda {

	private List<Conference> conferences = new CopyOnWriteArrayList<Conference>();
	
	@Override
	public List<Conference> listConferences() {
		return conferences;
	}

	@Override
	public void addConference(Conference conference) {
		conferences.add(conference);
	}
	
	public void start(){
		conferences.add(new Conference("Devoxx", "Antwerp"));
		conferences.add(new Conference("Jfokus", "Stockholm"));
		
		System.out.println("Added " + conferences.size() + " conferences.");
	}

}
