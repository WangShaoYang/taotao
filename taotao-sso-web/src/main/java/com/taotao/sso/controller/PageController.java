package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}

	//
	@RequestMapping("/page/login")
	public String showLogin(String url, Model model) {
		// 跳转到login的时候取url后面跟的参数
		// 在login.jsp的62行，可以看到jsp需要一个redirectUrl参数
		// 如果参数为""，则跳转到www.taotao.com，否则跳转到自定义的url
		model.addAttribute("redirect", url);
		return "login";
	}
}
