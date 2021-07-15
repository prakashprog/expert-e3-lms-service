package com.expertworks.lms.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.expertworks.lms.aspect.EnableLogging;

@Aspect
@Component
public class Aspects {

@AfterReturning(pointcut = "execution(@com.expertworks.lms.aspect.EnableLogging * *(..)) && @annotation(enableLogging) && args(reqArg, reqArg1,..)", returning = "result")
public void auditInfo(JoinPoint joinPoint, Object result, EnableLogging enableLogging, Object reqArg, String reqArg1) {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();

    if (result instanceof HttpServletResponse) {
    	HttpServletResponse responseObj = (HttpServletResponse) result;

    String requestUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath() + request.getRequestURI()
                + "?" + request.getQueryString();
    }

//String clientIp = request.getRemoteAddr();
//String clientRequest = reqArg.toString();
//int httpResponseStatus = responseObj.getStatus();
//responseObj.getEntity();
// Can log whatever stuff from here in a single spot.
}

@AfterThrowing(pointcut = "execution(@com.expertworks.lms.aspect.EnableLogging * *(..)) && @annotation(enableLogging) && args(reqArg, reqArg1,..)", throwing="exception")
public void auditExceptionInfo(JoinPoint joinPoint, Throwable exception, EnableLogging enableLogging, Object reqArg, String reqArg1) {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();

    String requestUrl = request.getScheme() + "://" + request.getServerName()
    + ":" + request.getServerPort() + request.getContextPath() + request.getRequestURI()
    + "?" + request.getQueryString();

    exception.getMessage();
    exception.getCause();
    exception.printStackTrace();
    exception.getLocalizedMessage();
    // Can log whatever exceptions, requests, etc from here in a single spot.
    }
}