package com.taotao.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private ItemService itemService;

	@Autowired
	private FreeMarkerConfig freeMarkerConfig;

	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {
		try {
			// 从消息中取出商品id
			TextMessage textMessage = (TextMessage) message;
			Long itemId = Long.parseLong(textMessage.getText());
			// 等待事务提交
			Thread.sleep(1000);
			// 根据商品id查询商品信息及商品描述
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			// 使用FreeMarker生成静态页面
			Configuration configuration = freeMarkerConfig.getConfiguration();
			// 创建模板
			// 加载模板对象
			Template template = configuration.getTemplate("item.ftl");
			// 准备模板需要的数据
			Map map = new HashMap();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			// 指定输出目录及文件名
			Writer writer = new FileWriter(HTML_OUT_PATH + itemId + ".html");
			// 生成静态页面
			template.process(map, writer);
			// 关闭流
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
