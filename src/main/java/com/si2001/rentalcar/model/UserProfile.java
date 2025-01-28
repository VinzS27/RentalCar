package com.si2001.rentalcar.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TYPE", length = 15, unique = true, nullable = false)
    private String type;

    @ManyToMany(mappedBy = "userProfiles")
    List<User> users;

    public UserProfile() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserProfile [id=" + id + ", type=" + type + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, users);
    }
}
