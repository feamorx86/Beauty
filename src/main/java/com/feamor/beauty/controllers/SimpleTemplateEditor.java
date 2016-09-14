package com.feamor.beauty.controllers;

import com.feamor.beauty.blocks.templateeditor.TemplateEditorPageControl;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.dao.Texts;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.BlockTemplate;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.templates.Render;
import com.feamor.beauty.templates.TemplateElement;
import com.feamor.beauty.templates.TemplateScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Home on 23.05.2016.
 */
public class SimpleTemplateEditor extends BaseControllerWithPage {

    @Autowired
    private PageDao pageDao;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private Texts texts;

    @Override
    public int controllerId() {
        return Constants.Controllers.TEMPLATE_EDITOR;
    }

    private String text(String name) {
        return texts.getDefault("template_editor", name);
    }


    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        String action = request.getParameter("action");
        int platform = Constants.Platform.fromString("Platform");

        if (HttpMethod.POST.matches(request.getMethod())) {
            if (!StringUtils.isEmpty(action) && "save".equalsIgnoreCase(action)) {
                saveTemplate(request, response, user);
            } else if ("check".equalsIgnoreCase(action)) {
                checkTemplate(request, response, user);
            }
        } else {
            if (StringUtils.isEmpty(action)) {
                newTemplatePage(request, response, user);
            } else if ("new".equalsIgnoreCase(action)) {
                newTemplatePage(request, response, user);
            } else if ("list".equalsIgnoreCase(action)){
                listTemplatePages(request, response, user);
            } else if ("view".equalsIgnoreCase(action)){
                editTemplate(request, response, user);
            } else if ("edit".equalsIgnoreCase(action)) {
                editTemplate(request, response, user);
            }
        }
        return null;
    }

    private void checkTemplate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        int status;
        String message;

        Integer templateId = extractId(request);
        String alias = request.getParameter("alias");
        String content = request.getParameter("content");

        if (content == null) {
            status = 1;
            message = "Error, there is no content";
        } else if (alias == null) {
            status = 2;
            message = "Error, there is no template alias";
        } else {
            ByteArrayInputStream textStream = new ByteArrayInputStream(content.getBytes());
            status = 0;
            message = "Success";
            try {
                TemplateScanner scanner = new TemplateScanner(textStream);
                ArrayList<TemplateElement> elements = new ArrayList<TemplateElement>();

                TemplateElement element = scanner.next();
                while (element != null && element.getType() != TemplateElement.Types.DOCUMENT_END) {
                    elements.add(element);
                    element = scanner.next();
                }
            }catch (Exception ex) {
                status = 3;
                message = ex.toString();
            } finally {
                textStream.close();
            }
        }
        response.getOutputStream().print("status: "+status+", message : "+message);
    }

    private void saveTemplate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Integer templateId = extractId(request);
        String alias = request.getParameter("alias");
        String content = request.getParameter("content");

        if (content == null) {
            content = "";
        }

        if (alias == null) {
            alias = "";
        }

        ByteArrayInputStream textStream = new ByteArrayInputStream(content.getBytes());
        TemplateScanner scanner = new TemplateScanner(textStream);
        ArrayList<TemplateElement> elements = new ArrayList<TemplateElement>();

        TemplateElement element = scanner.next();
        while(element != null && element.getType()!=TemplateElement.Types.DOCUMENT_END) {
            elements.add(element);
            element = scanner.next();
        }
        textStream.close();

        BlockTemplate template = BlockTemplate.createFrom(templateId, 0, alias, "", elements);

        pageDao.saveOrUpdateTemplate(template);

        EditorData editor = new EditorData();
        editor.message = new MessageData(false, text("save_success_title"), text("save_success_message"), text("save_success_description"), "/template?action=view&id="+template.getId());
        editor.alias = template.getAlias();
        editor.menu = loadMenu();
        editor.templateId = template.getId();

        StringBuilder contentStringBuilder = new StringBuilder();
        template.printTo(contentStringBuilder);
        editor.content = contentStringBuilder.toString();

        editor.templateId = template.getId();
        editor.editUrl = "/template?action=save&id="+template.getId();
        editor.type = EditorData.EDIT;

        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_VIEW_TEMPLATE, editor, request, response, user);
    }

    @Override
    protected Page loadPage(HttpServletRequest request, HttpServletResponse response, User user) {
        return super.loadPage(request, response, user);
    }

    private MenuData loadMenu() {
        MenuData menu = new MenuData();
        String json = siteDao.getStringOfType(Constants.GroupDataType.TEMPLATE_EDITOR_MENU_JSON);
        JsonParser parser = new JacksonJsonParser();
        Map<String, Object> menuJson= parser.parseMap(json);
        menu.title = (String) menuJson.get("title");
        ArrayList<Object> menuItems = (ArrayList<Object>) menuJson.get("items");
        if (menuItems!=null && menuItems.size() > 0) {
            for(Object o : menuItems) {
                Map<String, Object> menuItemJson = (Map<String, Object>) o;
                String title = (String) menuItemJson.get("name");
                String action = (String) menuItemJson.get("action");
                menu.menuNames.add(title);
                menu.menuUrls.add("/template?action="+action);
            }
        }
        return menu;
    }

    private void renderPageOfType(int pageType, EditorData editor, HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Page page = pageDao.getPageOfType(pageType);
        Render render = onCreateRender(request, response, user, page);
        for (Render.RenderItem item : render.getStack()) {
            if (item.block.control instanceof TemplateEditorPageControl) {
                item.extra = editor;
            }
        }
        onRenderPage(render);
    }

    private void listTemplatePages(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        EditorData editor = new EditorData();
        editor.menu = loadMenu();
        ArrayList<BlockTemplate.TemplateHeader> headers = pageDao.listTemplateHeaders();
        editor.listNames = new ArrayList<String>();
        editor.listIds = new ArrayList<Integer>();
        editor.listUrls = new ArrayList<String>();
        for (BlockTemplate.TemplateHeader header : headers) {
            if (StringUtils.isEmpty(header.alias)) {
                editor.listNames.add("<empty>");
            } else {
                editor.listNames.add(header.alias);
            }
            editor.listIds.add(header.id);
            editor.listUrls.add("/template?action=view&id="+header.id);
        }

        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_LIST_TEMPLATES, editor, request, response, user);
    }

    private void unknownAction(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        EditorData editor = new EditorData();
        editor.menu = loadMenu();
        editor.alias = "";
        //editor.message = new MessageData()
        editor.content = "";
        editor.type = EditorData.VIEW;
        editor.editUrl = "/template?action=save";
        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_VIEW_TEMPLATE, editor, request, response, user);
    }

    private void newTemplatePage(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        EditorData editor = new EditorData();
        editor.menu = loadMenu();
        editor.alias = "";
        editor.content = "";
        editor.type = EditorData.EDIT;
        editor.editUrl = "/template?action=save";

        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_VIEW_TEMPLATE, editor, request, response, user);
    }

    private void viewTemplate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Integer templateId = extractId(request);
        EditorData editor = new EditorData();
        editor.menu = loadMenu();

        BlockTemplate template = null;
        if (templateId != null) {
            template =  pageDao.getTemplate(templateId);
        }

        if (template==null) {
            editor.message = new MessageData(true, text("view_not_found_title"), text("view_not_found_message"), text("view_not_found_description"), "/template?action=list");
            editor.alias = "";
            editor.content = "";
            editor.type = EditorData.VIEW;
        } else {
            editor.alias = template.getAlias();
            editor.templateId = template.getId();
            StringBuilder content = new StringBuilder();
            template.printTo(content);
            editor.content = content.toString();
            editor.type = EditorData.VIEW;
        }

        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_VIEW_TEMPLATE, editor, request, response, user);
    }

    private void editTemplate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Integer templateId = extractId(request);
        EditorData editor = new EditorData();
        editor.menu = loadMenu();

        BlockTemplate template = null;
        if (templateId != null) {
            template =  pageDao.getTemplate(templateId);
        }

        if (template==null) {
            editor.message = new MessageData(true, text("view_not_found_title"), text("view_not_found_message"), text("view_not_found_description"), "/template?action=list");
            editor.alias = "";
            editor.content = "";
            editor.type = EditorData.EDIT;
        } else {
            editor.alias = template.getAlias();
            editor.templateId = template.getId();
            editor.editUrl = "/template?action=save&id="+templateId;
            StringBuilder content = new StringBuilder();
            template.printTo(content);
            editor.content = content.toString();
            editor.type = EditorData.EDIT;
        }

        renderPageOfType(Constants.Pages.TEMPLATE_EDITOR_VIEW_TEMPLATE, editor, request, response, user);
    }

    private Integer extractId(HttpServletRequest request) {
        Integer templateId = null;
        try {
            String templateStr = request.getParameter("id");
            if (templateStr!=null) {
                templateId = Integer.valueOf(templateStr);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return  templateId;
    }


    public static class MenuData {
        public String title;
        public ArrayList<String> menuNames = new ArrayList<String>();
        public ArrayList<String> menuUrls = new ArrayList<String>();
    }

    public static class MessageData {
        boolean isError;
        public String title;
        public String message;
        public String description;
        public String url;

        public MessageData() {

        }

        public static MessageData create(boolean isError, String title, String message, String description, String url) {
            MessageData messageData = new MessageData();
            messageData.isError = isError;
            messageData.title = title;
            messageData.message = message;
            messageData.description = description;
            messageData.url = url;
            return  messageData;
        }

        public MessageData(boolean isError, String title, String message, String description, String url) {
            this.isError = isError;
            this.title = title;
            this.message = message;
            this.description = description;
            this.url = url;
        }
    }

    public static class EditorData{

        public static final int EDIT = 0;
        public static final int VIEW = 1;
        public static final int LIST = 2;

        public MenuData menu;
        public MessageData message;
        public String alias;
        public Integer templateId = null;
        public String editUrl;
        public String content;

        public int type = EDIT;

        public ArrayList<Integer> listIds;
        public ArrayList<String> listNames;
        public ArrayList<String> listUrls;

    }
}