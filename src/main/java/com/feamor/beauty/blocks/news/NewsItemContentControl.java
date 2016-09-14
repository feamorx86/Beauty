package com.feamor.beauty.blocks.news;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 26.05.2016.
 */
public class NewsItemContentControl extends BaseBlockControl {

    @Autowired
    private SiteDao siteDao;

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.News.LIST_PAGE);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        //main-css title header content footer
        if ("main-css".equalsIgnoreCase(tag)) {
            String text = current.block.findFirstDataByAlias("home-css").getStringValue();
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(current.block.findFirstDataByAlias("title").getStringValue());
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("header".equalsIgnoreCase(tag) || "footer".equalsIgnoreCase(tag)) {
            render.push(current);
            BlockWithDependence contentBlock = current.block.findFirstByAlias(tag);
            Render.RenderItem renderItem = contentBlock.toRenderItem();
            render.push(renderItem);
            result = Render.RenderTemplateResult.BreakBlock;
        } else if("content".equalsIgnoreCase(tag)) {
            render.push(current);
            List<BlockWithDependence> contentItems = current.block.findAllWithAlias(tag);
            for (int i = contentItems.size() - 1; i >= 0; i--) { //inverse order - because stack!!!
                Render.RenderItem renderItem = contentItems.get(i).toRenderItem();
                render.push(renderItem);
            }
            result = Render.RenderTemplateResult.BreakBlock;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
