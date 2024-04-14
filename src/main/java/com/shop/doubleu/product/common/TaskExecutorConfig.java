package com.shop.doubleu.product.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskExecutorConfig {
    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 실행 대기 thread 수 설정값
        executor.setMaxPoolSize(10); // 동시 동작하는 최대 thread 수
        executor.setQueueCapacity(100); // 대기 queue에 최대 수용 가능 한 수
        executor.setThreadNamePrefix("CrawlService-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
