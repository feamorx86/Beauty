package com.feamor.beauty.models.ui;

/**
 * Created by Home on 27.05.2016.
 */
public class MenuItem {
    private String title;
    private String url;

    public MenuItem() {

    }
    public MenuItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
