package com.taotao.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
	@Test
	// 因为单机版的redis和集群版的redis通常是不并存的，使用一个就可以了
	// 所以在做测试的时候，需要在applicationContext-redis.xml中把另一个的配置注释掉
	public void testJedisClientPool() throws Exception {
		// 初始化Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-redis.xml");
		// 从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		// 使用JedisClient对象操作redis
		jedisClient.set("jedisClient", "mytest");
		String result = jedisClient.get("jedisClient");
		System.out.println(result);
	}
}
