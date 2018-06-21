package ru.geekbrains.server.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users_info")
public class UserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "free_disc_space", nullable = false)
    private Long freeDiscSpace;

    @Column(name = "home_path", unique = true)
    private String nomePath;

    @Column(name = "token", unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    public UserInfo() {
    }


    public UserInfo(String name, long freeDiscSpace) {
        this.name = name;
        this.freeDiscSpace = freeDiscSpace;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFreeDiscSpace() {
        return freeDiscSpace;
    }

    public void setFreeDiscSpace(Long freeDiscSpace) {
        this.freeDiscSpace = freeDiscSpace;
    }

    public String getHomePath() {
        return nomePath;
    }

    public void setHomePath(String nomePath) {
        this.nomePath = nomePath;
    }
}
