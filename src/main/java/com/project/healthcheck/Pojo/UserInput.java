package com.project.healthcheck.Pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.aspectj.lang.annotation.RequiredTypes;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Type;

public class UserInput {
    @JsonProperty(required = true)
    private String username;
    @JsonProperty(required = true)
    @Pattern(regexp = "^[^\\s].*$")
    private String password;
    @JsonProperty(required = true)
    private String first_name;
    @JsonProperty(required = true)
    private String last_name;
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getFirst_name(){
        return this.first_name;
    }
    public String getLast_name(){
        return this.last_name;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }
}
