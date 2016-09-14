package com.feamor.beauty.models.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home on 31.08.2016.
 */
public class Catalog {
    protected int id;
    protected int type;
    protected Catalog parent;
    protected ArrayList<Catalog> subCatalogs;
    protected ArrayList<ServiceInCatalog> services;
    protected Data data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Catalog getParent() {
        return parent;
    }

    public void setParent(Catalog parent) {
        this.parent = parent;
    }

    public ArrayList<Catalog> getSubCatalogs() {
        return subCatalogs;
    }

    public void setSubCatalogs(ArrayList<Catalog> subCatalogs) {
        this.subCatalogs = subCatalogs;
    }

    public ArrayList<ServiceInCatalog> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceInCatalog> services) {
        this.services = services;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
