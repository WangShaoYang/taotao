package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

	TaotaoResult addItem(TbItem item, String desc) throws Exception;

	TbItemDesc getItemDescById(long itemId);
}
