package com.feamor.beauty.models.views;

import java.util.Date;

/**
 * Created by Home on 25.10.2016.
 */
public class BasePageModel extends BaseViewContainer {
    private Date time;
    private String url;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
