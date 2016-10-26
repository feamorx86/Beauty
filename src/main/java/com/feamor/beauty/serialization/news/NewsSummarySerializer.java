package com.feamor.beauty.serialization.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.ui.PageDom;
import com.feamor.beauty.models.views.news.*;
import com.feamor.beauty.serialization.Serializer;

import java.util.List;

/**
 * Created by Home on 21.10.2016.
 */
public class NewsSummarySerializer extends Serializer {

    @Override
    protected Object loadFromJson(JsonNode jsonNode, Object... args) {
        if (jsonNode!= null && jsonNode instanceof ObjectNode) {
            ObjectNode json = (ObjectNode) jsonNode;
            BaseSummary summary = fromJson(json);
            return summary;
        } else {
            return  null;
        }
    }

    @Override
    protected JsonNode saveToJson(Object value, Object... args) {
        JsonNode result;
        if (value != null && value instanceof BaseSummary) {
            result = toJson((BaseSummary) value);
        } else {
            result = null;
        }
        return result;
    }


    @Override
    protected void saveToDb(Object value, Object... args) {
        if (value != null && value instanceof BaseSummary) {
            BaseSummary summary = (BaseSummary) value;
            PageDao pageDao = (PageDao) args[0];
            Page page = (Page) args[1];
            toDb(summary, page.getPageId(), pageDao);
        }
    }

    @Override
    protected Object loadFromDom(Object domData, Object... args) {
        BaseSummary summary;
        if (domData != null && domData instanceof PageDom) {
            summary = fromPageDom((PageDom) domData);
        } else {
            summary = null;
        }
        return summary;
    }

    private JsonNode toJson(BaseSummary summary) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();

        json.put("title", summary.getTitle());
        json.put("date", summary.getType());
        json.put("id", summary.getId());

