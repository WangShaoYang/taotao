package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	// 假如请求为manager.taotao.com/item-add，那么就响应item-add.jsp
	// 这里的请求是指点击左侧菜单所发出的请求url，不是手动输入到地址栏的
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
