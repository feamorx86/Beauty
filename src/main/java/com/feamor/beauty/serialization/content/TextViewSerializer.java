package com.feamor.beauty.serialization.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.models.views.content.ContentViewModel;
import com.feamor.beauty.models.views.content.TextViewModel;
import org.springframework.util.StringUtils;

/**
 * Created by Home on 25.10.2016.
 */
public class TextViewSerializer extends ContentViewSerializer {

    @Override
    protected Object loadFromDom(Object domData, Object... args) {
        PageDomBlock domBlock = (PageDomBlock) domData;
        TextViewModel model = new TextViewModel();

        addContentFromDom(model, domBlock);

        return model;
    }

    @Override
    protected void addContentFromDom(ContentViewModel model, PageDomBlock domBlock) {
        super.addContentFromDom(model, domBlock);
        PageBlockData textData = domBlock.findFirstDataWithType(Constants.PageData.ContentData.SIMPLE_TEXT);
        if (textData != null) {
            ((TextViewModel)model).setText(textData.getStringValue());
            ((TextViewModel)model).setTextId(textData.getDataId());
        }
    }

    @Override
    protected void saveContentToDB(ContentViewModel model, int pageId, int blockId, PageDao pageDao) {
        TextViewModel textModel = (TextViewModel) model;
        int textId = pageDao.addPageBlockData(textModel.getTextId(), blockId, pageId, Constants.PageData.ContentData.SIMPLE_TEXT, null, null, textModel.getText(), null, null);
        textModel.setTextId(textId);
    }

    @Override
    protected void saveContentToJson(ContentViewModel model, ObjectNode json) {
        TextViewModel textModel = (TextViewModel) model;
        if (textModel.getTextId() != null) {
            json.put("textId", textModel.getTextId());
        }

        if (StringUtils.isEmpty(textModel.getText())) {
            json.put("text", textModel.getText());
        }
    }

    @Override
    protected Object loadFromJson(JsonNode jsonNode, Object... args) {
        ObjectNode json = (ObjectNode) jsonNode;
        int type = (Integer) args[0];
        TextViewModel model = new TextViewModel();
        model.setType(type);

        addDataFromJson(model, json);
        return model;
    }

    @Override
    protected void addDataFromJson(ContentViewModel model, ObjectNode json) {
        super.addDataFromJson(model, json);
        TextViewModel textModel = (TextViewModel) model;
        JsonNode node;
        node = json.get("textId");
        if (node != null) {
            textModel.setTextId(node.intValue());
        }
        node = json.get("text");
        if (node != null) {
            textModel.setText(node.textValue());
        }
    }
}
