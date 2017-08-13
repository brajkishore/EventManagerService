package com.nroutes.events.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpTask implements Callable<Event> {

	private String url;
	private Event event;
	private ResponseListener listener;
	private DefaultHttpClient httpClient;
	private String msg;
	
	
	public HttpTask(String url,Event event){
		this.url=url;
		this.event=event;
	}
	
	public HttpTask setResponseListener(ResponseListener listener){
		this.listener=listener;
		System.out.println("set :"+(this.listener));
		return this;
	}
	
	@Override
	public Event call() throws Exception {
		// TODO Auto-generated method stub
		
		
		try {

			httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);

			ObjectMapper mapper = new ObjectMapper();
			
			String jsonInString = mapper.writeValueAsString(event);
			StringEntity input = new StringEntity(jsonInString);
			input.setContentType("application/json");
			postRequest.setEntity(input);

			System.out.println("posting "+postRequest.getURI()+"::"+postRequest.getRequestLine());
			HttpResponse response = httpClient.execute(postRequest);
			System.out.println("response "+response+" "+response.getStatusLine());

			if (response.getStatusLine().getStatusCode() == 200) {
				
				
				BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

						String s="";
						StringBuilder sb=new StringBuilder();		
						while ((s= br.readLine()) != null) {
							sb.append(s);
							System.out.println("calling success handler "+event+" list1:"+listener+"read :"+s);
						}
						
											
						Map<String,Object> map=mapper.readValue(sb.toString(), Map.class);
						System.out.println("map :"+map);
						event.setFcmName(map.get("name").toString());
						System.out.println("calling success handler "+event+" list1:"+listener+"fcm later");
						if(listener!=null){
							
							listener.onSuccess(event);
						}
						return null;
				
			}

			System.out.println("Failed : HTTP error code : "
					+ response.getStatusLine().getStatusCode());
						
			

		  } catch (MalformedURLException e) {

			e.printStackTrace();
			msg=e.getMessage();

		  } catch (IOException e) {

			e.printStackTrace();
			msg=e.getMessage();

		  }finally{
			  httpClient.getConnectionManager().shutdown();
		  }
		
			if(listener!=null)
				listener.onSuccess(msg);
			
		return null;
	}	
}
