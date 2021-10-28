package com.expertworks.lms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.expertworks.lms.http.PayPalOrder;
import com.expertworks.lms.service.PaypalService;
import com.expertworks.lms.util.EnvUtil;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
//https://medium.com/oril/spring-boot-paypal-angular-2-9ca70d940e5f
//https://www.youtube.com/watch?v=tkN4EQt1P6I
//https://www.sourcecodeexamples.net/2021/01/spring-boot-paypal-payment-gateway.html
@RestController
public class LMSPaypalController {
	
	
	@Autowired
	private EnvUtil envUtil;

	@Autowired
	PaypalService service;
	

	public static String SUCCESS_REDIRECT_URL = "https://www.expert-works.com/";
	public static String CANCEL_REDIRECT_URL = "https://www.expert-works.com/cancel";

	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@CrossOrigin
	@PostMapping("/pay")
	public String payment(@RequestBody PayPalOrder order) {
		
		

		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9091/" + CANCEL_URL,
					"http://localhost:9091/" + SUCCESS_URL);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					System.out.println("link : " + link.toJSON());
					return "redirect:" + link.getHref();
				}
			}

		} catch (PayPalRESTException e) {
			
			

			e.printStackTrace();
		}
		return "redirect:/";
	}
	
	
	
	
	@CrossOrigin
	@PostMapping("/payredirect")
	public RedirectView paymentRedirect(@RequestBody PayPalOrder order) {
		
		RedirectView redirectView = new RedirectView();

		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9091/" + CANCEL_URL,
					"http://localhost:9091/" + SUCCESS_URL);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					System.out.println("link : " + link.toJSON());
					redirectView.setUrl(link.getHref());
					//return "redirect:" + link.getHref();
				}
			}

		} catch (PayPalRESTException e) {
			redirectView.setUrl("https://www.expert-works.com/error");
			
			e.printStackTrace();
		}
		return redirectView;
	}

	
//	@CrossOrigin
//	@PostMapping("/payurl")
//	public List<Links> paymenturl(@RequestBody PayPalOrder order) throws Exception {
//		
//		List<Links> links = new ArrayList<>();
//		
//		System.out.println("HostName : "+envUtil.getHostname());
//		final String baseUrl = 
//				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//		System.out.println("baseUrl : "+baseUrl);
//  
//		try {
//			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
//					order.getIntent(), order.getDescription(), "http://localhost:9091/" + CANCEL_URL,
//					"http://localhost:9091/" + SUCCESS_URL);
//			for (Links link : payment.getLinks()) {
//				System.out.println("link : " + link.toJSON());  
//				if (link.getRel().equals("approval_url")) {
//					links = payment.getLinks();
//					return payment.getLinks();
//				}
//			}
//
//		} catch (PayPalRESTException e) {
//
//			e.printStackTrace();
//			throw e;
//		}
//		return links;
//	}
	
	
	@CrossOrigin
	@PostMapping("/payurl")
	public Map paymenturl(@RequestBody PayPalOrder order) throws Exception {
				
		Map map = new HashMap();
		System.out.println("HostName : "+envUtil.getHostname());
		final String baseUrl = 
				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		System.out.println("baseUrl : "+baseUrl);
		String sucessUrl = baseUrl+"/" + SUCCESS_URL;
		String cancelUrl = baseUrl+"/" + CANCEL_URL;
		SUCCESS_REDIRECT_URL = order.getSuccessUrl();
		CANCEL_REDIRECT_URL = order.getCancelUrl();
  
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), cancelUrl,
					sucessUrl);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					System.out.println("approval_url link : " + link.toJSON());  
					map.put("approval_url", link.getHref());
					map.put("success_api", sucessUrl);
					map.put("cancel_api", cancelUrl);
					map.put("success_redirect", SUCCESS_REDIRECT_URL);
					map.put("cancel_redirect", CANCEL_REDIRECT_URL);
					//return link.getHref();
				}
			}

		} catch (PayPalRESTException e) {

			e.printStackTrace();
			throw e;
		}
		return map;
	}

	@CrossOrigin
	@GetMapping(value = CANCEL_URL)
	public RedirectView cancelPay(@RequestParam("token") String token) {
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(CANCEL_REDIRECT_URL);
		return redirectView;
		//return "Payment has been canceled";
	}

//	@CrossOrigin
//	@GetMapping(value = SUCCESS_URL)
//	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//		try {
//			Payment payment = service.executePayment(paymentId, payerId);
//			System.out.println("reponse after payment : "+payment.toJSON());
//			if (payment.getState().equals("approved")) {
//				return "success";
//			}
//		} catch (PayPalRESTException e) {
//			System.out.println(e.getMessage());
//		}
//		return "redirect:/";
//	}

	
	@CrossOrigin
	@GetMapping(value = SUCCESS_URL)
	public RedirectView successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(SUCCESS_REDIRECT_URL);
		try {
			Payment payment = service.executePayment(paymentId, payerId);
			System.out.println("reponse after payment : "+payment.toJSON());
			if (payment.getState().equals("approved")) {
				//return "success";
				return redirectView;
			}
		} catch (PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return redirectView;
	}
}
