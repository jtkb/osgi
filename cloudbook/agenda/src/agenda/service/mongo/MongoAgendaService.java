package agenda.service.mongo;

import java.util.ArrayList;
import java.util.List;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.DBCollection;

import agenda.api.Agenda;
import agenda.api.Conference;

public class MongoAgendaService implements Agenda {

	private volatile MongoDBService mongoDbService;
	
	@Override
	public List<Conference> listConferences() {
		DBCollection coll = mongoDbService.getDB().getCollection("conferences");
		JacksonDBCollection<Conference, Object> conferences = JacksonDBCollection.wrap(coll, Conference.class);
		DBCursor<Conference> cursor = conferences.find();
		
		List<Conference> result = new ArrayList<Conference>();
		while(cursor.hasNext()){
			result.add(cursor.next());
		}
		return result;
	}

	@Override
	public void addConference(Conference conference) {
		DBCollection coll = mongoDbService.getDB().getCollection("conferences");
		JacksonDBCollection<Conference, Object> conferences = JacksonDBCollection.wrap(coll, Conference.class);
		conferences.save(conference);
	}
	
}
