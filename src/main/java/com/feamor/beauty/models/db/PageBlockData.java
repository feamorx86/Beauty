package com.feamor.beauty.models.db;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Home on 27.02.2016.
 */

@Entity(name = "pageBlockData")
@Table(name = "\"LastPageBlockData\"")
public class PageBlockData {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true, updatable = false, insertable = false)
    private int id;

    @Column(name = "\"dataId\"", nullable = false, updatable = false, insertable = false)
    private int dataId;

    @Column(name = "\"blockId\"", nullable = false, updatable = false, insertable = false)
    private int blockId;

    @Column(name = "\"pageId\"", nullable = true, updatable = false, insertable = false)
    private Integer pageId;

    @Column(name = "\"type\"", nullable = false, updatable = false, insertable = false)
    private int type;

    @Column(name = "\"position\"", updatable = false, insertable = false)
    private Integer position;

    @Column(name = "\"removed\"", updatable = false, insertable = false)
    private Boolean removed;

    @Column(name = "\"intValue\"", updatable = false, insertable = false)
    private Integer intValue;

    @Column(name = "\"strValue\"", length = 1000, updatable = false, insertable = false)
    private String stringValue;

    @Column(name = "\"alias\"", length = 200, updatable = false, insertable = false)
    private String alias;

    @Column(name = "\"dateValue\"", updatable = false, insertable = false)
    private Date dateValue;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public boolean getRemoved() {
        return removed == null || removed.booleanValue();
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "PageBlockData{" +
                "id=" + id +
                ", dataId=" + dataId +
                ", blockId=" + blockId +
                ", type=" + type +
                ", position=" + position +
                ", removed=" + removed +
                ", intValue=" + intValue +
                ", stringValue='" + stringValue + '\'' +
                ", dateValue=" + dateValue +
                '}';
    }
}
