package com.feamor.beauty.controllers.mobileapp;

import com.feamor.beauty.controllers.BaseController;
import com.feamor.beauty.dao.GroupDataDAO;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.models.db.UserGroupData;
import com.feamor.beauty.views.BaseViewRender;
import com.feamor.beauty.views.ViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 15.09.2016.
 */
public class MainMenuManager extends BaseController {

    @Override
    public int controllerId() {
        return Constants.Controllers.MOBILE_MAIN_MENU;
    }

    public static class ResultCode {
        public static final int SUCCESS = 0;
        public static final int INVALID_DATA = 9;
        public static final int ERROR_NOT_SUPPORTED_ACTION = 10;
        public static final int ERROR_NOT_SUPPORTED_METHOD = 11;
        public static final int ERROR_INVALID_ARGUMENTS = 12;
        public static final int ERROR_NOT_ALLOWED_FOR_USER = 20;
        public static final int ERROR_UNKNOWN_RENDER_TYPE = 30;
        public static final int ERROR_UNKNOWN = 40;
    }

    public static class MainMenuRenderTypes {
        public static final int TYPE_Problem = 0;
        public static final int TYPE_ListMenuItemTypes = 1;
        public static final int TYPE_EditMenuItemType = 2;

        public static final int TYPE_GetMenu = 10;
        public static final int TYPE_AddMenuItem = 11;
        public static final int TYPE_RemoveMenuItem = 12;
        public static final int TYPE_EditMenuItem = 13;
        public static final int TYPE_ListMenuItems = 14;

        public static final int TYPE_ListStyles = 20;
        public static final int TYPE_AddStyle = 21;
        public static final int TYPE_RemoveStyle = 22;
        public static final int TYPE_EditStyle = 23;

        public static final int TYPE_InitializationDb = 30;
    }


    @Autowired
    private GroupDataDAO groupDataDAO;
    @Autowired
    private PageDao pageDao;
    @Autowired
    private ViewFactory viewFactory;

