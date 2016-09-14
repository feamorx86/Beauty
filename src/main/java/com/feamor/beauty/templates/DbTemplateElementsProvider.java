package com.feamor.beauty.templates;

import com.feamor.beauty.dao.PageDao;

import java.util.ArrayList;

/**
 * Created by Home on 19.05.2016.
 */
public class DbTemplateElementsProvider {
    private int templateId;
    private PageDao pageDao;
    private ArrayList<TemplateElement> loaded = new ArrayList<TemplateElement>();
    private int index = 1;
    private int loadedIndex = 0;
    private int readBy;
    private int maxElements = -1;

    private boolean isStarted;
    private boolean hasMoreElements;

    public DbTemplateElementsProvider(int templateId, PageDao pageDao, int readBy) {
        this.templateId = templateId;
        this.pageDao = pageDao;
        this.readBy = readBy;
        isStarted = false;
        hasMoreElements = false;
    }

    private void loadCounts() {
        Integer elements = pageDao.getTemplateElementsCount(templateId);
        if (elements!=null) {
            maxElements = elements.intValue();
            hasMoreElements = true;
        } else {
            hasMoreElements = false;
            maxElements = 0;
        }
        isStarted = true;
    }

    private void loadNext() {
        loadedIndex = 0;
        if (index <= maxElements) {
            int read = pageDao.loadTemplateElements(templateId, index, readBy, maxElements, loaded);
            if (read <= 0) {
                hasMoreElements = false;
            } else {
                index += read;
            }
        } else {
            hasMoreElements = false;
        }
    }

    public boolean hasNext() {
        if (!isStarted) {
            loadCounts();
        }

        if (hasMoreElements) {
            if (loadedIndex >= loaded.size()) {
                loadNext();
            }
        }

        return hasMoreElements;
    }

    public TemplateElement next() {
        if (!isStarted) {
            loadCounts();
        }

        TemplateElement result = null;

        if (hasMoreElements) {
            if (loadedIndex >= loaded.size()) {
                loadNext();
            }

            if (loadedIndex < loaded.size()) {
                result = loaded.get(loadedIndex);
                loadedIndex++;
            }
        }

        return result;
    }
}