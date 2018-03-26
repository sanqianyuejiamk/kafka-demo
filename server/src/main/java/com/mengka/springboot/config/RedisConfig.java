package com.mengka.springboot.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 *  redis配置
 *
 * @author huangyy
 * @version cabbage-forward2.0,2018-2-28
 * @since cabbage-forward2.0
 */
@Log4j2
@Configuration
public class RedisConfig {

    @Bean(name = {"MyRedisProperties"})
    @ConditionalOnMissingBean
    public MyRedisProperties redisProperties() {
        return new MyRedisProperties();
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties().getPool().getMaxActive());
        poolConfig.setMaxIdle(redisProperties().getPool().getMaxIdle());
        poolConfig.setMinIdle(redisProperties().getPool().getMinIdle());
        poolConfig.setMaxWaitMillis(redisProperties().getPool().getMaxWait());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(redisProperties().getHost());
        if (null != redisProperties().getPassword()) {
            jedisConnectionFactory.setPassword(redisProperties().getPassword());
        }
        jedisConnectionFactory.setPort(redisProperties().getPort());

        return jedisConnectionFactory;
    }

    @Bean(name = "cabbageforwardRedisTemplate")
    @Autowired
//    @ConditionalOnMissingBean(
//            name = {"cabbageforwardRedisTemplate"}
//    )
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        if(redisConnectionFactory==null){
            log.error("Redis Template Service is not available");
            return null;
        }
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }
}
