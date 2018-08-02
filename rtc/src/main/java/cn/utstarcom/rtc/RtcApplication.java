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
public class RtcApplication {

    private static final Logger logger = LoggerFactory.getLogger(RtcApplication.class);

    static {

        System.getProperties().put("spring.config.location",
                "file:${spring.property.path}/config/rtc.yml");
    }

    public static void main(String[] args) {

        logger.info("the rtc begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(RtcApplication.class, args);
        logger.info("the rtc start completed. the user dir: {}", userDir);
        LogToConsole.out("RtcApplication",
                "the rtc start completed. the user dir: " + userDir);
    }
}
