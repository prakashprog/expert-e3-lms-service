package com.expertworks.lms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.util.DateUtils;

@Configuration

public class CloudFrontSignatureHelper {

	

	
	@Value("${aws.cloudfront.privatekeyfilepath}")
	private String privateKeyFilePath;

	



	@Autowired
	ResourceLoader resourceLoader;

	@Bean
	public RSAPrivateKey getSignedCookies() throws Exception {

		System.out.println("privateKeyFilePath : " + privateKeyFilePath);
		

		// String privateKeyFilePath = "files/pk-APKAIXDUZ5ZQ4EQM4P6Q.der";
		// Resource resource = resourceLoader.getResource("classpath:" +
		// privateKeyFilePath);

		// File privateKeyFile = resource.getFile();

//	      ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext();
//	      Resource resource  =  appContext.getResource("classpath:files/pk-APKAIXDUZ5ZQ4EQM4P6Q.der");
//	      InputStream ips=    resource.getInputStream();
//	       String fileContent = this.asString(resource);
//	       System.out.println("fileContent +++++++++++++++"+fileContent);  
//	       File privateKeyFile = resource.getFile();

		// -----------------------------------------------------------------
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext();
		Resource resource = appContext.getResource("classpath:"+privateKeyFilePath);
		//Resource resource  = new UrlResource("file:///pk-APKAIXDUZ5ZQ4EQM4P6Q.der");
		//Resource resource = resourceLoader.getResource("classpath:" +
		// privateKeyFilePath);
		
		InputStream inputStream = resource.getInputStream();
		
		//File file = resource.getFile();
		
		 File tempFile = File.createTempFile("stream2file", "tmp");
		 FileOutputStream out = new FileOutputStream(tempFile);
	            IOUtils.copy(inputStream, out);
	
	            
		
		System.out.println("getAbsolutePath : "+tempFile.getAbsolutePath());
		byte[] privateKeyByteArray = Files.readAllBytes(tempFile.toPath());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByteArray);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
		// ------------------------------------------------------------------

		//Date activeFrom = getPlusMinutesFromCurrentTime(timeToActive); // (1)
		//Date expiresOn = getPlusMinutesFromCurrentTime(timeToExpire); // (2)
		//Date activeFrom = DateUtils.parseISO8601Date("2021-04-20T22:20:00.000Z");
		//Date expiresOn = DateUtils.parseISO8601Date("2021-05-20T22:20:00.000Z");
      
		
		System.out.println(Instant.now().toString());
		System.out.println(Instant.now().plusSeconds(60).toString());  
		Date activeFrom = DateUtils.parseISO8601Date(Instant.now().toString());
		Date expiresOn = DateUtils.parseISO8601Date(Instant.now().plusSeconds(120).toString());
		Date now = new Date();
		
		return privateKey;
	}

	private Date getPlusMinutesFromCurrentTime(Integer minutes) {
		if (minutes == null) {
			return null;
		}
		
		
		DateTime currentTime = new DateTime(DateTimeZone.UTC);
		return currentTime.plusMinutes(minutes).toDate();  
	}

	public String asString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.US_ASCII)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	// omitted
	public Date addHoursToJavaUtilDate(Date date, int hours) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.MILLISECOND, hours);
	    return calendar.getTime();
	}

}
