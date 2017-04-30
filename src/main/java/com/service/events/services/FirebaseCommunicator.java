package com.service.events.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;

public class FirebaseCommunicator {

	public FirebaseCommunicator(){
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream("google-services.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					  .setDatabaseUrl("https://nearme-c2a6f.firebaseio.com/")
					  .build();

					FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
