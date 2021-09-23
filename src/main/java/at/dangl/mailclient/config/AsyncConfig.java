package at.dangl.mailclient.config;

import lombok.Data;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Data
@ConfigurationProperties(prefix = "async")
public class AsyncConfig implements AsyncConfigurer {

//    @Value("${async.core-pool-size:"+Const.ASYNC_CORE_POOLSIZE+"}")
    private int corePoolSize;

//    @Value("${async.max-pool-size:"+Const.ASYNC_MAX_POOLSIZE+"}")
    private int maxPoolSize;

//    @Value("${async.queue-capacity:"+Const.ASYNC_QUEUE_CAPACITY+"}")
    private int queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandlerConfig();
    }
}
