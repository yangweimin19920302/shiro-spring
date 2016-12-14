package com.spring.shiro;

import com.redis.JedisClusterKeys;
import com.redis.JedisClusterManage;
import com.redis.RedisManager;
import com.springapp.mvc.SerializeUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class RedisSessionDAO extends AbstractSessionDAO {

    private static final String ID = "Id:";
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    /**
     * 创建session
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    /**
     * 获取session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            logger.error("session id is null");
            return null;
        }
        byte[] values = JedisClusterManage.get(this.getByteKey(sessionId));
        if (values == null) {
            return null;
        }
        Session session = (Session) SerializeUtils.deserialize(values);
        return session;
    }

    /**
     * 更新session
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * 删除session
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        JedisClusterManage.del(this.getByteKey(session.getId()));
    }

    /**
     * 获取所有session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();
        Set<String> keys = JedisClusterManage.keys(this.ID + "*");
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                Session s = (Session) SerializeUtils.deserialize(JedisClusterManage.get(key.getBytes()));
                sessions.add(s);
            }
        }
        return sessions;
    }

    /**
     * save session
     *
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        byte[] key = getByteKey(session.getId());
        byte[] value = SerializeUtils.serialize(session);
        JedisClusterManage.set(key, value);
    }

    /**
     * 获得byte[]型的key
     *
     * @return
     */
    private byte[] getByteKey(Serializable sessionId) {
        String preKey = this.ID + sessionId;
        return preKey.getBytes();
    }
}
