package com.expertworks.lms.controller;

import java.io.FileInputStream;
import java.security.Security;

import org.jets3t.service.CloudFrontService;
import org.jets3t.service.utils.ServiceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;







@RestController
@Component
public class LMSController2 {
	
	
	// Signed URLs for a private distribution
	// Note that Java only supports SSL certificates in DER format, 
	// so you will need to convert your PEM-formatted file to DER format. 
	// To do this, you can use openssl:
	// openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der 
    //	    -outform DER 
	// So the encoder works correctly, you should also add the bouncy castle jar
	// to your project and then add the provider.
	
	
	public static void main(String[] args) throws Exception {
		
		LMSController2 lmsController= new LMSController2();
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		String distributionDomain = "d3s24np0er9fug.cloudfront.net";
		String privateKeyFilePath = "C:\\expertworks\\cloudfrontkeys\\pk-APKAIXDUZ5ZQ4EQM4P6Q.der";
		//String s3ObjectKey = "hls3.html";
		//String s3ObjectKey = "sample-mp4-file.mp4";
		
		String s3ObjectKey = "folder1/sample-mp4-file.m3u8";
		//String policyResourcePath = "https://" + distributionDomain + "/" + s3ObjectKey;

		// Convert your DER file into a byte array.

		byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(new
		    FileInputStream(privateKeyFilePath));

		// Generate a "canned" signed URL to allow access to a 
		// specific distribution and file

		String signedUrlCanned = CloudFrontService.signUrlCanned(
		    "https://" + distributionDomain + "/" + s3ObjectKey, // Resource URL or Path
		    "APKAIXDUZ5ZQ4EQM4P6Q",     // Certificate identifier, 
		                   // an active trusted signer for the distribution
		    derPrivateKey, // DER Private key data
		    ServiceUtils.parseIso8601Date("2021-02-18T22:20:00.000Z") // DateLessThan
		    );
		System.out.println(signedUrlCanned);

		// Build a policy document to define custom restrictions for a signed URL.

		
		
		
	}


  
}
