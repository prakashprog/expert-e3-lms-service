package com.expertworks.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.http.PayPalOrder;
import com.expertworks.lms.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
//https://medium.com/oril/spring-boot-paypal-angular-2-9ca70d940e5f
@RestController
public class LMSPaypalController {

	@Autowired
	PaypalService service;

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
					order.getIntent(), order.getDescription(), "http://localhost:9090/" + CANCEL_URL,
					"http://localhost:9090/" + SUCCESS_URL);
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
	@PostMapping("/payurl")
	public String paymenturl(@RequestBody PayPalOrder order) {
  
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:9091/" + CANCEL_URL,
					"http://localhost:9091/" + SUCCESS_URL);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					System.out.println("link : " + link.toJSON());  
					return link.getHref();
				}
			}

		} catch (PayPalRESTException e) {

			e.printStackTrace();
		}
		return "No url generated";
	}

	@CrossOrigin
	@GetMapping(value = CANCEL_URL)
	public String cancelPay() {
		return "cancel";
	}

	@CrossOrigin
	@GetMapping(value = SUCCESS_URL)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = service.executePayment(paymentId, payerId);
			System.out.println(payment.toJSON());
			if (payment.getState().equals("approved")) {
				return "success";
			}
		} catch (PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/";
	}

}
