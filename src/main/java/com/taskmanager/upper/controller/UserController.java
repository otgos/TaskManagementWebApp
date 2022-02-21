package com.taskmanager.upper.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/userIndex")
    public String userIndex(){
        return "/users/userIndex";
    }

    @GetMapping("/signIn")
    public String signIn(){
        return "/users/signIn";
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfile(){
        return "/users/profile";
    }

    @GetMapping("/adminPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String adminPanel(){
        return "/users/adminPanel";
    }
    @GetMapping("/accessError")
    public String accessError(){
        return "/users/accessDenied";
    }
}
