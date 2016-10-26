package com.feamor.beauty.blocks.home;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.views.NewsData;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 27.05.2016.
 */
public class HomeNewsBlock extends BaseBlockControl {
    @Autowired
    private SiteDao siteDao;

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.News.NEWS_BLOCK_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }
    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        NewsData news = (NewsData) current.extra;
        if ("url".equalsIgnoreCase(tag)) {
            String text = "/news?view&id=" + news.getId();
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("icon".equalsIgnoreCase(tag)) {
            String text = news.getIcon();
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(news.getTitle());
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("button-title".equalsIgnoreCase(tag)) {
            //TODO: add support of current user language!
            String text = HtmlUtils.htmlEscape(current.block.findFirstDataByAlias("more-title").getStringValue());
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("text".equalsIgnoreCase(tag)) {
            //TODO: add some kind of Utils to divide strings to lines and so on
            String text;
            if (!StringUtils.isEmpty(news.getSummary())) {
                String[] lines = news.getSummary().split("\\r?\\n");
                StringBuilder builder = new StringBuilder();
                for (String line : lines) {
                    builder.append("<p>").append(HtmlUtils.htmlEscape(line)).append("</p>");
                }
                text = builder.toString();
            } else {
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

    @Override
    public void renderSubContent(Render.RenderItem current, BlockWithDependence contentBlock, Render render) {
        int count = contentBlock.findFirstDataByAlias("count").getIntValue();
        List<NewsData> news = siteDao.loadLastNewsSummary(count, 0);
        if(news != null) {
            for (int i = news.size() - 1; i >= 0; i--) {
                Render.RenderItem newRenderItem = contentBlock.toRenderItem();
                newRenderItem.extra = news.get(i);
                render.push(newRenderItem);
            }
        }
    }
}
