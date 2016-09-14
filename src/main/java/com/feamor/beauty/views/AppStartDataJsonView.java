package com.feamor.beauty.views;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.controllers.mobileapp.AppStartDataManager;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.UserGroupData;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 07.09.2016.
 */
public class AppStartDataJsonView extends BaseViewRender {
    @Override
    public int getFormat() {
        return ViewFactory.RenderFormats.FORMAT_JSON;
    }

    @Override
    public Class getSupportedClass() {
        return AppStartDataManager.class;
    }

    @Override
    public void renderView(HttpServletRequest request, HttpServletResponse response, int type, BaseController controller, Object... args) throws IOException {
        switch (type) {
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_Problem:
                sendProblem(response, (Integer)args[0], (String)args[1], (String)args[2]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_CheckAppVersionResult:
                checkAppVersionResult(response, (String) args[0]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onSendStartAppData:
                onSendStartAppData(response, (List<UserGroupData>) args[0]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onSendAppData:
                onSendAppData(response, (List<UserGroupData>) args[0]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onListAvailableAppTypes:
                onListAvailableAppTypes(response, (List<UserGroupData>) args[0]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onListAvailableComponentOfApp:
                onListAvailableComponentOfApp(response, (List<UserGroupData>) args[0]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onListComponentOfApp:
                onListComponentOfApp(response, (Integer)args[0], (List<UserGroupData>) args[1]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onAddNewComponentForApp:
                onAddNewComponentForApp(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3], (Integer)args[4], (String)args[5]);
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onUpdateComponentForApp:
                onAddNewComponentForApp(response, (Integer)args[0], (Integer)args[1], (Integer)args[2], (Integer)args[3], (Integer)args[4], (String)args[5]);
                //onUpdateComponentForApp
                break;
            case AppStartDataManager.AppStartDataRenderTypes.TYPE_onRemoveComponentOfApp:
                onRemoveComponentOfApp(response, (Integer)args[0], (Integer)args[1], (Integer)args[2]);
                break;
            default:
                sendProblem(response, AppStartDataManager.ResultCode.ERROR_UNKNOWN_RENDER_TYPE, Integer.toString(type), "Unknown or unsupported render type");
                break;
        }
    }

    private void sendProblem(HttpServletResponse response, int errorCode, String data, String message) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json
                .put("status", "error")
                .put("error_code", errorCode);
        if (!StringUtils.isEmpty(data)) {
            json.put("error_data", data.toString());
        }
        if (!StringUtils.isEmpty(message)) {
            json.put("error_message", message);
        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void checkAppVersionResult(HttpServletResponse response, String status) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", status);
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onSendStartAppData(HttpServletResponse response, List<UserGroupData> config) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        ObjectNode dataJson = json.putObject("data");
        for(UserGroupData data : config) {
            switch(data.getType()) {
                case Constants.GroupDataType.MobileStartAppDataTypes.SPLASH_BACKGROUND_TYPE:
                    dataJson.put("background_type", data.getIntValue());
                    break;
                case Constants.GroupDataType.MobileStartAppDataTypes.SPLASH_BACKGROUND_IMAGE_URL:
                    dataJson.put("background_image", data.getStrValue());
                    break;
                case Constants.GroupDataType.MobileStartAppDataTypes.SPLASH_TITLE_TEXT:
                    dataJson.put("title", data.getStrValue());
                    break;
                case Constants.GroupDataType.MobileStartAppDataTypes.SPLASH_MESSAGE:
                    dataJson.put("message", data.getStrValue());
                    break;
            }
        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onSendAppData(HttpServletResponse response, List<UserGroupData> config) throws  IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        ObjectNode dataJson = json.putObject("data");
//        for(UserGroupData data : config) {
//              dataJson.put("background_type", data.getIntValue());
//        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onListAvailableAppTypes(HttpServletResponse response, List<UserGroupData> items) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        ArrayNode dataJson = json.putArray("types");
        for(UserGroupData data : items) {
            ObjectNode item = dataJson.addObject();
            item.put("type", data.getType());
            item.put("id", data.getDataId());
            item.put("description", data.getStrValue());
        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onListAvailableComponentOfApp(HttpServletResponse response, List<UserGroupData> items) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        ArrayNode dataJson = json.putArray("components");
        for(UserGroupData data : items) {
            ObjectNode item = dataJson.addObject();
            item.put("id", data.getDataId());
            item.put("type", data.getType());
            item.put("description", data.getStrValue());
        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onListComponentOfApp(HttpServletResponse response, int appTypeParentId, List<UserGroupData> items) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        json.put("app_id", appTypeParentId);
        ArrayNode dataJson = json.putArray("components");
        for(UserGroupData data : items) {
            ObjectNode item = dataJson.addObject();
            item.put("id", data.getDataId());
            item.put("type", data.getType());
            item.put("link", data.getIntValue());
            item.put("comment", data.getStrValue());
        }
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

    private void onAddNewComponentForApp(HttpServletResponse response, int appTypeParentId, Integer componentLink, int componentId, int componentType, int userId, String componentDescription) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        json.put("app_id", appTypeParentId);
        json.put("component_id", componentId);
        json.put("component_type", componentType);
        json.put("component_link", componentLink);
        json.put("user_id", userId);
        json.put("component_description", componentDescription);
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }

//    private void onUpdateComponentForApp(HttpServletResponse response, int componentId, int componentType, String componentDescription, int appTypeParentId, int userId) throws IOException {
//        ObjectNode json = JsonNodeFactory.instance.objectNode();
//        json.put("status", "success");
//        json.put("app_id", appTypeParentId);
//        json.put("component_id", componentId);
//        json.put("component_type", componentType);
//        json.put("user_id", userId);
//        json.put("component_description", componentDescription);
//        response.setContentType("application/json");
//        response.getOutputStream().print(json.toString());
//    }
    private void onRemoveComponentOfApp(HttpServletResponse response, int appTypeParentId, int componentId, int userId) throws IOException {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", "success");
        json.put("app_id", appTypeParentId);
        json.put("component_id", componentId);
        json.put("user_id", userId);
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }
}
