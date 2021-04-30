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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.util.DateUtils;
import com.expertworks.lms.http.ApiResponse;
import com.expertworks.lms.util.CloudFrontSignatureHelper;

@RestController
public class CookieController {

	public static final String SUCCESS = "success";

	@Autowired
	CookiesForCustomPolicy cookiesForCustomPolicy;
	
	@Autowired
	ResourceLoader resourceLoader;

	@CrossOrigin
	@GetMapping("/set-cookie")
	public ApiResponse setCookie(HttpServletResponse res) throws Exception {

		System.out.println("Request recieved");
		HashMap cookiesMap = new HashMap();

		/*
		 * CookiesForCustomPolicy cookies =
		 * CloudFrontCookieSigner.getCookiesForCustomPolicy( url + resourcePath,
		 * privateKey, keyPairId, expiresOn, activeFrom, ipRange);
		 */

		CookiesForCustomPolicy cookies = cookiesForCustomPolicy;

		System.out.println(cookies.getPolicy().getKey() + "=" + cookies.getPolicy().getValue());
		System.out.println(cookies.getSignature().getKey() + "=" + cookies.getSignature().getValue());
		System.out.println(cookies.getKeyPairId().getKey() + "=" + cookies.getKeyPairId().getValue());

		cookiesMap.put(cookies.getPolicy().getKey(), cookies.getPolicy().getValue());
		cookiesMap.put(cookies.getSignature().getKey(), cookies.getSignature().getValue());
		cookiesMap.put(cookies.getKeyPairId().getKey(), cookies.getKeyPairId().getValue());

		// res.sendRedirect(url + "folder1/hls3.html");

		return new ApiResponse(HttpStatus.OK, SUCCESS, cookiesMap);

	}

}
