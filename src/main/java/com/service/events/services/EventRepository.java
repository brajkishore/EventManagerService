package com.service.events.services;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.Repository;

import com.service.events.model.Event;

public class EventRepository implements Repository<Event,String>  {
	
	MongoTemplate mongoTemplate;

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public List<Event> getAll(){
		return mongoTemplate.findAll(Event.class);
	}
	
	public List<Event> saveNFetch(Event event){
		mongoTemplate.save(event);
		return getAll();
	}
	
	public void deleteById(String id){
		mongoTemplate
		.remove(new Query(Criteria.where("id").is(id)), Event.class);
	}
	
	

}
