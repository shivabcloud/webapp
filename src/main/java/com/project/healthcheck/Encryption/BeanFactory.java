package com.project.healthcheck.Encryption;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanFactory {
    @Bean
    public BCryptPasswordEncoder pwdEncoder(){
        return new BCryptPasswordEncoder();
    }


}
