package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

// 这个pojo类在服务端要用，在表现层也要用（用到网络传输，所以实现Serializable接口），所以放到common中，就都能用到这pojo了
// EasyUI中datagrid控件要求格式为：{total:"2",rows[{"id":"1","name":"张三"},{"id":"2","name":"李四"}]}
// 必须包含total和rows两个属性，而且rows中的key要和item-list.jsp中的field对应，数据才会自动放到对应列下面
public class EasyUIDataGridResult implements Serializable {
	private long total;
	private List rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
}
