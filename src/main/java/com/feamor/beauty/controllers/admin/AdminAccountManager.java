package com.feamor.beauty.controllers.admin;

import com.feamor.beauty.models.core.User;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 01.09.2016.
 */
public class AdminAccountManager {

    public static class Results {
        public static final int SUCCESS = 0;
        public static final int ERROR_NOT_IMPLEMENTED = 1;
    }

    public User createAdmin(User creator) {
        return null;
    }

    public int removeAdmin(User admin, User remover) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public User loginAsAdmin(int loginBy, HashMap<Integer, Object> authorization) {
        return null;
    }

    public int logoutAsAdmin(User user) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public List<User> listOnlineAdmins() {
        return null;
    }
}
