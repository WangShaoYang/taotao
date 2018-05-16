package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class CartController {
	@Value("${CART_KEY}")
	private String CART_KEY;

	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 取购物车商品列表
		List<TbItem> cartItemList = getCartItemList(request);
		// 判断商品在购物车中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartItemList) {
			// tbItem.getId()和itemId都是Long类型，用==直接比较，比较的是地址，至少把其中一个转换成值的形式
			if (tbItem.getId() == itemId.longValue()) {
				// 如果存在，那么数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				break;
			}
		}
		// 如果不存在，添加一个新的商品
		if (!flag) {
			// 需要调用服务获取商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			// 设置商品的购买数量
			tbItem.setNum(num);
			// 取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				tbItem.setImage(image.split(",")[0]);
			}
			// 商品添加到购物车
			cartItemList.add(tbItem);
		}
		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		// 返回添加成功页面
		return "cartSuccess";
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

	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request) {
		// 从cookie中取购物车列表
		List<TbItem> cartItemList = getCartItemList(request);
		// 把购物车列表传递给jsp
		request.setAttribute("cartList", cartItemList);
		// 返回逻辑视图
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 从cookie中取出购物车列表
		List<TbItem> cartItemList = getCartItemList(request);
		// 查询到对应商品
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 更新商品数量
				tbItem.setNum(num);
				break;
			}
		}
		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		// 返回成功
		return TaotaoResult.ok();
	}

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 从cookie中取出购物车列表
		List<TbItem> cartItemList = getCartItemList(request);
		// 查询到对应商品
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 删除商品
				cartItemList.remove(tbItem);
				break;
			}
		}
		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		// 重定向到购物车列表页面
		return "redirect:/cart/cart.html";
	}
}
