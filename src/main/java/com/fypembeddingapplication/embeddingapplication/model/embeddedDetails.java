package com.fypembeddingapplication.embeddingapplication.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "embedded_detail")

public class embeddedDetails implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "embedded_detail_Id")
    private Long embeddedDetailId;
    @Column(name = "user_Id")
    private Long userId;
    @Column(name = "embedded_key")
    private String embeddedKey;
    @Column(name = "original_images_id")
    private Long originalImagesID;
    @Column(name = "embedded_images_id")
    private Long embededImagesID;
    public embeddedDetails(){
        super();
    }
    public embeddedDetails(Long userId,String embededKey, Long originalImagesID, Long embededImagesID) {
        super();
        this.userId = userId;
        this.embeddedKey = embededKey;
        this.originalImagesID = originalImagesID;
        this.embededImagesID = embededImagesID;
    }

    public Long getEmbededDetailId() {
        return embeddedDetailId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmbededDetailId(Long embededDetailId) {
        this.embeddedDetailId = embededDetailId;
    }

    public String getEmbededKey() {
        return embeddedKey;
    }

    public void setEmbededKey(String embededKey) {
        this.embeddedKey = embededKey;
    }

    public Long getOriginalImagesID() {
        return originalImagesID;
    }

    public void setOriginalImagesID(Long originalImagesID) {
        this.originalImagesID = originalImagesID;
    }

    public Long getEmbededImagesID() {
        return embededImagesID;
    }

    public void setEmbededImagesID(Long embededImagesID) {
        this.embededImagesID = embededImagesID;
    }
}
