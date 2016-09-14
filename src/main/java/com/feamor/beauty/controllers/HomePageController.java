package com.feamor.beauty.controllers;

import com.feamor.beauty.blocks.templateeditor.TemplateEditorPageControl;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.dao.UserDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.BlockTemplate;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.templates.Render;
import com.feamor.beauty.tests.HTMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 18.05.2016.
 */
public class HomePageController extends BaseControllerWithPage {
    @Override
    public int controllerId() {
        return Constants.Controllers.HOME_PAGE_CONTROLLER;
    }

    @Override
    protected Page loadPage(HttpServletRequest request, HttpServletResponse response, User user) {
        Page page = pageDao.getPageOfType(Constants.Pages.HOME_PAGE);
        return page;
    }
}
