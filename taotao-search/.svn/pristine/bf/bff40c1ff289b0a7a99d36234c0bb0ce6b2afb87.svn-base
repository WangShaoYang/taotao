package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	@Test
	public void testSolrCloudAddDocument() throws Exception {
		// 创建一个CloudSolrServer对象，构造方法中指定ZooKeeper的地址列表
		CloudSolrServer cloudSolrServer = new CloudSolrServer(
				"192.168.156.60:2181,192.168.156.60:2182,192.168.156.60:2183");
		// 需要设置more的Collection
		cloudSolrServer.setDefaultCollection("collection2");
		// 创建一个文档对象
		SolrInputDocument solrInputDoucment = new SolrInputDocument();
		// 向文档对象中添加域
		solrInputDoucment.addField("id", "test001");
		solrInputDoucment.addField("item_title", "测试商品名称");
		solrInputDoucment.addField("item_price", 100);
		// 把文档写入索引库
		cloudSolrServer.add(solrInputDoucment);
		// 提交
		cloudSolrServer.commit();
	}
}
