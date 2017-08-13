
package com.nroutes.events.services;

import com.nroutes.events.model.Event;
import com.nroutes.events.model.EventStatus;
import com.nroutes.events.model.ResponseListener;

public class HttpResponseListener implements ResponseListener {

	private EventsRepository repository;
	
	public HttpResponseListener(EventsRepository repository){
		this.repository=repository;
	}
	
	@Override
	public void onSuccess(Object success) {
		// TODO Auto-generated method stub
		
		System.out.println("onSuccess:"+success);
		Event event=(Event)success;		
		event.setStatus(EventStatus.RUNNING);
		
		repository.save(event);
		
	}

	@Override
	public void onError(Object error) {
		// TODO Auto-generated method stub
		System.out.println("onSuccess:"+error);
		Event event=(Event)error;		
		event.setStatus(EventStatus.ERROR);
		repository.save(event);
	}

}
