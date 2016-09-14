package com.feamor.beauty.models.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Home on 09.05.2016.
 */
@Entity(name = "userGroupData")
@Table(name = "\"LastUserGroupData\"")
public class UserGroupData {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true, updatable = false, insertable = false)
    private int id;

    @Column(name = "\"dataId\"", nullable = false, updatable = false, insertable = false)
    private int dataId;

    @Column(name = "\"version\"", nullable = false, updatable = false, insertable = false)
    private int version;

    @Column(name = "\"type\"", nullable = false, updatable = false, insertable = false)
    private int type;

    @Column(name = "\"creator\"",  updatable = false, insertable = false)
    private Integer userCreatorId;

    @Column(name = "\"intValue\"",  updatable = false, insertable = false)
    private Integer intValue;

    @Column(name = "\"textValue\"",  updatable = false, insertable = false)
    private String textValue;

    @Column(name = "\"strValue\"",  updatable = false, insertable = false)
    private String strValue;

    @Column(name = "\"dateValue\"",  updatable = false, insertable = false)
    private Date dateValue;

    @Column(name = "\"parentId\"",  updatable = false, insertable = false)
    private Integer parentId;

    @Column(name = "\"removed\"",  updatable = false, insertable = false)
    private Boolean removed;

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

    public Integer getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(Integer userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
}
