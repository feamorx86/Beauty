package com.feamor.beauty.models.ui;

import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 04.05.2016.
 */
public class PageDom {
    private Page page;
    private HashMap<Integer, PageDomBlock> blocks;
    private List<PageDomBlock> blocksList;
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

    public int findNextDataWithType(int start, int type){
        for (int i = start; i<pageData.size(); i++) {
            if (pageData.get(i).getType() == type ) {
                return i;
            }
        }
        return -1;
    }

    public PageData findFirstDataWithType(int type){
        for (int i = 0; i<pageData.size(); i++) {
            if (pageData.get(i).getType() == type ) {
                return pageData.get(i);
            }
        }
        return null;
    }

    public List<PageData> findPageDataChildren(int parentId){
        List<PageData> result = new ArrayList<>();
        for (int i = 0; i<pageData.size(); i++) {
            if (pageData.get(i).getParentId() == parentId) {
                result.add(pageData.get(i));
            }
        }
        return result;
    }

    public void setPageData(List<PageData> pageData) {
        this.pageData = pageData;
    }

    public List<PageDomBlock> getBlocksList() {
        return blocksList;
    }

    public void setBlocksList(List<PageDomBlock> blocksList) {
        this.blocksList = blocksList;
    }
}
