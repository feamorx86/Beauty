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
 * Created by Home on 23.05.2016.
 */
public class TemplateEditorMenuItem extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.MENU_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        HashMap<String, String> map = (HashMap<String, String>) current.extra;
        if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(map.get("title"));
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("url".equalsIgnoreCase(tag)) {
            String text = map.get("url");
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
