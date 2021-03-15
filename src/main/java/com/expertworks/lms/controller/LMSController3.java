package com.expertworks.lms.controller;

import java.io.File;
//import java.net.http.HttpClient;
import java.util.Date;

import org.apache.http.client.methods.HttpGet;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.http.HttpResponse;
import com.amazonaws.protocol.Protocol;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCannedPolicy;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.util.DateUtils;







@RestController
@Component
public class LMSController3 {
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		
//		 Protocol protocol = Protocol.https;
//		 String distributionDomain = "d1b2c3a4g5h6.cloudfront.net";
//		 File privateKeyFile = new File("/path/to/cfcurlCloud/rsa-private-key.der");
//		 String resourcePath = "a/b/images.jpeg";
//		 String keyPairId = "APKAJCEOKRHC3XIVU5NA";
//		 Date activeFrom = DateUtils.parseISO8601Date("2012-11-14T22:20:00.000Z");
//		 Date expiresOn = DateUtils.parseISO8601Date("2011-11-14T22:20:00.000Z");
//		 String ipRange = "192.168.0.1/24";
//
//		 CookiesForCannedPolicy cookies = CloudFrontCookieSigner.getCookiesForCannedPolicy(
//		              protocol, distributionDomain, privateKeyFile, s3ObjectKey,
//		              keyPairId, expiresOn);
//
//		 HttpClient client = new DefaultHttpClient();
//		 HttpGet httpGet = new HttpGet(
//		              SignerUtils.generateResourcePath(protocol, distributionDomain,
//		              resourcePath));
//
//		 httpGet.addHeader("Cookie", cookies.getExpires().getKey() + "=" +
//		     cookies.getExpires().getValue());
//		 httpGet.addHeader("Cookie", cookies.getSignature().getKey() + "=" +
//		     cookies.getSignature().getValue());
//		 httpGet.addHeader("Cookie", cookies.getKeyPairId().getKey() + "=" +
//		     cookies.getKeyPairId().getValue());
//
//		 HttpResponse response = client.execute(httpGet);
//
//		 CookiesForCustomPolicy cookies2 = CloudFrontCookieSigner.getCookiesForCustomPolicy(
//		              protocol, distributionDomain, privateKeyFile, s3ObjectKey,
//		              keyPairId, expiresOn, activeFrom, ipRange);
//		
		
		
	}


  
}
