package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;

@Controller
public class PictureController {
	// 首先要在springmvc.xml文件中加载resource.properties文件才能用@Value注解取到
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping("/pic/upload")
	@ResponseBody
	// 因为KindEditor在火狐中不兼容，以Map作为返回值会出现上传失败的情况，所以将返回值改成String
	public String picUpload(MultipartFile uploadFile) {
		Map result = new HashMap();
		try {
			// 接受上传的文件
			// 取扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			// 上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url = IMAGE_SERVER_URL + url;
			// 响应上传图片的url
			result.put("error", 0);
			result.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败");
		}
		// 使用utils将对象转换成json串
		// 还有一种方式是在taotao-parent中定义fastjson依赖，在taotao-common中加入fastjson依赖
		// 之后使用maven install打包两个工程，下面就要改成JSON.toJSONString(result);了
		return JsonUtils.objectToJson(result);
	}
}
