package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		criteria.andStatusEqualTo(1);
		// 执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<EasyUITreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}
		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		// 创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		// 补全对象属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// 状态。可选值：1（正常），2（删除）
		contentCategory.setStatus(1);
		// 排序，默认为1
		contentCategory.setSortOrder(1);
		// 新添加的节点一定不是父节点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 插入到数据库
		contentCategoryMapper.insert(contentCategory);
		// 判断父节点的状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 如果父节点为叶子节点，改为父节点
			parent.setIsParent(true);
			// 更新父节点
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		// 把结果包装一下并返回
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		// 通过id查询节点对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 判断新name与原来的值是否相同，相同则不更新
		if (name != null && name.equals(contentCategory.getName())) {
			return TaotaoResult.ok();
		}
		contentCategory.setName(name);
		// 设置更新时间
		contentCategory.setUpdated(new Date());
		// 更新数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContentCategory(long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 状态，可选值1：正常；2：删除
		contentCategory.setStatus(2);
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 需要判断删除的节点是否是父节点，如果是父节点，就级联删除
		if (contentCategory.getIsParent()) {
			deleteNode(contentCategory.getId());
		}
		// 判断被删除节点的父节点是否还有子节点，如果没有子节点了，需要修改父节点isParent属性为false
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
		// 判断父节点是否有子节点是判断这个父节点下所有子节点的状态，如果子节点的状态都是2说明没有子节点了
		boolean flag = false;
		for (TbContentCategory tbContentCategory : list) {
			if (tbContentCategory.getStatus() == 1) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok();
	}

	// 通过父节点id查询所有子节点
	private List<TbContentCategory> getContentCategoryListByParentId(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		return list;
	}

	// 递归删除节点
	private void deleteNode(long parentId) {
		List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
		for (TbContentCategory contentCategory : list) {
			// 改状态为2就代表删除了
			contentCategory.setStatus(2);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
			if (contentCategory.getIsParent()) {
				deleteNode(contentCategory.getId());
			}
		}
	}
}
