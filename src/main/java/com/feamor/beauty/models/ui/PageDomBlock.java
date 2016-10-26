package com.feamor.beauty.models.ui;

import com.feamor.beauty.models.db.PageBlock;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.ui.PageDom;

import java.util.List;

/**
 * Created by Home on 04.05.2016.
 */
public class PageDomBlock {
    private PageDom page;
    private PageBlock block;
    private List<PageBlockData> blockData;
    public PageDom getPage() {
        return page;
    }

    public void setPage(PageDom page) {
        this.page = page;
    }

    public PageBlock getBlock() {
        return block;
    }

    public void setBlock(PageBlock block) {
        this.block = block;
    }

    public List<PageBlockData> getBlockData() {
        return blockData;
    }

    public PageBlockData findFirstDataWithType(int type) {
        PageBlockData result = null;
        for (PageBlockData data : blockData) {
            if (data.getType() == type ) {
                result = data;
                break;
            }
        }
        return result;
    }

    public void setBlockData(List<PageBlockData> blockData) {
        this.blockData = blockData;
    }
}
