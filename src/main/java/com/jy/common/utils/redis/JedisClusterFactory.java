package com.jy.common.utils.redis;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * Jedis集群工厂
 * @author xbq
 * @Date 2017-05-14
 */
public class JedisClusterFactory implements InitializingBean,FactoryBean<JedisCluster>{

	private String addressConfig;

	// 下面变量 对应spring redis配置文件中的 property的name
	private JedisCluster jedisCluster;
	private String addressKeyPrefix;
	private Integer timeout;
	private Integer maxRedirections;
	private GenericObjectPoolConfig genericObjectPoolConfig;

	// 正则表达式 匹配 ip和port
	private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

	/**
	 * 初始化的bean时，执行此方法，创建jedisCluster
	 */
	public void afterPropertiesSet() throws Exception {
		Set<HostAndPort> jedisClusterNode= this.parseHostAndPort();
		jedisCluster = new JedisCluster(jedisClusterNode, timeout, maxRedirections, genericObjectPoolConfig);
	}

	/**
	 * 实现 FactoryBean 的接口
	 * 获取 jedisCluster对象
	 */
	public JedisCluster getObject() throws Exception {
		return jedisCluster;
	}

	/**
	 * 实现 FactoryBean 的接口
	 * 获取 jedisCluster的类型
	 */
	public Class<? extends JedisCluster> getObjectType() {
		return (jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
	}

	/**
	 * 实现 FactoryBean 的接口
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * 解析Jedis配置文件，看是否满足 IP和端口
	 * @return
	 */
	private Set<HostAndPort> parseHostAndPort() throws Exception{
		Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
		try {
			Properties properties = new Properties();
			properties.load(this.getClass().getResourceAsStream(this.addressConfig));

			for(Object key : properties.keySet()){
				// 如果key不是以 addressKeyPrefix的值 开头，则continue
				if(!((String)key).startsWith(addressKeyPrefix)){
					continue;
				}
				// 根据 key从properties中取出值
				String valus = (String) properties.get(key);
				// 判断取出的value是否是ip和port
				boolean isIPProt = p.matcher(valus).matches();
				if(!isIPProt){
					throw new IllegalArgumentException("ip和port不合法！");
				}
				String[] ipAndPort = valus.split(":");
				HostAndPort hostAndPort = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
				hostAndPorts.add(hostAndPort);
			}
		} catch (Exception e) {
			throw new Exception("解析 jedis 配置文件失败!");
		}
		return hostAndPorts;
	}

	// set方法
	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	public void setAddressKeyPrefix(String addressKeyPrefix) {
		this.addressKeyPrefix = addressKeyPrefix;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public void setMaxRedirections(Integer maxRedirections) {
		this.maxRedirections = maxRedirections;
	}
	public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
		this.genericObjectPoolConfig = genericObjectPoolConfig;
	}
	public void setAddressConfig(String addressConfig) {
		this.addressConfig = addressConfig;
	}
}
