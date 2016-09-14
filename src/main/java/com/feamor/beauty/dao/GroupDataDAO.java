package com.feamor.beauty.dao;

import com.feamor.beauty.models.db.UserGroupData;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ParameterRegistration;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.ResultSetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.ParameterMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Home on 02.09.2016.
 */
@Transactional
@Repository
public class GroupDataDAO {
    public static Logger logger = Logger.getLogger(GroupDataDAO.class);

    @Autowired
    private SessionFactory sessionFactory;

    public UserGroupData firstGroupDataOfType(int type) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<UserGroupData> query = builder.createQuery(UserGroupData.class);
        Root<UserGroupData> root = query.from(UserGroupData.class);
        query = query.select(root).where(builder.equal(root.get("type"), type));
        UserGroupData data = sessionFactory.getCurrentSession().createQuery(query).setMaxResults(1).getSingleResult();
        return data;
    }

    public List<UserGroupData> getChildOfGroupData(int parentId, boolean withDeleted, String ...orders) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<UserGroupData> query = builder.createQuery(UserGroupData.class);
        Root<UserGroupData> root = query.from(UserGroupData.class);
        if (withDeleted) {
            query = query.select(root).where(
                builder.equal(root.get("parentId"), parentId)
            );
        } else {
            query = query.select(root).where(
                    builder.and(
                            builder.or(
                                    builder.isNull(root.get("removed")),
                                    builder.equal(root.get("removed"), Boolean.FALSE)
                            ),
                            builder.equal(root.get("parentId"), parentId))

            );
        }
        if (orders!=null) {
            ArrayList<Order> orderList = new ArrayList<>(orders.length);
            for(String order : orders) {
                Order orderItem = builder.asc(root.get(order));
                orderList.add(orderItem);
            }
            query = query.orderBy(orderList);
        }
        List<UserGroupData> data = sessionFactory.getCurrentSession().createQuery(query).getResultList();
        return data;
    }

    public void add(UserGroupData data) {
        add(null, data.getType(), data.getUserCreatorId(), data.getParentId(), data.getIntValue(), data.getStrValue(), data.getDateValue(), data.getTextValue(), null);
    }


    public int add(Integer dataId, int type, int creatorId, Integer parentId, Integer intValue, String strValue, Date dateValue, String textValue, Boolean removed) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"addOrUpdateUserGroupData\"");
        ParameterRegistration param;

        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dataId);
        param = call.registerParameter(1, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(type);
        param = call.registerParameter(2, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(creatorId);
        param = call.registerParameter(3, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(parentId);
        param = call.registerParameter(4, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(intValue);
        param = call.registerParameter(5, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(strValue);
        param = call.registerParameter(6, Timestamp.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dateValue);
        param = call.registerParameter(7, String.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(textValue);
        param = call.registerParameter(8, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(removed);
        ResultSetOutput resultSetOutput = (ResultSetOutput)call.getOutputs().getCurrent();
        Integer newDataId = (Integer) resultSetOutput.getSingleResult();
        return newDataId.intValue();
    }

    public Integer delete(Integer dataId, int userId, Boolean remove) {
        ProcedureCall call = sessionFactory.getCurrentSession().createStoredProcedureCall("public.\"removeUserGroupData\"");
        ParameterRegistration param;
        param = call.registerParameter(0, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(dataId);
        param = call.registerParameter(1, Integer.class, ParameterMode.IN);
        param.enablePassingNulls(false);
        param.bindValue(userId);
        param = call.registerParameter(2, Boolean.class, ParameterMode.IN);
        param.enablePassingNulls(true);
        param.bindValue(remove);
        Integer newDataId = (Integer) ((ResultSetOutput)call.getOutputs().getCurrent()).getSingleResult();
        return newDataId.intValue();
    }

    public boolean delete(UserGroupData data) {
        boolean result;
        result = delete(data.getDataId(), data.getUserCreatorId(), true) != null;
        return result;
    }

    public boolean delete(int dataId, int userId) {
        boolean result;
        result = delete(dataId, userId, true) != null;
        return result;
    }

    public void update(UserGroupData data) {
        add(data.getDataId(), data.getType(), data.getUserCreatorId(), data.getParentId(), data.getIntValue(), data.getStrValue(), data.getDateValue(), data.getTextValue(), data.getRemoved());
    }

    public void update(int dataId, int type, int creatorId, Integer parentId, Integer intValue, String strValue, Date dateValue, String textValue, Boolean removed) {
        add(dataId, type, creatorId, parentId, intValue, strValue, dateValue, textValue, removed);
    }

    public GroupData loadSingleByType(int type, String... orderValuesBy) {
        GroupData result = null;
        UserGroupData root = firstGroupDataOfType (type);
        if (root != null) {
            result = new GroupData();
            result.setKey(root);
            List<UserGroupData> list = getChildOfGroupData(root.getDataId(), true, orderValuesBy);
            result.setValues(list);
        }
        return result;
    }

    /*public GroupData loadSingleByType(int type, String... orderValuesBy) {
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

    public static class GroupData {
        private UserGroupData key;
        private List<UserGroupData> values;

        public int size() {
            return  values.size();
        }

        public UserGroupData getKey() {
            return key;
        }

        public void setKey(UserGroupData key) {
            this.key = key;
        }

        public List<UserGroupData> getValues() {
            return values;
        }

        public void setValues(List<UserGroupData> values) {
            this.values = values;
        }
    }


}
