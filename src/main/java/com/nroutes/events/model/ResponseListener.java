package com.nroutes.events.model;

public interface ResponseListener {

	public void onSuccess(Object success);
	public void onError(Object error);
	
}
