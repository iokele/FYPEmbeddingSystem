package com.fypembeddingapplication.embeddingapplication.responseModel;

public class requestForExtraction {
    private String embeddedImage;
    private Long originalImageId;
    private Long userId;
    private String filter;
    public requestForExtraction() {
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getEmbeddedImage() {
        return embeddedImage;
    }

    public void setEmbeddedImage(String embeddedImage) {
        this.embeddedImage = embeddedImage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOriginalImageId() {
        return originalImageId;
    }

    public void setOriginalImageId(Long originalImageId) {
        this.originalImageId = originalImageId;
    }
}
