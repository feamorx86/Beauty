package com.feamor.beauty.controllers.mobileapp;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.dao.GroupDataDAO;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.User;

import com.feamor.beauty.models.db.UserGroupData;
import com.feamor.beauty.views.BaseViewRender;
import com.feamor.beauty.views.ViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Home on 01.09.2016.
 */
public class AppStartDataManager extends BaseController {

    @Override
    public int controllerId() {
        return Constants.Controllers.MOBILE_APP_START_DATA;
    }

    public static class ResultCode {
        public static final int SUCCESS = 0;
        public static final int INVALID_DATA = 9;
        public static final int ERROR_NOT_SUPPORTED_ACTION = 10;
        public static final int ERROR_NOT_SUPPORTED_METHOD = 11;
        public static final int ERROR_INVALID_ARGUMENTS = 12;
        public static final int ERROR_NOT_ALLOWED_FOR_USER = 20;
        public static final int ERROR_UNKNOWN_RENDER_TYPE = 30;
    }

    public static class AppStartDataRenderTypes {
        public static final int TYPE_Problem = 0;
        public static final int TYPE_CheckAppVersionResult = 1;
        public static final int TYPE_onSendStartAppData = 2;
        public static final int TYPE_onSendAppData = 3;
        public static final int TYPE_onAddNewComponentForApp = 4;
        public static final int TYPE_onUpdateComponentForApp = 5;
        public static final int TYPE_onRemoveComponentOfApp = 6;
        public static final int TYPE_onListAvailableComponentOfApp = 7;
        public static final int TYPE_onListAvailableAppTypes = 8;
        public static final int TYPE_onListComponentOfApp = 9;
    }


    @Autowired
    private GroupDataDAO groupDataDAO;
    @Autowired
    private PageDao pageDao;
    @Autowired
    private ViewFactory viewFactory;

    ComponentEditorUrlProvider editorUrlProvider = new ComponentEditorUrlProvider();

