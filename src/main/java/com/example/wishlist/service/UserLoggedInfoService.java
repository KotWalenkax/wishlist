package com.example.wishlist.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class UserLoggedInfoService {

    private String username;

    public String getUsername() {
        if (username == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authentication.getAuthorities();
            username = authentication.getName();
        }

        return username;
    }

    public void editUsername(String editedUsername) {
        username = editedUsername;
    }
}
