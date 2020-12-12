package com.example.demo.provider;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.source.CustomWebAuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);
        String encodedSecurityPin = "";
        CustomWebAuthenticationDetails details = null;
        if(!(authentication.getDetails() instanceof String)) {
            details = (CustomWebAuthenticationDetails) authentication.getDetails();
            encodedSecurityPin = details.getSecurityPin();
        }
        else{
            encodedSecurityPin = authentication.getDetails().toString();
        }
        final CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        final String securityPin = customUserDetails.getSecurityPin();
        if(!getPasswordEncoder().matches(encodedSecurityPin, securityPin)){
            throw new BadCredentialsException("Security pin is not correct");
        }
        customUserDetails.setSecurityPin(null);
    }

    @Autowired
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Autowired
    @Override
    protected PasswordEncoder getPasswordEncoder() {
        return super.getPasswordEncoder();
    }
}
