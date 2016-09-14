package com.feamor.beauty.controllers;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Home on 19.05.2016.
 */
public class TestPageController extends BaseControllerWithPage {

    @Override
    public int controllerId() {
        return Constants.Controllers.TEST_CONTROLLER;
    }

    @Override
    protected Page loadPage(HttpServletRequest request, HttpServletResponse response, User user) {
        List<Integer> pageIds = pageDao.listPagesOfType(Constants.Pages.TEST_PAGE_TYPE);
        Page page = null;
        if (pageIds.size() > 0) {
            page = pageDao.getPage(pageIds.get(0));
        }
        return page;
    }
}
