package com.dinhhuan.note.config;

import com.dinhhuan.note.service.UmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class NoteSecurityConfig {
    @Autowired
    private UmsUserService umsUserService;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> umsUserService.loadUserByUsername(username);
    }
}
