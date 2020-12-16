package com.fypembeddingapplication.embeddingapplication.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity

@Table(name = "confirmationtoken")
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    @Column(name = "confirmation_token")
    private String confirmatinToken;
    @Column(name = "created_date")
    private LocalDate createdDate;
    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "id")
    private User user;

   public ConfirmationToken(User user){
        this.user =user;
        this.createdDate =LocalDate.now();
        this.confirmatinToken = UUID.randomUUID().toString();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getConfirmatinToken() {
        return confirmatinToken;
    }

    public void setConfirmatinToken(String confirmatinToken) {
        this.confirmatinToken = confirmatinToken;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
