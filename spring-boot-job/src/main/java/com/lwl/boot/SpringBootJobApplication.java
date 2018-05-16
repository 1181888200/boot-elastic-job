package com.lwl.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@SpringBootApplication
public class SpringBootJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJobApplication.class, args);
	}
	
	/**
	 * 配置zookeeper
	 * @param serverLists
	 * @param namespace
	 * @return
	 */
	@Bean
    public ZookeeperRegistryCenter regCenter(@Value("${zookeeper.serverLists}") final String serverLists,@Value("${zookeeper.namespace}") final String namespace) {
	    	ZookeeperRegistryCenter registryCenter = 
					new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverLists, namespace));
		registryCenter.init();
		return registryCenter;
    }
}
