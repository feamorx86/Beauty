package com.feamor.beauty.managers;

import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.dao.PageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Home on 18.05.2016.
 */
@Component
public class RequestManager {

    @Autowired
    private PageDao pageDao;

    @Qualifier("requestMappingHandlerMapping")
    @Autowired
    RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private WebApplicationContext context;

    public static class MappingInfo {
        public BaseController controller;
        public Integer dbId;
        public String path;
        public String bean;
        public String method;
        public RequestMappingInfo mappingInfo;
    }

    private HashMap<String, MappingInfo> mappings = new HashMap<>();


    @PostConstruct
    public void initialize() throws NoSuchMethodException {
        ArrayList<PageDao.PageMapping> pageMappings =  pageDao.getPageMappings();
        Map<String, BaseController> controllers = context.getBeansOfType(BaseController.class);
        HashMap<Integer, BaseController> controllersById = new HashMap<Integer, BaseController>();
        HashMap<Integer, String> beansById = new HashMap<Integer, String>();
        for (Map.Entry<String, BaseController> entry: controllers.entrySet()) {
            controllersById.put(entry.getValue().controllerId(), entry.getValue());
            beansById.put(entry.getValue().controllerId(), entry.getKey());
        }


        for (PageDao.PageMapping mapping: pageMappings) {
            String bean = beansById.get(mapping.getClassId());
            String path = mapping.getPageMapping();
            int id = mapping.getId();
            BaseController baseController = controllersById.get(mapping.getClassId());
            if (bean != null) {
                registerMapping(id, path, bean, baseController, "handleRequest", baseController.getContentType());
            }
        }
    }

    public MappingInfo registerMapping(Integer id, String path, String bean, BaseController controller, String method, String produces) throws NoSuchMethodException {
        if (produces == null) produces = "text/html; charset=utf-8";
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo(bean+"_mapping", new PatternsRequestCondition(path), null,
                null, null, null,
                new ProducesRequestCondition(produces), null);
        MappingInfo info = new MappingInfo();
        info.controller = controller;
        info.path = path;
        info.dbId = id;
        info.bean = bean;
        info.method = method;
        info.mappingInfo = requestMappingInfo;
        mappings.put(path, info);
        handlerMapping.registerMapping(requestMappingInfo, controller, controller.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class));
        return info;
    }

    public MappingInfo getMappingWithId(int dbId) {
        for(MappingInfo info : mappings.values()) {
            if (info.dbId != null && info.dbId == dbId) {
                return  info;
            }
        }
        return  null;
    }

    public boolean unregister(String path) {
        MappingInfo info = mappings.remove(path);
        if (info != null) {
            handlerMapping.unregisterMapping(info.mappingInfo);
            return true;
        }else {
            return  false;
        }
    }

    public HashMap<String, MappingInfo> getMappings() {
        return mappings;
    }
}
