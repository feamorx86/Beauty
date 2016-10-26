package com.feamor.beauty.models.views.news;

/**
 * Created by Home on 25.10.2016.
 */
public class ComplexSummary extends BaseSummary {

    private String imageUrl;

    private String text;

    private Integer imageId;

    private Integer textId;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
