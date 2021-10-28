package com.expertworks.lms.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class TestMain {
	
	
	public static void main(String[] args) {
		
		//This is the amount which we want to format
		  Double currencyAmount = new Double(1.0);
		   
		  //Using France locale
		  Locale currentLocale = Locale.US;
		   
		  //Get currency instance from locale; This will have all currency related information
		  Currency currentCurrency = Currency.getInstance(currentLocale);
		   
		  //Currency Formatter specific to locale
		  NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		 
		 
		  //Test the output
		  System.out.println(currentLocale.getDisplayName());
		  
		  System.out.println(currentLocale.getCountry());
		   
		  System.out.println(currentCurrency.getDisplayName());
		   
		  System.out.println(currencyFormatter.format(currencyAmount));
		
	}
	

}
