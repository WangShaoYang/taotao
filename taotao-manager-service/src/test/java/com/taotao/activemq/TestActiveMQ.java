package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMQ {
	// Queue Producer
	@Test
	public void testQueueProducer() throws Exception {
		// 创建一个连接工厂ConnectionFactory对象，指定ActiveMQ服务的ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 使用ConnectionFactory创建一个连接对象Connection
		Connection connection = connectionFactory.createConnection();
		// 开启连接，调用Connection对象的start方法
		connection.start();
		// 使用Connection对象创建一个Session对象
		// 第一个参数：是否开启事务（效率问题一般不使用，会产生临时的数据不一致问题，但是保证数据的最终一致性）；
		// 第二个参数：如果第一个参数是true，第二个参数自动忽略；如果第一个参数是false，第二个参数为消息的应答模式，一般自动应答就可以
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session对象创建一个Destination对象，有queue和topic两种形式，参数是消息队列的名称
		Queue queue = session.createQueue("test-queue");
		// 使用Session对象创建一个Producer对象，指定一个destination
		MessageProducer messageProducer = session.createProducer(queue);
		// 创建一个TestMessage对象
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello, ActiveMQ!");
		// 第二种方法
		// TextMessage textMessage = session.createTextMessage("hello ActiveMQ Queue");
		// 发送消息
		messageProducer.send(textMessage);
		// 关闭资源
		messageProducer.close();
		session.close();
		connection.close();
	}

	// Queue Consumer
	@Test
	public void testQueueConsumer() throws Exception {
		// 创建连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session创建一个Destination对象，有queue和topic两种形式，Destination对象应该和消息的发送端一致
		Queue queue = session.createQueue("test-queue");
		// 使用Session创建一个Consumer对象
		MessageConsumer messageConsumer = session.createConsumer(queue);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		messageConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				// 取消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						// 打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// 系统等待接收消息，等待键盘输入，敲回车结束等待
		System.in.read();
		// 关闭资源
		messageConsumer.close();
		session.close();
		connection.close();
	}

	// Topic Producer
	@Test
	public void testTopicProducer() throws Exception {
		// 创建一个连接工厂ConnectionFactory对象，指定ActiveMQ服务的ip和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 使用ConnectionFactory创建一个连接对象Connection
		Connection connection = connectionFactory.createConnection();
		// 开启连接，调用Connection对象的start方法
		connection.start();
		// 使用Connection对象创建一个Session对象
		// 第一个参数：是否开启事务（效率问题一般不使用，会产生临时的数据不一致问题，但是保证数据的最终一致性）；
		// 第二个参数：如果第一个参数是true，第二个参数自动忽略；如果第一个参数是false，第二个参数为消息的应答模式，一般自动应答就可以
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session对象创建一个Destination对象，有queue和topic两种形式，参数是消息队列的名称
		Topic topic = session.createTopic("test-topic");
		// 使用Session对象创建一个Producer对象，指定一个destination
		MessageProducer messageProducer = session.createProducer(topic);
		// 创建一个TestMessage对象
		TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello, ActiveMQ Topic!");
		// 第二种方法
		// TextMessage textMessage = session.createTextMessage("hello ActiveMQ Topic");
		// 发送消息
		messageProducer.send(textMessage);
		// 关闭资源
		messageProducer.close();
		session.close();
		connection.close();
	}

	// Topic Consumer
	@Test
	public void testTopicConsumer() throws Exception {
		// 创建连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 使用连接工厂对象创建一个连接
		Connection connection = connectionFactory.createConnection();
		// 开启连接
		connection.start();
		// 使用连接对象创建一个Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session创建一个Destination对象，有queue和topic两种形式，Destination对象应该和消息的发送端一致
		Topic topic = session.createTopic("test-topic");
		// 使用Session创建一个Consumer对象
		MessageConsumer messageConsumer = session.createConsumer(topic);
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		messageConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				// 取消息的内容
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						// 打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// 系统等待接收消息，等待键盘输入，敲回车结束等待
		System.out.println("topic消费者1");
		System.in.read();
		// 关闭资源
		messageConsumer.close();
		session.close();
		connection.close();
	}

	// 持久化topic消息，生产者
	@Test
	public void testTopicPersistenceProducer() throws JMSException {
		// 创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
		// tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 设置使用异步发送消息，这样可以显著提高发送性能
		connectionFactory.setUseAsyncSend(true);
		// 使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();
		// 对于每一个生产者来说，其clientID的值必须唯一
		connection.setClientID("producer1");
		// 开启连接。调用Connection对象的start方法
		connection.start();
		// 使用Connection对象创建一个Session对象
		// 第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
		// 做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
		// 下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
		// 发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
		// 也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
		// 第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用topic
		// 参数就是消息队列的名称
		Topic topic = session.createTopic("test-topic");
		// 使用Session对象创建一个Producer对象
		MessageProducer messageProducer = session.createProducer(topic);
		// DeliveryMode设置为PERSISTENT（持久化）
		messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
		// 创建一个TextMessage对象
		// 有两种方式，第一种方式：
		// TextMessage textMessage = new ActiveMQTextMessage();
		// textMessage.setText("hello,activemq!!!");
		// 第二种方式：
		TextMessage textMessage = session.createTextMessage("持久化的activemq topic1");
		// 发送消息
		messageProducer.send(textMessage);
		// 关闭资源
		messageProducer.close();
		session.close();
		connection.close();
	}

	// 持久化topic消息，消费者
	@Test
	public void testTopicPersistenceConsumer() throws Exception {
		// 创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
		// tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.156.60:61616");
		// 设置使用异步接收消息，这样可以显著提高接收性能
		connectionFactory.setUseAsyncSend(true);
		// 使用ConnectionFactory创建一个连接Connection对象
		Connection connection = connectionFactory.createConnection();
		// 设置消费者ID，每个消费者的clientID都不能相同！
		connection.setClientID("consumer1");
		// 开启连接。调用Connection对象的start方法
		connection.start();
		// 使用Connection对象创建一个Session对象
		// 第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
		// 做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
		// 下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
		// 发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
		// 也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
		// 第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
		// 参数就是消息队列的名称
		Topic topic = session.createTopic("test-topic");
		// 使用Session对象创建一个Consumer对象
		MessageConsumer messageProducer = session.createDurableSubscriber(topic, "consumer1");
		// 向Consumer对象中设置一个MessageListener对象，用来接收消息
		messageProducer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						System.out.println(text);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			}
		});
		// 程序等待接收用户结束操作
		// 程序自己并不知道什么时候有消息，也不知道什么时候不再发送消息了，这就需要手动干预，
		// 当我们想停止接收消息时，可以在控制台输入任意键，然后回车即可结束接收操作（也可以直接按回车）。
		System.out.println("topic持久化消费者1");
		System.in.read();
		// 关闭资源
		messageProducer.close();
		session.close();
		connection.close();
	}
}
