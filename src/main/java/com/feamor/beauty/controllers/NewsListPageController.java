package com.feamor.beauty.controllers;

import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.views.NewsData;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Home on 25.06.2016.
 */
public class NewsListPageController extends BaseControllerWithPage {

    @Autowired
    private SiteDao siteDao;

    @Override
    public int controllerId() {
        return Constants.Controllers.NEWS_LIST_CONTROLLER;
    }

    @Override
    public Render onCreateRender(HttpServletRequest request, HttpServletResponse response, User user, Page page) {
        Render render = super.onCreateRender(request, response, user, page);
        String pageNumber = request.getParameter("page");
        int newsPerPage = 10;//TODO: add to
        int offset = 0;
        try {
            int pageNum = Integer.parseInt(pageNumber);
            offset = pageNum * newsPerPage;
        } catch (NumberFormatException ex) {
            offset = 0;
        }
        if (offset < 0) offset=0;
        if (newsPerPage <=0) newsPerPage = 10;
        List<NewsData> news = siteDao.loadLastNewsSummary(newsPerPage, offset);
        render.putData(Constants.RenderData.NEWS_SUMMARU_LIST, news);
        return render;
    }

    @Override
    protected Page loadPage(HttpServletRequest request, HttpServletResponse response, User user) {
        Page page = pageDao.getPageOfType(Constants.Pages.HOME_PAGE);
        return page;
    }
}
