package com.jy.service.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/** 
 */
public abstract class RedisService<T, PK extends Serializable> implements RedisDao<T, PK> {
	
	protected final static Logger log = Logger.getLogger(RedisService.class);
	private RedisTemplate redisTemplate;
	/** 
	 * 配置redisCacheName的名字 
	 */
	private String redisBusinessName;

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setRedisBusinessName(String redisBusinessName) {
		this.redisBusinessName = redisBusinessName;
	}

	/**
	 * 向list尾部追加记录
	 * @param key
	 * @param obj
	 * @return
	 */
	public long leftPush(String key, Object obj){
		return redisTemplate.opsForList().leftPush(redisBusinessName + key,obj);
	}

	/**
	 * 向list头部追加记录
	 * @param key
	 * @param obj
	 * @return
	 */
	public long rightPush(String key, Object obj){
		return redisTemplate.opsForList().rightPush(redisBusinessName + key, obj);
	}

	/**
	 * 删除List中count条记录，被删除的记录为obj
	 * @param key
	 * @param count	要删除的数量，如果为负数，则从List的尾部检查并删除符合的记录
	 * @param obj 要匹配的值
	 * @return	删除后List中的记录数
	 */
	public long remove(String key,long count,Object obj){
		return redisTemplate.opsForList().remove(redisBusinessName + key, count, obj);
	}

	/**
	 * 获取缓存的list对象，如果start=0，end=-1，就获取缓存中所有数据
	 * @param key
	 * @param start	0
	 * @param end	-1
	 * @return
	 */
	public T range(PK key,long start,long end){
		try{
			// 从rediscache中获取成功,则返回
			if (redisTemplate != null) {
				Object obj = redisTemplate.opsForList().range(redisBusinessName + key, start, end);
				if (obj != null){
					return (T) obj;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *	将内容放到缓存map
	 * @param field	内容ID
	 * @param obj	内容对象
	 */
	public void setContentMap(PK field,T obj){
		HashOperations<PK, PK, T> hashOperations = redisTemplate.opsForHash();
		if (hashOperations != null){
			hashOperations.put((PK) (redisBusinessName +"detail"),field,obj);
		}
	}

	/**
	 * 通过field获取map
	 * @param field
	 * @return
	 */
	public T getContentMap(PK field){
		return (T) redisTemplate.boundHashOps(redisBusinessName+"detail").get(field);
	}
	/**
	 * 删除map中的某个对象
	 * @param field map中该对象的key
	 */
	public void delContentMapField(String... field){
		BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(redisBusinessName+"detail");
		boundHashOperations.delete(field);
	}

	public void delMapField(PK key, String... field){
		BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
		boundHashOperations.delete(field);
	}

	/**
	 * increment
	 * @param key	内容ID
	 * @param step
	 * @return
	 */
	public long incrementCont(PK key, long step) {
		return redisTemplate.opsForValue().increment(redisBusinessName+"counter::"+key, step);
	}

	/**
	 * 设置过期时间
	 * @param key
	 * @param time
	 * @return
	 */
	public boolean expire(PK key, long time) {
		return redisTemplate.expire(key, time, TimeUnit.SECONDS);
	}
}
