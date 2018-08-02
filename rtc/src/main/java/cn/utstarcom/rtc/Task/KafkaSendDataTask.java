/**
 * created on 2018年6月9日 下午5:58:16
 */
package cn.utstarcom.rtc.Task;

import java.util.LinkedHashMap;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.common.RtcConstants;
import cn.utstarcom.rtc.config.RtcCache;
import cn.utstarcom.rtc.config.RtcConfiguration;
import cn.utstarcom.rtc.kafka.KafkaProducer;
import cn.utstarcom.rtc.util.RtcUtil;

/**
 * @author UTSC0167
 * @date 2018年6月9日
 *
 */
@Component
public class KafkaSendDataTask {

    private final Logger log = LoggerFactory.getLogger(KafkaSendDataTask.class);

    private final RtcConfiguration rtcConfiguration;

    private final KafkaProducer kafkaProducer;

    public KafkaSendDataTask(RtcConfiguration rtcConfiguration, KafkaProducer kafkaProducer) {
        super();
        this.rtcConfiguration = rtcConfiguration;
        this.kafkaProducer = kafkaProducer;
    }

    @PostConstruct
    public final void initSendData() {

        final int rtcTopicNumPartitions = rtcConfiguration.getRtcTopicNumPartitions();
        log.info("initSendData begin to run. the topicName: {} rtcTopicNumPartitions: {}",
                rtcConfiguration.getRtcTopicName(), rtcTopicNumPartitions);

        for (int i = 0; i < rtcTopicNumPartitions; i++) {
            new Thread(new KafkaSendDataThread(i), "KafkaSendDataThread-" + i).start();
        }

        log.info("initSendData end to run.");
    }

    private final class KafkaSendDataThread implements Runnable {

        private final int sleepTime = 50;

        private final int numPartion;

        private BlockingQueue<LinkedHashMap<String, String>> dataQueue;

        public KafkaSendDataThread(int numPartion) {
            super();
            this.numPartion = numPartion;
        }

        @Override
        public void run() {

            waitHandledQueueMap(numPartion);

            log.info("send data thread {} bein to run.", numPartion);
            while (true) {

                LinkedHashMap<String, String> dataMap = null;
                try {
                    dataMap = this.dataQueue.take();
                } catch (InterruptedException e) {
                    log.warn("run dataQueue.take() get dataMap generate exception:", e);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e1) {
                        log.warn(
                                "after get dataMap generate exception to sleep: {} generate exception:",
                                sleepTime, e1);
                    }
                }

                final String userId = dataMap.get(RtcConstants.FIELD_USER_ID);
                final String json = RtcUtil.linkedHashMap2Json(dataMap);
                final long curUnixTime = RtcUtil.getCurrentUnixTime();
                if (json == null) {
                    log.warn("run for userId: {} linkedHashMap2Json get json is null, don't send!",
                            userId);
                    RtcCache.COLLECT_RESULT_MAP.get(curUnixTime).getDiscardNums().incrementAndGet();
                    continue;
                }

                if (rtcConfiguration.isCollectPrintSentData()) {
                    log.info("collect sent-data: {}", json);
                }

                log.debug("run send for userId: {} partion: {} json: {}", userId, numPartion, json);
                kafkaProducer.send(rtcConfiguration.getRtcTopicName(), numPartion, userId, json);
                RtcCache.COLLECT_RESULT_MAP.get(curUnixTime).getSentNums().incrementAndGet();
            }
        }

        private final void waitHandledQueueMap(int curPartion) {

            log.info("waitHandledQueueMap begin for numPartion: {}", curPartion);
            dataQueue = RtcCache.HANDLED_DATA_QUEUE_MAP.get(curPartion);
            while (dataQueue == null) {
                log.info(
                        "waitHandledQueueMap for numPartion: {} get dataQueue is null, sleep {} millisecond",
                        curPartion, sleepTime);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    log.info(
                            "waitHandledQueueMap for numPartion: {} get dataQueue is null to sleep {} generate Exception:",
                            curPartion, sleepTime, e);
                }
            }
            log.info("waitHandledQueueMap end for numPartion: {}", curPartion);
        }
    }
}
