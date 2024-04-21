package com.project.healthcheck.SpringSecurity;

import com.project.healthcheck.Service.AuthenticationService;
import com.project.healthcheck.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.naming.ServiceUnavailableException;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationService authenticationService;



    @Autowired
    private BCryptPasswordEncoder pwdEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        try {
            http
                    .sessionManagement((session) -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )


                    .authorizeHttpRequests(authz -> authz
                            .requestMatchers("/healthz").permitAll()
                            .requestMatchers("/v1/user").permitAll()
                            .requestMatchers("/v1/user/self").authenticated()
                            .requestMatchers("/error").permitAll()
                            .anyRequest().permitAll()

                    )


                    .csrf((csrf) -> csrf.disable())
                    .httpBasic(Customizer.withDefaults());


            return http.build();
        }
        catch(Exception e){
            System.out.println("HEREEE");

            if ( e.getMessage() == "Service Unavailable") {
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noCache()).body(null);
            }
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).body(null);
            return http.build();
        }
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(authenticationService)
                .passwordEncoder(pwdEncoder);

    }

}