        switch(summary.getType()) {
           case Constants.PageData.News.SUMMARY_ROOT:
                //nothing
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT:
                json.put("text", ((SummaryWithText)summary).getText());
                json.put("textId", ((SummaryWithText)summary).getDataId());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE:
                json.put("image", ((SummaryWithImage)summary).getImageUrl());
                json.put("imageId", ((SummaryWithImage)summary).getDataId());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_HTML:
                json.put("html", ((SummaryWithHtml)summary).getHtmlData());
                json.put("htmlId", ((SummaryWithHtml)summary).getDataId());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_COMPLEX:
                json.put("text", ((ComplexSummary)summary).getText());
                json.put("image", ((ComplexSummary)summary).getImageUrl());
                json.put("textId", ((ComplexSummary)summary).getTextId());
                json.put("imageId", ((ComplexSummary)summary).getImageId());
                break;
            default:
                //TODO: log problem
                break;
        }
        return json;
    }

    private BaseSummary fromJson(ObjectNode json) {
        int type = json.get("type").intValue();
        BaseSummary summary = null;
        JsonNode idNode;
        switch (type) {
            case Constants.PageData.News.SUMMARY_ROOT:
                //nothing
                summary = new BaseSummary();
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT:
                summary = new SummaryWithText();
                ((SummaryWithText)summary).setText(json.get("text").textValue());
                idNode = json.get("textId");
                if (idNode != null)
                    ((SummaryWithText)summary).setDataId(idNode.intValue());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE:
                summary = new SummaryWithImage();
                ((SummaryWithImage)summary).setImageUrl(json.get("image").textValue());
                idNode = json.get("imageId");
                if (idNode != null)
                    ((SummaryWithImage)summary).setDataId(idNode.intValue());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_HTML:
                summary = new SummaryWithHtml();
                ((SummaryWithHtml)summary).setHtmlData(json.get("html").textValue());
                idNode = json.get("htmlId");
                if (idNode != null)
                    ((SummaryWithHtml)summary).setDataId(idNode.intValue());
                break;
            case Constants.PageData.News.SUMMARY_TYPE_COMPLEX:
                summary = new ComplexSummary();
                ((ComplexSummary)summary).setText(json.get("text").textValue());
                ((ComplexSummary)summary).setImageUrl(json.get("image").textValue());
                idNode = json.get("textId");
                if (idNode != null)
                    ((ComplexSummary)summary).setTextId(idNode.intValue());
                idNode = json.get("imageId");
                if (idNode != null)
                    ((ComplexSummary)summary).setImageId(idNode.intValue());
                break;
            default:
                //TODO: log problem
            break;
        }
        if (summary != null) {
            summary.setType(type);
            summary.setTitle(json.get("title").textValue());
            idNode = json.get("id");
            if (idNode != null) {
                summary.setId(idNode.intValue());
            } else {
                summary.setId(null);
            }
        }
        return summary;
    }

    private BaseSummary fromPageDom(PageDom dom) {
        PageData rootData = dom.findFirstDataWithType(Constants.PageData.News.SUMMARY_ROOT);
        BaseSummary result;
        if (rootData != null) {
            switch (rootData.getIntValue()) {
                case Constants.PageData.News.SUMMARY_ROOT: {
                    result = new BaseSummary();
                    result.setId(rootData.getDataId());
                    result.setType(Constants.PageData.News.SUMMARY_ROOT);
                    result.setTitle(rootData.getStringValue());
                }
                break;
                case Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE: {
                    PageData imageData = dom.findFirstWithTypeAndParent(Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE, rootData.getDataId());
                    SummaryWithImage summary = new SummaryWithImage();
                    summary.setId(rootData.getDataId());
                    summary.setType(Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE);
                    summary.setTitle(rootData.getStringValue());

                    summary.setImageUrl(imageData.getStringValue());
                    summary.setDataId(imageData.getDataId());

                    result = summary;
                }
                break;
                case Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT: {
                    PageData textData = dom.findFirstWithTypeAndParent(Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT, rootData.getDataId());
                    SummaryWithText summary = new SummaryWithText();
                    summary.setId(rootData.getDataId());
                    summary.setType(Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT);
                    summary.setTitle(rootData.getStringValue());

                    summary.setText(textData.getStringValue());
                    summary.setDataId(textData.getDataId());

                    result = summary;
                }
                break;
                case Constants.PageData.News.SUMMARY_TYPE_WITH_HTML: {
                    PageData htmlData = dom.findFirstWithTypeAndParent(Constants.PageData.News.SUMMARY_TYPE_WITH_HTML, rootData.getDataId());
                    SummaryWithHtml summary = new SummaryWithHtml();
                    summary.setId(rootData.getDataId());
                    summary.setType(Constants.PageData.News.SUMMARY_TYPE_WITH_HTML);
                    summary.setTitle(rootData.getStringValue());

                    summary.setHtmlData(htmlData.getStringValue());
                    summary.setDataId(htmlData.getDataId());

                    result = summary;
                }
                break;
                case Constants.PageData.News.SUMMARY_TYPE_COMPLEX: {
                    List<PageData> complexDataList = dom.findPageDataChildren(rootData.getDataId());
                    ComplexSummary summary = new ComplexSummary();

                    summary.setId(rootData.getDataId());
                    summary.setType(Constants.PageData.News.SUMMARY_TYPE_COMPLEX);
                    summary.setTitle(rootData.getStringValue());

                    for (PageData data : complexDataList){
                        if (data.getType() == Constants.PageData.News.SUMMARY_TYPE_COMPLEX) {
                            int dataType = data.getIntValue();
                            if (dataType == Constants.PageData.News.SUMMARY_TEXT) {
                                summary.setText(data.getStringValue());
                                summary.setTextId(data.getDataId());
                            } else if (dataType == Constants.PageData.News.SUMMARY_IMAGE_URL) {
                                summary.setImageUrl(data.getStringValue());
                                summary.setImageId(data.getDataId());
                            }
                        }
                    }

                    result = summary;
                }
                break;
                default:
                    //TODO: add logs here
                    result = null;
                    break;
            }
        } else {
            result = null;
        }
        return  result;
    }

    private void toDb(BaseSummary summary, int pageId, PageDao dao) {
        //sample storage int db
        //row 0 : id, page, type : SUMMARY_ROOT,         int : SummaryType,          str : summary_title ... parent : null
        //row 1 : id, page, type : SummaryType(complex), int : data-type(image url), str : url,          ... parent : rootId
        //row 2 : id, page, type : SummaryType(complex), int : data-type(text)       str : text          ... parent : rootId
        int rootId = dao.addPageData(summary.getId(), pageId, Constants.PageData.News.SUMMARY_ROOT, summary.getType(), summary.getTitle(), null, null, null, null, null);
        summary.setId(rootId);
        switch(summary.getType()) {
            case Constants.PageData.News.SUMMARY_ROOT:
                //nothing
                break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT: {
                SummaryWithText textSummary = (SummaryWithText) summary;
                int id = dao.addPageData(textSummary.getDataId(), pageId, Constants.PageData.News.SUMMARY_TYPE_WITH_TEXT, Constants.PageData.News.SUMMARY_TEXT, textSummary.getText(), null, null, null, rootId, null);
                textSummary.setDataId(id);
            }
            break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE: {
                SummaryWithImage imageSummary = (SummaryWithImage) summary;
                int id = dao.addPageData(imageSummary.getDataId(), pageId, Constants.PageData.News.SUMMARY_TYPE_WITH_IMAGE, Constants.PageData.News.SUMMARY_IMAGE_URL, imageSummary.getImageUrl(), null, null, null, rootId, null);
                imageSummary.setDataId(id);
            }
            break;
            case Constants.PageData.News.SUMMARY_TYPE_WITH_HTML: {
                SummaryWithHtml htmlSummary = (SummaryWithHtml) summary;
                int id = dao.addPageData(htmlSummary.getDataId(), pageId, Constants.PageData.News.SUMMARY_TYPE_WITH_HTML, Constants.PageData.News.SUMMARY_HTML, htmlSummary.getHtmlData(), null, null, null, rootId, null);
                htmlSummary.setDataId(id);
            }
            break;
            case Constants.PageData.News.SUMMARY_TYPE_COMPLEX: {
                ComplexSummary complexSummary = (ComplexSummary) summary;
                int id = dao.addPageData(complexSummary.getImageId(), pageId, Constants.PageData.News.SUMMARY_TYPE_COMPLEX, Constants.PageData.News.SUMMARY_IMAGE_URL, complexSummary.getImageUrl(), null, null, null, rootId, null);
                complexSummary.setImageId(id);
                id = dao.addPageData(complexSummary.getTextId(), pageId, Constants.PageData.News.SUMMARY_TYPE_COMPLEX, Constants.PageData.News.SUMMARY_TEXT, complexSummary.getText(), null, null, null, rootId, null);
                complexSummary.setTextId(id);
            }
            break;
            default:
                //TODO: log problem
                break;
        }
    }
}
