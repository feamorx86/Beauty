package com.feamor.beauty.blocks.commons;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.templates.BlockWithDependence;
import com.feamor.beauty.templates.Render;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Home on 24.06.2016.
 */
public class FooterAboutControl extends BaseBlockControl {
    @Autowired
    private SiteDao siteDao;

    @Override
    public void initialize(String beanName, ControlsManager blockManager) {
        blockManager.registerBlock(this, Constants.Blocks.Footer.FOOTER_ABOUT);
        blockManager.registerBlockWithName(this, getClass().getSimpleName());
    }

    @Override
    public void prepare(Render render, BlockWithDependence dependence) {
        HashMap<Integer, String> companyInfo = (HashMap<Integer, String>) render.getData(Constants.GroupDataType.COMPANY_INFO);
        if (companyInfo == null) {
            companyInfo = siteDao.getCompanyInfo();
            render.putData(Constants.RenderData.COMPANY_INFO, companyInfo);
        }
    }

    private String getField(Render render, int fieldType) {
        HashMap<Integer, String> companyInfo = (HashMap<Integer, String>) render.getData(Constants.GroupDataType.COMPANY_INFO);
        String text = companyInfo.get(fieldType);
        if (StringUtils.isEmpty(text)) {
            text = "";
        } else {
            text = HtmlUtils.htmlEscape(text);
        }
        return text;
    }

    @Override
    public int renderTag(String tag, Render.RenderItem current, Render render) throws IOException {
        int result;
        if ("company-name".equalsIgnoreCase(tag)) {
            render.getResponse().getOutputStream()
                    .print(getField(render, Constants.GroupDataType.CompanyInfoFieldTypes.ShortName));
            result = Render.RenderTemplateResult.Continue;
        } else if ("company-email".equalsIgnoreCase(tag)) {
            render.getResponse().getOutputStream()
                    .print(getField(render, Constants.GroupDataType.CompanyInfoFieldTypes.MainEmail));
            result = Render.RenderTemplateResult.Continue;
        } else if ("company-phone".equalsIgnoreCase(tag)) {
            render.getResponse().getOutputStream()
                    .print(getField(render, Constants.GroupDataType.CompanyInfoFieldTypes.MainPhone));
            result = Render.RenderTemplateResult.Continue;
        } else {
            //TODO: log error - unsupported tag
            result = Render.RenderTemplateResult.Continue;
        }
        return  result;
    }
}