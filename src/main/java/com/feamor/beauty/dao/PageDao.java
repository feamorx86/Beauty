package com.feamor.beauty.dao;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.NewsData;
import com.feamor.beauty.models.db.*;
import com.feamor.beauty.models.ui.PageDom;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.templates.TemplateElement;
import com.feamor.beauty.utils.CachedData;
import com.feamor.beauty.utils.DataCache;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ParameterRegistration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.NativeQuery;
import org.hibernate.result.ResultSetOutput;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.ParameterMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

/**
 * Created by Home on 27.02.2016.
 */

@Transactional
@Repository
public class PageDao {
    public static Logger logger = Logger.getLogger(PageDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDataDAO groupDataDAO;

    public Page getPage(int id) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Page> query = builder.createQuery(Page.class);
        Root<Page> root = query.from(Page.class);
        query = query.select(root).where(builder.equal(root.get("pageId"), id));
        Page result = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return result;
    }

    public ArrayList<BlockTemplate.TemplateHeader> listTemplateHeaders(/*TODO: add pagination offset and size*/) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT t.id, t.alias, t.path, t.\"blockType\" FROM public.\"BlockTemplate\" as t ORDER BY t.id;");
        List<Object[]> list = query.list();
        ArrayList<BlockTemplate.TemplateHeader> headers = new ArrayList<BlockTemplate.TemplateHeader>();
        if (list!=null) {
            for(Object[] row : list) {
                BlockTemplate.TemplateHeader header = new BlockTemplate.TemplateHeader((Integer) row[0], (String) row[1], (String) row[2], (Integer) row[3]);
                headers.add(header);
            }
        }
        return headers;
    }

    public BlockTemplate getTemplate(int id) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<BlockTemplate> query = builder.createQuery(BlockTemplate.class);
        Root<BlockTemplate> root = query.from(BlockTemplate.class);
        query = query.select(root).where(builder.equal(root.get("id"), id));
        BlockTemplate template = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return  template;
    }

    public boolean checkTemplateExist(int templateId) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT t.id FROM public.\"BlockTemplate\" as t WHERE t.id = ?;");
        Integer id = (Integer) query.setParameter(1, templateId).uniqueResult();
        return id!=null;
    }

    public void saveOrUpdateTemplate(BlockTemplate template) {
        sessionFactory.getCurrentSession().saveOrUpdate(template);
    }

