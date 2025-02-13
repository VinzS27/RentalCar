package com.si2001.rentalcar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "NotEmpty.user.username")
    @Column(name = "username", unique=true, nullable = false)
    private String username;

    @NotEmpty(message = "NotEmpty.user.password")
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty(message = "NotEmpty.user.email")
    @Column(name = "email", unique=true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Reservation> reservation;

    @NotEmpty(message = "NotEmpty.user.userProfiles")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profile_joinTable",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_profile_id") })
    private Set<UserProfile> userProfiles;

    public User() {}

    public User(int id, String username, String password, String email, List<Reservation> reservation, Set<UserProfile> userProfiles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.reservation = reservation;
        this.userProfiles = userProfiles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", reservation=" + reservation +
                ", userProfiles=" + userProfiles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(reservation, user.reservation) && Objects.equals(userProfiles, user.userProfiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, reservation, userProfiles);
    }
}
