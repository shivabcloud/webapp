package com.project.healthcheck.Service;

import com.project.healthcheck.Dao.IHealthzDao;
import com.project.healthcheck.Dao.IUserDao;
import com.project.healthcheck.Pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    IUserDao userDao;
    @Autowired
    IHealthzService healthzService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!healthzService.getHealthz()){
            throw new UsernameNotFoundException("Service Unavailable");
        }

        System.out.println("Entered Auth service\n\n\n\n\n");
        Optional<User> accountOptional = userDao.findByUsername(username);
        if (accountOptional.isPresent()) {
            User user = accountOptional.get();

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),authorities
            );
        } else {
            throw new UsernameNotFoundException("Account not found with email: " + username);
        }
    }
}
