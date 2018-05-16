<html>
<head>
	<title>测试页面</title>
</head>
<body>
	学生信息：<br>
	学号：${student.id}<br>
	姓名：${student.name}<br>
	年龄：${student.age}<br>
	家庭住址：${student.address}<br>
	学生列表：<br>
	<table border="1">
		<tr>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>家庭住址</th>
		</tr>
		<!-- studentList对应map中的key -->
		<#list studentList as student>
			<#if student_index % 2 == 0>
				<tr bgcolor="red">			
			<#else>
				<tr bgcolor="green">
			</#if>
					<td>${student_index}</td>
					<td>${student.id}</td>
					<td>${student.name}</td>
					<td>${student.age}</td>
					<td>${student.address}</td>
				</tr>
		</#list>
	</table>
	<br>
	<!-- 下面的?string("yyyy/MM/dd HH:mm:ss")表示以什么格式展示日期，如果不加会报错 -->
	日期类型的处理：${date?string("yyyy/MM/dd HH:mm:ss")}
	<br>
	<!-- 数据中是没有val的，所以会报错，可以在val后面添加一个感叹号，或者指定默认值，加感叹号和默认值 -->
	null值的处理：${val!"默认值"}
	<br>
	使用if判断null值
	<#if val??>
		val是有值的
	<#else>
		val是null
	</#if>
	<br>
	include标签测试
	<#include "hello.ftl">
</body>
</html>