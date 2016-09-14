package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Home on 30.06.2016.
 */
public class PageWithHeaderContentAndFooterControl extends BaseBlockControl {
    protected String headerTag() {
        return "header";
    }

    protected String contentTag() {
        return "content";
    }

    protected String footerTag() {
        return "footer";
    }

    protected int renderDependence(Render.RenderItem current, Render render, BlockWithDependence block) {
        render.push(current);
        Render.RenderItem nextItem = block.toRenderItem();
        render.push(nextItem);
        return Render.RenderTemplateResult.BreakBlock;
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (headerTag().equalsIgnoreCase(tag)) {
            BlockWithDependence headerBlock = current.block.findFirstByAlias(headerTag());
            if (headerBlock != null) {
                result = renderDependence(current, render, headerBlock);
            } else {
                //TODO: log error - no header item
                result = Render.RenderTemplateResult.Continue;
            }
        } else if (footerTag().equalsIgnoreCase(tag)) {
            BlockWithDependence headerBlock = current.block.findFirstByAlias(footerTag());
            if (headerBlock != null) {
                result = renderDependence(current, render, headerBlock);
            } else {
                //TODO: log error - no header item
                result = Render.RenderTemplateResult.Continue;
            }
        } else if (contentTag().equalsIgnoreCase(tag)) {
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
