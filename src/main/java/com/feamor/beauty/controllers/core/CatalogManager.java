package com.feamor.beauty.controllers.core;

import com.feamor.beauty.models.core.Catalog;
import com.feamor.beauty.models.core.Service;
import com.feamor.beauty.models.core.ServiceInCatalog;

import java.util.ArrayList;

/**
 * Created by Home on 01.09.2016.
 */
public class CatalogManager {

    public static class Results {
        public static final int SUCCESS = 0;
        public static final int ERROR_NOT_IMPLEMENTED = 1;
    }

    public Catalog getCatalogById(int id) {
        return null;
    }

    public int addNewCatalog(Catalog catalog) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public int removeCatalog(Catalog catalog) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public ArrayList<Catalog> findCatalogByData(int type, Object data) {
        return null;
    }

    public Service getServiceById(int id) {
        return null;
    }

    public int addNewService(Service service) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public int removeService(Service service) {
        return Results.ERROR_NOT_IMPLEMENTED;
    }

    public ArrayList<Service> findServiceByData(int type, Object data) {
        return null;
    }

    public ServiceInCatalog addServiceToCatalog(Service service, Catalog catalog) {
        return null;
    }

    public ArrayList<ServiceInCatalog> findServiceInCatalogByData(int type, Object data) {
        return null;
    }


}
