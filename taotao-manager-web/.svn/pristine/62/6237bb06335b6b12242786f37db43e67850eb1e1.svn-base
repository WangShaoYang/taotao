package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;

	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		return contentService.getContentList(categoryId, page, rows);
	}

	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent content) {
		return contentService.addContent(content);
	}

	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent content) {
		TaotaoResult result = contentService.updateContent(content);
		return result;
	}

	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult deleteContent(String ids) {
		TaotaoResult result = contentService.deleteContent(ids);
		return result;
	}

	@RequestMapping("/content/getContent")
	@ResponseBody
	public TaotaoResult getContent(long id) {
		return contentService.getCoutent(id);
	}
}
