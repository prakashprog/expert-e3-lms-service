package com.expertworks.lms.controller;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.util.DateUtils;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.util.CloudFrontSignatureHelper;

@RestController
public class CookieController {

	public static final String SUCCESS = "success";


	@Value("${aws.cloudfront.distributiondomain}")
	private String distributionDomain;

	@Value("${aws.cloudfront.privatekeyfilepath}")
	private String privateKeyFilePath;

	@Value("${aws.cloudfront.resourcepath}")
	private String resourcePath;

	@Value("${aws.cloudfront.keypairid}")
	private String keyPairId;

	@Value("${aws.cloudfront.timetoactive}")
	private Integer timeToActive;

	@Value("${aws.cloudfront.timetoexpire}")
	private int timeToExpire;

	private String allowedIpRange =null;

	@Autowired 
	RSAPrivateKey privateKey;
	
	@Autowired
	ResourceLoader resourceLoader;

	@CrossOrigin
	@GetMapping("/set-cookie")
	public ApiResponse setCookie() throws Exception {

		System.out.println("Request recieved");
		System.out.println("timeToExpire : "+ timeToExpire);
		HashMap cookiesMap = new HashMap();
		
		//CloudFrontSignatureHelper cloudFrontSignatureHelper = new CloudFrontSignatureHelper();
		
		/*
		 * CookiesForCustomPolicy cookies =
		 * CloudFrontCookieSigner.getCookiesForCustomPolicy( url + resourcePath,
		 * privateKey, keyPairId, expiresOn, activeFrom, ipRange);
		 */
		Date activeFrom = getPlusMinutesFromCurrentTime(timeToActive); // (1)
		Date expiresOn = getPlusMinutesFromCurrentTime(timeToExpire); // (2)
	
		 CookiesForCustomPolicy cookies  = CloudFrontCookieSigner.getCookiesForCustomPolicy(SignerUtils.Protocol.https, distributionDomain,
				privateKey, resourcePath, keyPairId, expiresOn, null, allowedIpRange);  
		
		System.out.println("key pair id value :"+cookies.getKeyPairId().getValue());
		

		System.out.println(cookies.getPolicy().getKey() + "=" + cookies.getPolicy().getValue());
		System.out.println(cookies.getSignature().getKey() + "=" + cookies.getSignature().getValue());
		System.out.println(cookies.getKeyPairId().getKey() + "=" + cookies.getKeyPairId().getValue());

		cookiesMap.put(cookies.getPolicy().getKey(), cookies.getPolicy().getValue());
		cookiesMap.put(cookies.getSignature().getKey(), cookies.getSignature().getValue());
		cookiesMap.put(cookies.getKeyPairId().getKey(), cookies.getKeyPairId().getValue());

		// res.sendRedirect(url + "folder1/hls3.html");

		return new ApiResponse(HttpStatus.OK, SUCCESS, cookiesMap);

	}
	
	private Date getPlusMinutesFromCurrentTime(Integer minutes) {
		if (minutes == null) {
			return null;
		}
		
		
		DateTime currentTime = new DateTime(DateTimeZone.UTC);
		return currentTime.plusMinutes(minutes).toDate();  
	}

}
