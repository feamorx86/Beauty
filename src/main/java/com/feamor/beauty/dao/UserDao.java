package com.feamor.beauty.dao;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.PageBlockData;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.models.db.UserGroup;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Home on 13.05.2016.
 */
@Repository
@Transactional
public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class);

    @Autowired
    private SessionFactory sessionFactory;
    private HashMap<String, Integer> activeSessions = new HashMap<String, Integer>();
    private HashMap<Integer, UserGroup> userGroups = new HashMap<Integer, UserGroup>();

    @PostConstruct
    public void initialization() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<UserGroup> query = session.getCriteriaBuilder().createQuery(UserGroup.class);
        Root<UserGroup> root = query.from(UserGroup.class);
        query = query.select(root);
        List<UserGroup> groups = session.createQuery(query).list();
        for (UserGroup group : groups) {
            userGroups.put(group.getId(), group);
        }
        session.close();
    }

    public UserGroup findGroupWithName(String groupName) {
        UserGroup result = null;
        for (UserGroup group : userGroups.values()) {
            if (groupName.equalsIgnoreCase(group.getName())) {
                result = group;
                break;
            }
        }
        return result;
    }

    public UserGroup getGroup(int id) {
        return userGroups.get(id);
    }

    public void putActiveUserSession(String sessionId, int userId) {
        activeSessions.put(sessionId, userId);
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("INSERT INTO public.\"UserData\"(type, \"userId\", \"strValue\") " +
                "VALUES ("+Constants.UserDataTypes.SESSION_ID+", :userId, :strValue);");
        int result = query.setParameter("userId", userId).setParameter("strValue", sessionId).executeUpdate();
        if (result !=1 ) {
            logger.error("putActiveUserSession : insert values, updated != 1, value : "+result);
        };
    }

    public User getUserForSession(String sessionId) {
        User user = null;
        if (!StringUtils.isEmpty(sessionId)) {
            Integer userId = activeSessions.get(sessionId);
            if (userId != null) {
                user = getUserById(userId.intValue());
            } else {
                NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT ud.\"userId\" FROM \"UserData\" as ud WHERE type = " + Constants.UserDataTypes.SESSION_ID + " AND \"strValue\" = ?");
                query.setParameter(1, sessionId);
                userId = (Integer) query.uniqueResult();
                if (userId != null) {
                    user = getUserById(userId.intValue());
                }
            }
        }
        return user;
    }

    public User getUserForLoginAndPassword(String login, String password) {
        User user = null;
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery(
                "SELECT t1.\"userId\" FROM public.\"UserData\" as t1, \"UserData\" as t2 " +
                "WHERE t1.\"type\" = "+ Constants.UserDataTypes.REGISTRATION_LOGIN+" and t1.\"strValue\" = :login and " +
                    "t2.\"type\" = "+ Constants.UserDataTypes.REGISTRATION_PASSWORD+" and t2.\"strValue\" = :password and " +
                    "t1.\"userId\" = t2.\"userId\";");
        Integer userId = (Integer) query.setParameter("login", login).setParameter("password", password).uniqueResult();
        if (userId != null) {
            user = getUserById(userId.intValue());
        }
        return user;
    }

    public User getUserById(int id) {
        CriteriaBuilder builder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query = query.select(root).where(builder.equal(root.get("id"), id));
        User result = sessionFactory.getCurrentSession().createQuery(query).uniqueResult();
        return result;
    }

    public ArrayList<UserGroup> getUserGroups(int userId) {
        ArrayList<UserGroup> groups = null;
        NativeQuery query = sessionFactory.getCurrentSession().createNativeQuery("SELECT ug.\"groupId\" FROM public.\"UserInGroup\" as ug WHERE ug.\"userId\" = :uid;");
        List<Integer> groupIds = query.setParameter("uid", userId).list();
        if (groupIds!=null && groupIds.size()>0) {
            groups = new ArrayList<UserGroup>();
            for(Integer groupId : groupIds) {
                groups.add(getGroup(groupId));
            }
        }
        return groups;
    }
}
