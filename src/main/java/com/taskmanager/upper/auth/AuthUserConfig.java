package com.taskmanager.upper.auth;

import com.taskmanager.upper.entity.AuthUsers;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUserConfig {
    protected AuthUsers getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            AuthUsers authUser = (AuthUsers) authentication.getPrincipal();
            return authUser;
        }
        return null;
    }
}
