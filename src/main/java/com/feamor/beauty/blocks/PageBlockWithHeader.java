package com.feamor.beauty.blocks;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Home on 21.05.2016.
 */
@Component
public class PageBlockWithHeader extends BaseBlockControl {

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.SIMPLE_CONTAINER_WITH_HEADER);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if ("header".equalsIgnoreCase(tag)) {

            BlockWithDependence headerBlock = current.block.findFirstByAlias("header");
            if (headerBlock != null) {
                render.push(current);

                Render.RenderItem nextItem = headerBlock.toRenderItem();
                render.push(nextItem);
                result = Render.RenderTemplateResult.BreakBlock;
            } else {
                //TODO: log error - no header item
                result = Render.RenderTemplateResult.Continue;
            }
        } else if ("content".equalsIgnoreCase(tag)) {
            ArrayList<BlockWithDependence> list;
            list = current.block.findAllWithAlias("content");
            if (list!=null) {
                render.push(current);

                for(int i = list.size()-1; i>=0; i--) { //inverse order - because stack!!!
                    BlockWithDependence dependence = list.get(i);
                    render.push(dependence.toRenderItem());
                }
                result = Render.RenderTemplateResult.BreakBlock;
            } else {
                //No content - just continue
                result = Render.RenderTemplateResult.Continue;
            }
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
