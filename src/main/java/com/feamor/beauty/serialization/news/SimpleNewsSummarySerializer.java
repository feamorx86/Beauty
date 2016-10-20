package com.feamor.beauty.serialization.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.views.NewsItem;
import com.feamor.beauty.serialization.Serializer;

/**
 * Created by Home on 19.10.2016.
 */
public class SimpleNewsSummarySerializer extends Serializer {
    @Override
    protected JsonNode saveToJson(Object value, Object... args) {
        JsonNode result;
        if (value != null && value instanceof NewsItem.BaseSummary) {
            result = super.saveToJson(value);
            ObjectNode json;
            if (result != null && result instanceof ObjectNode) {
                json = (ObjectNode) result;
            } else {
                json = JsonNodeFactory.instance.objectNode();
                result = json;
            }
            NewsItem.BaseSummary summary = (NewsItem.BaseSummary) value;
            json.put("title", summary.getTitle());
            json.put("date", summary.getType());
            json.put("id", summary.getId());
        } else {
            result = null;
        }
        return result;
    }

    @Override
    protected void saveToDb(Object value, Object... args) {
        if (value != null && value instanceof NewsItem.BaseSummary) {
            NewsItem.BaseSummary summary = (NewsItem.BaseSummary) value;
            PageDao pageDao = (PageDao) args[0];
            Page page = (Page) args[1];
            Integer id = pageDao.addPageData(summary.getId(), page.getPageId(), Constants.PageData.News.SUMMARY_ROOT, summary.getType(), summary.getTitle(), null, null, null, null, null);
            summary.setId(id);
        }
    }

    @Override
    protected Object loadFromDB(Object data, Object... args) {
        NewsItem.BaseSummary summary = (NewsItem.BaseSummary) data;
        PageData pageData = (PageData) args[0];
        summary.setId(pageData.getDataId());
        summary.setTitle(pageData.getStringValue());
        summary.setType(pageData.getType());
        return summary;
    }

    @Override
    protected Object loadFromJson(JsonNode jsonNode, Object... args) {
        if (jsonNode!= null && jsonNode instanceof ObjectNode) {
            ObjectNode json = (ObjectNode) jsonNode;
            NewsItem.BaseSummary summary;
            if (args!=null && args.length > 0) {
                summary = (NewsItem.BaseSummary) args[0];
            } else {
                summary = new NewsItem.BaseSummary();
            }

            summary.setId(optInt(json, "id", -1));
            summary.setType(optInt(json, "type", -1));
            summary.setTitle(optStr(json, "title", ""));
            return summary;
        } else {
            return  null;
        }
    }
}
