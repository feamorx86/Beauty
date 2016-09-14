package com.feamor.beauty.controllers;

import com.feamor.beauty.dao.UserDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.models.db.UserGroup;
import org.apache.log4j.Logger;
import org.hibernate.type.NumericBooleanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Home on 18.05.2016.
 */
@Controller
public class BaseController extends AbstractController {
//    protected User user;
//    protected UserGroup userGroup;
//    protected HttpServletRequest request;
//    protected HttpServletResponse response;
    protected static Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    protected UserDao userDao;

    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, NoSuchMethodException {
        return null;
    }

    public void initialize() {

    }

    public void finalize() {

    }

    public static Integer getParameter(String name, HttpServletRequest request) {
        Integer result;
        try {
            String param = request.getParameter(name);
            if (param != null) {
                result = Integer.parseInt(param);
            } else {
                result = null;
            }
        } catch (NumberFormatException ex) {
            result = null;
        }
        return result;
    }

    public static boolean checkNotNull(Object ... args) {
        if (args!=null && args.length>0) {
            for(int i = 0; i < args.length; i++){
                if (args[i] == null)
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public int controllerId() {
        return Constants.Controllers.BASE;
    }

    public String name() {
        return getClass().getSimpleName();
    }

    public String getContentType() {
        return "text/html; charset=utf-8";
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sessionId = request.getRequestedSessionId();
        User user = userDao.getUserForSession(sessionId);
        response.setContentType(getContentType());
        UserGroup userGroup = null;
        ModelAndView model;
        try {
            model = render(request, response, user);
        } catch (IOException ex) {
            logger.error("Handler request error", ex);
            model = null;
        }
        return model;
    }
}
