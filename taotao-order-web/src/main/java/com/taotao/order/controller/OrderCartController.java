package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.OrderInfo;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;

@Controller
public class OrderCartController {
	@Value("${CART_KEY}")
	private String CART_KEY;

	@Autowired
	private OrderService orderService;

	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		// 用户必须是登录状态
		// 取用户id
		TbUser tbUser = (TbUser) request.getAttribute("user");
		System.out.println(tbUser.getUsername());
		// 根据用户信息获取收货地址列表，这里使用静态数据模拟
		// 把收货地址列表传递给页面
		// 从cookie中取购物车商品列表展示到页面
		List<TbItem> cartItemList = getCartItemList(request);
		request.setAttribute("cartList", cartItemList);
		// 返回逻辑视图
		return "order-cart";
	}

	private List<TbItem> getCartItemList(HttpServletRequest request) {
		// 从cookie中取出购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if (StringUtils.isBlank(json)) {
			// 如果cookie中没有购物车的信息，返回一个空的列表
			return new ArrayList();
		}
		return JsonUtils.jsonToList(json, TbItem.class);
	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo, Model model) {
		// 生成订单
		TaotaoResult taotaoResult = orderService.createOrder(orderInfo);
		// 向页面传递数据，参考success.jsp
		model.addAttribute("orderId", taotaoResult.getData().toString());
		model.addAttribute("payment", orderInfo.getPayment());
		// 预计送达时间是当前时间+3天
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		return "success";
	}

}
