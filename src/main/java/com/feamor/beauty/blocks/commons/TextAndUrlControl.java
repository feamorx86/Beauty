package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.ui.MenuItem;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 10.08.2016.
 */
public class TextAndUrlControl extends  BaseBlockControl {

    protected String textTag() {
        return "text";
    }

    protected String urlTag() {
        return "url";
    }

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.TEXT_AND_URL);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    protected String getText(Render.RenderItem current, Render render) {
        return ((HashMap<String, String>)current.extra).get(textTag());
    }

    protected String getUrl(Render.RenderItem current, Render render) {
        return ((HashMap<String, String>)current.extra).get(urlTag());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (textTag().equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(getText(current, render));
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if (urlTag().equalsIgnoreCase(tag)) {
            String text = getUrl(current, render);
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else {
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
