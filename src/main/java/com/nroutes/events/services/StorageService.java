package com.nroutes.events.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
	String store(MultipartFile file);
}
