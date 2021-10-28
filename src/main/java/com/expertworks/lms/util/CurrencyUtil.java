package com.expertworks.lms.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.expertworks.lms.http.CurrencyDTO;
//https://howtodoinjava.com/java/date-time/location-based-currency-formatting-in-java/
public class CurrencyUtil {

	public static List<CurrencyDTO> getCurrencies(Double currencyAmount,Double actualCurrencyAmount) {

		ArrayList<CurrencyDTO> currencyDTOList = new ArrayList();
		
		CurrencyDTO currencyDTO = null;

		// This is the amount which we want to format
		// Double currencyAmount = new Double(1.0);

		// Using France locale
		
		// Get currency instance from locale; This will have all currency related
		// information
	
		// Currency Formatter specific to locale

		// Test the output
		

		Locale currentLocale = Locale.US;
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		Currency currentCurrency = Currency.getInstance(currentLocale);
		currencyDTO = new CurrencyDTO();
		System.out.println(currentLocale.getDisplayName());
		System.out.println(currentLocale.getCountry());
		System.out.println(currentCurrency.getDisplayName());
		System.out.println(currencyFormatter.format(currencyAmount));
		currencyDTO.setAmount(currencyFormatter.format(currencyAmount));
		currencyDTO.setActualAmount(currencyFormatter.format(actualCurrencyAmount));
		
		currencyDTO.setCountry(currentLocale.getCountry());
		currencyDTO.setName(currentCurrency.getDisplayName());
		currencyDTOList.add(currencyDTO);
		
		currentLocale = new Locale("en", "IN");
		currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		currentCurrency = Currency.getInstance(currentLocale);
		currencyDTO = new CurrencyDTO();
		currencyDTO.setAmount(currencyFormatter.format(currencyAmount*74.99));
		currencyDTO.setActualAmount(currencyFormatter.format(actualCurrencyAmount*74.99));
		currencyDTO.setCountry(currentLocale.getCountry());
		currencyDTO.setName(currentCurrency.getDisplayName());
		currencyDTOList.add(currencyDTO);
		
		
		currentLocale = Locale.UK;
		currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		currentCurrency = Currency.getInstance(currentLocale);
		currencyDTO = new CurrencyDTO();
		currencyDTO.setAmount(currencyFormatter.format(currencyAmount*0.73));
		currencyDTO.setActualAmount(currencyFormatter.format(actualCurrencyAmount*0.73));
		currencyDTO.setCountry(currentLocale.getCountry());
		currencyDTO.setName(currentCurrency.getDisplayName());
		currencyDTOList.add(currencyDTO);
		
		
		currentLocale = Locale.CANADA;
		currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
		currentCurrency = Currency.getInstance(currentLocale);
		currencyDTO = new CurrencyDTO();
		currencyDTO.setAmount(currencyFormatter.format(currencyAmount*1.24));
		currencyDTO.setActualAmount(currencyFormatter.format(actualCurrencyAmount*1.24));
		currencyDTO.setCountry(currentLocale.getCountry());
		currencyDTO.setName(currentCurrency.getDisplayName());
		currencyDTOList.add(currencyDTO);
		
		System.out.println("currencyDTOList : "+currencyDTOList.size());
		
		return currencyDTOList;

	}
}
