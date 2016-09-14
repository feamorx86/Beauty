package com.feamor.beauty.blocks.news;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 25.06.2016.
 */
public class NewsPagesControl extends BaseBlockControl {

    @Autowired
    private SiteDao siteDao;

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Content.News.LIST_CONTENT_ITEM);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
//        1. Сделть сделать передачу запроса кол-ва страниц новостей, расчет и номер текущей страницы передачу параметров.
//        2. Сделать возможность скрывать страницы, если всё помещается на 1 страницу
//        3. добавить счктчик кол-ва новостей на страницк
//        4. сделать корректную работу счетчика страниц.
//        5. добавить единый контроллер обработки номера страницы и кнопкок next-prev
        if ("switch-previous".equalsIgnoreCase(tag)) {
            render.push(current);
            String text = current.block.findFirstDataByAlias("title").getStringValue();
            if (StringUtils.isEmpty(text)) text = "";
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("switch-next".equalsIgnoreCase(tag)) {
            String text = current.block.findFirstDataByAlias("title").getStringValue();
            if (StringUtils.isEmpty(text)) text = "";
            render.getResponse().getOutputStream().print(text);
            result = Render.RenderTemplateResult.Continue;
        } else if ("items".equalsIgnoreCase(tag)) {
            render.push(current);
            List<BlockWithDependence> contentItems = current.block.findAllWithAlias(tag);
            for (int i = contentItems.size() - 1; i >= 0; i--) { //inverse order - because stack!!!
                Render.RenderItem renderItem = contentItems.get(i).toRenderItem();
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
