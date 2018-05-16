package com.taotao.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {
	@Test
	public void testPageHelper() throws Exception {
		// 1.在mybatis的配置文件中加入分页插件
		// 2.在执行查询之前配置分页条件，使用PageHelper的静态方法加入分页条件
		PageHelper.startPage(1, 10);
		// 3.执行查询，现在的查询已经被加入了分页信息
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dao.xml");
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		// 生成Example对象
		TbItemExample example = new TbItemExample();
		// 如果需要使用条件查询，先创建Criteria，然后用它来拼接查询条件，这里不按条件查询。
		// Criteria criteria = example.createCriteria();
		// criteria..andIdEqualTo(1L);
		List<TbItem> list = itemMapper.selectByExample(example);
		// 4.使用PageInfo对象来取分页信息：总记录数和查询记录
		// PageHelper的Page类继承了ArrayList，Page里面有分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println("总记录数：" + pageInfo.getTotal());
		System.out.println("总页数：" + pageInfo.getPages());
		System.out.println("返回的记录数：" + list.size());
	}
}
