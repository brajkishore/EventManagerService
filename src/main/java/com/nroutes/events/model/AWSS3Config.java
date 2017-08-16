/**
 * 
 */
package com.nroutes.events.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author brakisho
 *
 */

@Component("awss3Config")
public class AWSS3Config {

	@Value("${awss3.accessKey}")
	private String awsAccessKey;
	
	@Value("${awss3.secretKey}")
	private String awsSecretKey;
	
	@Value("${awss3.bucketName}")
	private String awsBucketName;
	
	@Value("${awss3.uri}")
	private String awsURI;
	
	

	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	public void setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
	}

	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}

	public String getAwsBucketName() {
		return awsBucketName;
	}

	public void setAwsBucketName(String awsBucketName) {
		this.awsBucketName = awsBucketName;
	}

	public String getAwsURI() {
		return awsURI;
	}

	public void setAwsURI(String awsURI) {
		this.awsURI = awsURI;
	}

	@Override
	public String toString() {
		return "AWSS3Config [awsAccessKey=" + awsAccessKey + ", awsSecretKey=" + awsSecretKey + ", awsBucketName="
				+ awsBucketName + ", awsURI=" + awsURI + "]";
	}
	
	
	
	
	
}
