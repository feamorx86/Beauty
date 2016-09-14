package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.models.ui.MenuItem;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 30.06.2016.
 */
public class ContainerInDependenceControl extends BaseBlockControl {

    protected String itemsTag() {
        return "items";
    }

    protected String itemsData() {
        return "item";
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if (itemsTag().equalsIgnoreCase(tag)) {
            render.push(current);
            List<BlockWithDependence> contentItems = current.block.findAllWithAlias(itemsData());
            for (int i = contentItems.size() - 1; i >= 0; i--) {
                BlockWithDependence block = contentItems.get(i);
                Render.RenderItem renderItem = block.toRenderItem();
                render.push(renderItem);
            }
            result = Render.RenderTemplateResult.BreakBlock;
        } else {
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
