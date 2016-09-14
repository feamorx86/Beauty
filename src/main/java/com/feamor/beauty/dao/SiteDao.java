package com.feamor.beauty.dao;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.NewsData;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageData;
import com.feamor.beauty.models.ui.PageDomBlock;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 09.05.2016.
 */
@Transactional
@Repository
public class SiteDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PageDao pageDao;

    public String getStringOfType(int type) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select t.\"strValue\" " +
                "FROM public.\"LastUserGroupData\" as t  WHERE t.\"type\" = ? AND (t.removed is null or not t.removed) limit 1");
        String result = (String) query.setParameter(1, type).uniqueResult();
        return  result;
    }

    public List<String> getStringsOfType(int type) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select t.\"strValue\" " +
                "FROM public.\"LastUserGroupData\" as t WHERE t.\"type\" = ? AND (t.removed is null or not t.removed)");
        List<String> result = (List<String>) query.setParameter(1, type).list();
        return  result;
    }

    public String getTextOfType(int type) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select t.\"textValue\" " +
                "FROM public.\"LastUserGroupData\" as t  WHERE t.\"type\" = ? AND (t.removed is null or not t.removed) limit 1");
        String result = (String) query.setParameter(1, type).uniqueResult();
        return  result;
    }

    public List<String> getTextsOfType(int type) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select t.\"textValue\" " +
                "FROM public.\"LastUserGroupData\" as t WHERE t.\"type\" = ? AND (t.removed is null or not t.removed)");
        List<String> result = (List<String>) query.setParameter(1, type).list();
        return  result;
    }

    public static class LoadNewsResult {
        public static final int SUCCESS = 0;
        public static final int NO_SUCH_PAGE = 1;
        public static final int PAGE_IS_NOT_NEWS_PAGE = 2;
        public static final int NO_SUMMART_DATA = 2;
    }

    public List<NewsData> loadLastNewsSummary(int count, int offset) {
        ArrayList<NewsData> newsSummary = null;
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT u.\"userId\", u.\"intValue\", u.\"dateValue\" FROM public.\"UserData\" as u " +
                "WHERE u.type = ? ORDER BY u.\"dateValue\" DESC LIMIT ? OFFSET ?;");
        query.setParameter(1, Constants.UserDataTypes.NEWS_PAGE).setParameter(2, count).setParameter(3, offset);
        List<Object[]> pages = query.list();
        if(pages != null) {
            newsSummary = new ArrayList<NewsData>();
            for(Object[] pageInfo : pages) {
                int userId = (Integer)pageInfo[0];
                int pageId = (Integer)pageInfo[1];
                Date date = (Date)pageInfo[2];

                NewsData news = new NewsData();
                news.setCreatorId(userId);
                news.setId(pageId);
                news.setTime(date);
                loadNewsSummary(news);

                newsSummary.add(news);
            }
        }
        return  newsSummary;
    }

    public int loadNewsSummary(NewsData out) {
        int result;

        Page page = pageDao.getPage(out.getId());
        //Page page = (Page) sessionFactory.getCurrentSession().cricreateCriteria(Page.class).add(Restrictions.eq("pageId",out.getId())).uniqueResult();
        if (page != null) {
            if (page.getType() == Constants.Pages.NEWS_CREATED_BY_USER) {
                List<PageData> summaryData = pageDao.getDataOfPage(page.getPageId());
                if (summaryData != null) {
                    for (PageData data : summaryData) {
                        switch(data.getType()) {
                            case Constants.PageData.News.TITLE:
                                out.setTitle(data.getStringValue());
                                break;
                            case Constants.PageData.News.SUMMARY:
                                out.setSummary(data.getTextValue());
                                break;
                            case Constants.PageData.News.ICON:
                                out.setIcon(data.getStringValue());
                                break;
                            case Constants.PageData.News.SUMMARY_BLOCK_ID:
                                Integer summaryBlockId = data.getIntValue();
                                if (summaryBlockId != null) {
                                    PageDomBlock block = pageDao.loadDomBlock(null, summaryBlockId);
                                    out.setSummaryBlock(block);
                                }
                                break;
                            default:
                                //TODO: log unsupported user data
                                break;
                        }
                    }
                    result = LoadNewsResult.SUCCESS;
                } else {
                    result = LoadNewsResult.PAGE_IS_NOT_NEWS_PAGE;
                }
            } else {
                result = LoadNewsResult.PAGE_IS_NOT_NEWS_PAGE;
            }
        } else {
            result = LoadNewsResult.NO_SUCH_PAGE;
        }

        return result;
    }

    public  HashMap<Integer, String> getCompanyInfo() {
        HashMap<Integer, String> fields = new HashMap<>();
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("Select t.\"intValue\", t.\"strValue\" " +
                "FROM public.\"LastUserGroupData\" as t WHERE t.\"type\" = ? AND (t.removed is null or not t.removed)");
        query.setParameter(1, Constants.GroupDataType.COMPANY_INFO);
        List<Object[]> pages = query.list();
        if(pages != null) {
            for(Object[] pageInfo : pages) {
                int fieldType = (Integer)pageInfo[0];
                String fieldValue = (String)pageInfo[1];
                fields.put(fieldType, fieldValue);
            }
        }
        return fields;
    }
}
