package com.indrajagadeesh.webgradle.config;

import com.indrajagadeesh.webgradle.model.User;
import com.indrajagadeesh.webgradle.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * custom property definition and initial user setup
 */
@Log
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "admin")
@Component
public class DBConfig {
    @Autowired
    private UserService userService;

    private String username;
    private String password;

    /**
     * This function will be called automatically during
     * initial boot time, This will add admin to database.
     */
    @PostConstruct
    private void loadAdmin(){
        User admin = new User(username,password,1);
        userService.saveUser(admin);
        log.info("admin added successfully");
    }
}
