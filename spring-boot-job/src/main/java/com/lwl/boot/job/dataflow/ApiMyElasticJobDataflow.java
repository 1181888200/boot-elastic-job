package com.lwl.boot.job.dataflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

/**
 * Dataflow类型用于处理数据流，
 * 		需实现DataflowJob接口。该接口提供2个方法可供覆盖，分别用于抓取(fetchData)和处理(processData)数据。
 *
 */
public class ApiMyElasticJobDataflow implements DataflowJob<Foo>{

	private FooRepository  fooRepository = FooRepositoryFactory.repository();
	
	@Override
	public List<Foo> fetchData(ShardingContext context) {
		System.out.println("-------------------------------------fetchData: "+context.getShardingParameter()+"---------------------------------------------");
		
		List<Foo> result =  fooRepository.findTodoData(context.getShardingParameter(), 10);
		System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s | count: %d",
				context.getShardingItem(), 
				new SimpleDateFormat("HH:mm:ss").format(new Date()), 
				Thread.currentThread().getId(), 
				"DATAFLOW FETCH",
				CollectionUtils.isEmpty(result)?0:result.size()));
	        return result;
		
	}

	@Override
	public void processData(ShardingContext shardingContext, List<Foo> data) {
		System.out.println("-------------------------------------processData: "+shardingContext.getShardingParameter()+"---------------------------------------------");
		System.out.println(String.format("Item: %s | Time: %s | Thread: %s | %s | count: %d",
                shardingContext.getShardingItem(), 
                new SimpleDateFormat("HH:mm:ss").format(new Date()), 
                Thread.currentThread().getId(), "DATAFLOW PROCESS",
                CollectionUtils.isEmpty(data)?0:data.size()));
        for (Foo each : data) {
            fooRepository.setCompleted(each.getId());
        }
	}

}
