package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.ui.MenuItem;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by Home on 26.05.2016.
 */
public class NavigationItemControl extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.MENU_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        MenuItem menuItem = (MenuItem) current.extra;
        if ("label".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(menuItem.getTitle());
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("url".equalsIgnoreCase(tag)) {
            String text = menuItem.getUrl();
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
