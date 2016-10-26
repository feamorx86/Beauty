package com.feamor.beauty.models.views.news;

import com.feamor.beauty.models.views.BasePageModel;
import com.feamor.beauty.models.views.news.BaseSummary;

/**
 * Created by Home on 12.10.2016.
 */
public class NewsPageData extends BasePageModel {

    private BaseSummary summary;

    private int userId;

    public BaseSummary getSummary() {
        return summary;
    }

    public void setSummary(BaseSummary summary) {
        this.summary = summary;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
