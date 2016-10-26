package com.feamor.beauty.models.views.content;

/**
 * Created by Home on 25.10.2016.
 */
public class FormattedTextViewModel extends ContentViewModel {
    private String text;
    private Integer textId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }
}
