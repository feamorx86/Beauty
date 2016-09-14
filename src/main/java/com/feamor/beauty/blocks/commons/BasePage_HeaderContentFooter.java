package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 10.08.2016.
 */
public class BasePage_HeaderContentFooter extends BasePage_Content {

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.BASE_PAGE_WITH_HEADER_CONTENT_FOOTER);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    protected String itemsTag() {
        return contentTag();
    }

    protected String headerTag() {
        return "header";
    }

    protected String footerTag() {
        return "footer";
    }

    protected BlockWithDependence getHeader(Render.RenderItem current, Render render) {
        return current.block.findFirstByAlias(headerTag());
    }

    protected BlockWithDependence getFooter(Render.RenderItem current, Render render) {
        return current.block.findFirstByAlias(footerTag());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (headerTag().equalsIgnoreCase(tag)) {
            BlockWithDependence header = getHeader(current, render);
            result = putBlockAndBreak(header, current, render);
        } else  if (footerTag().equalsIgnoreCase(tag)) {
            BlockWithDependence footer = getFooter(current, render);
            result = putBlockAndBreak(footer, current, render);
        } else {
            result = super.renderTag(tag, current, render);
        }
        return  result;
    }
}
