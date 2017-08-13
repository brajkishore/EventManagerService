package com.service.events.services;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.events.model.AWSFileUploader;


@Service("storageService")
public class CDNStorageService implements StorageService {

	
	@Resource(name = "eventService")
	private EventService eventService;
	
	@Value("${data.temp.dir}")
	private String dataTempDir;
	
	@Override
	public String store(MultipartFile file) {
		// TODO Auto-generated method stub		
		return upload2S3(UUID.randomUUID().toString()+"_"+file.getOriginalFilename(), file);
	}
	
	public String upload2S3(String fileName, MultipartFile file) {
		// TODO Auto-generated method stub
		
			String url="";
			if(file==null ||file.isEmpty())
				return url;
					
		    try {
		    	    
		    	AWSFileUploader awsFileUploader=new AWSFileUploader();
		    	System.out.println("Temp:"+dataTempDir);
		    	url=awsFileUploader.upload(dataTempDir,fileName,file);
		    	
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		
		return url;
	}


}
