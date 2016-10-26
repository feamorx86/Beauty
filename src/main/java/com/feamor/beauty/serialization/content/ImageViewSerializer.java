package com.feamor.beauty.serialization.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.models.views.content.ContentViewModel;
import com.feamor.beauty.models.views.content.ImageViewModel;
import com.feamor.beauty.models.views.content.TextViewModel;
import org.springframework.util.StringUtils;

/**
 * Created by Home on 25.10.2016.
 */
public class ImageViewSerializer extends ContentViewSerializer {

    @Override
    protected Object loadFromDom(Object domData, Object... args) {
        PageDomBlock domBlock = (PageDomBlock) domData;
        ImageViewModel model = new ImageViewModel();

        addContentFromDom(model, domBlock);
        return model;
    }

    @Override
    protected void addContentFromDom(ContentViewModel model, PageDomBlock domBlock) {
        super.addContentFromDom(model, domBlock);
        PageBlockData imageData = domBlock.findFirstDataWithType(Constants.PageData.ContentData.IMAGE_URL);
        if (imageData != null) {
            ((ImageViewModel)model).setImageUrl(imageData.getStringValue());
            ((ImageViewModel)model).setImageId(imageData.getDataId());
        }
    }

    @Override
    protected void saveContentToDB(ContentViewModel model, int pageId, int blockId, PageDao pageDao) {
        ImageViewModel imageModel = (ImageViewModel) model;
        int imageId = pageDao.addPageBlockData(imageModel.getImageId(), blockId, pageId, Constants.PageData.ContentData.IMAGE_URL, null, null, imageModel.getImageUrl(), null, null);
        imageModel.setImageId(imageId);
    }

    @Override
    protected void saveContentToJson(ContentViewModel model, ObjectNode json) {
        ImageViewModel imageModel = (ImageViewModel) model;
        if (imageModel.getImageId() != null) {
            json.put("imageId", imageModel.getImageId());
        }

        if (StringUtils.isEmpty(imageModel.getImageUrl())) {
            json.put("imageUrl", imageModel.getImageUrl());
        }
    }

    @Override
    protected Object loadFromJson(JsonNode jsonNode, Object... args) {
        ObjectNode json = (ObjectNode) jsonNode;
        int type = (Integer) args[0];
        ImageViewModel model = new ImageViewModel();
        model.setType(type);

        addDataFromJson(model, json);
        return model;
    }

    @Override
    protected void addDataFromJson(ContentViewModel model, ObjectNode json) {
        super.addDataFromJson(model, json);
        ImageViewModel imageModel = (ImageViewModel) model;
        JsonNode node;
        node = json.get("imageId");
        if (node != null) {
            imageModel.setImageId(node.intValue());
        }
        node = json.get("imageUrl");
        if (node != null) {
            imageModel.setImageUrl(node.textValue());
        }
    }
}
