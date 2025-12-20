package com.hrapp.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {

    
    private static final String RH_HOME_URL = "/homeRH";
    private static final String DRH_HOME_URL = "/homeDRH";
    private static final String ADMIN_HOME_URL = "/homeADMIN";
    
    
    private static final String DEFAULT_HOME_URL = "/home"; 

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication) throws IOException, ServletException {
        
        String targetUrl = DEFAULT_HOME_URL;

        
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();

            if (role.contains("ROLE_RH")) {
                targetUrl = RH_HOME_URL;
                break;
            } else if (role.contains("ROLE_DRH")) {
                targetUrl = DRH_HOME_URL;
                break;
            }
            else if(role.contains("ROLE_ADMIN")){
                targetUrl = ADMIN_HOME_URL;
                break;
            }
        }
        
        response.sendRedirect(targetUrl);
    }
}
