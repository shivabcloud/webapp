package com.project.healthcheck.Service;

import com.project.healthcheck.Dao.ITokenDao;
import com.project.healthcheck.Dao.IUserDao;
import com.project.healthcheck.Pojo.User;
import com.project.healthcheck.Pojo.UserResponse;

import com.project.healthcheck.Pojo.VerifyToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements IUserService{
    @Autowired
    IUserDao userDao;
    @Autowired
    PublishMessageService publishMessageService;
    @Autowired
    ITokenDao tokenDao;
    
    private static final Logger logger = LogManager.getLogger(UserService.class);

    public UserResponse getUser(String username){

        Optional<User>user = userDao.findByUsername(username);
        if(user == null){
            return null;
        }
        return returnResponseFromUser(user.get());
    }

    public UserResponse createUser(User user){
        System.out.println("Reached service");
        logger.debug("Reached service next publish to be done", user);
        try{
        publishMessageService.publishMessage("dev-cloud-bhargav", "verify_email", user);
        logger.debug("publish completed, next saving in Db");
        return returnResponseFromUser(userDao.save(user)) ;
        }
        catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            logger.error(e.getStackTrace());
            System.out.println(e.getMessage());
            return returnResponseFromUser(userDao.save(user));
        }
    }

    private UserResponse returnResponseFromUser(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirst_name(user.getFirst_name());
        response.setLast_name(user.getLast_name());
        response.setAccount_created(user.getAccount_created());
        response.setAccount_updated(user.getAccount_updated());
        response.setIsVerified(user.getIsVerified());
        return response;
    }

    public UserResponse updateUser(User user){
        return returnResponseFromUser(userDao.save(user));
    }

    public Optional<User> getUserAllDetails(String username){
        Optional<User> user = userDao.findByUsername(username);
        return user;
    }


    public UserResponse userVerification(String token){
        Optional<VerifyToken> details= tokenDao.findByToken(token);

        if(details == null){
            return null;
        }
        LocalDateTime current_time = LocalDateTime.now();

        if(details.get().getExpiration().isAfter(current_time)){
            User user = userDao.findByUsername(details.get().getUsername()).get();
            user.setIsVerified(true);
            userDao.save(user);
            return getUser(details.get().getUsername());
        }
        return getUser(details.get().getUsername());
    }
}
