package com.feamor.beauty.models.core;

/**
 * Created by Home on 31.08.2016.
 */
public class Service {
    protected int id;
    protected int type;
    protected Data data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
