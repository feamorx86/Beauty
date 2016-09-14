package com.feamor.beauty.controllers.core;

import com.feamor.beauty.models.core.Order;

import java.util.List;

/**
 * Created by Home on 01.09.2016.
 */
public class OrderManager {
    public static class Results {
        public static final int SUCCESS = 0;
        public static final int ERROR_NOT_IMPLEMENTED = 1;
    }

    public Order getOrderById(int id) {
        return null;
    }

    public int addNewOrder(Order order) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public int removeOrder(Order order) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public List<Order> findOrderByData(int type, Object data) {
        return null;
    }

}
