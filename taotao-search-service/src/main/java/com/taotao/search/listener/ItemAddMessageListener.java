package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

// 监听商品添加事件，同步索引库
public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		try {
			// 从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			// 根据id查询数据，举出来商品信息
			// 在com.taotao.service.impl.ItemServiceImpl.addItem(TbItem,String)方法中，有可能JmsTemplate已经把消息发送了，return还没执行
			// 所以此时查询数据库肯定是查不到的，因此这里稍等一会儿，等待事务提交
			Thread.sleep(1000);
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			// 创建文档对象
			SolrInputDocument solrInputDocument = new SolrInputDocument();
			// 向文档对象中添加域
			solrInputDocument.addField("id", searchItem.getId());
			solrInputDocument.addField("item_title", searchItem.getTitle());
			solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
			solrInputDocument.addField("item_price", searchItem.getPrice());
			solrInputDocument.addField("item_image", searchItem.getImage());
			solrInputDocument.addField("item_category", searchItem.getCategory_name());
			solrInputDocument.addField("item_desc", searchItem.getItem_desc());
			// 把文档写入索引库
			solrServer.add(solrInputDocument);
			// 提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
