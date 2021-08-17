package com.expertworks.lms.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expertworks.lms.service.PayPalClient;

@RestController
@RequestMapping(value = "/paypal")
public class MyPayPalController {

    private final PayPalClient payPalClient;
    @Autowired
    MyPayPalController(PayPalClient payPalClient){
        this.payPalClient = payPalClient;
    }

    //@CrossOrigin()
    //@PostMapping(value = "/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
        return payPalClient.createPayment(sum);
    }

    //@CrossOrigin(origins = "http://localhost:4200")
    //@CrossOrigin()
    //@PostMapping(value = "/complete/payment")
    public Map<String, Object> completePayment(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
        return payPalClient.completePayment(request);
    }


}
