package com.feamor.beauty.dao;

import com.feamor.beauty.models.BlockType;
import com.feamor.beauty.models.Page;
import com.feamor.beauty.models.PageBlock;
import com.feamor.beauty.models.PageType;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Home on 27.02.2016.
 */

@Transactional
@Repository
public class PageDao {
//    @Autowired
//    private HibernateTemplate sessionTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    public List<Page> listPages() {
        List<Page> result = sessionFactory.getCurrentSession().createCriteria(Page.class).list();
        return result;
    }

    public List<PageBlock> listPageBlocks() {
        List<PageBlock> result = sessionFactory.getCurrentSession().createCriteria(PageBlock.class).list();
        return result;
    }

    public List<BlockType> listBlockTypes() {
        List<BlockType> result = sessionFactory.getCurrentSession().createCriteria(BlockType.class).list();
        return result;
    }

    public List<PageType> listPageTypes() {
        List<PageType> result = sessionFactory.getCurrentSession().createCriteria(PageType.class).list();
        return result;
    }

    public PageBlock getPageBlockWithId(int id) {
        PageBlock block = (PageBlock) sessionFactory.getCurrentSession().createCriteria(PageBlock.class).add(Restrictions.idEq(id)).uniqueResult();
        return block;
    }

    public  List<PageBlock> listPageBlocks(int pageId) {
        List<PageBlock> result = sessionFactory.getCurrentSession().createCriteria(PageBlock.class).add(Restrictions.sqlRestriction("fk_page = "+pageId)).list();
        return result;
    }
}
