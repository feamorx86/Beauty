package com.feamor.beauty.controllers.mobileapp;

import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.dao.UserDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.models.db.UserGroup;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Home on 01.09.2016.
 */
public class MainScreenController extends BaseController {

    @Override
    public int controllerId() {
        return Constants.Controllers.MOBILE_AUTHORIZATION_SCREEN;
    }
}