package com.feamor.beauty.utils;

/**
 * Created by Home on 18.10.2016.
 */
public class CachedData {
    private long addTime;
    private Object data;
    private int index;
    private Object id;

    public CachedData(long addTime, Object id, Object data) {
        this.addTime = addTime;
        this.data = data;
        this.id= id;
    }

    public Object getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getAddTime() {
        return addTime;
    }
}
