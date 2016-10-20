package com.feamor.beauty.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Home on 18.10.2016.
 */
public class DataCache {
    public static final int DEFAULT_MIN_STORE_TIME = 1 * 60 * 60 * 1000; // 1h
    public static final int DEFAULT_UPDATE_PERIOD = 10 * 60 * 1000; // 10 min

    private HashMap<Object, CachedData> dataHash = new HashMap<>();
    private ArrayList<CachedData> dataList = new ArrayList<>();
    private long updateTimeout;
    private long minTimeInCache;
    private long lastUpdateTime;

    public DataCache(long minStoreTime, long updateTimeout) {
        this.minTimeInCache = minStoreTime;
        this.updateTimeout = updateTimeout;
    }

    public CachedData create(Object data, Object id) {
        updateCache();
        long time = Calendar.getInstance().getTimeInMillis();
        CachedData cached = new CachedData(time, id, data);
        synchronized (this) {
            if (dataList.size() == 0) {
                lastUpdateTime = time;
            }
            CachedData oldValue = dataHash.put(id, cached);
            if (oldValue != null) {
                dataList.remove(oldValue.getIndex());
            }
            dataList.add(cached);
            int index = dataList.size() - 1;
            cached.setIndex(index);
        }
        return cached;
    }


    public CachedData get(Object id) {
        CachedData result;
        synchronized (this) {
            result = dataHash.get(id);
        }
        return result;
    }

    public void clear() {
        synchronized (this) {
            dataHash.clear();
            dataList.clear();
            lastUpdateTime = 0;
        }
    }

    public void remove(CachedData cached) {
        synchronized (this) {
            if (dataHash.remove(cached.getId()) != null) {
                dataList.remove(cached.getIndex());
            }
        }
        updateCache();
    }

    public void updateCache() {
        synchronized (this) {
            long now = Calendar.getInstance().getTimeInMillis();
            if (now - lastUpdateTime > updateTimeout) {
                lastUpdateTime = now;
                while (dataList.size() > 0 && now - dataList.get(0).getAddTime() > minTimeInCache) {
                    dataHash.remove(dataList.get(0).getId());
                    dataList.remove(0);
                }
            }
        }
    }
}
