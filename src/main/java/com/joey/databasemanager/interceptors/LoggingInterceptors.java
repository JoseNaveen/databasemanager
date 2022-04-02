package com.joey.databasemanager.interceptors;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoggingInterceptors implements HandlerInterceptor {

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//get all cookies
		//get session id
		String sessionId = null;
		
		if (request.getCookies()!=null) {
			for (Cookie cookie: request.getCookies()) {
				if ("JSESSIONID".equals(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
		}
		
		System.out.println("incoming request data log: session: " + sessionId +" at: "+ new Date() +" for " + request.getRequestURI());
		
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("in post handle");
	}
}
