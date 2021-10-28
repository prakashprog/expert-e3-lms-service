package com.expertworks.lms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
	
	@Autowired
	private APIContext apiContext;
	
	
	
	
	public Payment createPayment(
			Double total, 
			String currency, 
			String method,
			String intent,
			String description, 
			String cancelUrl, 
			String successUrl) throws PayPalRESTException{
		Amount amount = new Amount();
		amount.setCurrency(currency);
		total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", total));
		Details amountDetails = new Details();
		amountDetails.setShipping("1.2");
		amountDetails.setTax("1.3");
		//   $details->setShipping(1.2)->setTax(1.3)->setSubtotal(17.5);
	    // $amount = new Amount();
	    // $amount->setCurrency("USD")->setTotal(20)->setDetails($details);
		//amount.setDetails(new Details("ok"));

		Transaction transaction = new Transaction();
		transaction.setDescription(description);//userEmail
		transaction.setAmount(amount);
		
		ItemList itemList = new ItemList();
		Item item= new Item();	
		item.setName("Java Beginner");
		item.setCurrency(currency);
		item.setQuantity("1");
		item.setPrice(Double.toString(total));
		List<Item> myItemList = new ArrayList<Item>();
		myItemList.add(item);
		itemList.setItems(myItemList);
		transaction.setItemList(itemList); 
		transaction.setDescription("Payment");
		transaction.setInvoiceNumber("ExpertInvoiceNo1234");
		

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);
		

		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());

		//Below is the Main Object
		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);  
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		
		payment.setRedirectUrls(redirectUrls);

		return payment.create(apiContext);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecute);
	}

}
