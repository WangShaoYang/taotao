package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {
	@Test
	public void testFreeMarker() throws Exception {
		// 创建一个模板文件，放在WEB-INF下的ftl文件夹中
		// 创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 设置模板所在的路径
		configuration.setDirectoryForTemplateLoading(
				new File("E:\\eclipse-workspace\\taotao-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		// 设置模板的字符集，一般为utf-8
		configuration.setDefaultEncoding("utf-8");
		// 使用Configuration对象加载一个模板文件，指定模板文件的文件名
		// Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 创建一个数据集，可以是pojo也可以是map，推荐用map
		Map map = new HashMap();
		map.put("hello", "hello,I am FreeMarker!");
		Student student = new Student(1, "wangshaoyang", 22, "liaocheng");
		map.put("student", student);
		List<Student> studentList = new ArrayList<Student>();
		studentList.add(new Student(1, "name1", 1, "address1"));
		studentList.add(new Student(2, "name2", 2, "address2"));
		studentList.add(new Student(3, "name3", 3, "address3"));
		studentList.add(new Student(4, "name4", 4, "address4"));
		studentList.add(new Student(5, "name5", 5, "address5"));
		map.put("studentList", studentList);
		map.put("date", new Date());
		// 创建一个Writer对象，指定输出文件的路径及文件名
		Writer writer = new FileWriter(new File("C:\\Users\\Chris\\Desktop\\student.html"));
		// 使用模板对象的process方法输出文件
		template.process(map, writer);
		// 关闭流
		writer.close();
	}
}
