package com.feamor.beauty.blocks.templateeditor;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.controllers.SimpleTemplateEditor;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Home on 23.05.2016.
 */
public class TemplateEditorPageControl extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.PAGE);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        SimpleTemplateEditor.EditorData editor = (SimpleTemplateEditor.EditorData) current.extra;
        if ("error".equalsIgnoreCase(tag)) {
            if (editor.message != null) {
                render.push(current);
                BlockWithDependence errorBlock = current.block.findFirstByAlias("error");
                Render.RenderItem renderItem = errorBlock.toRenderItem();
                renderItem.extra = editor.message;
                render.push(renderItem);
                result = Render.RenderTemplateResult.BreakBlock;
            } else  {
                //no error - ignore block
                result = Render.RenderTemplateResult.Continue;
            }
        } else if ("content".equalsIgnoreCase(tag)) {
            render.push(current);
            BlockWithDependence contentBlock = current.block.findFirstByAlias("content");
            Render.RenderItem renderItem = contentBlock.toRenderItem();
            renderItem.extra = editor;
            render.push(renderItem);
            result = Render.RenderTemplateResult.BreakBlock;
        } else if ("menu-items".equalsIgnoreCase(tag)) {
            BlockWithDependence menuBlock = current.block.findFirstByAlias("menu-item");
            render.push(current);
            for (int i = editor.menu.menuNames.size() - 1; i >= 0; i--) {
                Render.RenderItem renderItem = menuBlock.toRenderItem();
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("title", editor.menu.menuNames.get(i));
                map.put("url", editor.menu.menuUrls.get(i));
                renderItem.extra = map;
                render.push(renderItem);
            }
            result = Render.RenderTemplateResult.BreakBlock;
        } else if ("title".equalsIgnoreCase(tag)) {
            String text = HtmlUtils.htmlEscape(editor.menu.title);
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
