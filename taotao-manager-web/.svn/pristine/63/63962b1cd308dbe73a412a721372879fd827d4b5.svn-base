package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	// 当返回的数据不是html标签页面，而是其他格式的（json，xml）数据时候需要加上
	@ResponseBody
	// 如果出现406错误，很有可能是因为没有加入json相关的jar包
	public TbItem getItemById(@PathVariable Long itemId) {
		return itemService.getItemById(itemId);
	}

	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		return itemService.getItemList(page, rows);
	}

	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc) {
		try {
			TaotaoResult result = itemService.addItem(item, desc);
			return result;
		} catch (Exception e) {
			// 在Controller层捕获从实现类中抛出的异常，避免异常抛到前台页面
			e.printStackTrace();
			return TaotaoResult.build(500, "添加商品失败");
		}
	}
}
