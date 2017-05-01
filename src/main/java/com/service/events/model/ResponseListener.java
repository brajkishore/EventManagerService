package com.service.events.model;

public interface ResponseListener {

	public void onSuccess(Object success);
	public void onError(Object error);
	
}
