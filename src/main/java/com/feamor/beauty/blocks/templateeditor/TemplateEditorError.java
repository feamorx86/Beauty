package com.feamor.beauty.blocks.templateeditor;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.controllers.SimpleTemplateEditor;
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
public class TemplateEditorError extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.ERROR_BLOCK);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        SimpleTemplateEditor.MessageData message = (SimpleTemplateEditor.MessageData) current.extra;
        if ("error-message".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(message.message);
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("error-title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(message.title);
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("error-solution".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(message.description);
            if (StringUtils.isEmpty(text)) {
                text = "";
            }
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("error-url".equalsIgnoreCase(tag)) {
            String url = message.url;
            render.getResponse().getOutputStream().print(url);
            result = Render.RenderTemplateResult.Continue;
        }else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}