/**
 * created on 2018年5月8日 下午2:28:31
 */
package cn.utstarcom.rtc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author UTSC0167
 * @date 2018年5月8日
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncTaskConfiguration {

    private final Logger log = LoggerFactory.getLogger(AsyncTaskConfiguration.class);

    private final RtcConfiguration rtcConfiguration;

    public AsyncTaskConfiguration(RtcConfiguration rtcConfiguration) {
        this.rtcConfiguration = rtcConfiguration;
    }

    @Bean
    public TaskScheduler geTaskScheduler() {

        final int poolSize = rtcConfiguration.getTaskSchedulerPoolSize();
        log.info("Creating Async TaskScheduler. poolSize: {}", poolSize);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("rtc-task-");
        return threadPoolTaskScheduler;
    }
}
