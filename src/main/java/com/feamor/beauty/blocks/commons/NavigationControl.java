package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.dao.GroupDataDAO;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.db.UserGroupData;
import com.feamor.beauty.models.ui.MenuItem;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Home on 26.05.2016.
 */
public class NavigationControl extends BaseBlockControl {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private GroupDataDAO groupDataDAO;

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Common.MENU);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public void prepare(Render render, BlockWithDependence dependence) {
        ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) render.getData(Constants.GroupDataType.HOME_MENU);
        if (menuItems == null) {
            GroupDataDAO.GroupData menuData = groupDataDAO.loadSingleByType(Constants.GroupDataType.HOME_MENU, "intValue", "type");
            menuItems = new ArrayList<MenuItem>();
            for (int i = 0; i < menuData.getValues().size(); i++) {
                MenuItem item = new MenuItem();
                UserGroupData data = menuData.getValues().get(i);
                item.setTitle(data.getStrValue());
                i++;
                data = menuData.getValues().get(i);
                item.setUrl(data.getStrValue());
                menuItems.add(item);
            }
            render.putData(Constants.RenderData.MENU, menuItems);
        }
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if ("items".equalsIgnoreCase(tag)) {
            render.push(current);
            BlockWithDependence contentBlockItem = current.block.findFirstByAlias("menu-item");
            ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) render.getData(Constants.GroupDataType.HOME_MENU);
            for (int i = menuItems.size() - 1; i >= 0; i--) { //inverse order - because stack!!!
                Render.RenderItem renderItem = contentBlockItem.toRenderItem();
                renderItem.extra = menuItems.get(i);
                render.push(renderItem);
            }
            result = Render.RenderTemplateResult.BreakBlock;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}
