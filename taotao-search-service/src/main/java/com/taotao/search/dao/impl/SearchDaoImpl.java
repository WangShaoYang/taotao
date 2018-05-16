package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;

@Repository
public class SearchDaoImpl implements SearchDao {
	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery solrQuery) throws Exception {
		// 根据solrQuery对象进行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		// 取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 取查询结果总记录数
		long numFound = solrDocumentList.getNumFound();
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(numFound);
		List<SearchItem> itemList = new ArrayList<SearchItem>();
		// 把查询结果封装到SearchItem对象中
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem item = new SearchItem();
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setId((String) solrDocument.get("id"));
			// 如果上传图片的时候，一次性上传了多张，在数据库中image字段保存的是多个图片的url用逗号分隔开的形式
			// 所以在前台界面显示的时候，显示的是一非法的url，因此不能正常显示，如果只上传了一张图片，那么会正常显示
			// 解决方法，把image的数据做处理，只取某部分url，那么就可以正常显示图片了
			String image = (String) solrDocument.get("item_image");
			if (StringUtils.isNotBlank(image)) {
				image = image.split(",")[0];
			}
			item.setImage(image);
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			// 取高亮显示
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			itemList.add(item);
		}
		// 把结果添加到SearchResult中
		searchResult.setItemList(itemList);
		// 返回
		return searchResult;
	}
}
