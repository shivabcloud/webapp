package com.project.healthcheck.userTesting;

import com.project.healthcheck.Dao.ITokenDao;
import com.project.healthcheck.Pojo.User;
import com.project.healthcheck.Pojo.UserResponse;
import com.project.healthcheck.Pojo.VerifyToken;
import com.project.healthcheck.Service.PublishMessageService;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class userTesting {
    @LocalServerPort
    private int port;

    private static final Logger logger = LogManager.getLogger(userTesting.class);

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    ITokenDao tokenDao;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + port;
        logger.info("Setting up base URL: {}", baseUrl);
    }

    @Test
    @Order(1)
    public void healthCheckPost(){
        ResponseEntity<Void> response = testRestTemplate.postForEntity(baseUrl + "/healthz", null, null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        //assertThat(response.getHeaders()).isEqualTo(CacheControl.noCache());
        assertThat(response.getBody()).isEqualTo(null);
    }

    @Test
    @Order(2)
    public void healthCheck(){
        ResponseEntity<Void> response = testRestTemplate.getForEntity(baseUrl + "/healthz", null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    @Order(3)
    public void createUserTest(){

        assertionOfObjects(userCreate(), getUser(userCredentials().getUsername(), userCredentials().getPassword()));

    }

    @Test
    @Order(4)
    public void updateUserDetails(){

        User createdUser = getUser(userCredentials().getUsername(), userCredentials().getPassword());
        createdUser.setPassword( userCredentials().getPassword());
        HttpHeaders headers = createHeaders(userCredentials().getUsername(), userCredentials().getPassword());


        createdUser = updateUserBody(createdUser);
        HttpEntity<User> request = new HttpEntity<>(createdUser, headers);
        ResponseEntity<User> response = testRestTemplate.exchange(baseUrl+"/v1/user/self", HttpMethod.PUT, request, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        User gettingUserDetails = getUser(userCredentials().getUsername(), userCredentials().getPassword());
        User user = updatingParametersAsRequired(createdUser, gettingUserDetails);
        assertionOfObjects(user, gettingUserDetails);
    }

    private User userCredentials(){
        User user = new User();
        user.setUsername("shivabhargav@gmail.com");
        user.setPassword("abc@1234");
        return user;
    }

    private User createUserBody(){
        User user = new User();

        user.setUsername(userCredentials().getUsername());
        user.setPassword(userCredentials().getPassword());
        user.setFirst_name("Shiva Bhargav");
        user.setLast_name("Bhuvanam");
        user.setIsVerified(true);

        return user;
    }
    private User updateUserBody(User user){

        user.setFirst_name("firstNameChanged");

        return user;
    }

    private VerifyToken setToken(String username){
        VerifyToken token = new VerifyToken();
        token.setToken("daipojdaa");
        token.setExpiration(LocalDateTime.now().plusMinutes(2));
        token.setUsername(username);
        return token;
    }

    public User userCreate(){
        ResponseEntity<User> response = testRestTemplate.postForEntity(baseUrl+"/v1/user", createUserBody(), User.class);
        VerifyToken token = setToken(createUserBody().getUsername());
        tokenDao.save(token);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<UserResponse> new_response = testRestTemplate.getForEntity(baseUrl+"/v1/user/verify?token=daipojdaa", UserResponse.class);
        return response.getBody();
    }

    private void assertionOfObjects(User actualResponse,User expectedResponse){
        assertThat(actualResponse.getUsername()).isEqualTo(expectedResponse.getUsername());
        assertThat(actualResponse.getFirst_name()).isEqualTo(expectedResponse.getFirst_name());
        assertThat(actualResponse.getLast_name()).isEqualTo(expectedResponse.getLast_name());
        assertThat(actualResponse.getId()).isEqualTo(expectedResponse.getId());
        assertThat(actualResponse.getAccount_created()).isEqualTo(expectedResponse.getAccount_created());
        assertThat(actualResponse.getAccount_updated()).isEqualTo(expectedResponse.getAccount_updated());
        assertThat(actualResponse.getPassword()).isEqualTo(expectedResponse.getPassword());
    }
    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    private User getUser(String username, String password){
        HttpHeaders headers = createHeaders(username, password);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<User> response = testRestTemplate.exchange(baseUrl+"/v1/user/self", HttpMethod.GET, request, User.class);
        return response.getBody();
    }

    private User updatingParametersAsRequired(User updateUser, User latestDetails){
        updateUser.setPassword(null);
        updateUser.setAccount_updated(latestDetails.getAccount_updated());
        return updateUser;
    }
}
