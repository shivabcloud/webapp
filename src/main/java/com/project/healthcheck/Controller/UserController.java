package com.project.healthcheck.Controller;

import com.project.healthcheck.Pojo.User;
import com.project.healthcheck.Pojo.UserInput;
import com.project.healthcheck.Pojo.UserResponse;
import com.project.healthcheck.Service.IHealthzService;
import com.project.healthcheck.Service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/v2/user")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    IHealthzService healthzService;

    
    private static final Logger logger = LogManager.getLogger(UserController.class);


    @GetMapping("/self")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request)
    {
        try {
            if(request.getContentLength()>0 || request.getQueryString()!=null)
            {
                return responseHandling(HttpStatus.BAD_REQUEST,null);
            }
            if(!healthzService.getHealthz()) {
                return responseHandling(HttpStatus.SERVICE_UNAVAILABLE, null);
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()) {

                if (auth.getPrincipal() instanceof UserDetails) {
                    try{
                        UserDetails userDetails = (UserDetails) auth.getPrincipal();
                        UserResponse response = userService.getUser(userDetails.getUsername());
                        if(response==null){
                            return responseHandling(HttpStatus.BAD_REQUEST, null);
                        }
                        if(!response.getIsVerified()){
                            return responseHandling(HttpStatus.FORBIDDEN, null);
                        }
                        return responseHandling(HttpStatus.OK, response);
                    } catch (Exception e) {
                        return responseHandling(HttpStatus.UNAUTHORIZED, null);
                    }
                }

            }

            return responseHandling(HttpStatus.UNAUTHORIZED, null);
        }
        catch(Exception e){
            return responseHandling(HttpStatus.BAD_REQUEST,null);
        }
    }

    private ResponseEntity<UserResponse> responseHandling(HttpStatusCode code, UserResponse user){
        return ResponseEntity.status(code)
                .cacheControl(CacheControl.noCache())
                .body(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserInput userInput)
    {
        try{
            if(!healthzService.getHealthz()) {
                return responseHandling(HttpStatus.SERVICE_UNAVAILABLE, null);
            }
            User user = convertUserInputToUser(userInput);
            System.out.println(user.getUsername());
            user.setAccount_created(new Date());
            user.setAccount_updated(new Date());

            logger.info("Entered user controller- and account create, update date set");
            UserResponse userResponse = userService.createUser(user);
            logger.debug("User creation complete without fail");
            return responseHandling(HttpStatus.CREATED, userResponse);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            return responseHandling(HttpStatus.BAD_REQUEST, null);
        }
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<UserResponse> userDetailsMethods(@RequestBody User user)
    {
        try {

            return responseHandling(HttpStatus.NOT_FOUND, null);
        }
        catch(Exception e){
            return responseHandling(HttpStatus.NOT_FOUND, null);
        }
    }

    @PutMapping("/self")
    public ResponseEntity<UserResponse> updateSelfDetails(@Valid @RequestBody UserInput userInput){
        try {
            if(!healthzService.getHealthz()) {
                return responseHandling(HttpStatus.SERVICE_UNAVAILABLE, null);
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                if (auth.getPrincipal() instanceof UserDetails) {
                    try {
                        UserDetails userDetails = (UserDetails) auth.getPrincipal();
                        User oldData = userService.getUserAllDetails(userDetails.getUsername()).orElse(null);
                        if(oldData==null || !(oldData.getUsername().toLowerCase().equals(userInput.getUsername().toLowerCase()))){
                            return responseHandling(HttpStatus.BAD_REQUEST, null);
                        }

                        UserResponse response = userService.updateUser(updatingUserDetails(oldData,userInput));
                        if(response==null){
                            return responseHandling(HttpStatus.BAD_REQUEST, null);
                        }
                        return responseHandling(HttpStatus.NO_CONTENT, null);
                    } catch (Exception e) {
                        return responseHandling(HttpStatus.BAD_REQUEST, null);
                    }
                }
            }
            return responseHandling(HttpStatus.UNAUTHORIZED, null);
        }
        catch(Exception e){
            return responseHandling(HttpStatus.BAD_REQUEST,null);
        }
    }

    private User convertUserInputToUser(UserInput input){
        User user = new User();
        user.setUsername(input.getUsername());
        user.setFirst_name(input.getFirst_name());
        user.setLast_name(input.getLast_name());
        user.setPassword(bCryptPasswordEncoder.encode(input.getPassword()));
        return user;
    }
    private User updatingUserDetails(User oldData, UserInput currentData){
        User user = new User();
        user.setId(oldData.getId());
        user.setUsername(oldData.getUsername());
        user.setAccount_created(oldData.getAccount_created());
        user.setIsVerified(oldData.getIsVerified());
        user.setAccount_updated(new Date());
        user.setFirst_name(currentData.getFirst_name());
        user.setLast_name(currentData.getLast_name());
        user.setPassword(bCryptPasswordEncoder.encode(currentData.getPassword()));
        return user;
    }
    @GetMapping("/verify")
    public ResponseEntity<UserResponse> userVerification(@RequestParam String token){

        try {

            if(!healthzService.getHealthz()) {
                return responseHandling(HttpStatus.SERVICE_UNAVAILABLE, null);
            }

            if(userService.userVerification(token).getIsVerified()==true) {
                return responseHandling(HttpStatus.OK,null);
            }

            return responseHandling(HttpStatus.UNAUTHORIZED, null);
        }
        catch(Exception e){
            return responseHandling(HttpStatus.BAD_REQUEST,null);
        }
    }
}
