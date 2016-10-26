package com.feamor.beauty.models.views.content;

/**
 * Created by Home on 25.10.2016.
 */
public class TitleViewModel extends ContentViewModel {
    private String title;
    private Integer titleId;
    private int level;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
