package com.example.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import jakarta.annotation.security.RolesAllowed;

@Component
public class VaadinRouteSecurity implements BeforeEnterObserver {
    
    private final SecurityUtils securityUtils;
    
    public VaadinRouteSecurity(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Class<?> target = event.getNavigationTarget();
        
        if (target.getAnnotation(AnonymousAllowed.class) == null && 
            !securityUtils.isUserLoggedIn()) {
            event.rerouteTo("login");
            return;
        }
        
        RolesAllowed rolesAllowed = target.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null && !securityUtils.hasAccess(target)) {
            event.rerouteToError(AccessDeniedException.class, "Access denied");
        }
    }
}