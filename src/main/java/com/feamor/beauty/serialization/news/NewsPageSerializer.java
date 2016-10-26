package com.feamor.beauty.serialization.news;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.ui.PageDom;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.models.views.content.ContentViewModel;
import com.feamor.beauty.models.views.news.NewsPageData;
import com.feamor.beauty.models.views.news.BaseSummary;
import com.feamor.beauty.serialization.Serializer;
import com.feamor.beauty.serialization.content.ContentViewFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Home on 20.10.2016.
 */
public class NewsPageSerializer extends Serializer {

    @Autowired
    private NewsSummarySerializer newsSummarySerializer;

    @Autowired
    private ContentViewFactory contentViewFactory;

    @Override
    protected Object loadFromDom(Object domData, Object... args) {

        PageDom pageDom = (PageDom) domData;

        boolean withSummary = (Boolean) args[0];
        boolean withBlocks = (Boolean) args[1];

        NewsPageData newsPageData = new NewsPageData();
        newsPageData.setType(pageDom.getPage().getType());
        newsPageData.setId(pageDom.getPage().getPageId());

        PageData pageInfo = pageDom.findFirstDataWithType(Constants.PageData.CommonPageData.CREATION_INFO);
        newsPageData.setUserId(pageInfo.getIntValue());
        newsPageData.setTime(pageInfo.getDateValue());

        if (withSummary) {
            BaseSummary summary = (BaseSummary) newsSummarySerializer.loadFrom(DataTypes.DOM, pageDom);
            newsPageData.setSummary(summary);
        }

        if (withBlocks) {
            for (PageDomBlock block : pageDom.getBlocksList()) {
                Serializer serializer = contentViewFactory.getSerializer(block.getBlock().getType());
                ContentViewModel model = (ContentViewModel) serializer.loadFrom(DataTypes.DOM, block);
                newsPageData.addItem(model);
            }
        }

        return newsPageData;
    }
}
