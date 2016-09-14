package com.feamor.beauty.models.core;

import java.util.HashMap;

/**
 * Created by Home on 31.08.2016.
 */
public class User {
    protected int id;
    protected Data userData;
    protected UserGroup group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Data getUserData() {
        return userData;
    }

    public void setUserData(Data userData) {
        this.userData = userData;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }
}
