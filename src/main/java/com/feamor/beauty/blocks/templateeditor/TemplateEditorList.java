package com.feamor.beauty.blocks.templateeditor;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.controllers.SimpleTemplateEditor;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Home on 24.05.2016.
 */
public class TemplateEditorList extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.TemplateEditor.LIST_BLOCK);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        SimpleTemplateEditor.EditorData editor = (SimpleTemplateEditor.EditorData) current.extra;
        if ("items".equalsIgnoreCase(tag)) {
            render.push(current);
            BlockWithDependence listItemBlock = current.block.findFirstByAlias("list-item");
            for (int i = editor.listNames.size() - 1; i >= 0; i--) { //inverse order - because stack!!!
                Render.RenderItem renderItem = listItemBlock.toRenderItem();
                HashMap<String, Object> extras = new HashMap<String, Object>();
                extras.put("url", editor.listUrls.get(i));
                extras.put("id", editor.listIds.get(i));
                extras.put("title", editor.listNames.get(i));
                renderItem.extra = extras;
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
