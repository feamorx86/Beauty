package com.feamor.beauty.models.views.content;

import com.feamor.beauty.models.views.BasePageModel;
import com.feamor.beauty.models.views.BaseViewModel;

/**
 * Created by Home on 25.10.2016.
 */
public class ContentViewModel extends BaseViewModel {
    private Integer style;
    private Integer styleId;
    private String styleParams;

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public String getStyleParams() {
        return styleParams;
    }

    public void setStyleParams(String styleParams) {
        this.styleParams = styleParams;
    }
}
