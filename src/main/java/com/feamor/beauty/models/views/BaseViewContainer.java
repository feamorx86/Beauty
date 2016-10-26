package com.feamor.beauty.models.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 25.10.2016.
 */
public class BaseViewContainer extends BaseViewModel {
    protected ArrayList<BaseViewModel> items;

    public ArrayList<BaseViewModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<BaseViewModel> items) {
        this.items = items;
    }

    public void addItem(BaseViewModel item) {
        items.add(item);
    }
}
