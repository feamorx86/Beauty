package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;

import java.io.IOException;

/**
 * Created by Home on 27.05.2016.
 */
public class FooterControl extends BaseBlockControl {

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Footer.FOOTER);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if ("navigation".equalsIgnoreCase(tag) || "about".equalsIgnoreCase(tag)) {
            render.push(current);
            BlockWithDependence contentBlock = current.block.findFirstByAlias(tag);
            Render.RenderItem renderItem = contentBlock.toRenderItem();
            render.push(renderItem);
            result = Render.RenderTemplateResult.BreakBlock;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}