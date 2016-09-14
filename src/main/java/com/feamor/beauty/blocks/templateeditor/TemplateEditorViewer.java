package com.feamor.beauty.blocks.templateeditor;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.controllers.SimpleTemplateEditor;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.Render;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * Created by Home on 24.05.2016.
 */
public class TemplateEditorViewer extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.VIEWER_BLOCK);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        SimpleTemplateEditor.EditorData editor = (SimpleTemplateEditor.EditorData) current.extra;
        if ("alias".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(editor.alias);
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        }
        if ("content".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(editor.content);
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        }
        if ("url-and-method".equalsIgnoreCase(tag)) {
            String url;
            if (editor.type == SimpleTemplateEditor.EditorData.EDIT) {
                url = "\""+editor.editUrl+"\" method=\"POST\"";
            } else {
                url = "";
            }
            render.getResponse().getOutputStream().print(url);
            result = Render.RenderTemplateResult.Continue;
        }
        if ("editable".equalsIgnoreCase(tag)) {
            if (editor.type == SimpleTemplateEditor.EditorData.VIEW) {
                render.getResponse().getOutputStream().print("readonly");
            }
            result = Render.RenderTemplateResult.Continue;
        }else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
