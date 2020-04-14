package com.test.job;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJob {
	public void run() {
		System.out.println("任务执行了----" + 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
}
