package com.expertworks.lms.util;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.services.cloudfront.util.SignerUtils;
@Configuration

public class CloudFrontSignatureHelper {
	

        private boolean secure = true;

	  
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

	  
	    private String allowedIpRange;
	    
	    @Autowired
	    ResourceLoader resourceLoader;

	    @Bean
	    public CookiesForCustomPolicy getSignedCookies() throws Exception {

	      System.out.println("privateKeyFilePath : "+ privateKeyFilePath);
	     
	    // String privateKeyFilePath = "files/pk-APKAIXDUZ5ZQ4EQM4P6Q.der";    
	        Resource resource = resourceLoader.getResource("classpath:" + privateKeyFilePath);
	        File privateKeyFile = resource.getFile();
	     
	             
			//File privateKeyFile = new File(privateKeyFilePath);

	  
	        Date activeFrom = getPlusMinutesFromCurrentTime(timeToActive); // (1)

	       
	        Date expiresOn = getPlusMinutesFromCurrentTime(timeToExpire);  // (2)

	       
	        CookiesForCustomPolicy cookies = null;
	        try {
	            cookies = CloudFrontCookieSigner.getCookiesForCustomPolicy(
	            		SignerUtils.Protocol.https, distributionDomain, privateKeyFile,
	                    resourcePath, keyPairId, expiresOn, activeFrom,
	                    allowedIpRange);
	        } catch (IOException e) {
	            throw e;
	        } catch (InvalidKeySpecException e) {
	            throw e;
	        }
	        return cookies;
	    }

	    private Date getPlusMinutesFromCurrentTime(Integer minutes) {
	        if (minutes == null) {
	            return null;
	        }
	        DateTime currentTime = new DateTime(DateTimeZone.UTC);
	        return currentTime.plusMinutes(minutes).toDate();
	    }

	    // omitted


	

}
