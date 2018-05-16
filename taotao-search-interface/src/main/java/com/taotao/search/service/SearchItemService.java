package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {
	// 商品数据导入索引库
	TaotaoResult importItemToIndex();
}
