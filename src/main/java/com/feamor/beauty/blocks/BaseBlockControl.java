package com.feamor.beauty.blocks;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import com.feamor.beauty.templates.TemplateElement;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */
public class BaseBlockControl {

    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.BASE_BLOCK);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    public void render(Object ... args) {

    }

    public void prepare(Render render, BlockWithDependence dependence) {

    }

    public void renderSubContent(Render.RenderItem current, BlockWithDependence contentBlock, Render render) {
    }

    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        return Render.RenderTemplateResult.Continue;
    }

    public String stringFromBlockForTag(String tag, Render.RenderItem current) {
        PageBlockData data = current.block.findFirstDataByAlias(tag);
        if (data != null) {
            return data.getStringValue();
        }
        return null;
    }

    public Integer integerFromBlockForTag(String tag, Render.RenderItem current) {
        PageBlockData data = current.block.findFirstDataByAlias(tag);
        if (data != null) {
            return data.getIntValue();
        }
        return null;
    }

    protected int putTextAndContinue(String text, Render.RenderItem current, Render render) throws IOException {
        String append;
        if (StringUtils.isEmpty(text)) {
            append = "";
        } else {
            append = HtmlUtils.htmlEscape(text);
        }
        render.getResponse().getOutputStream().print(append);
        return Render.RenderTemplateResult.Continue;
    }

    protected int putBlockAndBreak(BlockWithDependence block, Render.RenderItem current, Render render) throws IOException {
        render.push(current);
        Render.RenderItem renderItem = block.toRenderItem();
        render.push(renderItem);
        return Render.RenderTemplateResult.BreakBlock;
    }

    protected int putListAndBreak(List<BlockWithDependence> list, Render.RenderItem current, Render render) throws IOException {
        render.push(current);
        for (int i = list.size() - 1; i >= 0; i--) {
            BlockWithDependence block = list.get(i);
            Render.RenderItem renderItem = block.toRenderItem();
            render.push(renderItem);
        }
        return Render.RenderTemplateResult.BreakBlock;
    }

    public int renderTemplate(Render.RenderItem current, Render render) throws IOException {
        int result;
        TemplateElement templateElement = current.next();
        if (templateElement != null) {
            switch(templateElement.getType()) {
                case TemplateElement.Types.TEXT: {
                    String text = templateElement.getText();
                    render.getResponse().getOutputStream().print(text);
                    result = Render.RenderTemplateResult.Continue;
                }
                break;
                case TemplateElement.Types.TAG: {
                    String tag = templateElement.getText();
                    result = renderTag(tag, current, render);
                }
                break;
                default:
                    //TODO: log error - has unsupported template type
                    result = Render.RenderTemplateResult.Continue;
                    break;
            }
        } else {
            result = Render.RenderTemplateResult.BreakBlock;
        }
        return result;
    }
}
