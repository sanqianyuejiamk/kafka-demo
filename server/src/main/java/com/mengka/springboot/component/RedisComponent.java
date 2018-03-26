package com.mengka.springboot.component;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  redis工具类
 *
 * @author huangyy
 * @version cabbage-forward2.0,2017-10-18
 * @since cabbage-forward2.0
 */
@Log4j2
@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate<Object, Object> cabbageforwardRedisTemplate;

    private ValueOperations<Object, Object> operations;

    private HashOperations<Object, Object, Object> hashOperations;

    private ZSetOperations<Object, Object> zSetOperations;

    @PostConstruct
    public void init() {
        operations = cabbageforwardRedisTemplate.opsForValue();
        hashOperations = cabbageforwardRedisTemplate.opsForHash();
        zSetOperations = cabbageforwardRedisTemplate.opsForZSet();
    }

    public void setString(String key, String value) {
        operations.set(key, value);
    }

    public void setString(String key, String value, Long expire) {
        operations.set(key, value, expire, TimeUnit.SECONDS);
    }

    public String getString(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object obj = operations.get(key);
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    public void set(Object key, Object value) {
        operations.set(key, value);
    }

    public void set(Object key, Object value, Long expire) {
        operations.set(key, value, expire, TimeUnit.SECONDS);
    }

    public Object get(Object key) {
        return operations.get(key);
    }

    public void delete(String key) {
        cabbageforwardRedisTemplate.delete(key);
    }

    public Map<Object, Object> getCacheMap(String key) {
        return hashOperations.entries(key);
    }

    /**
     * 获取MAP中缓存对象
     *
     * @param key
     * @param paramKey
     * @return
     */
    public Object getCacheMap(String key, String paramKey) {
        return hashOperations.get(key, paramKey);
    }


    /**
     * 缓存MAP项等
     *
     * @param key
     * @param value
     */
    public void setCacheMap(String key, String paramKey, Object value) {
        hashOperations.put(key, paramKey, value);
    }

    /**
     *  缓存数据自增
     *
     * @param key
     * @param paramKey
     * @param incNum
     * @return
     */
    public Long incCacheMap(String key,String paramKey, long incNum){
        return hashOperations.increment(key, paramKey, incNum);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public void setCacheMap(String key, Map<String, Object> dataMap) {
        log.debug("berth info size:" + dataMap.size());
        if (null != dataMap) {
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 删除MAP中缓存对象
     *
     * @param key
     * @param paramKey
     */
    public void removeCacheMap(String key, String paramKey) {
        hashOperations.delete(key, paramKey);
    }

    /**
     * 根据KEY获取缓存MAP中的所有关键字
     *
     * @param key
     * @return
     */
    public Set<Object> getCacheKeys(String key) {
        return hashOperations.keys(key);
    }

    /**
     * ZADD key score member，将一个 member元素及其 score值加入到有序集 key当中
     *
     * @param key
     * @param score
     * @param member
     */
    public void zaddString(String key, double score, Object member) {
        zSetOperations.add(key, member, score);
    }

    /**
     * ZRANGE key start stop，返回有序集 key中，指定区间内的成员
     *
     * @param key
     * @param fromValue
     * @param toValue
     * @return
     */
    public Set<Object> zrangeByScoreString(String key, double fromValue, double toValue) {
        return zSetOperations.rangeByScore(key, fromValue, toValue);
    }

    /**
     * ZRANGE key start stop，返回有序集 key中，指定区间内的成员
     *
     * @param key
     * @param fromValue
     * @param toValue
     * @param offset
     * @param count
     * @return
     */
    public Set<Object> zrangeByScoreString(String key, double fromValue, double toValue, int offset, int count) {
        return zSetOperations.rangeByScore(key, fromValue, toValue, offset, count);
    }

    /**
     * ZREM key start stop
     *
     * @param key
     * @param minScore
     * @param maxScore
     * @return
     */
    public Long zremrangeByScore(String key, double minScore, double maxScore) {
        return zSetOperations.removeRangeByScore(key, minScore, maxScore);
    }

    /**
     *  从有序集移除元素
     *
     * @param key
     * @param values
     * @return
     */
    public Long zremString(String key, Object... values) {
        return zSetOperations.remove(key, values);
    }
}
