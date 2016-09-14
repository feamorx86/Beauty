package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 10.08.2016.
 */
public class BaseContainerControl extends BaseBlockControl {

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.BASE_CONTAINER);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    protected String itemsTag() {
        return "items";
    }

    protected String itemsDataAlias() {
        return "item";
    }

    protected List<BlockWithDependence> getItems(Render.RenderItem current, Render render) {
        return current.block.findAllWithAlias(itemsDataAlias());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (itemsTag().equalsIgnoreCase(tag)) {
            List<BlockWithDependence> contentItems = getItems(current, render);
            result = putListAndBreak(contentItems, current, render);
        } else {
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
