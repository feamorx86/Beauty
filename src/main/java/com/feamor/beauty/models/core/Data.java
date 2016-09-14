package com.feamor.beauty.models.core;

import java.util.HashMap;

/**
 * Created by Home on 31.08.2016.
 */
public class Data {
    protected HashMap<Integer, Object> byType;

    public HashMap<Integer, Object> getByType() {
        return byType;
    }

    public void setByType(HashMap<Integer, Object> byType) {
        this.byType = byType;
    }
}
