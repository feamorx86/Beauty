package com.feamor.beauty.blocks.home;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.ui.MenuItem;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 26.05.2016.
 */
public class HomeContentControl extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.Home.CONTENT_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        MenuItem menuItem = (MenuItem) current.extra;
        if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(current.block.findFirstDataByAlias("title").getStringValue());
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("more-label".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(current.block.findFirstDataByAlias("more-label").getStringValue());
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("more-url".equalsIgnoreCase(tag)) {
            String text = current.block.findFirstDataByAlias("more-url").getStringValue();
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("content".equalsIgnoreCase(tag)) {
            render.push(current);
            List<BlockWithDependence> contentItems = current.block.findAllWithAlias("data");
            for (int i = contentItems.size() - 1; i >= 0; i--) { //inverse order - because stack!!!
                BlockWithDependence block = contentItems.get(i);
                block.control.renderSubContent(current, block, render);
            }
            result = Render.RenderTemplateResult.BreakBlock;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
