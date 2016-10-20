package com.feamor.beauty.models.views;

import com.feamor.beauty.models.db.PageBlock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Home on 12.10.2016.
 */
public class NewsItem {
    private int id;
    private Date date;
    private int type;
    private BaseSummary summary;
    private Data data;
    private String url;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BaseSummary getSummary() {
        return summary;
    }

    public void setSummary(BaseSummary summary) {
        this.summary = summary;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static class Data {
        private String title;

        private List<PageBlock> blocks;
    }

    public static class BaseSummary {
        private int type;
        private Integer id;
        private String title;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class SummaryWithHtml extends BaseSummary {
        private String htmlData;

        public String getHtmlData() {
            return htmlData;
        }

        public void setHtmlData(String htmlData) {
            this.htmlData = htmlData;
        }
    }

    public static class SummaryWithImage extends BaseSummary {
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public static class SummaryWithText extends BaseSummary {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class ComplexSummary extends BaseSummary {
        private String imageUrl;
        private String text;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
