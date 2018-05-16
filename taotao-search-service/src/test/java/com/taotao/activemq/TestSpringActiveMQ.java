package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActiveMQ {
	@Test
	public void testSpringActiveMQ() throws Exception {
		// 初始化一个Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		// 等待
		System.in.read();
	}
}
