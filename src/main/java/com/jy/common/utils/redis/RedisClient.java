package com.jy.common.utils.redis;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.jy.entity.system.Comment;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

/**
 * Created by spring on 2017/5/26.
 */
public class RedisClient {
    private JedisPool jedisPool;//非切片连接池
    private ShardedJedisPool shardedJedisPool;//切片连接池
    private static RedisClient redisClient;
    private static JedisCluster jc;

    static {
        //只给集群里一个实例就可以
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7000));
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7001));
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7002));
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.10.142", 7005));

        jc = new JedisCluster(jedisClusterNodes);
    }

    public RedisClient() {
        initialPool();
//        initialShardedPool();
    }

    public static RedisClient getRedisClient() {
        if (redisClient == null) {
            synchronized (RedisClient.class) {
                if (redisClient == null) redisClient = new RedisClient();
            }
        }
        return redisClient;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public ShardedJedis getShardedJedis() {
        return shardedJedisPool.getResource();
    }

    public JedisCluster getJedisCluster() {
        return jc;
    }

    /**
     * 初始化非切片池
     */
    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void Close() {
        jedisPool.close();
        shardedJedisPool.close();
    }
    @Before
	public void initConn(){
        redisClient = RedisClient.getRedisClient();
	}
    @Test
    public void test1(){
        /**
         * hset  hget
         * 将哈希表key 中的域field 的值设为value 。
         * 如果key 不存在，一个新的哈希表被创建并进行HSET操作。如果域field 已经存在于哈希表中，旧值将被覆盖。
         */
        long n = redisClient.getJedis().hset("hsetkey", "hashKey1", "hashValue222");//
        System.out.println("n1 : "+n);
        n = redisClient.getJedis().hset("hsetkey", "hashKey1", "hashValue");//
        System.out.println("n2 : "+n);
        String hash = redisClient.getJedis().hget("hsetkey", "hashKey");//返回哈希表key 中给定域field 的值
        System.out.println("测试 hset hget ： hsetkey 的返回值："+hash);
    }
    @Test
    public void test2(){
        /**
         * hsetnx 将哈希表 key 中的域 field(指第二个参数) 的值设置为 value ，当且仅当域 field 不存在。
         * 若域 field(指第二个参数) 已经存在，该操作无效。
         */
        long n = redisClient.getJedis().hsetnx("hsetkeynx", "hashkeynx", "hashvaluenx");
        System.out.println(n!=0?"操作成功":"操作失败"); // print 操作成功
        n = redisClient.getJedis().hsetnx("hsetkeynx", "hashkey", "hashvaluenx");
        System.out.println(n!=0?"操作成功":"操作失败"); // print 操作成功
        n = redisClient.getJedis().hsetnx("hsetkeynx", "hashkey", "hashvaluenx");
        System.out.println(n!=0?"操作成功":"操作失败"); // print 操作失败
    }
    @Test
    public void test3(){
        /**
         * hmset hmget
         *
         */
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hashMap1", "hashValue1");
        hashMap.put("hashMap2", "hashValue2");
        hashMap.put("hashMap3", "hashValue3");
        hashMap.put("hashMap4", "hashValue4");
        String status  = redisClient.getJedis().hmset("hashMapkey", hashMap);//如果命令执行成功，返回OK 。当key 不是哈希表(hash) 类型时，返回一个错误。
        String hash = redisClient.getJedis().hget("hashMapkey", "hashMap4");
        System.out.println("OK".equals(status)?"操作成功  返回值："+hash:"操作失败");
        //返回值： 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
        List<String> hashList = redisClient.getJedis().hmget("hashMapkey", "hashMap1 hashMap2 hashMap3 hashMap4".split(" "));
        for(String value : hashList){
            System.out.print("对应的value值：  "+value+" ");//返回值： 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样
        }
        System.out.println();

        //hgetall  获得一个Map 返回key整个file域
        Map<String,String> hashMapKey = redisClient.getJedis().hgetAll("hashMapkey");
        System.out.println(hashMapKey);
        // map 的第一种迭代方式
        Set<Map.Entry<String, String>> entry = hashMapKey.entrySet();
        Iterator<Map.Entry<String, String>> it = entry.iterator();
        while(it.hasNext()){
            Map.Entry<String, String> e  = it.next();
            System.out.println("key: "+e.getKey()+"  value: "+e.getValue());
        }

        // map的第二种迭代方式
        Set<String> keySet = hashMapKey.keySet();// map中的所有key在set中存放着，可以通过迭代set的方式 来获得key
        Iterator<String> iter = keySet.iterator();
        while(iter.hasNext()){
            String key = iter.next();
            String value = hashMapKey.get(key);
            System.out.println("key2: "+key+"  value2: "+value);
        }
    }

    @Test
    public void test4(){
        //hscan  类似于 scan 遍历库中 key 下所有的域   返回  file-value 以map 的形式；
		ScanResult<Map.Entry<String, String>> hscanResult = redisClient.getJedis().hscan("hashMapkey", "2");
		int cursor = hscanResult.getCursor(); // 返回0 说明遍历完成
		System.out.println("游标"+cursor);
		List<Map.Entry<String, String>> scanResult = hscanResult.getResult();
		for(int m = 0;m < scanResult.size();m++){
			Map.Entry<String, String> mapentry  = scanResult.get(m);
			System.out.println("key: "+mapentry.getKey()+"  value: "+mapentry.getValue());
		}
    }
    @Test
    public void test5(){
        //hkeys
        Set<String> setKey = redisClient.getJedis().hkeys("hashMapkey");// keys 返回 所有的key  ,hkeys 返回 key 下面的所有的 域
        Iterator<String> itset = setKey.iterator();
        String files = "";
        while(itset.hasNext()){
            files =files+" "+itset.next();
        }
        System.out.println("hashMapkey 中的所有域 为："+files);
    }
    @Test
    public void test6(){
        //hvals 返回哈希表key 中所有域的值。可用版本： >= 2.0.0时间复杂度： O(N)，N 为哈希表的大小。返回值：一个包含哈希表中所有值的表。当key 不存在时，返回一个空表。
        List<String> list = redisClient.getJedis().hvals("hashMapkey");
        for(String s : list){
            System.out.println(s);
        }
    }
    @Test
    public void test7(){
        // 以上 域对应的值是String  下面域对应的值 是list
        Map<String,List<String>> testMapList = new HashMap<String,List<String>>();
        List<String> testList = Arrays.asList("testList testList testList testList testList testList testList 01");
        List<String> testList1 = Arrays.asList("testList1 testList1 testList1 testList1 testList1 testList1 testList1 11");
        List<String> testList2 = Arrays.asList("testList2 testList2 testList2 testList2 testList2 testList2 testList2 21");
        testMapList.put("testList", testList);
        testMapList.put("testList1", testList1);
        testMapList.put("testList2", testList2);
        String mapString  =  JSON.toJSONString(testMapList, true);// map 转为json串
        redisClient.getJedis().set("hashMapkey2", mapString);
        mapString = redisClient.getJedis().get("hashMapkey2");
        System.out.println(mapString);
        testMapList = (Map<String,List<String>>) JSON.parse(mapString);
        Set<Map.Entry<String, List<String>>> mapListSet = testMapList.entrySet();
        Iterator<Map.Entry<String, List<String>>> maplistIter = mapListSet.iterator();
        while(maplistIter.hasNext()){
            Map.Entry<String, List<String>> mapentryList = maplistIter.next();
            String key = mapentryList.getKey();
            List<String> entryList = mapentryList.getValue();
            System.out.println("testMapList key: "+key+", testMapList value: "+entryList.toString());
        }
    }
    @Test
    public void test8(){
        // Map 里面存储实体对象
		Map<String,Comment> testMapEntity = new HashMap<String,Comment>();
        Comment bar = new Comment();
        bar.setContId(101);bar.setLoginName("lvxiaojian");
        Comment bar1 = new Comment();
        bar1.setContId(102);bar.setLoginName("wagnbo");
		testMapEntity.put("bar", bar);
		testMapEntity.put("bar1", bar1);
		String entityString  =  JSON.toJSONString(testMapEntity,true);// map 转为json串
        redisClient.getJedis().set("hashMapkey3", entityString);
		entityString = redisClient.getJedis().get("hashMapkey3");
		testMapEntity = (Map<String,Comment>)JSON.parse(entityString);
		Set<String> entitySet = testMapEntity.keySet();
		Iterator<String> iterentity = entitySet.iterator();
		while(iterentity.hasNext()){
			System.out.println("testMapEntity key: "+iterentity.next()+", testMapEntity value: "+testMapEntity.get(iterentity.next()));
		}
    }
    @Test
    public void test9(){
        //hlen  返回值：哈希表中域的数量。当key 不存在时，返回0 。
        long n = redisClient.getJedis().hlen("hashMapkey");
        System.out.println("hashMapkey 中域的数量为： "+n);
    }
    @Test
    public void test10(){
        //hdel  返回值: 被成功移除的域的数量，不包括被忽略的域
        long n = redisClient.getJedis().hdel("hashMapkey", "hashMap1 hashMap2 hashMap3 hashMap4".split(" "));
        System.out.println("被成功移除的域的数量，不包括被忽略的域: "+n);
    }

    @Test
    public void test11(){
        //hexists  返回值：如果哈希表含有给定域，返回1 。如果哈希表不含有给定域，或key 不存在，返回0 。
        boolean flag = redisClient.getJedis().hexists("hashMapkey", "hashMap1");
        System.out.println(flag?"哈希表含有给定域":"哈希表不含有给定域");
    }
    @Test
    public void test12(){
        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.clear();// 清除map
        hashMap.put("hashMap1", "1");
        hashMap.put("hashMap2", "2");
        hashMap.put("hashMap3", "3");
        hashMap.put("hashMap4", "4");
        hashMap.put("hashMap5", "5");
        hashMap.put("hashMap6", "6");
        redisClient.getJedis().hmset("hashMapkey", hashMap);
        boolean flag = redisClient.getJedis().hexists("hashMapkey", "hashMap1");
        System.out.println(flag?"哈希表含有给定域":"哈希表不含有给定域");
    }
    @Test
    public void test13(){
        //hincrBy  key 存在  域也存在的情况  返回值： 执行HINCRBY 命令之后，哈希表key 中域field 的值
//        System.out.println("对 hash表中key 为hashMapkey 的域hashMap1 的值   减去 1 之前数据为："+redisClient.getJedis().hget("hashMapkey", "hashMap1"));// 返回值：对 hash表中key 为hashMapkey 的域hashMap1 的值   减去 1 之前数据为：1
//        long n = redisClient.getJedis().hincrBy("hashMapkey", "hashMap1", -1); // 对 hash表中key 为hashMapkey 的域hashMap1 的值  减去 1
//        System.out.println("对 hash表中key 为hashMapkey 的域hashMap1 的值  减去 1 结果为："+n);// 返回值：对 hash表中key 为hashMapkey 的域hashMap1 的值  减去 1 结果为：0

        System.out.println("对 hash表中key 为hashMapkey 的域hashMap2 的值  加上 2 之前数据为：" + redisClient.getJedis().hget("hashMapkey", "hashMap2"));//返回值：对 hash表中key 为hashMapkey 的域hashMap2 的值  加上 2 之前数据为：2
        long n = redisClient.getJedis().hincrBy("hashMapkey", "hashMap2", 2); // 对 hash表中key 为hashMapkey 的域hashMap2 的值  加上 2
        System.out.println("对 hash表中key 为hashMapkey 的域hashMap2 的值  加上 2 结果为："+n);//返回值：对 hash表中key 为hashMapkey 的域hashMap2 的值  加上 2 结果为：4
    }
    @Test
    public void testlist1(){
        /**
         * List 实际是堆栈，先进后出；而不是队列，先进先出
         * lpush lrange ；lpush 当key 存在但不是列表类型时，返回一个错误  返回值：
         * 执行LPUSH 命令后，列表的长度。 先进后出
         */
        long n = redisClient.getJedis().lpush("jedisList", "a b c d e f".split(" "));
        List<String> jedisList = redisClient.getJedis().lrange("jedisList", 0, 8);
        System.out.println("jedisList 返回列表长度： "+ n +" 列表的值： "+jedisList);
        n = redisClient.getJedis().lpush("jedisList", "a b c d e f".split(" "));// 列表允许值有重复
        jedisList = redisClient.getJedis().lrange("jedisList", 0, 16);
        System.out.println("jedisList 返回列表长度： "+ n +" 列表的值： "+jedisList);
    }
    @Test
    public void testlist2(){
        /**
         * lpushx 一次只能向表头push一个值，当且仅当key存在时，如果不存在则不做任何操作。
         */
        Comment comment = new Comment();
        comment.setContId(101);
        comment.setLoginName("101");
        Comment comment2 = new Comment();
        comment2.setContId(102);
        comment2.setLoginName("102");
        redisClient.getJedisCluster().lpush("jedisList1", "");
        redisClient.getJedis().lpush("jedisList1", "12");
        long n= redisClient.getJedis().lpushx("jedisList1", "13");
        List<String>jedisList = redisClient.getJedis().lrange("jedisList1", 0, 20);
        System.out.println("jedisList 返回列表长度： "+ n +" 列表的值： "+jedisList);
        //lpop 移除头元素，返回头元素
        String top = redisClient.getJedis().lpop("jedisList1");
        System.out.println(top);
    }
    @Test
    public void testlist3(){
        //llen返回列表key 的长度。 如果key 不存在，则key 被解释为一个空列表，返回0 . 如果key 不是列表类型，返回一个错误。
        long n = redisClient.getJedis().llen("jedisList1");
        System.out.println(n);
    }
}