    /**
     * actions:
     * 1. check_version (int version_code) - check does client app version are supported.
     *      -> problem : no code
     *      -> { status: outdated | supported | newest | invalid }
     * 2. app_data () - list data used by app (menu, news, catalog,...)
     *      -> {status : success, version: int, items [ { type: news | menu |..., url : str, version : int  }, ...{}] }
     * 3. start_data () - get first needed data (splash image, title, link to app data)
     *      -> {status: success, data : { @link com.feamor.beauty.managers.Constants.GroupDataType.MobileStartAppDataTypes } }
     * 4. list_apps (user) - list all available app types: client, employer, admin, ...
     *      -> problem: access denied
     *      -> {status: success, types : [ {id : int, type: int, description: str},... ] }
     * 5. list_all_components(user) - list all available types of app components: menu, clients news,....
     *      -> problem: access denied
     *      -> {status: success, components : [ {id : int, type: int, description: str},... ] }
     * 6. list_app_components (app_id : int)- list components of app
     * 7. add_app_component (app_id : int, component_link: int, component_type : int, component_comment : String)   - add component to app
     * 8. remove_app_component (app_id : int, component_id: int)- remove component from app.
     * 9. update_app_component(app_id : int, component_link: int, component_type : int, component_comment : String, component_id : int) -
     *      update component.
     */
    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, NoSuchMethodException {

        String action = request.getParameter("action");
        String formatStr  =request.getParameter("format");
        int format  = ViewFactory.RenderFormats.forStingOrDefault(formatStr);
        BaseViewRender render = viewFactory.getRender(this, format);

        if(render == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().print("Unsupported format : "+ (StringUtils.isEmpty(formatStr)?"<empty>":formatStr));
        } else {
            if (HttpMethod.GET.matches(request.getMethod())) {
                if (StringUtils.isEmpty(action)) {
                    sendProblem(render, request, response, ResultCode.ERROR_NOT_SUPPORTED_ACTION, "<no-action>", "This action does not supported.");
                } else {
                    switch (action) {
                        case "check_version":
                            checkAppVersion(render, request, response);
                            break;
                        case "app_data":
                            sendAppData(render, request, response, user);
                            break;
                        case "start_data":
                            sendStartAppData(render, request, response);
                            break;
                        case "list_apps":
                            listAvailableAppTypes(render, request, response, user);
                            break;
                        case "list_all_components":
                            listAvailableAppComponents(render, request, response, user);
                            break;
                        case "list_app_components": {
                            Integer appId = getParameter("app_id", request);
                            if (checkNotNull(appId)) {
                                getComponentsForAppType(appId, render, request, response, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "add_app_component": {
                            Integer appId = getParameter("app_id", request);
                            Integer linked = getParameter("component_link", request);
                            Integer componentType = getParameter("component_type", request);
                            String componentDescription = request.getParameter("component_comment");
                            if (checkNotNull(appId, componentType)) {
                                addNewComponentForApp(linked, componentType, componentDescription, appId, render, request, response, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "remove_app_component": {
                            Integer appId = getParameter("app_id", request);
                            Integer componentId = getParameter("component_id", request);
                            if (checkNotNull(appId, componentId)) {
                                removeComponentOfApp(componentId, appId, render, request, response, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "update_app_component": {
                            Integer appId = getParameter("app_id", request);
                            Integer linked = getParameter("component_link", request);
                            Integer componentId = getParameter("component_id", request);
                            Integer componentType = getParameter("component_type", request);
                            String componentDescription = request.getParameter("component_comment");
                            if (checkNotNull(appId, componentId, componentType)) {
                                updateComponentForApp(linked, componentId, componentType, componentDescription, appId, render, request, response, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }

                        }
                        break;
                        case "open_editor":{
                            Integer appId = getParameter("app_id", request);
                            Integer componentId = getParameter("component_id", request);
                            if (checkNotNull(appId, componentId)) {
                                openEditorFor(render, request, response, appId, componentId, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        default:
                            sendProblem(render, request, response, ResultCode.ERROR_NOT_SUPPORTED_ACTION, action, "This action does not supported.");
                            break;
                    }
                }
            } else {
                sendProblem(render, request, response, ResultCode.ERROR_NOT_SUPPORTED_METHOD, request.getMethod(), "This request method does not supported.");
            }
        }
        return null;
    }

    private void openEditorFor(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, int appId, int componentId, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            //groupDataDAO.update(componentId, componentType, user.getId(), appTypeParentId, linkedId, componentComment, null, null, null);
            UserGroupData component = groupDataDAO.getById(componentId);
            if (component == null || component.getParentId() == appId) {
                String url = editorUrlProvider.getUrl(component.getType());
                if (StringUtils.isEmpty(url)) {
                    sendProblem(render, request, response, ResultCode.INVALID_DATA, null, "Can't find editor for such type of component : "+component.getType());
                } else {
                    url = String.format(url, componentId);
                    response.sendRedirect(url);
                }
            } else {
                sendProblem(render, request, response, ResultCode.INVALID_DATA, null, "Can't find component with id : "+componentId+"for app : "+appId);
            }
        }
    }

    private void sendProblem(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, int errorCode, String data, String message) throws IOException {
        render.renderView(request, response, AppStartDataRenderTypes.TYPE_Problem, this, errorCode, data, message);
    }
    private void sendStartAppData(BaseViewRender render, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MOBILE_START_DATA_ROOT);
        List<UserGroupData> config = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
        render.renderView(request, response, AppStartDataRenderTypes.TYPE_onSendStartAppData, this, config);
    }

    private void sendAppData(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        //news and sales
        //menu
        //contacts
        //catalog
        //stuff
        //personal info
        //beast works
        //schedule
        render.renderView(request, response, AppStartDataRenderTypes.TYPE_onSendAppData, this, new ArrayList<UserGroupData>());
    }

    private void checkAppVersion(BaseViewRender render, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String version = request.getParameter("version_code");
        if (StringUtils.isEmpty(version)) {
            sendProblem(render, request, response, ResultCode.INVALID_DATA, null, "There is no version code.");
        } else {
            Integer code = null;
            try {
                code = Integer.parseInt(version);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                code = null;
            }
            if (code == null) {
                sendProblem(render, request, response, ResultCode.INVALID_DATA, version, "Version code does not Integer value.");
            } else {
                String status = doesVersionSupported(code);
                render.renderView(request, response, AppStartDataRenderTypes.TYPE_CheckAppVersionResult, this, status);
            }
        }
    }

    private String doesVersionSupported(int versionCode) {
        String result;
        if (versionCode < 0) {
            result = "outdated";
        } else if (versionCode < 10) {
            result = "supported";
        } else if (versionCode == 10) {
            result = "newest";
        } else {
            result = "invalid";
        }
        return  result;
    }

    private boolean checkUserAccessLevel(User user, int level) {
        //TODO: add checking user rights!!!
        return true;
    }

    private void listAvailableAppComponents(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MOBILE_COMPONENTS_ROOT);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
            render.renderView(request, response, AppStartDataRenderTypes.TYPE_onListAvailableComponentOfApp, this, items);
        }
    }

    private void listAvailableAppTypes(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MOBILE_APP_TYPES);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
            render.renderView(request, response, AppStartDataRenderTypes.TYPE_onListAvailableAppTypes, this, items);
        }
    }

    private void getComponentsForAppType(int appTypeParentId, BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(appTypeParentId, false);
            render.renderView(request, response, AppStartDataRenderTypes.TYPE_onListComponentOfApp, this, appTypeParentId, items);
        }
    }

    private void addNewComponentForApp(Integer linkedId, int componentType, String componentComment, int appTypeParentId,
                                       BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            int componentId = groupDataDAO.add(null, componentType, user.getId(), appTypeParentId, linkedId, componentComment, null, null, null);
            render.renderView(request, response, AppStartDataRenderTypes.TYPE_onAddNewComponentForApp, this,
                    appTypeParentId, linkedId, componentId, componentType, user.getId(), componentComment);
        }
    }

    private void updateComponentForApp(Integer linkedId,int componentId, int componentType, String componentComment, int appTypeParentId,
                                       BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            groupDataDAO.update(componentId, componentType, user.getId(), appTypeParentId, linkedId, componentComment, null, null, null);
            render.renderView(request, response, AppStartDataRenderTypes.TYPE_onAddNewComponentForApp, this,
                    appTypeParentId, linkedId, componentId, componentType, user.getId(), componentComment);
        }
    }

    private void removeComponentOfApp(int componentId, int appTypeParentId,
                                      BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            if (groupDataDAO.delete(componentId, user.getId())) {
                render.renderView(request, response, AppStartDataRenderTypes.TYPE_onRemoveComponentOfApp, this,
                        appTypeParentId, componentId, user.getId());
            } else {
                sendProblem(render, request, response, ResultCode.INVALID_DATA, Integer.toString(componentId), "No component with such id");
            }
        }
    }

    public static class ComponentEditorUrlProvider {
        private HashMap<Integer, String> urls = new HashMap<>();
        public ComponentEditorUrlProvider() {
            initialize();
        }

        public String getUrl(int componentType) {
            return urls.get(componentType);
        }

        private void initialize() {
            urls.put(Constants.GroupDataType.MobileAppCommonComponents.CLIENT_MAIN_MENU, "/web/pages/app_components_manager/menu_editor.html?component_id=%d");
        }
    }
}
