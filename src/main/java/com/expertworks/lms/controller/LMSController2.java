package com.expertworks.lms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;
import java.util.Date;

import org.jets3t.service.CloudFrontService;
import org.jets3t.service.utils.ServiceUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.cloudfront.CloudFrontCookieSigner;
import com.amazonaws.services.cloudfront.CloudFrontCookieSigner.CookiesForCustomPolicy;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.amazonaws.util.DateUtils;
import com.expertworks.lms.http.ApiResponse;

@RestController
@Component
public class LMSController2 {

	public static final String SUCCESS = "success";

	// get all courses
	@CrossOrigin
	@GetMapping("/hls/")
	public ApiResponse getCourses() {

		return new ApiResponse(HttpStatus.OK, SUCCESS, "ok");
	}

	String privateKey = "-----BEGIN RSA PRIVATE KEY----- " + '\n'
			+ "MIIEpQIBAAKCAQEA5i8NjBlxTEOCo9GpHSZg4ItQGAzWIReFqGK/q97CmNDOIHNY" + '\n'
			+ "jQmrEsbd0lJuVY/Y/1aQkT0HeE40+DoXLt8lPqJCrMCrZ3YAy03lDL5vZqVoBqha" + '\n'
			+ "m/7p6lwxwAhf8Cyms6jnIB1RvA0duVLwCzIg2doB71mf8965My8SSMNqfrRaeT9e" + '\n'
			+ "pxdEObTDPEt/Tsljt69LLVDMKRTu3vkir/Yhqles+5OnaSRbZA4o7Qktzrbol+Ut" + '\n'
			+ "vTR/fQtnFVThSZD3lL4qjqicYr5l3+ZpgvqYni7gL4sGaXZ/7VbRIp6brxnqGdXx" + '\n'
			+ "V7YBwiGi0bNYg5+OWGk8dGMZHljmnJAOo28BzwIDAQABAoIBAQCDHA/6Wc+X4eg6" + '\n'
			+ "iURrTx3lKFTxrfYVmUFPPRfxvItY4a5W+3xXsUcZBd75WmxwHeQ65EKqB3oGw30W" + '\n'
			+ "Wng0AXREbcXOglvfLW6cXnz1uk7Xx6UuDZy2uAbir6rCt8J1melF8hHbz/drRolE" + '\n'
			+ "swH5To85pxxtCb66+ITCq7Adc/lnuxw3qMEGerJJ7PeqAQ1y5++8BH3Xv5UnHkif" + '\n'
			+ "HoloIOBAP+bqmacnDpq2OaB4yn/uFLiCadQv7tsFCUDD99zPYBpkcF5CTThdQL5H" + '\n'
			+ "89rUCBUx1Y8ImH1LhircF/UCgQr2WZehSaRseyXDydtuJejsp24MN0FUa4YXBmqu" + '\n'
			+ "O/HovPPJAoGBAPNXS3sVsZjWxKBvJKRd2fF5rDaeiX7Jij9x2r5fwTlvA9ineNXm" + '\n'
			+ "/9DKsDSfA+7735OB6/G8doRH076IJ4U6CXqeVBydQtFAaZBKoPk+ojh0VzHvWOMU" + '\n'
			+ "KebJp4pKS73FyE+z+4Qp/8clip9EO/TMRtX1m7/95xBNc25uFTJK9HZlAoGBAPIo" + '\n'
			+ "iVieHtsiUFzMlaKhgsqicCnkISfXfa80OyTBmAk7NbcOLyWi/nJ3mKBFhhEciDcT" + '\n'
			+ "F0ZfrR/yVH9VcDue+HJkCICaZQAROhkw1DVn88ZlMxySyEzZ36vBxK39+vOkOmOe" + '\n'
			+ "Ep9WMLlI/MN8Vvp4bCviB3IcDMSKnNwQH1VsuWojAoGAHhn/WVN+q6RtoFb/VmHR" + '\n'
			+ "swBVIFCOO6PcuSpxX7lioqMr+6ID3Zn8rSxVjbzZ9LctmgapVDb3BjgIZnpbQSzD" + '\n'
			+ "vWpWh0+g01e/dzz0GsD3ujoSCzhLa5Oz9zucwQudwPT1aX6fL2Muo8juE/OR4x9d" + '\n'
			+ "59vBD4fuDGM4U0HE13rUMM0CgYEArE37rHqWKgZ4eA96SYXysoxyktwoW1kRrSDV" + '\n'
			+ "FIpLzAW9astE8NrRglFm4GOXyYUrx0RmQ0TAwwKC7Te17DZpQbu78QXq5+laFaKQ" + '\n'
			+ "3I9i/b/0Zlhq/YPpiyUM9WDKt6IbidZoGcgSU0SD1fUbTo1xKqhh8+fRjeezRUKT" + '\n'
			+ "iQ0bwVECgYEAukJMagVgXCW0zClrqdzpAs/oKmGl6YGmNQ8KqTBI+fN/kvQOof6u" + '\n'
			+ "3zLEIgo40s0vcP4KFPPmCbIhJlEbp/pp+N3IihsjecT6SqzPJ+fHWTxNuXLq9ahg" + '\n'
			+ "Geg49B1Zwpc0ktX3fJcK3l9gvBaDtOtqMsQrYCD5NYP2gk0DDEYTGUs=" + '\n' + "-----END RSA PRIVATE KEY-----";

