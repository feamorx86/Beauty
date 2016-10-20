package com.feamor.beauty.views.mobileapp;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.controllers.mobileapp.NewsController;
import com.feamor.beauty.models.db.UserGroupData;
import com.feamor.beauty.views.SimpleJsonView;
import com.feamor.beauty.views.ViewFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 18.10.2016.
 */
public class NewsControllerJsonView extends SimpleJsonView {
    @Override
    public Class getSupportedClass() {
        return NewsController.class;
    }

    @Override
    public void renderView(HttpServletRequest request, HttpServletResponse response, int type, BaseController controller, Object... args) throws IOException {
        switch (type) {
            case NewsController.NewsRenderTypes.TYPE_Problem:
                sendProblem(response, (Integer)args[0], (String)args[1], (String)args[2]);
                break;
            case NewsController.NewsRenderTypes.TYPE_InitializationDb:
                onCreateDBData(response);
                break;
            case NewsController.NewsRenderTypes.TYPE_SendLastSummary:
                onSendLastNewsSummary()
                break;
            default:
                sendProblem(response, NewsController.ResultCode.ERROR_UNKNOWN_RENDER_TYPE, Integer.toString(type), "Unknown or unsupported render type");
                break;
        }
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

    private void onCreateDBData(HttpServletResponse response) throws IOException {
        ObjectNode json = withStatus("success");

//        ArrayNode dbArrayJson = json.putArray("db_types");
//        for(UserGroupData type : dbTypes) {
//            ObjectNode typeJson = dbArrayJson.addObject();
//            typeJson.put("id", type.getDataId());
//            typeJson.put("type", type.getIntValue());
//            typeJson.put("name", type.getStrValue());
//        }
//
//        ArrayNode classesTypesJson = json.putArray("classes_types");
//        for(MainMenuManager.MenuItemType type : classesTypes) {
//            ObjectNode typeJson = classesTypesJson.addObject();
//            typeJson.put("id", type.id);
//            typeJson.put("type", type.type);
//            typeJson.put("name", type.name);
//        }

        sendJson(response, json);
    }
}
