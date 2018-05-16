package com.lwl.boot.job.dataflow;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * 可通过DataflowJobConfiguration配置是否流式处理。

		流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去； 
		非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。

		如果采用流式作业处理方式，建议processData处理数据后更新其状态，避免fetchData再次抓取到，从而使得作业永不停止。 
		流式数据处理参照TbSchedule设计，适用于不间歇的数据处理。
 *
 */

public class ApiJobDataflow {

	
	public static void main(String[] args) {
		
		new JobScheduler(registryCenter(),configuration()).init();
	}
	
	
	private static CoordinatorRegistryCenter registryCenter() {
		//配置zookeeper
		CoordinatorRegistryCenter registryCenter = 
					new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "elastic-job-demo"));
		registryCenter.init();
		return registryCenter;
	}
	
	
	private static LiteJobConfiguration configuration() {
		// 定义作业核心配置
		String shardingItemParameters = "0=Beijing,1=Shanghai,2=Guangzhou";
	    JobCoreConfiguration dataflowCoreConfig 
	    	= JobCoreConfiguration.newBuilder("dataflowJob", "0/20 * * * * ?", 3).shardingItemParameters(shardingItemParameters).build();
	    
	    // 定义DATAFLOW类型配置
	    DataflowJobConfiguration dataflowJobConfig 
	    	= new DataflowJobConfiguration(dataflowCoreConfig, ApiMyElasticJobDataflow.class.getCanonicalName(), true);
	    
	    // 定义Lite作业根配置
	    String jobShardingStrategyClass = null;
	    LiteJobConfiguration dataflowJobRootConfig 
	    	= LiteJobConfiguration.newBuilder(dataflowJobConfig).jobShardingStrategyClass(jobShardingStrategyClass).build();
	    
	    return dataflowJobRootConfig;
	}
	
	
}
