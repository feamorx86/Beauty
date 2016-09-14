package com.feamor.beauty.pages;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;

import java.io.IOException;

/**
 * Created by Home on 19.05.2016.
 */
public class BasePageControl {

    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerPage(this, Constants.Pages.BASE_PAGE);
    }

    public void render(Object ... args) throws IOException {

    }
}
