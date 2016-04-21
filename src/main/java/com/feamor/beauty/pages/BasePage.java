package com.feamor.beauty.pages;

import com.feamor.beauty.models.Page;

/**
 * Created by Home on 27.02.2016.
 */
public class BasePage {
    protected Page page;

    public void setup(Page page) {
        this.page = page;
    }

    public void display(StringBuilder builder) {

    }

    public void clear() {

    }
}
