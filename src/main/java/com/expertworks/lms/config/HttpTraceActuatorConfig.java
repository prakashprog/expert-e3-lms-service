package com.expertworks.lms.config;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
//import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class HttpTraceActuatorConfig {
//	  @Bean
//	    public HttpTraceRepository httpTraceRepository() {
//	        return new InMemoryHttpTraceRepository() {
//	        	
//	        };
//	    }
//	  
	  @Bean
	    public CommonsRequestLoggingFilter requestLoggingFilter() {
	         System.out.println("inside logging filter");
	         CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter() ;
	        		
	        loggingFilter.setIncludeClientInfo(true);
	        loggingFilter.setIncludeQueryString(true);
	        loggingFilter.setIncludePayload(true);
	        loggingFilter.setIncludeHeaders(true);
	        loggingFilter.setMaxPayloadLength(64000);
	        return loggingFilter;
	    }
	
}
