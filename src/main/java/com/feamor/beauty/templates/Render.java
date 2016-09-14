package com.feamor.beauty.templates;

import com.feamor.beauty.controllers.BaseControllerWithPage;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Home on 22.05.2016.
 */
public class Render {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private User user;
    private BaseControllerWithPage controller;
    private ArrayList<RenderItem> stack;
    private ArrayList<BlockWithDependence> rootElements = new ArrayList<BlockWithDependence>();
    private HashMap<Integer, BlockWithDependence> pageDependence = new HashMap<Integer, BlockWithDependence>();
    private HashMap<Integer, Object> data = new HashMap<>();
    private Page page;

    public Render(HttpServletRequest request, HttpServletResponse response, User user, BaseControllerWithPage controller) {
        this.request = request;
        this.response = response;
        this.user = user;
        this.controller = controller;
        stack = new ArrayList<RenderItem>();
    }

    public Object getData(int id) {
        Object dataItem = this.data.get(id);
        return  dataItem;
    }

    public Object putData(int id, Object data) {
        return this.data.put(id, data);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public User getUser() {
        return user;
    }

    public BaseControllerWithPage getController() {
        return controller;
    }

    public void push(RenderItem item) {
        stack.add(item);
    }

    public RenderItem pop() {
        RenderItem result;
        if (stack.size() > 0) {
            result = stack.remove(stack.size() - 1);
        } else {
            result = null;
        }
        return result;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setController(BaseControllerWithPage controller) {
        this.controller = controller;
    }

    public ArrayList<RenderItem> getStack() {
        return stack;
    }

    public void setStack(ArrayList<RenderItem> stack) {
        this.stack = stack;
    }

    public ArrayList<BlockWithDependence> getRootElements() {
        return rootElements;
    }

    public BlockWithDependence getRootElementWithBlockType(int blockType) {
        BlockWithDependence result = null;
        for (BlockWithDependence dependence : rootElements) {
            if (dependence.block.getType() == blockType) {
                result = dependence;
                break;
            }
        }
        return  result;
    }

    public void setRootElements(ArrayList<BlockWithDependence> rootElements) {
        this.rootElements = rootElements;
    }

    public HashMap<Integer, BlockWithDependence> getPageDependence() {
        return pageDependence;
    }

    public void setPageDependence(HashMap<Integer, BlockWithDependence> pageDependence) {
        this.pageDependence = pageDependence;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void clear() {
        request = null;
        response = null;
        user = null;
        controller = null;
        stack.clear();
        stack = null;
        rootElements.clear();
        rootElements = null;
        pageDependence.clear();
        pageDependence = null;
        data.clear();
        data = null;
        page = null;
    }

    public static class RenderItem {
        public BlockWithDependence block;
        public int templatePosition;
        public Object extra = null;

        public RenderItem(BlockWithDependence block) {
            this.block = block;
            this.templatePosition = 0;
        }

        public TemplateElement next() {
            TemplateElement result;
            if(templatePosition < block.templateElements.size()) {
                result = block.templateElements.get(templatePosition);
                templatePosition++;
            } else {
                result = null;
            }
            return  result;
        }
    }

    public static class RenderTemplateResult {
        public static final int Error = -1;
        public static final int Continue = 0;
        public static final int BreakBlock = 1;
    }
}
