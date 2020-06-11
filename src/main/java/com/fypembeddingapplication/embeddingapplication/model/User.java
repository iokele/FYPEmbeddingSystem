package com.fypembeddingapplication.embeddingapplication.model;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

@Entity
@Table(name = "fypuser")

public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long Id;
    @NotBlank
    @Column(name = "user_name")
        private String userName;
    @NotBlank
    @Column(name = "default_digital_watermark")
        private String defaultDigitalWatermark;
    public User(){
        super();
    }
    public User (Long id,String user_name, String default_digital_watermark){
        super();
        this.Id = id;
        this.userName = user_name;
        this.defaultDigitalWatermark=default_digital_watermark;
    }
    public Long getId(){
        return Id;
    }
    public void setId(Long id) {
        this.Id = id;
    }
    public String getUserName (){
        return userName;
    }

    public void setUser_name(String user_name) {
        this.userName = user_name;
    }
    public String getDefault_digital_watermark() {
        return defaultDigitalWatermark;
    }
    public void setDefault_digital_watermark(String defaultDigitalWatermark) {
        this.defaultDigitalWatermark = defaultDigitalWatermark;
    }

}