    /**
     * actions:
     * 1. get_menu (appId, user, version) - get menu for app type
     *      ->  {status : error, error_code : code} problem invalid app | invalid user | access denied
     *      -> { status : success, version : version, styles : [ { name : 'name', int type, style_id: id, params : { font_size:...} }, {...}...  ],
     *           items : [{ id : id, type: type, style: styleId, config { value : 'value', url : 'url' }}] }
     * 2. list_menu_item_types (user) - list all available menu item types -> { items : [ {id: id, name : name, description : description},... ]}
     * 3. list_item_styles (user) - list all items styles -> { items : [ { name : 'name', int type, style_id: id, params : { font_size:...} }, ...] }
     * 4. add_style(
     * 5. remove_style
     * 6. update_style
     * 7. add_menu_item
     * 8. remove_menu_item
     * 9. edit_menu_item
     */
    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, NoSuchMethodException {
        String action = request.getParameter("action");
        String formatStr  = request.getParameter("format");
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
                        case "list_menu_item_types" :
                            sendMenuItemTypes(render, request, response, user);
                            break;
                        case "edit_menu_item_type": {
                            Integer id = getParameter("id", request);
                            Integer type = getParameter("type", request);
                            String name = request.getParameter("name");
                            if (checkNotNull(id, type, name)) {
                                editMenuItemType(render, request, response, user, id, type, name);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "list_item_styles" : {
                            sendMenuItemStyles(render, request, response, user);
                        }
                        break;
                        case "add_style": {
                            Integer type = getParameter("type", request);
                            String params = request.getParameter("params");
                            if (checkNotNull(type, params)) {
                                addItemStyle(render, request, response, user, type, params);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "remove_style" : {
                            Integer styleId = getParameter("id", request);
                            if (styleId != null) {
                                removeStyle(render, request, response, styleId, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "edit_style" : {
                            Integer styleId = getParameter("id", request);
                            Integer type = getParameter("type", request);
                            String params = request.getParameter("params");
                            if (checkNotNull(styleId, type, params)) {
                                editItemStyle(render, request, response, styleId, type, params, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "get_menu": {
                            Integer componentId = getParameter("component_id", request);
                            if (componentId != null) {
                                sendMenu(render, request, response, componentId, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "list_menu_items": {
                            Integer componentId = getParameter("component_id", request);
                            if (componentId != null) {
                                sendMenuItems(render, request, response, componentId, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "add_menu_item": {
                            Integer componentId = getParameter("component_id", request);
                            String itemConfig = request.getParameter("value");
                            Integer type = getParameter("type", request);
                            if (checkNotNull(componentId, type) && !StringUtils.isEmpty(itemConfig)) {
                                addItem(render, request, response, componentId, type, itemConfig, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "edit_menu_item": {
                            Integer itemId = getParameter("id", request);
                            Integer componentId = getParameter("component_id", request);
                            String itemConfig = request.getParameter("value");
                            Integer type = getParameter("type", request);
                            if (checkNotNull(itemId, componentId, type) && !StringUtils.isEmpty(itemConfig)) {
                                editItem(render, request, response, componentId, itemId, type, itemConfig, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "remove_menu_item": {
                            Integer itemId = getParameter("id", request);
                            Integer componentId = getParameter("component_id", request);
                            if (checkNotNull(componentId, itemId)) {
                                removeItem(render, request, response, componentId, itemId, user);
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_INVALID_ARGUMENTS, action, "Invalid arguments");
                            }
                        }
                        break;
                        case "initialize_db_data" :{
                            if (checkUserAccessLevel(user, 2)) {
                                createInitializationData(render, request, response, user.getId());
                            } else {
                                sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "2", "Access denied");
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

    private void editMenuItemType(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user, int id, int type, String name) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData itemTypesRoot = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_TYPES_ROOT);
            groupDataDAO.update(id, Constants.GroupDataType.MobileAppMenuSetup.TYPE_ITEM, user.getId(), itemTypesRoot.getDataId(), type, name, null, null, null);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_EditMenuItemType, this, user.getId(), id, type, name);
        }
    }

    private void sendMenuItemTypes(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_TYPES_ROOT);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_ListMenuItemTypes, this, items);
        }
    }

    private void sendMenuItemStyles(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_ListStyles, this, items);
        }
    }

    private void addItemStyle(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, User user, int type, String params) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            int styleId = groupDataDAO.add(null, Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLE, user.getId(), root.getDataId(), type, params, null, null, null);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_AddStyle, this, styleId, user.getId(), type, params);
        }
    }

    private void editItemStyle(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer styleId, int type, String params, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            groupDataDAO.update(styleId, Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLE, user.getId(), root.getDataId(), type, params, null, null, null);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_EditStyle, this, styleId, user.getId(), type, params);
        }
    }

    private void removeStyle(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer styleId, User user) throws IOException {
        if (checkUserAccessLevel(user, 0)) {
            if (groupDataDAO.delete(styleId, user.getId())) {
                render.renderView(request, response, MainMenuRenderTypes.TYPE_RemoveStyle, this, styleId, user.getId());
            } else {
                sendProblem(render, request, response, ResultCode.INVALID_DATA, Integer.toString(styleId), "No menu style with such id");
            }
        } else {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        }
    }

    private void addItem(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer componentId, int type, String itemConfig, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            int itemId = groupDataDAO.add(null, Constants.GroupDataType.MobileAppMenuSetup.TYPE_ITEM, user.getId(), componentId, type, itemConfig, null, null, null);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_AddMenuItem, this, itemId, componentId, type, itemConfig, user.getId());
        }
    }

    private void editItem(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer componentId, Integer itemId, Integer type, String itemConfig, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            groupDataDAO.update(itemId, Constants.GroupDataType.MobileAppMenuSetup.TYPE_ITEM, user.getId(), componentId, type, itemConfig, null, null, null);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_EditMenuItem, this, itemId, componentId, type, itemConfig, user.getId());
        }
    }

    private void removeItem(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer componentId, Integer itemId, User user) throws IOException {
        //TODO: add checking appId
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            if (groupDataDAO.delete(itemId, user.getId())) {
                render.renderView(request, response, MainMenuRenderTypes.TYPE_RemoveMenuItem, this, componentId, itemId, user.getId());
            } else {
                sendProblem(render, request, response, ResultCode.INVALID_DATA, Integer.toString(itemId), "No menu item with such id");
            }
        }
    }

    private void sendMenu(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer componentId, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            List<UserGroupData> styles = groupDataDAO.getChildOfGroupData(root.getDataId(), false);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(componentId, false);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_GetMenu, this, componentId, user.getId(), styles, items);
        }
    }

    private void sendMenuItems(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, Integer componentId, User user) throws IOException {
        if (!checkUserAccessLevel(user, 0)) {
            sendProblem(render, request, response, ResultCode.ERROR_NOT_ALLOWED_FOR_USER, "0", "Access denied");
        } else {
            UserGroupData root = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            List<UserGroupData> items = groupDataDAO.getChildOfGroupData(componentId, false);
            render.renderView(request, response, MainMenuRenderTypes.TYPE_ListMenuItems, this, componentId, user.getId(), items);
        }
    }

    private boolean checkUserAccessLevel(User user, int level) {
        //TODO: add checking user rights!!!
        return true;
    }

    private void sendProblem(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, int errorCode, String data, String message) throws IOException {
        render.renderView(request, response, MainMenuRenderTypes.TYPE_Problem, this, errorCode, data, message);
    }

    public static class MenuItemType {
        public int type;
        public int id;
        public String name;
    }

    private void createInitializationData(BaseViewRender render, HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        logger.info("Create initialization data for main menu");

        try {
            //collect menu item types
            logger.info("get MobileAppMenuItemTypes from static fields");
            ArrayList<MenuItemType> fromClassesMenuItems = new ArrayList<>();
            Field[] menuItemTypeFields = Constants.GroupDataType.MobileAppMenuItemTypes.class.getFields();
            for (Field field : menuItemTypeFields) {
                MenuItemType itemType = new MenuItemType();
                itemType.type = field.getInt(null);
                itemType.name = field.getName();
                fromClassesMenuItems.add(itemType);
            }
            logger.info("load MenuItemTypes from db");
            logger.info("get root for : MenuItemTypes");
            UserGroupData itemTypesRoot = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_TYPES_ROOT);
            int itemTypesRootId;
            List<UserGroupData> dbItems;
            if (itemTypesRoot == null) {
                logger.info("not root, create root for : MenuItemTypes");
                itemTypesRootId = groupDataDAO.add(null, Constants.GroupDataType.MobileAppMenuSetup.TYPE_TYPES_ROOT, userId, null, null, "Mobile app menu types root", null, null, null);
                logger.info("root id for MenuItemTypes : "+itemTypesRootId);
                dbItems = new ArrayList<>();
            } else {
                itemTypesRootId = itemTypesRoot.getDataId();
                logger.info("root for MenuItemTypes found in db, id: "+itemTypesRootId);
                //remove used items
                logger.info("Synchronize items id db and items from class fields");
                dbItems = groupDataDAO.getChildOfGroupData(itemTypesRootId, false);
                if (dbItems != null && dbItems.size() > 0) {
                    for (UserGroupData data : dbItems) {
                        int index = -1;
                        for (int i = 0; i < fromClassesMenuItems.size(); i++) {
                            if (fromClassesMenuItems.get(i).type == data.getIntValue()) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            fromClassesMenuItems.remove(index);
                        }
                    }
                }
            }
            logger.info("Add new item types to db : "+fromClassesMenuItems);
            for (MenuItemType item : fromClassesMenuItems) {
                item.id = groupDataDAO.add(null, Constants.GroupDataType.MobileAppMenuSetup.TYPE_TYPES, userId, itemTypesRootId, item.type, item.name, null, null, null);
            }
            logger.info("MenuItemTypes initialized");
            //work with styles, check root style exist
            logger.info("get MenuItemStyles root");
            UserGroupData itemStylesRoot = groupDataDAO.firstGroupDataOfType(Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT);
            int itemStylesRootId;
            if (itemStylesRoot == null) {
                logger.info("not root, create root for : MenuItemStyles");
                itemStylesRootId = groupDataDAO.add(null, Constants.GroupDataType.MobileAppMenuSetup.TYPE_STYLES_ROOT, userId, null, null, "Mobile app menu styles root", null, null, null);
                logger.info("root id for MenuItemStyles : "+itemStylesRootId);
            } else {
                itemStylesRootId = itemStylesRoot.getDataId();
                logger.info("root for MenuItemStyles found in db, id: "+itemStylesRootId);
            }
            logger.info("MenuItemStyles initialized");


            render.renderView(request, response,MainMenuRenderTypes.TYPE_InitializationDb, this, dbItems, fromClassesMenuItems);
        } catch (Exception ex) {
            logger.error("Fail to Create initialization data for main menu", ex);
            ex.printStackTrace();
            sendProblem(render, request, response, ResultCode.ERROR_UNKNOWN, null, "Fail to create initialization data for Menu");
        }
    }
}