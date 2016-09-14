package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by Home on 10.08.2016.
 */
public class BasePage_Content extends BaseContainerControl {

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.BASE_PAGE_WITH_CONTENT);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    protected String itemsTag() {
        return contentTag();
    }

    protected String contentTag() {
        return "content";
    }

    protected String titleTag() {
        return "title";
    }

    protected String cssTag() {
        return "css";
    }

    protected String jsTag() {
        return "js";
    }

    protected String getTitle(Render.RenderItem current, Render render) {
        String title = stringFromBlockForTag(titleTag(), current);
        return title;
    }

    protected String getCss(Render.RenderItem current, Render render) {
        String css = stringFromBlockForTag(cssTag(), current);
        return css;
    }

    protected String getJs(Render.RenderItem current, Render render) {
        String js = stringFromBlockForTag(jsTag(), current);
        return js;
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (titleTag().equalsIgnoreCase(tag)) {
            result = putTextAndContinue(getTitle(current, render), current, render);
        } else if (cssTag().equalsIgnoreCase(tag)) {
            result = putTextAndContinue(getCss(current, render), current, render);
        } else if (jsTag().equalsIgnoreCase(tag)) {
            result = putTextAndContinue(getJs(current, render), current, render);
        } else {
            result = super.renderTag(tag, current, render);
        }
        return  result;
    }
}
