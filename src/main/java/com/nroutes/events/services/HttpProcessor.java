package com.nroutes.events.services;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nroutes.events.model.Event;

@Component("httpProcessor")
public class HttpProcessor {
	
	private ScheduledExecutorService eService=Executors.newScheduledThreadPool(2);
	

	
	public Future<Event> scheduleTask(Callable command,long delay,TimeUnit unit){
		return eService.schedule(command, delay, unit);
	}
	
}
