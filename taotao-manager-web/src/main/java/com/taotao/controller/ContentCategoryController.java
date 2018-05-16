package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
		return contentCategoryService.getContentCategoryList(parentId);
	}

	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(Long parentId, String name) {
		return contentCategoryService.addContentCategory(parentId, name);
	}

	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(Long id, String name) {
		TaotaoResult taotaoResult = contentCategoryService.updateContentCategory(id, name);
		return taotaoResult;
	}

	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public TaotaoResult deleteContentCategory(Long id) {
		TaotaoResult taotaoResult = contentCategoryService.deleteContentCategory(id);
		return taotaoResult;
	}
}
