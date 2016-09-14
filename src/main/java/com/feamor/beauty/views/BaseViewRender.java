package com.feamor.beauty.views;

import com.feamor.beauty.Config;
import com.feamor.beauty.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Home on 06.09.2016.
 */
public class BaseViewRender {

    public int getFormat() {
        return ViewFactory.RenderFormats.DEFAULT;
    }

    public String [] getSupportedBeans() {
        return null;
    }

    public Class getSupportedClass() {
        return null;
    }

    public void renderView(HttpServletRequest request, HttpServletResponse response, int type, BaseController controller, Object ... args) throws IOException {

    }
}
