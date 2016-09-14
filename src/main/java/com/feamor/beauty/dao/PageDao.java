package com.feamor.beauty.dao;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.*;
import com.feamor.beauty.models.ui.PageDom;
import com.feamor.beauty.models.ui.PageDomBlock;
import com.feamor.beauty.templates.TemplateElement;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        query = query.select(root).where(builder.equal(root.get("blockId"), blockId)).orderBy(builder.asc(root.get("position")));
        List<PageBlockData> result = sessionFactory.getCurrentSession().createQuery(query).list();
        return result;
    }

    public List<PageData> getDataOfPage(int pageId) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<PageData> query = builder.createQuery(PageData.class);
        Root<PageData> root = query.from(PageData.class);
        query = query.select(root).where(builder.equal(root.get("pageId"), pageId));
        List<PageData> result = sessionFactory.getCurrentSession().createQuery(query).list();
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

    public PageDom loadPageData(Page page) {
        if (page != null) {
            PageDom dom = new PageDom();
            dom.setPage(page);
            List<PageBlock> blocks = getBlocksOfPage(page.getPageId());
            if (blocks != null) {
                HashMap<Integer, PageDomBlock> pageDomBlocks = new HashMap<Integer, PageDomBlock>();
                for (PageBlock block : blocks) {
                    PageDomBlock domBlock = new PageDomBlock();
                    domBlock.setBlock(block);
                    domBlock.setPage(dom);
                    pageDomBlocks.put(domBlock.getBlock().getBlockId(), domBlock);
                }
                dom.setBlocks(pageDomBlocks);
            }
            return dom;
        } else {
            return null;
        }
    }

    public PageDom loadPage(int id) {
        Page page = getPage(id);
        PageDom dom = loadPageData(page);
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

}
