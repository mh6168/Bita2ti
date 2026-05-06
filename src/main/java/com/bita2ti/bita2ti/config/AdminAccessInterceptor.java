package com.bita2ti.bita2ti.config;

import com.bita2ti.bita2ti.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAccessInterceptor implements HandlerInterceptor {

    private static final String SESSION_USER_KEY = "user";
    private static final String ADMIN_EMAIL = "admin@admin.com";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Object userObj = session.getAttribute(SESSION_USER_KEY);
        if (!(userObj instanceof User user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (!ADMIN_EMAIL.equalsIgnoreCase(user.getEmail())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}

