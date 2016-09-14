package com.feamor.beauty.models.ui;

import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 04.05.2016.
 */
public class PageDom {
    private Page page;
    private HashMap<Integer, PageDomBlock> blocks;
    private List<PageData> pageData;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public HashMap<Integer, PageDomBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<Integer, PageDomBlock> blocks) {
        this.blocks = blocks;
    }

    public PageDomBlock block(int id) {
        return blocks.get(id);
    }

    public List<PageData> getPageData() {
        return pageData;
    }

    public void setPageData(List<PageData> pageData) {
        this.pageData = pageData;
    }
}
