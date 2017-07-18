package com.jy.service.redis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 定义cache获取的接口类
 * 
 * @param <T>cache对象的类型
 * @param <PK>cache对象的key,为一个数组对象
 */
public interface RedisDao<T, PK extends Serializable> {

	/**
	 * LIST操作
	 */
	public long leftPush(PK key, Object obj);
	public long rightPush(PK key, Object obj);
	public long remove(PK key,long count,Object obj);
	public T range(PK key,long start,long end);

	/**
	 * MAP操作
	 */
	public void setContentMap(PK field,T obj);
	public T getContentMap(PK field);
	public void setMap(PK key,PK field,T obj);
	public T getMap(PK key,PK field);
	public void delContentMapField(PK... field);
	public void delMapField(PK key, String... field);

	/**
	 *	计数器操作
 	 */
	public long incrementCont(PK key, long step);

	public boolean expire(PK key, long time);
}
