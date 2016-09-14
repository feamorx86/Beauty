package com.feamor.beauty.controllers;

import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.db.*;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import com.feamor.beauty.views.ViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 19.05.2016.
 */
public class BaseControllerWithPage extends BaseController {
    @Autowired
    protected PageDao pageDao;

    @Autowired
    protected ControlsManager blockManager;

    public Render onCreateRender(HttpServletRequest request, HttpServletResponse response, User user, Page page) {

        Render render = new Render(request, response, user, this);
        render.setPage(page);

        List<PageBlock> pageBlocks = pageDao.getBlocksOfPage(render.getPage().getPageId());
        List<PageBlockData> pageBlockData = pageDao.getPageBlockDataForPage(render.getPage().getPageId());

        for (PageBlock block  : pageBlocks) {
            BlockWithDependence dependence = new BlockWithDependence();
            dependence.block = block;
            dependence.control = blockManager.getBlock(block.getType());
            BlockTemplate template = pageDao.getTemplate(block.getTemplateId());
            if (template != null) {
                dependence.templateElements = template.toElements();
            }
            render.getPageDependence().put(block.getBlockId(), dependence);
        }

        for(PageBlock block : pageBlocks) {
            BlockWithDependence dependence = render.getPageDependence().get(block.getBlockId());
            if (block.getParentId() == null) {
                render.getRootElements().add(dependence);
            } else {
                BlockWithDependence parent = render.getPageDependence().get(block.getParentId());
                parent.dependence.add(dependence);
            }
        }

        for(PageBlockData data : pageBlockData) {
            BlockWithDependence dependence = render.getPageDependence().get(data.getBlockId());
            dependence.blockData.add(data);
        }

        for(BlockWithDependence dependence : render.getPageDependence().values()) {
            if (dependence.control != null) {
                dependence.control.prepare(render, dependence);
            }
        }

        for(BlockWithDependence dependence : render.getRootElements()) {
            render.push(dependence.toRenderItem());
        }

        return  render;
    }

    protected void onRenderPage(Render render) throws IOException {
        Render.RenderItem current = render.pop();
        while(current != null) {
            int result = current.block.control.renderTemplate(current, render);
            switch (result) {
                case  Render.RenderTemplateResult.Error:
                    //todo: log error
                    break;
                case  Render.RenderTemplateResult.Continue:
                    //do - nothing
                    break;
                case Render.RenderTemplateResult.BreakBlock:
                    current = render.pop();
                    break;
                default://todo: log error
                    break;
            }
        }
    }

    public void renderPage(HttpServletRequest request, HttpServletResponse response, User user, Page page) throws IOException {
        Render render = onCreateRender(request, response, user, page);
        onRenderPage(render);
        render.clear();
    }

    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Page page = loadPage(request, response, user);
        if (page != null) {
            renderPage(request, response, user, page);
        } else {
            //TODO Log error
            response.sendRedirect("error");
        }
        return null;
    }

    protected Page loadPage(HttpServletRequest request, HttpServletResponse response, User user) {
        return null;
    }

    public PageDao getPageDao() {
        return pageDao;
    }

    public ControlsManager getBlockManager() {
        return blockManager;
    }
}
