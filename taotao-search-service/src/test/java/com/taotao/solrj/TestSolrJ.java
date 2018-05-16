package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	@Test
	public void testAddDocument() throws Exception {
		// 创建一个So利润Server对象，创建一个HttpSolrServer对象，需要指定solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.156.60:8080/solr/collection1");
		// 创建一个文档对象SolrInputDocument
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		// 向文档中添加域，必须有id域，业务域的名称必须在schema.xml中定义
		solrInputDocument.addField("id", "test002");
		solrInputDocument.addField("item_title", "测试商品2");
		solrInputDocument.addField("item_price", 1000);
		// 把文档对象写入到索引库并提交
		solrServer.add(solrInputDocument);
		solrServer.commit();
	}

	@Test
	public void testDeleteDocumentById() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.156.60:8080/solr/collection1");
		solrServer.deleteById("test001");
		solrServer.commit();
	}

	@Test
	public void testDeleteDocumentByQuery() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.156.60:8080/solr/collection1");
		solrServer.deleteByQuery("id:test003");
		solrServer.commit();
	}

	@Test
	public void testSearchDocument() throws Exception {
		// 创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.156.60:8080/solr/collection1");
		// 创建SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询条件，过滤条件，分页条件，排序条件，高亮
		solrQuery.set("q", "手机");
		// 分页条件
		solrQuery.set("start", 30);
		solrQuery.set("rows", 20);
		// 设置默认搜索域
		solrQuery.set("df", "item_keywords");
		// 设置高亮
		solrQuery.setHighlight(true);
		// 设置高亮显示的域
		solrQuery.addHighlightField("item_title");
		// 设置高亮的前后缀
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		// 执行查询，得到一个Response对象
		QueryResponse queryResponse = solrServer.query(solrQuery);
		// 取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		// 取总记录数
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			// 取高亮显示
			Map<String, Map<String, List<String>>> highlisghting = queryResponse.getHighlighting();
			List<String> list = highlisghting.get(solrDocument.get("id")).get("item_title");
			String itemTitle = "";
			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println("===================================");
		}
	}
}
