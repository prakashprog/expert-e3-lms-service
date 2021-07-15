package com.expertworks.lms.aspect;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.expertworks.lms.model.Audit;
import com.expertworks.lms.repository.AuditRepository;
import com.expertworks.lms.util.TokenUtil;

@Aspect
@Component
public class AuditAspect {
	

    private final static Logger logger = LoggerFactory.getLogger(AuditAspect.class);
	
    @Autowired
    TokenUtil tokenUtil;
    
    @Autowired
    AuditRepository auditRepository;
	
	@Around("@annotation(LogExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
	    long start = System.currentTimeMillis();
	    String token = null;
	    
	    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        Enumeration<String> headerNames = request.getHeaderNames();
        logger.info("==============================================================");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("Header Name: " + headerName + " Header Value : " + headerValue);
        }
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
        
        }
                 //This is the information to record the http request.
        logger.info("==============================================================");
        logger.info("tokenUtil.getUser() : "+tokenUtil.getUserId());
        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            logger.info(paramName + " = " + request.getParameter(paramName));
        }
        logger.info("==============================================================");
       
      
        //IP
        logger.info("IP = {}", request.getRemoteAddr());
        //URL
        logger.info("URL = {}", request.getRequestURL());
        //METHOD
        logger.info("METHOD = {}", request.getMethod());
        //CLASS_METHOD
        logger.info("CLASS_METHOD = {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //ARGS
        //System.out.println("ARGS = {}", joinPoint.getArgs());
        Object[] values = joinPoint.getArgs();
        for(Object obj :  values) {
        	System.out.println("****-- "+values.length);
        System.out.println("****-- "+obj.toString());
        }
        logger.info("==============================================================");
        Audit audit = new Audit();
        audit.setSk(request.getRequestURL().toString());
        audit.setMethod(request.getMethod());
        audit.setUrl(request.getRequestURL().toString());
        audit.setUser(tokenUtil.getUserId());
        if(joinPoint.getArgs().length>0)
        audit.setArgs(((Object[])joinPoint.getArgs())[0].toString());
        
        audit = auditRepository.save(audit);

	    Object proceed = joinPoint.proceed();

	    long executionTime = System.currentTimeMillis() - start;

	    logger.info(joinPoint.getSignature() + " executed in " + executionTime + "ms ;" +proceed.toString());
	    
	    audit.setPayload(proceed.toString());
	    audit.setTimetaken(String.valueOf(executionTime));
	   auditRepository.update(audit.getId(), audit);
	    
	    return proceed;
	}

	
	public String extractPostRequestBody(HttpServletRequest request) {
	    if ("POST".equalsIgnoreCase(request.getMethod())) {
	        Scanner s = null;
	        try {
	            s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return s.hasNext() ? s.next() : "";
	    }
	    return "";
	}
	
	public String httpServletRequestToString(HttpServletRequest request) throws Exception {

	    ServletInputStream mServletInputStream = request.getInputStream();
	    byte[] httpInData = new byte[request.getContentLength()];
	    int retVal = -1;
	    StringBuilder stringBuilder = new StringBuilder();

	    while ((retVal = mServletInputStream.read(httpInData)) != -1) {
	        for (int i = 0; i < retVal; i++) {
	            stringBuilder.append(Character.toString((char) httpInData[i]));
	        }
	    }

	    return stringBuilder.toString();
	}
}