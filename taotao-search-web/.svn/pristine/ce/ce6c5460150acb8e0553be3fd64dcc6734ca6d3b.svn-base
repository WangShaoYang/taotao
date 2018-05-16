package com.taotao.search.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class StackTrace {
	// 邮件发送的是字符串，而产生异常的时候是堆栈信息，需要将堆栈信息转化成字符串
	public static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
}
