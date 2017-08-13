package com.service.events.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.util.comparator.ComparableComparator;

import com.service.events.common.Constant;
import com.service.events.model.Event;

@Component("eventService")
public class EventService {

	@Autowired
	private EventsRepository eventsRepository;

	
	@Secured ({"ROLE_ADMIN"})
	public List<Event> findAll() {
		// TODO Auto-generated method stub
		List<Event> events=(List<Event>) eventsRepository.findAll();;
		Collections.sort(events,new Comparator<Event>(){
		final SimpleDateFormat sdf=new SimpleDateFormat(Constant.TIME_FORMAT);//01/05/2017 23:11:00
			@Override
			public int compare(Event o1, Event o2) {
				// TODO Auto-generated method stub
				Date d1=null;
				Date d2=null;
				try {
					d1 = sdf.parse(o1.getScheduleTime());
					d2=sdf.parse(o2.getScheduleTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(d1!=null && d2!=null)
					return d1.compareTo(d2);
				
				return 0;
			}
			
		});
		return events;
	}
	
	@Secured ({"ROLE_ADMIN"})
	public Event getById(String id) {
		// TODO Auto-generated method stub
		return eventsRepository.getById(id);
	}

	@Secured ({"ROLE_ADMIN"})
	public Event save(Event event) {
		// TODO Auto-generated method stub
		return eventsRepository.save(event);
	}

	
	@Secured ({"ROLE_ADMIN"})
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		eventsRepository.deleteById(id);
		
	}
	
	@Secured ({"ROLE_ADMIN"})
	public EventsRepository getEventsRepository(){
		return this.eventsRepository;
	}
	
	
}
