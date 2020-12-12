package com.example.demo.source;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

    private String securityPin;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String securityPin = request.getParameter("securityPin");
        this.securityPin = securityPin;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }
}
