package com.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.utils.FastDFSClient;

public class TestFastDFS {
	@Test
	public void uploadFile() throws Exception {
		// 添加jar包
		// 创建配置文件，配置Tracker服务器地址
		// 加载配置文件
		ClientGlobal.init("E:/eclipse-workspace/taotao-manager-web/src/main/resources/resource/client.conf");
		// 创建TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		// 使用TrackerClient获得TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		// 创建一个StorageServer的引用，null就可以
		StorageServer storageServer = null;
		// 创建一个StorageClient对象，TrackerServer和StorageServer两个参数
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 使用StorageClient对象上传文件
		String[] strings = storageClient.upload_file("C:/Users/Chris/Desktop/a.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}

	@Test
	public void testFastDFSClient() throws Exception {
		String confPath = "E:/eclipse-workspace/taotao-manager-web/src/main/resources/resource/client.conf";
		FastDFSClient fastDFSClient = new FastDFSClient(confPath);
		String string = fastDFSClient.uploadFile("C:/Users/Chris/Desktop/b.jpg");
		System.out.println(string);
	}
}
