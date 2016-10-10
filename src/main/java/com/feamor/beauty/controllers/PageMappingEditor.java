package com.feamor.beauty.controllers;

import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.RequestManager;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.tests.HTMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Home on 28.05.2016.
 */
public class PageMappingEditor extends BaseController {

    @Autowired
    private RequestManager requestManager;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PageDao pageDao;

    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, NoSuchMethodException {
        String action = request.getParameter("action");
        if (StringUtils.isEmpty(action) || "list".equalsIgnoreCase(action)) {
            drawAllMappings(request, response);
        } else if ("add".equalsIgnoreCase(action)) {
            String bean = request.getParameter("bean");
            String path = request.getParameter("path");
            String dbId = request.getParameter("id");
            Integer id = (StringUtils.isEmpty(dbId) || "new".equalsIgnoreCase(dbId) ? null : Integer.parseInt(dbId));
            addMapping(request, response, id, bean, path);
        } else if ("delete".equalsIgnoreCase(action)) {
            String dbId = request.getParameter("id");
            Integer id = (StringUtils.isEmpty(dbId) || "new".equalsIgnoreCase(dbId) ? null : Integer.parseInt(dbId));
            removeMapping(request, response, id);
        }
        return null;
    }

    private void removeMapping(HttpServletRequest request,HttpServletResponse response, Integer id) throws IOException {
        HTMLWriter writer = new HTMLWriter();
        writer.title("Remove mapping");
        writer.startPage();
        if (id == null) {
            writer.tag("h3","Fail remove mapping").writeln("There is no Id");
        } else {
            RequestManager.MappingInfo mapping = requestManager.getMappingWithId(id);
            if (mapping == null) {
                writer.tag("h3", "Fail remove mapping").writeln("There is no mapping with Id = " + id);
            } else {
                requestManager.unregister(mapping.path);
                int mappingId = pageDao.savePageMapping(id, 1, mapping.controller.controllerId(), mapping.path, true);
                writer.writeln("Mapping removed for bean : " + mapping.bean + ", path : " + mapping.path+ ", and saved, mappingId:" + mappingId);
            }
        }
        writer.ref("list", request.getServletPath());
        writer.endPage();
        response.getOutputStream().print(writer.toString());
    }

    private void addMapping(HttpServletRequest request, HttpServletResponse response, Integer dbId, String bean, String path) throws IOException, NoSuchMethodException {
        HTMLWriter writer = new HTMLWriter();
        writer.title("Add mapping");
        writer.startPage();
        if (StringUtils.isEmpty(bean)) {
            writer.tag("h3","Fail to save Mapping").writeln("Bean name is empty");
        } else if (StringUtils.isEmpty(path)) {
            writer.tag("h3","Fail to save Mapping").writeln("Path is empty");
        } else if (!context.containsBean(bean)) {
            writer.tag("h3","Fail to save Mapping").writeln("No such bean : "+bean);
        } else {
            BaseController controller = (BaseController) context.getBean(bean);
            if (dbId != null) {
                RequestManager.MappingInfo mapping = requestManager.getMappingWithId(dbId);
                if (mapping != null) {
                    requestManager.unregister(mapping.path);
                }
            }
            RequestManager.MappingInfo mapping = requestManager.registerMapping(dbId, path, bean, controller, "handleRequest", null);
            mapping.dataId = pageDao.savePageMapping(dbId, 1, controller.controllerId(), path, null);

            writer.writeln("Mapping added for bean : " + bean + ", path : " + path + ", and saved, mappingId:" + mapping.dataId);
        }
        writer.ref("list", request.getServletPath());
        writer.endPage();
        response.getOutputStream().print(writer.toString());
    }

    private void drawAllMappings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLWriter writer = new HTMLWriter();

        writer.startPage();
        writer.title("Page mappings");
        writer.startTable("position", "bean", "controllerId", "Class", "path");
        int id = 0;
        String optionNames[] = new String[requestManager.getMappings().size()+1];
        String optionValues[] = new String[requestManager.getMappings().size()+1];
        optionNames[0] = "New mapping";
        optionValues[0] = "new";
        for(RequestManager.MappingInfo mappingInfo : requestManager.getMappings().values()) {
            writer.tableLine(Integer.toString(id), mappingInfo.bean, Integer.toString(mappingInfo.controller.controllerId()), mappingInfo.controller.getClass().getSimpleName(), mappingInfo.path);

            id++;

            optionNames[id] = mappingInfo.bean+", "+Integer.toString(mappingInfo.controller.controllerId())+", "+mappingInfo.controller.getClass().getSimpleName()+", "+mappingInfo.path;
            optionValues[id] = Integer.toString(mappingInfo.dataId);
        }
        writer.endTable();
        writer
                .tag("h2", "Add new or edit mapping")
                .startForm(request.getServletPath()+"?action=add", "POST")
                .writeln("")
                .push("p").select(null, "id", optionValues, optionNames, 0).pop()
                .push("p", "Bean name").edit("bean", "").pop()
                .push("p", "Path").edit("path", "").pop()
                .push("p").submit("Save").pop()
                .endForm();
        writer.startForm(request.getServletPath()+"?action=delete", "POST")
                .tag("h2", "Remove mapping")
                .writeln("")
                .push("p").select(null, "id", Arrays.copyOfRange(optionValues, 1, optionValues.length), Arrays.copyOfRange(optionNames, 1, optionNames.length), 0).pop()
                .push("p").submit("delete").pop()
                .endForm();
        writer.endPage();
        response.getOutputStream().print(writer.toString());
    }

    @Override
    public int controllerId() {
        return Constants.Controllers.MAPPINGS_EDITOR;
    }
}
