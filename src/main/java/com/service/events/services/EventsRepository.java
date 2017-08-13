package com.service.events.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import com.service.events.model.Event;


@Secured ({"ROLE_ADMIN"})
public interface EventsRepository extends CrudRepository<Event, String> {

	public void deleteById(String id);
	public Event getById(String id);
}