	// Signed URLs for a private distribution
	// Note that Java only supports SSL certificates in DER format,
	// so you will need to convert your PEM-formatted file to DER format.
	// To do this, you can use openssl:
	// openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der
	// -outform DER
	// So the encoder works correctly, you should also add the bouncy castle jar
	// to your project and then add the provider.

	public static void main(String[] args) throws Exception {

		LMSController2 lmsController = new LMSController2();

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		String distributionDomain = "d3s24np0er9fug.cloudfront.net";
		String privateKeyFilePath = "C:\\expertworks\\cloudfrontkeys\\pk-APKAIXDUZ5ZQ4EQM4P6Q.der";
		//String s3ObjectKey = "hls3.html";
		// String s3ObjectKey = "sample-mp4-file.mp4";

		//String s3ObjectKey = "folder1/sample-mp4-file.m3u8";
		String s3ObjectKey = "folder1/sample-mp4-file_1.m3u8";
		

		// String s3ObjectKey = "folder1/sample-mp4-file_1_00001.ts";
		// String policyResourcePath = "https://" + distributionDomain + "/" +
		// s3ObjectKey;

		// Convert your DER file into a byte array.

		byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(new FileInputStream(privateKeyFilePath));

		// Generate a "canned" signed URL to allow access to a
		// specific distribution and file

		String signedUrlCanned = CloudFrontService.signUrlCanned("https://" + distributionDomain + "/" + s3ObjectKey, // Resource
																														// URL
																														// or
																														// Path
				"APKAIXDUZ5ZQ4EQM4P6Q", // Certificate identifier,
				// an active trusted signer for the distribution
				derPrivateKey, // DER Private key data
				ServiceUtils.parseIso8601Date("2021-04-30T22:20:00.000Z") // DateLessThan
		);
		System.out.println(signedUrlCanned);

		// Build a policy document to define custom restrictions for a signed URL.
//------------------------------CannedPolicy Start-----------------------------------------------------------
//		CloudFrontCookieSigner.CookiesForCannedPolicy cookies = CloudFrontCookieSigner.getCookiesForCannedPolicy(
//                SignerUtils.Protocol.https, distributionDomain, new File(privateKeyFilePath), s3ObjectKey,
//                "APKAIXDUZ5ZQ4EQM4P6Q",  ServiceUtils.parseIso8601Date("2021-04-30T22:20:00.000Z"));

//		 
//        System.out.println( cookies.getExpires().getKey() + "=" +
//                cookies.getExpires().getValue());
//        System.out.println( cookies.getSignature().getKey() + "=" +
//                cookies.getSignature().getValue());
//        System.out.println( cookies.getKeyPairId().getKey() + "=" +
//        		cookies.getKeyPairId().getValue());

// ------------------------------CannedPolicy		// End-----------------------------------------------------------

		Date activeFrom = DateUtils.parseISO8601Date("2021-04-20T22:20:00.000Z");
		Date expiresOn = DateUtils.parseISO8601Date("2021-05-10T22:20:00.000Z");
		String ipRange = null;
		//https://aws.amazon.com/blogs/developer/serving-private-content-through-amazon-cloudfront-using-signed-cookies/#:~:text=Canned%20policies%20allow%20you%20to,Custom%20Policies%20for%20Signed%20Cookies.
		//for wild card serches  s3ObjectKey
		CookiesForCustomPolicy cookies = CloudFrontCookieSigner.getCookiesForCustomPolicy(SignerUtils.Protocol.https,
				distributionDomain, new File(privateKeyFilePath), "folder1/*", "APKAIXDUZ5ZQ4EQM4P6Q", expiresOn,
				activeFrom, ipRange);

		// https://a1010100z.tistory.com/142
		/*
		 * httpGet.addHeader("Cookie", cookiesForCannedPolicy.getExpires().getKey() +
		 * "=" + cookies.getExpires().getValue()); httpGet.addHeader("Cookie",
		 * cookiesForCannedPolicy.getSignature().getKey() + "=" +
		 * cookies.getSignature().getValue()); httpGet.addHeader("Cookie",
		 * cookiesForCannedPolicy.getKeyPairId().getKey() + "=" +
		 * cookies.getKeyPairId().getValue());
		 */

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		//headers.set("User-Agent", "eltabo");
		
		//------------------------------Canned-----------------------------------------------------

//		headers.add("Cookie", cookies.getExpires().getKey() + "=" + cookies.getExpires().getValue());
//		headers.add("Cookie", cookies.getSignature().getKey() + "=" + cookies.getSignature().getValue());
//		headers.add("Cookie", cookies.getKeyPairId().getKey() + "=" + cookies.getKeyPairId().getValue());
//		String url = "https://" + distributionDomain + "/" + s3ObjectKey;
		
		
		//------------------------------Custom policy-----------------------------------------------------
		//headers.add("Cookie", "Secure");
		headers.add("Cookie", cookies.getPolicy().getKey() + "=" + cookies.getPolicy().getValue());
		headers.add("Cookie", cookies.getSignature().getKey() + "=" + cookies.getSignature().getValue());
		headers.add("Cookie", cookies.getKeyPairId().getKey() + "=" + cookies.getKeyPairId().getValue());
		//String url = "https://" + distributionDomain + "/" + s3ObjectKey;
		System.out.println("Printing outpot=======================");
		System.out.println(cookies.getPolicy().getKey()+"="+cookies.getPolicy().getValue());
		System.out.println(cookies.getSignature().getKey()+"="+cookies.getSignature().getValue());
		System.out.println(cookies.getKeyPairId().getKey()+"="+cookies.getKeyPairId().getValue());
		
		
		//String url = "https://" + distributionDomain + "/" + "folder1/sample-mp4-file_1_00001.ts"; 
		//String url = "https://" + distributionDomain + "/" + "folder1/sample-mp4-file.m3u8"; 
		String url = "https://" + distributionDomain + "/" + "folder1/hls3.html"; 

		ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(headers), String.class);
		System.out.println("======================================");

		HttpHeaders resheaders = response.getHeaders();
		System.out.println(response.getBody());

		System.out.println("======================================" + url);
		

//      //  https://d3s24np0er9fug.cloudfront.net/folder1/sample-mp4-file_1_00001.ts
//         restTemplate = new RestTemplate();
//         headers = new HttpHeaders();
//        headers.add("Cookie", cookies.getExpires().getKey() + "=" +
//                cookies.getExpires().getValue());
//        headers.add("Cookie", cookies.getSignature().getKey() + "=" +
//                cookies.getSignature().getValue());
//        headers.add("Cookie", cookies.getKeyPairId().getKey() + "=" +
//                cookies.getKeyPairId().getValue());
//         url =   "https://d3s24np0er9fug.cloudfront.net/folder1/sample-mp4-file_1_00001.ts";
//       
//       response = restTemplate.exchange(url,
//                HttpMethod.GET,
//                new HttpEntity (headers) ,String.class);
//      System.out.println("======================================");
//         
//        System.out.println(response.getBody());
//        
//        System.out.println("======================================"+url);

	}

}
 