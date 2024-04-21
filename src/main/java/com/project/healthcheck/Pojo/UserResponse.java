package com.project.healthcheck.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class UserResponse{
    private String id;
    private Date account_created;
    private Date account_updated;
    private String username;
    private String first_name;
    private String last_name;

    @JsonIgnore
    private boolean isVerified;

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }

    public void setId(String id){
        this.id = id;
    }
    public void setAccount_created(Date account_created){
        this.account_created = account_created;
    }
    public void setAccount_updated(Date account_updated){
        this.account_updated = account_updated;
    }

    public String getId(){
        return this.id;
    }
    public Date getAccount_created(){
        return this.account_created;
    }
    public Date getAccount_updated(){
        return this.account_updated;
    }
    public String getUsername(){
        return this.username;
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

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }


}
