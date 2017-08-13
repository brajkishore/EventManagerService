/**
 * 
 */
package com.nroutes.events.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * @author braj.kishore
 *
 */
public class AWSFileUploader {	
	
	private static final String AWSAccessKeyId="xxxxx";
	private static final String AWSSecretKey="xx";
	private static final String BucketName="xxxx";
	private static final String awsURI="xxxxxxx";
		
	public String upload(String dataTempDir,String formattedFileName, MultipartFile part){
		
		String url="";
		try {
			
			AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKeyId,AWSSecretKey);
			TransferManager manager = new TransferManager(credentials);	
			if(manager.getAmazonS3Client().doesObjectExist(BucketName, formattedFileName)){
				manager.getAmazonS3Client().copyObject(BucketName, formattedFileName, BucketName, formattedFileName+"_"+getTimeStamp());
				//manager.getAmazonS3Client().deleteObject(BucketName, formattedFileName);
			}
						
			File file=new File(dataTempDir+File.pathSeparator+formattedFileName);
			part.transferTo(file);
			Upload upload = manager.upload(BucketName,formattedFileName,file);

			upload.waitForCompletion();
			if(upload.isDone()){
				manager.shutdownNow();
				url=awsURI+BucketName+"/"+formattedFileName;
			}			

			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return url;
	}


	private String getTimeStamp(){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HH:mm:ss.SSSZ");
		return sdf.format(new Date());
	
	}
}
