package agenda.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

import agenda.api.Agenda;
import agenda.api.Conference;

@Path("agenda")
public class AgendaResource {

	private volatile Agenda agendaService;
	
	@GET
	@Path("conferences")
	@Produces(MediaType.APPLICATION_JSON)
	@Description("Returns all Conferences as a list of type Conference")
	public List<Conference> listConferences(String conference) throws Exception {
		return agendaService.listConferences();
	}
	
	@POST
	@Path("conferences")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addConference(Conference conference) throws Exception {
		agendaService.addConference(conference);
	}
}
