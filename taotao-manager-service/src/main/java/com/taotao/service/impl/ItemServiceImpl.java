package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;
	// @Autowired是默认按照类型装配（属于Spring），默认情况下必须要求依赖对象必须存在
	// @Resource是默认按照名称装配（属于JDK1.6），名称通过name属性指定，如果没有指定，当注解写在字段上时，默认取字段名
	// 当注解写在setter方法上，默认取属性名进行装配，当找不到与名称匹配的bean的时候，才进行类型装配
	@Resource(name = "itemAddTopic")
	private Destination destination;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;

	@Value("${TIME_EXPIRE}")
	private Integer TIME_EXPIRE;

	@Override
	public TbItem getItemById(long itemId) {
		// 查询数据库之前先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有查询到数据
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		try {
			// 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
			// 设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", TIME_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItem;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		List<TbItem> list = itemMapper.selectByExample(new TbItemExample());
		// 取查询结果，把list封装一下
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) throws Exception {
		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全item属性
		item.setId(itemId);
		// 商品状态：1-正常；2-下架；3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建一个商品描述表对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全pojo属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		// 向ActiveMQ发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送商品id
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		// 返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// 查询数据库之前先查询缓存
		try {
			String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有查询到数据
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			// 把查询结果添加到缓存
			jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置过期时间，提高缓存利用率
			jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", TIME_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}
}
