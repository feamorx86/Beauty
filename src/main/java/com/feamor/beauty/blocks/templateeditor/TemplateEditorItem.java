package com.feamor.beauty.blocks.templateeditor;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Home on 24.05.2016.
 */
public class TemplateEditorItem extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.LIST_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        HashMap<String, Object> map = (HashMap<String, Object>) current.extra;
        if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape((String) map.get("title"));
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("url".equalsIgnoreCase(tag)) {
            String url = (String) map.get("url");
            render.getResponse().getOutputStream().print(url);
            result = Render.RenderTemplateResult.Continue;
        } else if ("id".equalsIgnoreCase(tag)) {
            Integer id = ((Integer)map.get("id"));
            render.getResponse().getOutputStream().print(id);
            result = Render.RenderTemplateResult.Continue;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
