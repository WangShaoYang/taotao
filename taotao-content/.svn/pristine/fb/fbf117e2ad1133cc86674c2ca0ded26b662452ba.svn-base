package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);

	// 添加内容分类，注意参数名称要与content-category.jsp页面指定的参数名称一致
	TaotaoResult addContentCategory(long parentId, String name);

	// 修改内容分类，注意参数名称要与content-category.jsp页面指定的参数名称一致
	TaotaoResult updateContentCategory(long id, String name);

	// 删除内容分类，注意参数名称要与content-category.jsp页面指定的参数名称一致
	TaotaoResult deleteContentCategory(long id);
}
