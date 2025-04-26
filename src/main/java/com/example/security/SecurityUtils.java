package com.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;
import jakarta.annotation.security.RolesAllowed;
import java.util.Arrays;

@Component
public class SecurityUtils {
    
    public boolean isUserLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && 
               auth.isAuthenticated() && 
               !(auth instanceof AnonymousAuthenticationToken);
    }
    
    public boolean hasAccess(Class<?> securedClass) {
        RolesAllowed rolesAllowed = securedClass.getAnnotation(RolesAllowed.class);
        if (rolesAllowed == null) {
            return true;
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> 
                Arrays.stream(rolesAllowed.value())
                    .anyMatch(role -> authority.equals("ROLE_" + role))
            );
    }
}