package com.feamor.beauty.models.views;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Home on 27.05.2016.
 */
public class NewsData {
    private int creatorId;
    private int id;
    private Date time;
    private String title;
    private String summary;
    private String icon;
    private Object summaryBlock;
    private ArrayList<Object> blocks;

    public int getCreatorId() {
        return creatorId;
    }

    public Object getSummaryBlock() {
        return summaryBlock;
    }

    public void setSummaryBlock(Object summaryBlock) {
        this.summaryBlock = summaryBlock;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Object> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Object> blocks) {
        this.blocks = blocks;
    }
}
