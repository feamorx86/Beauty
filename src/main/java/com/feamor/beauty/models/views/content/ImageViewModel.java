package com.feamor.beauty.models.views.content;

/**
 * Created by Home on 25.10.2016.
 */
public class ImageViewModel extends ContentViewModel {
    private String imageUrl;
    private Integer imageId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
