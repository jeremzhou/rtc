package cn.utstarcom.rtc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.utstarcom.rtc.common.LogToConsole;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class RtcApplicationDev {

    private static final Logger logger = LoggerFactory.getLogger(RtcApplicationDev.class);
    
    static {
        String userDir = System.getProperty("user.dir");
        System.getProperties().put("spring.property.path", userDir + "/src/main");
        System.getProperties().put("spring.config.location", "file:${spring.property.path}/config/rtc.yml");
        System.getProperties().put("logging.level.root", "info");
    }
    
    public static void main(String[] args) {

        logger.info("the rtc begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(RtcApplicationDev.class, args);
        logger.info("the rtc start completed. the user dir: {}", userDir);
        LogToConsole.out("RtcApplication",
                "the rtc start completed. the user dir: " + userDir);
    }
}
