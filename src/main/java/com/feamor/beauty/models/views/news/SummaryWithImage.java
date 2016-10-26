package com.feamor.beauty.models.views.news;

/**
 * Created by Home on 25.10.2016.
 */
public class SummaryWithImage extends BaseSummary {

    private String imageUrl;

    private Integer dataId;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
