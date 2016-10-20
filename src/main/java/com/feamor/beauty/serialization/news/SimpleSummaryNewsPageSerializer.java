package com.feamor.beauty.serialization.news;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.ui.PageDom;
import com.feamor.beauty.models.views.NewsItem;
import com.feamor.beauty.serialization.Serializer;

/**
 * Created by Home on 20.10.2016.
 */
public class SimpleSummaryNewsPageSerializer extends Serializer {

    @Override
    protected Object loadFromDB(Object data, Object... args) {
        PageDom pageDom = (PageDom) data;
        NewsItem newsItem = new NewsItem();
        newsItem.setType(pageDom.getPage().getType());
        newsItem.setId(pageDom.getPage().getPageId());

        PageData pageInfo = pageDom.findFirstDataWithType(Constants.PageData.CommonPageData.CREATION_INFO);
        newsItem.setUserId(pageInfo.getIntValue());
        newsItem.setDate(pageInfo.getDateValue());

        PageData summaryRoot = pageDom.findFirstDataWithType(Constants.PageData.News.SUMMARY_ROOT);
        if (summaryRoot != null) {
            NewsItem.BaseSummary summary = createSummaryByTypeId(summaryRoot.getIntValue());
            Serializer serializer  добавить авто сюосборку сериалайзеров для модели, их хранилище и методы получения
        } else {
            newsItem.setSummary(null);
        }

        return newsItem;
    }

    public NewsItem.BaseSummary createSummaryByTypeId(int type) {
        NewsItem.BaseSummary result;
        switch (type) {
            case Constants.PageData.NewsSummaryType.BASE:
                result = new NewsItem.BaseSummary();
                break;
            case Constants.PageData.NewsSummaryType.SummaryWithText:
                result = new NewsItem.SummaryWithText();
                break;
            case Constants.PageData.NewsSummaryType.SummaryWithImage:
                result = new NewsItem.SummaryWithImage();
                break;
            case Constants.PageData.NewsSummaryType.SummaryWithHtml:
                result = new NewsItem.SummaryWithHtml();
                break;
            case Constants.PageData.NewsSummaryType.ComplexSummary:
                result = new NewsItem.ComplexSummary());
                break;
            default:
                result = null;
                //TODO: add logs
            break;
        }
        return result;
    }
}
