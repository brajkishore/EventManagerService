package com.service.events.services;

import org.springframework.data.repository.CrudRepository;

import com.service.events.model.Event;

public interface EventsRepository extends CrudRepository<Event, String> {

	public void deleteById(String id);
}
