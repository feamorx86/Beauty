package com.feamor.beauty.views.mobileapp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.controllers.mobileapp.MainMenuManager;
import com.feamor.beauty.models.db.UserGroupData;
import com.feamor.beauty.views.SimpleJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 01.10.2016.
 */
public class MainMenuManagerJsonView extends SimpleJsonView {

    @Override
    public Class getSupportedClass() {
        return MainMenuManager.class;
    }

    @Override
    public void renderView(HttpServletRequest request, HttpServletResponse response, int type, BaseController controller, Object... args) throws IOException {
        switch (type) {
            case MainMenuManager.MainMenuRenderTypes.TYPE_Problem:
                sendProblem(response, (Integer)args[0], (String)args[1], (String)args[2]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_ListMenuItemTypes:
                onListMenuItemTypes(response, (List<UserGroupData>) args[0]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_EditMenuItemType:
                onEditMenuItemType(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (String)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_ListStyles:
                onListMenuItemStyles(response, (List<UserGroupData>) args[0]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_AddStyle:
                onAddStyle(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (String)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_EditStyle:
                onAddStyle(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (String)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_RemoveStyle:
                onRemoveStyle(response, (Integer)args[0]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_AddMenuItem:
                onAddMenuItem(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (String)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_RemoveMenuItem:
                onRemoveMenuItem(response, (Integer)args[0], (Integer)args[1]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_EditMenuItem:
                onAddMenuItem(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (String)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_ListMenuItems:
                onGetMenuItems(response, (Integer)args[0], (Integer)args[1], (List<UserGroupData>)args[2]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_GetMenu:
                onGetMenu(response, (Integer)args[0], (Integer)args[1], (List<UserGroupData>)args[2], (List<UserGroupData>)args[3]);
                break;
            case MainMenuManager.MainMenuRenderTypes.TYPE_InitializationDb:
                onCreateDBData(response,  (List<UserGroupData>)args[0], (List<MainMenuManager.MenuItemType>)args[1]);
                break;
            default:
                sendProblem(response, MainMenuManager.ResultCode.ERROR_UNKNOWN_RENDER_TYPE, Integer.toString(type), "Unknown or unsupported render type");
                break;
        }
    }

    private void onGetMenuItems(HttpServletResponse response, int componentId, int userId, List<UserGroupData> items) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("component", componentId);

        ArrayNode itemsJson =  json.putArray("items");
        for(UserGroupData item : items) {
            ObjectNode typeJson = itemsJson.addObject();
            typeJson.put("id", item.getDataId());
            typeJson.put("type", item.getIntValue());
            typeJson.put("config", item.getStrValue());
        }
        sendJson(response, json);
    }

    private void onGetMenu(HttpServletResponse response, int componentId, int userId, List<UserGroupData> styles, List<UserGroupData> items) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("component", componentId);

        ArrayNode stylesJson =  json.putArray("styles");
        for(UserGroupData type : styles) {
            ObjectNode typeJson = stylesJson.addObject();
            typeJson.put("id", type.getDataId());
            typeJson.put("type", type.getIntValue());
            typeJson.put("params", type.getStrValue());
        }

        ArrayNode itemsJson =  json.putArray("items");
        for(UserGroupData item : items) {
            ObjectNode typeJson = itemsJson.addObject();
            typeJson.put("id", item.getDataId());
            typeJson.put("component", item.getParentId());
            typeJson.put("type", item.getIntValue());
            typeJson.put("config", item.getStrValue());
        }
        sendJson(response, json);
    }

    private void onCreateDBData(HttpServletResponse response, List<UserGroupData> dbTypes, List<MainMenuManager.MenuItemType> classesTypes) throws IOException {
        ObjectNode json = withStatus("success");

        ArrayNode dbArrayJson = json.putArray("db_types");
        for(UserGroupData type : dbTypes) {
            ObjectNode typeJson = dbArrayJson.addObject();
            typeJson.put("id", type.getDataId());
            typeJson.put("type", type.getIntValue());
            typeJson.put("name", type.getStrValue());
        }

        ArrayNode classesTypesJson = json.putArray("classes_types");
        for(MainMenuManager.MenuItemType type : classesTypes) {
            ObjectNode typeJson = classesTypesJson.addObject();
            typeJson.put("id", type.id);
            typeJson.put("type", type.type);
            typeJson.put("name", type.name);
        }

        sendJson(response, json);
    }

    private void onRemoveMenuItem(HttpServletResponse response, int componentId, int itemId) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("id", itemId);
        json.put("component", componentId);
        sendJson(response, json);
    }

    private void onAddMenuItem(HttpServletResponse response, int itemId, int componentId, int type, String itemConfig) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("id", itemId);
        json.put("component", componentId);
        json.put("type", type);
        json.put("config", itemConfig);
        sendJson(response, json);
    }

    private void onRemoveStyle(HttpServletResponse response, int styleId) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("id", styleId);
        sendJson(response, json);
    }

    private void onAddStyle(HttpServletResponse response, int styleId, int userId, int type, String params) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("id", styleId);
        json.put("type", type);
        json.put("params", params);
        sendJson(response, json);
    }

    private void onListMenuItemStyles(HttpServletResponse response, List<UserGroupData> styles) throws IOException {
        ObjectNode json = withStatus("success");

        ArrayNode stylesJson = json.putArray("styles");
        for(UserGroupData type : styles) {
            ObjectNode typeJson = stylesJson.addObject();
            typeJson.put("id", type.getDataId());
            typeJson.put("type", type.getIntValue());
            typeJson.put("params", type.getStrValue());
        }
        sendJson(response, json);
    }

    private void onListMenuItemTypes(HttpServletResponse response, List<UserGroupData> types) throws IOException {
        ObjectNode json = withStatus("success");
        ArrayNode arrayItems = json.putArray("types");
        for(UserGroupData type : types) {
            ObjectNode typeJson = arrayItems.addObject();
            typeJson.put("id", type.getDataId());
            typeJson.put("type", type.getIntValue());
            typeJson.put("name", type.getStrValue());
        }
        sendJson(response, json);
    }

    private void onEditMenuItemType(HttpServletResponse response, int userId, int id, int type, String name) throws IOException {
        ObjectNode json = withStatus("success");
        json.put("id", id);
        json.put("type", type);
        json.put("name", name);
        sendJson(response, json);
    }
}
