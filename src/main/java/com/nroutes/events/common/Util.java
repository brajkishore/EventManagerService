package com.nroutes.events.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.nroutes.events.model.AWSS3Config;

public class Util {	
	
	public static String upload(AWSS3Config awss3Config,String dataTempDir,String formattedFileName, MultipartFile part){
	
		String url="";
		try {
			
			AWSCredentials credentials = new BasicAWSCredentials(awss3Config.getAwsAccessKey(),awss3Config.getAwsSecretKey());
			TransferManager manager = new TransferManager(credentials);	
			if(manager.getAmazonS3Client().doesObjectExist(awss3Config.getAwsBucketName(), formattedFileName)){
				manager.getAmazonS3Client().copyObject(awss3Config.getAwsBucketName(), formattedFileName, awss3Config.getAwsBucketName(), formattedFileName+"_"+getTimeStamp());
				//manager.getAmazonS3Client().deleteObject(BucketName, formattedFileName);
			}
						
			File file=new File(dataTempDir+File.pathSeparator+formattedFileName);
			part.transferTo(file);
			Upload upload = manager.upload(awss3Config.getAwsBucketName(),formattedFileName,file);

			upload.waitForCompletion();
			if(upload.isDone()){
				manager.shutdownNow();
				url=awss3Config.getAwsURI()+awss3Config.getAwsBucketName()+"/"+formattedFileName;
			}			

			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return url;
	}


	private static String getTimeStamp(){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HH:mm:ss.SSSZ");
		return sdf.format(new Date());
	
	}

}
