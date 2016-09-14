package com.feamor.beauty.models.db;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Home on 02.05.2016.
 */
@Entity(name = "pageData")
@Table(name = "\"LastPageData\"")
public class PageData {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true, updatable = false, insertable = false)
    private int id;

    @Column(name = "\"dataId\"", updatable = false, insertable = false)
    private int dataId;

    @Column(name = "\"version\"", updatable = false, insertable = false)
    private int bersion;

    @Column(name = "\"pageId\"", updatable = false, insertable = false)
    private int pageId;

    @Column(name = "\"type\"", nullable = false, updatable = false, insertable = false)
    private int type;

    @Column(name = "\"intValue\"", updatable = false, insertable = false)
    private Integer intValue;

    @Column(name = "\"strValue\"", updatable = false, insertable = false)
    private String stringValue;

    @Column(name = "\"textValue\"", updatable = false, insertable = false)
    private String textValue;

    @Column(name = "\"floatValue\"", updatable = false, insertable = false)
    private Double doubleValue;

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

    public int getBersion() {
        return bersion;
    }

    public void setBersion(int bersion) {
        this.bersion = bersion;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
}