package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.OrderInfo;

public interface OrderService {
	TaotaoResult createOrder(OrderInfo orderInfo);
}
