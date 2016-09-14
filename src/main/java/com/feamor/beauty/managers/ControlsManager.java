package com.feamor.beauty.managers;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.pages.BasePageControl;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 19.05.2016.
 */
@Component
public class ControlsManager {

    private HashMap<Integer, BaseBlockControl> blocksById = new HashMap<Integer, BaseBlockControl>();
    private HashMap<Integer, BasePageControl> pagesById = new HashMap<Integer, BasePageControl>();
    private HashMap<String, BaseBlockControl> blocksByTag = new HashMap<String, BaseBlockControl>();

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void initialize(){
        Map<String, BaseBlockControl> blocks = context.getBeansOfType(BaseBlockControl.class);
        for(HashMap.Entry<String, BaseBlockControl> entry : blocks.entrySet()) {
            entry.getValue().initialize(entry.getKey(), this);
        }

        Map<String, BasePageControl> pages = context.getBeansOfType(BasePageControl.class);
        for(HashMap.Entry<String, BasePageControl> entry : pages.entrySet()) {
            entry.getValue().initialize(entry.getKey(), this);
        }
    }

    public void registerBlock(BaseBlockControl block, int id) {
        synchronized (blocksById) {
            blocksById.put(id, block);
        }
    }

    public BaseBlockControl unregisterBlock(int blockId) {
        synchronized (blocksById) {
            return blocksById.remove(blockId);
        }
    }

    public BaseBlockControl getBlock(int id) {
        BaseBlockControl block;
        synchronized (blocksById) {
            block = blocksById.get(id);
        }
        return  block;
    }

    public void registerPage(BasePageControl page, int id) {
        synchronized (pagesById) {
            pagesById.put(id, page);
        }
    }

    public BasePageControl unregisterPage(int pageId) {
        synchronized (pagesById) {
            return pagesById.remove(pageId);
        }
    }

    public BasePageControl getPage(int id) {
        BasePageControl page;
        synchronized (pagesById) {
            page = pagesById.get(id);
        }
        return  page;
    }

    public void registerBlockWithName(BaseBlockControl block, String tag) {
        synchronized (blocksByTag) {
            blocksByTag.put(tag, block);
        }
    }

    public BaseBlockControl unregisterBlockForName(String tag) {
        synchronized (blocksByTag) {
            return blocksByTag.remove(tag);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public BaseBlockControl getBlockForName(String blockName) {
        BaseBlockControl block;
        synchronized (blocksByTag) {
            block = blocksByTag.get(blockName);
        }
        return  block;
    }
}
