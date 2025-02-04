package com.si2001.rentalcar.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {

    @Id
    private String series;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @SuppressWarnings("deprecation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_used;

    public PersistentLogin() {}

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }


}
