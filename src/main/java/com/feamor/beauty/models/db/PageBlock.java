package com.feamor.beauty.models.db;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */
@Entity(name = "pageBlock")
@Table(name = "\"LastPageBlock\"")
public class PageBlock {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true, updatable = false, insertable = false)
    private int id;

    @Column(name = "\"blockId\"", nullable = false, updatable = false, insertable = false)
    private int blockId;

    @Column(name = "\"parentId\"", nullable = true, updatable = false, insertable = false)
    private Integer parentId;

    @Column(name = "\"alias\"", nullable = true, updatable = false, insertable = false)
    private String alias;

    @Column(name = "\"version\"", nullable = false, updatable = false, insertable = false)
    private int version;

    @Column(name = "\"pageId\"", nullable = false, updatable = false, insertable = false)
    private int pageId;

    @Column(name = "\"type\"", nullable = false, updatable = false, insertable = false)
    private int type;

    @Column(name = "\"removed\"", updatable = false, insertable = false)
    private Boolean removed;

    @Column(name = "\"template\"", updatable = false, insertable = false)
    private Integer templateId;

    @Column(name = "\"position\"", updatable = false, insertable = false)
    private Integer position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean getRemoved() {
        return removed == null || removed.booleanValue();
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getType() {
        return type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public void setType(int type) {
        this.type = type;
    }
}
