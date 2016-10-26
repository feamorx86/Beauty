package com.feamor.beauty.models.views.news;

/**
 * Created by Home on 25.10.2016.
 */
public class SummaryWithText extends BaseSummary {
    private String text;

    private Integer dataId;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
