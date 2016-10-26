package com.feamor.beauty.serialization.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.models.views.content.ContentViewModel;
import com.feamor.beauty.models.views.content.ImageViewModel;
import com.feamor.beauty.models.views.content.TitleViewModel;
import org.springframework.util.StringUtils;

/**
 * Created by Home on 25.10.2016.
 */
public class TitleViewSerializer extends ContentViewSerializer {

    @Override
    protected Object loadFromDom(Object domData, Object... args) {
        PageDomBlock domBlock = (PageDomBlock) domData;
        TitleViewModel model = new TitleViewModel();

        addContentFromDom(model, domBlock);
        return model;
    }

    @Override
    protected void addContentFromDom(ContentViewModel model, PageDomBlock domBlock) {
        super.addContentFromDom(model, domBlock);

        TitleViewModel titleModel = (TitleViewModel) model;
        PageBlockData titleData = domBlock.findFirstDataWithType(Constants.PageData.ContentData.TITLE);
        if (titleData != null) {
            titleModel.setTitle(titleData.getStringValue());
            titleModel.setLevel(titleData.getIntValue());
            titleModel.setTitleId(titleData.getDataId());
        }

    }

    @Override
    protected void saveContentToDB(ContentViewModel model, int pageId, int blockId, PageDao pageDao) {
        TitleViewModel titleModel = (TitleViewModel) model;
        int titleId = pageDao.addPageBlockData(titleModel.getTitleId(), blockId, pageId, Constants.PageData.ContentData.TITLE, null, titleModel.getLevel(), titleModel.getTitle(), null, null);
        titleModel.setTitleId(titleId);
    }

    @Override
    protected void saveContentToJson(ContentViewModel model, ObjectNode json) {
        TitleViewModel titleModel = (TitleViewModel) model;
        if (titleModel.getTitleId() != null) {
            json.put("titleId", titleModel.getTitleId());
        }

        if (StringUtils.isEmpty(titleModel.getTitle())) {
            json.put("title", titleModel.getTitle());
        }

        if (StringUtils.isEmpty(titleModel.getLevel())) {
            json.put("level", titleModel.getLevel());
        }
    }

    @Override
    protected Object loadFromJson(JsonNode jsonNode, Object... args) {
        ObjectNode json = (ObjectNode) jsonNode;
        int type = (Integer) args[0];
        TitleViewModel model = new TitleViewModel();
        model.setType(type);

        addDataFromJson(model, json);
        return model;
    }

    @Override
    protected void addDataFromJson(ContentViewModel model, ObjectNode json) {
        super.addDataFromJson(model, json);
        TitleViewModel imageModel = (TitleViewModel) model;
        JsonNode node;
        node = json.get("titleId");
        if (node != null) {
            imageModel.setTitleId(node.intValue());
        }
        node = json.get("title");
        if (node != null) {
            imageModel.setTitle(node.textValue());
        }
        node = json.get("level");
        if (node != null) {
            imageModel.setLevel(node.intValue());
        }
    }
}
