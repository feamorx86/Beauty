package com.feamor.beauty.blocks;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.templates.Render;

import java.io.IOException;

/**
 * Created by Home on 19.05.2016.
 */
public class SimpleBlockControlWithText extends BaseBlockControl {
    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.ContentBlock.SIMPLE_TEXT_CONTROL);
        blockManager.registerBlockWithName(this, "simple-text");
    }

    @Override
    public void render(Object... args) {
        super.render(args);
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if ("text".equalsIgnoreCase(tag)) {
            PageBlockData data = current.block.findFirstDataByAlias(tag);
            String text;
            if (data != null && data.getStringValue()!=null) {
                text = data.getStringValue();
            } else {
                text = "";
                //TODO: log - no or empty text data.
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
