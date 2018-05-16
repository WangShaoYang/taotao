package com.taotao.item.controller;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class HtmlGenController {
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@RequestMapping("/genhtml")
	@ResponseBody
	public String getHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		Template template = configuration.getTemplate("hello");
		Map map = new HashMap();
		map.put("hello", "Spring and FreeMarker!");
		Writer writer = new FileWriter("C:\\Users\\Chris\\Desktop\\hello.html");
		template.process(map, writer);
		writer.close();
		return "OK";
	}
}
