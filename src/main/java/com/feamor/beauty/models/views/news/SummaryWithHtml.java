package com.feamor.beauty.models.views.news;

/**
 * Created by Home on 25.10.2016.
 */
public class SummaryWithHtml extends BaseSummary {

    private String htmlData;

    private Integer dataId;

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }
}
