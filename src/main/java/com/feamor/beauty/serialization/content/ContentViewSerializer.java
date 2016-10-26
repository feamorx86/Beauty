package com.feamor.beauty.serialization.content;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.models.views.content.ContentViewModel;
import com.feamor.beauty.models.views.news.BaseSummary;
import com.feamor.beauty.models.views.news.SummaryWithText;
import com.feamor.beauty.serialization.Serializer;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by Home on 25.10.2016.
 */
public class ContentViewSerializer extends Serializer {

    protected void addDataFromJson(ContentViewModel model, ObjectNode json) {
        JsonNode node;
        node = json.get("id");
        if (node != null){
            model.setId(node.intValue());
        }
        node = json.get("styleId");
        if (node != null) {
            model.setStyleId(node.intValue());
        }
        node = json.get("style");
        if (node != null) {
            model.setStyle(node.intValue());
        }
        node = json.get("styleParams");
        if (node != null) {
            model.setStyleParams(node.textValue());
        }
    }

    @Override
    protected JsonNode saveToJson(Object value, Object... args) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();

        ContentViewModel model = (ContentViewModel) value;

        if (model.getId() != null) {
            json.put("id", model.getId());
        }

        json.put("type", model.getType());

        if (model.getStyleId() != null) {
            json.put("styleId", model.getStyleId());
        }

        if (model.getStyleId() != null) {
            json.put("style", model.getStyle());
        }

        if(!StringUtils.isEmpty(model.getStyleParams())) {
            json.put("styleParams", model.getStyleParams());
        }

        saveContentToJson(model, json);
        return json;
    }

    protected void saveContentToJson(ContentViewModel model, ObjectNode json) {

    }

    @Override
    protected void saveToDb(Object value, Object... args) {
        ContentViewModel model = (ContentViewModel) value;
        PageDao dao = (PageDao) args[0];
        int pageId = (int) args[1];

        int blockId = dao.addPageBlock(model.getId(), pageId, model.getType(), null, null, null, null, null);
        model.setId(blockId);

        int styleId = dao.addPageBlockData(model.getStyleId(), blockId, pageId, Constants.PageData.ContentData.STYLE, null, model.getStyle(), model.getStyleParams(), null, null);
        model.setStyleId(styleId);

        saveContentToDB(model, pageId, blockId, dao);
    }

    protected void saveContentToDB(ContentViewModel model, int pageId, int blockId, PageDao pageDao) {

    }

    protected void addContentFromDom(ContentViewModel model, PageDomBlock domBlock) {
        model.setId(domBlock.getBlock().getBlockId());
        model.setType(domBlock.getBlock().getType());

        PageBlockData styleData = domBlock.findFirstDataWithType(Constants.PageData.ContentData.STYLE);
        if (styleData != null){
            model.setStyle(styleData.getIntValue());
            model.setStyleParams(styleData.getStringValue());
            model.setStyleId(styleData.getDataId());
        }
    }

    public Integer getDataId(int type, PageDomBlock domBlock, Integer fallback) {
        Integer result;
        if (domBlock != null) {
            PageBlockData data = domBlock.findFirstDataWithType(type);
            if (data != null) {
                result = data.getDataId();
            } else {
                result = fallback;
            }
        } else {
            result = fallback;
        }
        return result;
    }

    public Integer getInteger(int type, PageDomBlock domBlock, Integer fallback) {
        Integer result;
        if (domBlock != null) {
            PageBlockData data = domBlock.findFirstDataWithType(type);
            if (data != null) {
                result = data.getIntValue();
            } else {
                result = fallback;
            }
        } else {
            result = fallback;
        }
        return result;
    }

    public String getInteger(int type, PageDomBlock domBlock, String fallback) {
        String result;
        if (domBlock != null) {
            PageBlockData data = domBlock.findFirstDataWithType(type);
            if (data != null) {
                result = data.getStringValue();
            } else {
                result = fallback;
            }
        } else {
            result = fallback;
        }
        return result;
    }

    public Date getDate(int type, PageDomBlock domBlock, Date fallback) {
        Date result;
        if (domBlock != null) {
            PageBlockData data = domBlock.findFirstDataWithType(type);
            if (data != null) {
                result = data.getDateValue();
            } else {
                result = fallback;
            }
        } else {
            result = fallback;
        }
        return result;
    }
}
