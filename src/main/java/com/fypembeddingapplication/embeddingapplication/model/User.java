package com.fypembeddingapplication.embeddingapplication.model;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

@Entity
@Table(name = "fypuser")
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    @NotBlank
    @Column(name = "user_name")
    private String userName;
    @NotBlank
    @Column(name = "email")
    private String email;
    @NotBlank
    @Column(name = "password")
    private String password;
    @NotBlank
    @Column(name = "default_digital_watermark")
    private String defaultDigitalWatermark;
    @Column(name = "enabled")
    private Boolean enabled = false;
    @Column(name = "role")
    private String role = "USER";

    public User(){
        super();
    }
    public User(String email,String username,String password,String defaultDigitalWatermark){
        this.email=email;
        this.userName = username;
        this.password=password;
        this.defaultDigitalWatermark=defaultDigitalWatermark;
    }
    public Collection<? extends GrantedAuthority> getAuthorities(){
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
        return Collections.singletonList(simpleGrantedAuthority);
    }
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultDigitalWatermark() {
        return defaultDigitalWatermark;
    }

    public void setDefaultDigitalWatermark(String defaultDigitalWatermark) {
        this.defaultDigitalWatermark = defaultDigitalWatermark;
    }
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
