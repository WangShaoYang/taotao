package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			Model model) throws Exception {
		// 人为设置异常
		// int a = 1 / 0;
		// 把查询条件进行转码，解决get乱码问题
		queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
		SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getTotalPage());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		return "search";
	}
}
