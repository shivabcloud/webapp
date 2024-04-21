package com.project.healthcheck.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class User {
    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false, updatable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @Column(nullable = false)
    private String first_name;
    @Column(nullable = false)
    private String last_name;
    @Pattern(regexp = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]+$")
    @Column(unique = true, nullable = false)
    private String username;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date account_created;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date account_updated;

    @Column(nullable = false)
    private boolean isVerified;

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }

    @Column(nullable = false)
    private String password;

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getFirst_name(){
        return this.first_name;
    }
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public String getLast_name(){
        return this.last_name;
    }
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public Date getAccount_created(){
        return this.account_created;
    }
    public void setAccount_created(Date account_created){
        this.account_created = account_created;
    }

    public Date getAccount_updated(){
        return this.account_updated;
    }
    public void setAccount_updated(Date account_updated){
        this.account_updated = account_updated;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
