package com.lwl.boot.job.simple;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;


public class ApiJobSimple {

	
	public static void main(String[] args) {
		new JobScheduler(registryCenter(),configuration()).init();
	}
	
	
	private static CoordinatorRegistryCenter registryCenter() {
		//配置zookeeper
		//注册中心( CoordinatorRegistryCenter )：用于协调分布式服务
		//连接Zookeeper服务器的列表. 多个地址用逗号分隔. 如: host1:2181,host2:2181
		String serverLists = "localhost:2181";
		//如果你有多个不同 Elastic-Job集群 时，使用相同 Zookeeper，可以配置不同的 namespace 进行隔离
		String namespace = "elastic-job-demo";
		CoordinatorRegistryCenter registryCenter = 
					new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverLists, namespace));
		registryCenter.init();
		return registryCenter;
	}

	
	private static LiteJobConfiguration configuration() {
		
		 // 定义作业核心配置
		String jobName = "simpleJob"; //作业名称
		String cron = "0/15 * * * * ?";	//定时器表达式,用于控制作业触发时间
		int shardingTotalCount = 3; //作业分片总数,如果一个作业启动超过作业分片总数的节点，只有 shardingTotalCount 会执行作业.换句话说：当服务器数量大于分片总数，那么不是所有服务器都将会执行，而是根据分片总数来定。
		JobCoreConfiguration coreConfiguration = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).build();
		
		
		// 定义SIMPLE类型配置
		//		意为简单实现，未经任何封装的类型。需实现SimpleJob接口。该接口仅提供单一方法用于覆盖，
		//		此方法将定时执行。与Quartz原生接口相似，但提供了弹性扩缩容和分片等功能。
		//任务执行类名称:com.lwl.boot.job.simple.ApiMyElasticJobSimple
		String jobClass = ApiMyElasticJobSimple.class.getCanonicalName();
		SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(coreConfiguration, jobClass); 
		
		
		 // 定义Lite作业根配置
		//作业分片策略:系统提供三种策略和自定义可供选择
		/*
		 * 	1. AverageAllocationJobShardingStrategy：基于平均分配算法的分片策略
		 *  2. OdevitySortByNameJobShardingStrategy：根据作业名的哈希值奇偶数决定IP升降序算法的分片策略
		 *  3. RotateServerByNameJobShardingStrategy:根据作业名的哈希值对服务器列表进行轮转的分片策略
		 * 	4. 实现JobShardingStrategy接口，并且实现sharding的方法，
		 * 		接口方法参数为【作业服务器IP列表和分片策略选项】，【分片策略选项包括作业名称】，【分片总数以及分片序列号和个性化参数对照表】，可以根据需求定制化自己的分片策略。
		 * */
		//默认的分片策略
		String jobShardingStrategyClass = AverageAllocationJobShardingStrategy.class.getCanonicalName();
		
		
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfiguration).jobShardingStrategyClass(jobShardingStrategyClass).build();
		
		
		return simpleJobRootConfig;
	}
	
	
}
