package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper tbUserMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${USER_SESSION}")
	private String USER_SESSION;

	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public TaotaoResult checkData(String data, int type) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		if (type == 1) {// 判断用户名是否可用
			criteria.andUsernameEqualTo(data);
		} else if (type == 2) {// 判断手机号是否可用
			criteria.andPhoneEqualTo(data);
		} else if (type == 3) {// 判断Email是否可用
			criteria.andEmailEqualTo(data);
		} else {
			return TaotaoResult.build(400, "请求参数中有非法数据！");
		}
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		// 返回false说明查到了数据，所以不可用，返回true说明没有查到数据，所以可用
		return (list != null && list.size() > 0) ? TaotaoResult.ok(false) : TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser tbUser) {
		// 校验信息
		if (StringUtils.isBlank(tbUser.getUsername())) {
			return TaotaoResult.build(400, "用户名不能为空!");
		}
		if (!(boolean) checkData(tbUser.getUsername(), 1).getData()) {
			return TaotaoResult.build(400, "用户名重复！");
		}
		if (StringUtils.isBlank(tbUser.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空!");
		}
		if (StringUtils.isNotBlank(tbUser.getPhone()) && !(boolean) checkData(tbUser.getPhone(), 2).getData()) {
			return TaotaoResult.build(400, "电话号码重复！");
		}
		if (StringUtils.isNotBlank(tbUser.getEmail()) && !(boolean) checkData(tbUser.getEmail(), 3).getData()) {
			return TaotaoResult.build(400, "电子邮箱重复！");
		}
		// 补全属性
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		// 密码MD5加密
		String MD5Password = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(MD5Password);
		// 插入数据
		tbUserMapper.insert(tbUser);
		// 返回成功
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		// 判断用户名和密码是否正确
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名不存在！");
		}
		TbUser tbUser = list.get(0);
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码不正确！");
		}
		// 校验成功，生成token，使用uuid
		String token = UUID.randomUUID().toString();
		// 清空密码
		tbUser.setPassword("");
		// 把用户信息保存到redis中，key是token，value是用户信息
		jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(tbUser));
		// 设置过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 返回登陆成功，把token返回
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get(USER_SESSION + ":" + token);
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户未登录");
		}
		// 重置过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 把json转换成TbUser对象
		// 如果不转成对象的话，那么传递的参数是一个字符串，在页面显示的时候，引号都被转义了，不符合要求
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		return TaotaoResult.ok(tbUser);
	}

	@Override
	public TaotaoResult logout(String token) {
		jedisClient.expire(USER_SESSION + ":" + token, 0);
		return TaotaoResult.ok();
	}
}
