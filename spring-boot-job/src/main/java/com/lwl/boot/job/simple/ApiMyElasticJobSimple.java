package com.lwl.boot.job.simple;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 任务调度
 *
 */
public class ApiMyElasticJobSimple implements SimpleJob {

	@Override
	public void execute(ShardingContext content) {
		
		int key = content.getShardingItem();
		System.out.println();
		System.out.println("----------------------"+key+"-------------------");
		System.out.println();
		
		switch (key) {
		case 0:
			System.out.println("任务调度执行3: "+key);
			break;
		case 1:
			System.out.println("任务调度执行3: "+key);
			break;
		case 2:
			System.out.println("任务调度执行3: "+key);
			break;

		default:
			System.out.println("没有任务调度执行");
			break;
		}
		
	}

	
}
