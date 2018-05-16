package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	// 这里使用了@ResponseBody返回json数据，在访问的时候，如果url是*.html的话，会出现406错误
	// 406错误分析：
	// 1.没有添加jackson的包，所以报406错误；
	// 2.浏览器认为url是*.html，应该返回一个html格式的内容，可是返回了一个json数据，浏览器无法解析，所以报406错误
	public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
		return userService.checkData(param, type);
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser tbUser) {
		return userService.register(tbUser);
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		TaotaoResult taotaoResult = userService.login(username, password);
		// 登录成功后写入cookie
		if (taotaoResult.getStatus() == 200) {
			// 把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, taotaoResult.getData().toString());
		}
		return taotaoResult;
	}

	// @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ResponseBody
	// 因为请求的url是"http://sso.taotao.com/user/token/token_value?callback=callbackFunctionName"，所以下面的形参是callback正好和?后面的callback对应，所以可以去到callback的值
	// public String getUserByToken(@PathVariable String token, String callback) {
	// TaotaoResult taotaoResult = userService.getUserByToken(token);
	// // 判断是否为jsonp请求
	// if (StringUtils.isNotBlank(callback)) {
	// return callback + "(" + JsonUtils.objectToJson(taotaoResult) + ");";
	// }
	// return JsonUtils.objectToJson(taotaoResult);
	// }

	// jsonp的第二种使用方法，Spring4.1版本以上使用
	@RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
	@ResponseBody
	// 因为请求的url是"http://sso.taotao.com/user/token/token_value?callback=callbackFunctionName"，所以下面的形参是callback正好和?后面的callback对应，所以可以去到callback的值
	public Object getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult taotaoResult = userService.getUserByToken(token);
		// 判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
			// 设置回调方法
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return taotaoResult;
	}

	@RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		return userService.logout(token);
	}
}
