package com.feamor.beauty.controllers.core;

import com.feamor.beauty.models.core.User;
import com.feamor.beauty.models.core.UserGroup;

import java.util.ArrayList;

/**
 * Created by Home on 01.09.2016.
 */
public class UsersManager {

    public static class Results{
        public static final int SUCCESS = 0;
        public static final int ERROR_NOT_IMPLEMENTED = 1;
    }

    public User getUserById(int id) {
        return null;
    }

    public int addNewUser(User user) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public int removeUser(User user) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public ArrayList<User> findUsersByData(int dataType, Object data) {
        return null;
    }

    public UserGroup getGroupById(int id) {
        return null;
    }

    public int addNewGroup(UserGroup group) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public int removeGroup(UserGroup group) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public ArrayList<UserGroup> findGroupByData(int dataType, Object data) {
        return null;
    }
}
