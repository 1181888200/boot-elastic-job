package com.lwl.boot.config;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.lwl.boot.job.dataflow.ApiMyElasticJobDataflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class DataflowJobConfig {
    
    @Resource
    private ZookeeperRegistryCenter regCenter;
    
    
    @Bean
    public DataflowJob dataflowJob() {
        return new ApiMyElasticJobDataflow(); 
    }
    
    @Bean(initMethod = "init")
    public JobScheduler dataflowJobScheduler(final DataflowJob dataflowJob, @Value("${dataflowJob.cron}") final String cron, @Value("${dataflowJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${dataflowJob.shardingItemParameters}") final String shardingItemParameters) {
        return new SpringJobScheduler(dataflowJob, regCenter, getLiteJobConfiguration(dataflowJob.getClass(), cron, shardingTotalCount, shardingItemParameters));
    }
    
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends DataflowJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(), jobClass.getCanonicalName(), true)).overwrite(true).build();
    }
}