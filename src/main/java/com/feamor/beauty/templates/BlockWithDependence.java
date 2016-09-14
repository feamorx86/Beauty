package com.feamor.beauty.templates;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.models.db.PageBlock;
import com.feamor.beauty.models.db.PageBlockData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 22.05.2016.
 */
public class BlockWithDependence {
    public PageBlock block;
    public BaseBlockControl control;
    public Object Extras;
    public ArrayList<PageBlockData> blockData = new ArrayList<PageBlockData>();
    public ArrayList<BlockWithDependence> dependence = new ArrayList<BlockWithDependence>();
    public List<TemplateElement> templateElements;

    public Render.RenderItem toRenderItem() {
        Render.RenderItem item = new Render.RenderItem(this);
        return item;
    }


    public BlockWithDependence findFirstByAlias(String alias) {
        BlockWithDependence result = null;
        if (dependence != null) {
            for (BlockWithDependence item : dependence) {
                if (alias.equalsIgnoreCase(item.block.getAlias())) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    public ArrayList<BlockWithDependence> findAllWithAlias(String alias) {
        ArrayList<BlockWithDependence> result = new ArrayList<BlockWithDependence>();
        for (BlockWithDependence item : dependence) {
            if (alias.equalsIgnoreCase(item.block.getAlias())) {
                result.add(item);
            }
        }
        return result;
    }

    public PageBlockData findFirstDataByAlias(String alias) {
        PageBlockData result = null;
        if (blockData != null) {
            for (PageBlockData item : blockData) {
                if (alias.equalsIgnoreCase(item.getAlias())) {
                    result = item;
                    break;
                }
            }
        }
        return result;
    }

    public ArrayList<PageBlockData> findAllDataWithAlias(String alias) {
        ArrayList<PageBlockData> result = new ArrayList<PageBlockData>();
        for (PageBlockData item : blockData) {
            if (alias.equalsIgnoreCase(item.getAlias())) {
                result.add(item);
            }
        }
        return result;
    }
}
