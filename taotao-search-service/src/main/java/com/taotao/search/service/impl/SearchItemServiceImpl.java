package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public TaotaoResult importItemToIndex() {
		// 查询所有商品数据
		try {
			List<SearchItem> itemList = searchItemMapper.getItemList();
			// 遍历商品数据添加到索引库
			for (SearchItem searchItem : itemList) {
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
			}
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "数据导入失败");
		}
		// 提交，返回添加成功
		return TaotaoResult.ok();
	}
}