//    public int updateTemplate(BlockTemplate template) {
//        SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("UPDATE public.\"BlockTemplate\" SET \"blockType\"=:type, " +
//                "lines= :lines, types= :types, alias= :alias, path= :path WHERE Id = :id;");
//
//        query.setParameter("type", template.getBlockType())
//                .setParameterList("lines", template.getLines())
//                .setParameterList("types", template.getTypes())
//                .setParameter("alias", template.getAlias())
//                .setParameter("path", template.getPath());
//        int updated = (Integer) query.executeUpdate();
//        return updated;
//    }

    public List<Page> listPages() {
        CriteriaQuery<Page> query = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(Page.class);
        Root<Page> root = query.from(Page.class);
        query = query.select(root);
        List<Page> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public List<PageBlock> getBlocksOfPage(int pageId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageBlock> query = builder.createQuery(PageBlock.class);
        Root<PageBlock> root = query.from(PageBlock.class);
        query = query.select(root).where(builder.equal(root.get("pageId"), pageId)).orderBy(builder.asc(root.get("position")));
        List<PageBlock> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public List<PageBlockData> getPageBlockDataForPage(int pageId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageBlockData> query = builder.createQuery(PageBlockData.class);
        Root<PageBlockData> root = query.from(PageBlockData.class);
        query = query.select(root).where(builder.equal(root.get("pageId"), pageId)).orderBy(builder.asc(root.get("position")));
        List<PageBlockData> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public PageBlock getPageBlock(int id) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageBlock> query = builder.createQuery(PageBlock.class);
        Root<PageBlock> root = query.from(PageBlock.class);
        query = query.select(root).where(builder.equal(root.get("blockId"), id));
        PageBlock result = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return result;
    }

    public PageDomBlock loadDomBlock(PageDom page, int blockId) {
        PageDomBlock result = null;
        PageBlock block = getPageBlock(blockId);
        if (block != null) {
            List<PageBlockData> data = getDataOfPageBlock(blockId);
            result = new PageDomBlock();
            result.setBlock(block);
            result.setBlockData(data);
            result.setPage(page);
        }
        return result;
    }

    public List<PageBlockData> getDataOfPageBlock(int blockId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageBlockData> query = builder.createQuery(PageBlockData.class);
        Root<PageBlockData> root = query.from(PageBlockData.class);
        query = query.select(root).where(
                builder.and(
                        builder.equal(root.get("blockId"), blockId),
                        builder.or(builder.isNull(root.get("removed")), builder.equal(root.get("removed"), false)))

        ).orderBy(builder.asc(root.get("position")));
        List<PageBlockData> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public List<PageData> getPageDataForPage(int pageId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageData> query = builder.createQuery(PageData.class);
        Root<PageData> root = query.from(PageData.class);
        query = query.select(root).where(
                builder.and(
                        builder.equal(root.get("pageId"), pageId),
                        builder.or(builder.isNull(root.get("removed")), builder.equal(root.get("removed"), false))
                ));
        List<PageData> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public PageData getPageData(int dataId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageData> query = builder.createQuery(PageData.class);
        Root<PageData> root = query.from(PageData.class);
        query = query.select(root).where(
                builder.and(
                    builder.equal(root.get("dataId"), dataId),
                    builder.or(builder.isNull(root.get("removed")), builder.equal(root.get("removed"), false))
                ));
        PageData result = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return result;
    }

    public List<PageData> getPageDataOfType(int pageId, int withType) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageData> query = builder.createQuery(PageData.class);
        Root<PageData> root = query.from(PageData.class);
        query = query.select(root).where(
                builder.and(
                        builder.equal(root.get("pageId"), pageId),
                        builder.and(
                                builder.equal(root.get("type"), withType),
                                builder.or(builder.isNull(root.get("removed")), builder.equal(root.get("removed"), false)))
                ));
        List<PageData> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public PageData getFirstPageDataOfType(int pageId, int withType) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageData> query = builder.createQuery(PageData.class);
        Root<PageData> root = query.from(PageData.class);
        query = query.select(root).where(
                builder.and(
                        builder.equal(root.get("pageId"), pageId),
                        builder.and(
                            builder.equal(root.get("type"), withType),
                            builder.or(builder.isNull(root.get("removed")), builder.equal(root.get("removed"), false)))
                ));
        PageData result = sessionFactory.getCurrentSession().createQuery(query).setMaxResults(1).uniqueResult();
        return result;
    }


    public PageBlockData getPageBlockData(int id) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageBlockData> query = builder.createQuery(PageBlockData.class);
        Root<PageBlockData> root = query.from(PageBlockData.class);
        query = query.select(root).where(builder.equal(root.get("dataId"), id));
        PageBlockData result = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return result;
    }

    public Page getPageOfType(int type) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Page> query = builder.createQuery(Page.class);
        Root<Page> root = query.from(Page.class);
        query = query.select(root).where(builder.equal(root.get("type"), type));
        Page result = sessionFactory.getCurrentSession().createQuery(query).setMaxResults(1).uniqueResult();
        return result;
    }

    public void loadPageBlocksData(PageDom dom) {
        List<PageBlock> blocks = getBlocksOfPage(dom.getPage().getPageId());
        if (blocks != null) {
            HashMap<Integer, PageDomBlock> pageDomBlocks = new HashMap<Integer, PageDomBlock>();
            ArrayList<PageDomBlock> pageDomBlocksList = new ArrayList<>();
            for (PageBlock block : blocks) {
                PageDomBlock domBlock = new PageDomBlock();
                domBlock.setBlock(block);
                domBlock.setPage(dom);
                pageDomBlocksList.add(domBlock);
                pageDomBlocks.put(domBlock.getBlock().getBlockId(), domBlock);
            }
            dom.setBlocks(pageDomBlocks);
            dom.setBlocksList(pageDomBlocksList);
        }
    }

    public PageDom loadPage(int id, boolean loadPageData, boolean loadPageBlocks) {
        PageDom dom = null;
        Page page = getPage(id);
        if (page != null) {
            dom = new PageDom();
            dom.setPage(page);
            if (loadPageData) {
                List<PageData> data = getPageDataForPage(id);
                dom.setPageData(data);
            }
            if (loadPageBlocks) {
                loadPageBlocksData(dom);
            }
        }
        return  dom;
    }

    public static class PageMapping {
        private String pageMapping;
        private int classId;
        private int id;
        private UserGroup group;

        public PageMapping(String pageMapping, int classId, int id, UserGroup group) {
            this.pageMapping = pageMapping;
            this.classId = classId;
            this.id = id;
            this.group = group;
        }

        public int getId() {
            return id;
        }

        public String getPageMapping() {
            return pageMapping;
        }

        public int getClassId() {
            return classId;
        }

        public UserGroup getGroup() {
            return group;
        }
    }

    public int savePageMapping(Integer mappingId, int creatorGroupId, int controllerType, String path, Boolean removed) {
        int result = groupDataDAO.add(mappingId, Constants.GroupDataType.CONTROLLERS_MAPPING, creatorGroupId,null, controllerType, path, null, null, removed);
        return  result;
    }

    public ArrayList<PageMapping> getPageMappings() {

        ArrayList<PageMapping> result = new ArrayList<PageMapping>();

        Session session = sessionFactory.openSession();
        NativeQuery query = session.createNativeQuery(
                "SELECT d.\"dataId\", d.\"intValue\", d.\"strValue\", d.\"creator\" FROM public.\"LastUserGroupData\" as d WHERE d.type = "+
                        Constants.GroupDataType.CONTROLLERS_MAPPING+" and (d.removed is null or not d.removed);");
        List<Object[]> list = query.list();
        if (list!=null) {
            for(Object[] row : list) {
                Integer rowId = (Integer) row[0];
                Integer classId = (Integer) row[1];
                String mapping = (String) row[2];
                Integer ownerId = (Integer) row[3];
                UserGroup owner = userDao.getGroup(ownerId);
                if (rowId != null && classId != null && !StringUtils.isEmpty(mapping)) {
                    result.add(new PageMapping(mapping, classId, rowId, owner));
                }
            }
        }
        session.close();
        return  result;
    }

    public Integer getTemplateElementsCount(int templateId) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT cardinality(t.lines), cardinality(t.types) " +
                "FROM public.\"BlockTemplate\" as t WHERE t.id = :id;");
        Object[] record = (Object[]) query.setParameter("id", templateId).uniqueResult();
        Integer result;
        if (record != null) {
            Integer lines = (Integer) record[0];
            Integer types = (Integer) record[1];
            if (lines.intValue() != types.intValue()) {
                logger.error("Incorrect template! Load counts for template with id: " + templateId + ", total lines : " + lines + ", types : " + types);
                result = Math.min(lines.intValue(), types.intValue());
            } else {
                result = lines.intValue();
            }
        } else {
            result = null;
        }
        return  result;
    }

    public int loadTemplateElements(int templateId, int from, int by, int max, ArrayList<TemplateElement> loadTo) {
        loadTo.clear();
        int start = from;
        int end = from + by;
        if (end > max) {
            end = max;
        }
        int read;
        if (end-start > 0) {
            NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT t.lines[ ? : ?], t.types[ ? : ?] FROM public.\"BlockTemplate\" as t WHERE t.id = ?;");
            Object[] row = (Object[]) query
                    .setParameter(1, start).setParameter(2, end) //from-to lines
                    .setParameter(3, start).setParameter(4, end) //from-to types
                    .setParameter(5, templateId).uniqueResult();               //template

            if (row != null) {
                String[] lines = (String[]) row[0];
                Integer[] types = (Integer[]) row[1];
                if (lines.length != types.length) {
                    logger.error("Incorrect template! Load counts for template with id: " + templateId + ", total lines : " + lines + ", types : " + types);
                    read = Math.min(lines.length, types.length);
                } else {
                    read = lines.length;
                }
                for (int i = 0; i < read; i++) {
                    TemplateElement element = new TemplateElement(types[i], lines[i]);
                    loadTo.add(element);
                }
            } else {
                logger.error("Template not found! There is no template with id: " + templateId);
                read = -1;
            }
        } else {
            read =0;
        }
        return  read;
    }

    public List<Integer> listPagesOfType(int pageType) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT p.\"pageId\" FROM public.\"LastPage\" as p WHERE p.type = ? ; ");
        List<Integer> pages = query.setParameter(1, pageType).list();
        return  pages;
    }

    public Object query(String sqlQuery, HashMap<String, Object> args) {
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery(sqlQuery);
        for (Map.Entry<String, Object> entry:args.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        Object result = query.uniqueResult();
        return  result;
    }


/*    public GroupData loadSingleByType(int type, String... orderValuesBy) {
        GroupData result = null;
        UserGroupData root = (UserGroupData) sessionFactory.getCurrentSession().createCriteria(UserGroupData.class).add(Restrictions.eq("type", type)).setMaxResults(1).uniqueResult();
        if (root != null) {
            result = new GroupData();
            result.setKey(root);
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UserGroupData.class).add(Restrictions.eq("parentId", root.getDataId()));
            if (orderValuesBy != null) {
                for(String order : orderValuesBy) {
                    criteria.addOrder(Order.asc(order));
                }
            }
            List<UserGroupData> list = criteria.list();
            result.setValues(list);
        }
        return result;
    }*/

   /* public List<GroupData> loadAllOfType(int type) {
        List<GroupData> result = null;
        List<UserGroupData> roots = sessionFactory.getCurrentSession().createCriteria(UserGroupData.class).add(Restrictions.eq("type", type)).list();
        if(roots != null) {
            result = new ArrayList<GroupData>();
            for(UserGroupData root : roots) {
                GroupData current = new GroupData();
                current.setKey(root);
                List<UserGroupData> list = sessionFactory.getCurrentSession().createCriteria(UserGroupData.class).add(Restrictions.eq("parentId", root.getDataId())).list();
                current.setValues(list);
            }
        }
        return result;
    }*/

    public int addPage(Page page) {
        int id = addPage(null, page.getType(), page.getClassId(), page.getAlias(), page.getPath(), page.getTemplate(), page.getRemoved());
        page.setPageId(id);
        return id;
    }

    public int addPage(Integer pageId, int type, String classId, String alias, String path, Integer template, Boolean removed) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"addOrUpdatePage\"");
        ParameterRegistration param;

        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(pageId);
        param = call.registerParameter(1, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(type);
        param = call.registerParameter(2, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(classId);
        param = call.registerParameter(3, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(alias);
        param = call.registerParameter(4, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(path);
        param = call.registerParameter(5, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(template);
        param = call.registerParameter(6, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(removed);
        ResultSetOutput resultSetOutput = (ResultSetOutput)call.getOutputs().getCurrent();
        Integer newPageId = (Integer) resultSetOutput.getSingleResult();
        return newPageId.intValue();
    }

    public Integer deletePage(Integer pageId, Boolean remove) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"removePage\"");
        ParameterRegistration param;
        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(pageId);
        param = call.registerParameter(1, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(remove);
        Integer rowId = (Integer) ((ResultSetOutput)call.getOutputs().getCurrent()).getSingleResult();
        return rowId;
    }

    public boolean deletePage(Page page) {
        boolean result;
        result = deletePage(page.getPageId(),  true) != null;
        return result;
    }

    public boolean deletePage(int pageId) {
        boolean result;
        result = deletePage(pageId, true) != null;
        return result;
    }

    public void updatePage(Page page) {
        addPage(page.getPageId(), page.getType(), page.getClassId(), page.getAlias(), page.getPath(), page.getTemplate(), page.getRemoved());
    }

    public void updatePage(int pageId, int type, String classId, String alias, String path, Integer template, Boolean removed) {
        addPage(pageId, type, classId, alias, path, template, removed);
    }

    public int addPageData(PageData data) {
        int id = addPageData(null, data.getPageId(), data.getType(),
                data.getIntValue(), data.getStringValue(), data.getTextValue(), data.getDoubleValue(), data.getDateValue(),
                data.getParentId(), data.getRemoved());
        data.setDataId(id);
        return id;
    }

    public int addPageData(Integer dataId, int pageId, int type,
                           Integer intValue, String strValue, String textValue, Double floatValue, Date dateValue,
                           Integer parentId, Boolean removed) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"addOrUpdatePage\"");
        ParameterRegistration param;

        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dataId);
        param = call.registerParameter(1, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(pageId);
        param = call.registerParameter(2, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(type);

        param = call.registerParameter(3, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(intValue);
        param = call.registerParameter(4, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(strValue);
        param = call.registerParameter(5, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(textValue);
        param = call.registerParameter(6, Double.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(floatValue);
        param = call.registerParameter(7, Timestamp.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dateValue);

        param = call.registerParameter(8, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(parentId);
        param = call.registerParameter(9, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(removed);


        ResultSetOutput resultSetOutput = (ResultSetOutput)call.getOutputs().getCurrent();
        Integer newPageId = (Integer) resultSetOutput.getSingleResult();
        return newPageId.intValue();
    }

    public Integer deletePageData(Integer dataId, Boolean remove) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"removePageData\"");
        ParameterRegistration param;
        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dataId);
        param = call.registerParameter(1, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(remove);
        Integer rowId = (Integer) ((ResultSetOutput)call.getOutputs().getCurrent()).getSingleResult();
        return rowId;
    }

    public boolean deletePageData(PageData data) {
        boolean result;
        result = deletePageData(data.getId(),  true) != null;
        return result;
    }

    public boolean deletePageData(int dataId) {
        boolean result;
        result = deletePageData(dataId, true) != null;
        return result;
    }

    public void updatePageData(PageData data) {
        addPageData(data.getDataId(), data.getPageId(), data.getType(),
                data.getIntValue(), data.getStringValue(), data.getTextValue(), data.getDoubleValue(), data.getDateValue(),
                data.getParentId(), data.getRemoved());
    }

    public void updatePageData(int dataId, int pageId, int type,
                               Integer intValue, String strValue, String textValue, Double floatValue, Date dateValue,
                               Integer parentId, Boolean removed) {
        addPageData(dataId, pageId, type, intValue, strValue, textValue, floatValue, dateValue, parentId, removed);
    }

    private DataCache pagesByTypeCache;

    public int getPagesOfTypeCount(int pageType) {
        CachedData cached = pagesByTypeCache.get(pageType);
        if (cached == null) {
            NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery(
                    "SELECT count(d.id) " +
                            "FROM public.\"LastPageData\" as pd " +
                            "LEFT JOIN public.\"LastPage\" as p ON p.\"pageId\" = pd.\"pageId\" " +
                            "WHERE p.\"type\" = ? and pd.\"type\" = ? and (pd.removed is null or pd.removed = false) and (p.removed is null or p.removed = false)");
            query.setParameter(1, pageType).setParameter(2, Constants.PageData.CommonPageData.CREATION_INFO);
            Integer result = (Integer) query.uniqueResult();
            cached = pagesByTypeCache.create(result, pageType);
        }

        return (Integer)cached.getData();
    }

    public ArrayList<PageDom> listPagesOfTypeByCreateDate(int pageType, int count, int offset) {
        ArrayList<PageDom> pages = null;
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT p.id, p.\"pageId\", p.alias, p.path, p.\"classId\", p.template " +
                "FROM public.\"LastPageData\" as pd LEFT JOIN public.\"LastPage\" as p ON p.\"pageId\" = pd.\"pageId\" "+
                "WHERE p.\"type\" = ? and pd.\"type\" = ? and (pd.removed is null or pd.removed = false) and (p.removed is null or p.removed = false) "+
                "ORDER BY pd.\"dateValue\" LIMIT ? OFFSET ?");
            query.setParameter(1, pageType).setParameter(2, Constants.PageData.CommonPageData.CREATION_INFO).setParameter(3, count).setParameter(4, offset);
        List<Object[]> result = query.list();
        if(result != null) {
            pages = new ArrayList<PageDom>();
            for (Object[] data : result) {
                Page page = new Page();
                page.setType(pageType);
                page.setId((Integer) data[0]);
                page.setPageId((Integer) data[1]);
                page.setAlias((String) data[2]);
                page.setPath((String) data[3]);
                page.setClassId((String) data[4]);
                page.setTemplate((Integer) data[5]);

                PageDom dom = new PageDom();
                dom.setPage(page);
                dom.setPageData(getPageDataForPage(page.getPageId()));

                pages.add(dom);
            }
        }
        return pages;
    }

    public Integer createPage(int pageType, User user, String alias, String path, Integer templateId) {
        Integer pageId = addPage(null, pageType, null, alias, path, templateId, null);
        if (pageId != null) {
            Date date = Calendar.getInstance().getTime();
            addPageData(null, pageId.intValue(), Constants.PageData.CommonPageData.CREATION_INFO, user.getId(), null, null, null, date, null, null);
        }
        return pageId;
    }

}
