package com.feamor.beauty.models.db;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */

@Entity(name = "page")
@Table(name = "\"LastPage\"")
public class Page {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true, updatable = false, insertable = false)
    private int id;

    @Column(name = "\"pageId\"", nullable = false, updatable = false, insertable = false)
    private int pageId;

    @Column(name = "\"version\"", nullable = false, updatable = false, insertable = false)
    private int version;

    @Column(name = "\"type\"", nullable = false, updatable = false, insertable = false)
    private int type;

    @Column(name = "\"template\"", nullable = false, updatable = false, insertable = false)
    private Integer template;

    @Column(name = "\"classId\"", length = 300, updatable = false, insertable = false)
    private String classId;

    @Column(name = "\"alias\"", length = 200, updatable = false, insertable = false)
    private String alias;

    @Column(name = "\"path\"", length = 200, updatable = false, insertable = false)
    private String path;

    @Column(name = "\"removed\"", updatable = false, insertable = false)
    private Boolean removed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getRemoved() {
        return removed == null || removed.booleanValue();
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }
}