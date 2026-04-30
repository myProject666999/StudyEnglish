package com.english.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String requestURI = request.getRequestURI();
        
        if (requestURI.startsWith("/admin")) {
            Object admin = session.getAttribute("admin");
            if (admin == null) {
                response.sendRedirect("/admin/login");
                return false;
            }
        } else {
            Object user = session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("/login");
                return false;
            }
        }
        
        return true;
    }
}
