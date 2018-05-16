package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

public class LoginInterceptor implements HandlerInterceptor {
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@Value("${SSO_URL}")
	private String SSO_URL;

	@Autowired
	private UserService userService;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		// 在modelAndView返回之后执行，异常处理
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后，modelAndView返回之前执行
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 执行handler之前执行的方法
		// 从cookie中去token信息
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		// 如果取不到token，跳转到sso登录页面，需要把当前请求的url作为参数传递给sso，sso登录成功后跳转回请求的页面
		if (StringUtils.isBlank(token)) {
			// 取当前页面url
			String requestURL = request.getRequestURL().toString();
			// 跳转到登录页面
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
			// 拦截
			return false;
		}
		// 如果取到token，调用sso访问，判断用户是否登录
		TaotaoResult taotaoResult = userService.getUserByToken(token);
		// 如果用户是未登录状态，即没取到用户信息，跳转到sso登录页面
		if (taotaoResult.getStatus() != 200) {
			// 取当前页面url
			String requestURL = request.getRequestURL().toString();
			// 跳转到登录页面
			response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
			// 拦截
			return false;
		}
		// 把用户信息放到request中，因为OrderCartController中要取用户id
		TbUser tbUser = (TbUser) taotaoResult.getData();
		request.setAttribute("user", tbUser);
		// 如果取到用户信息，放行
		return true;
		// return false：拦截；return true：放行
	}

}
