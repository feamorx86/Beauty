package com.feamor.beauty.models.core;

/**
 * Created by Home on 31.08.2016.
 */
public class ServiceInCatalog {
    protected int id;
    protected int catalogId;
    protected int serviceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
