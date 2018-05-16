package com.lwl.boot.job.script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class ApiJobScript {

	public static void main(String[] args) throws IOException {
		new JobScheduler(registryCenter(),configuration()).init();
	}

	
	private static CoordinatorRegistryCenter registryCenter() {
		//配置zookeeper
		CoordinatorRegistryCenter registryCenter = 
					new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "elastic-job-demo"));
		registryCenter.init();
		return registryCenter;
	}

	
	private static LiteJobConfiguration configuration() throws IOException {
		
		 // 定义作业核心配置
		JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder("scriptElasticJob", "0/5 * * * * ?", 3).build();
        ScriptJobConfiguration scriptJobConfig = new ScriptJobConfiguration(coreConfig, buildScriptCommandLine());
		
		LiteJobConfiguration config = LiteJobConfiguration.newBuilder(scriptJobConfig).build();
		
		
		return config;
	}


	private static String buildScriptCommandLine() throws IOException {
	   if (System.getProperties().getProperty("os.name").contains("Windows")) {
            return Paths.get(ApiJobScript.class.getResource("/script/demo.bat").getPath().substring(1)).toString();
        }
        Path result = Paths.get(ApiJobScript.class.getResource("/script/demo.sh").getPath());
        Files.setPosixFilePermissions(result, PosixFilePermissions.fromString("rwxr-xr-x"));
        return result.toString();
	}
	
}
