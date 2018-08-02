/**
 * created on 2018年6月8日 下午2:16:30
 */
package cn.utstarcom.rtc.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.utstarcom.rtc.bo.CollectResult;
import cn.utstarcom.rtc.config.RtcCache;
import cn.utstarcom.rtc.config.RtcConfiguration;
import cn.utstarcom.rtc.util.RtcUtil;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
@Component
public final class StatisticalCollectResult {

    private final Logger log = LoggerFactory.getLogger(StatisticalCollectResult.class);

    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd_HH:mm:ss");

    private final long aheadSeonds = 3600;

    private final long expiredSeconds = 1800;

    private final long printDelaySeconds;

    private volatile boolean isReady = false;

    public StatisticalCollectResult(RtcConfiguration rtcConfiguration) {
        super();
        this.printDelaySeconds = rtcConfiguration.getCollectTaskPrintDelaySeconds();
    }

    @PostConstruct
    private void init() {

        log.info(
                "StatisticalCollectResult init begin aheadSeonds: {} expiredSeconds: {} printDelaySeconds: {}",
                aheadSeonds, expiredSeconds, printDelaySeconds);
        final long curUnixTime = RtcUtil.getCurrentUnixTime();
        final LocalDateTime localDateTime = RtcUtil.unixTime2LocalDateTime(curUnixTime);
        initCollectResult(curUnixTime, localDateTime);
        delExpiredResult(curUnixTime, localDateTime);
        doStatistics();
        this.isReady = true;
        log.info("StatisticalCollectResult init end");
    }

    @Scheduled(cron = "${rtc.collect.statistics.task.cron-expression}")
    private void scheduledStatistics() {

        log.debug("scheduledStatistics begin to run.");
        if (isReady)
            doStatistics();
        else {
            log.info("scheduledStatistics the isReady is false, do nothing");
        }

        log.debug("scheduledStatistics end to run.");
    }

    private final void doStatistics() {

        final long curUnixTime = RtcUtil.getCurrentUnixTime();
        final LocalDateTime localDateTime = RtcUtil.unixTime2LocalDateTime(curUnixTime);

        printResult(curUnixTime);

        if (curUnixTime % expiredSeconds == 0) {
            initCollectResult(curUnixTime, localDateTime);
            delExpiredResult(curUnixTime, localDateTime);
        }
    }

    private final void printResult(final long curUnixTime) {

        final long printUnixTime = curUnixTime - printDelaySeconds;
        final LocalDateTime localDateTime = RtcUtil.unixTime2LocalDateTime(printUnixTime);
        CollectResult result = RtcCache.COLLECT_RESULT_MAP.get(printUnixTime);
        if (result == null) {

            log.info("collectResult {} the collect result is null",
                    localDateTime.format(DATETIME_FORMATTER));
        } else {
            log.info(
                    "result {} recived: {} error: {} discard: {} fix: {} handled: {} sent: {}",
                    localDateTime.format(DATETIME_FORMATTER), result.getRecivedNums(),
                    result.getErrorNums(), result.getDiscardNums(), result.getFixNums(),
                    result.getHandledNums(), result.getSentNums());
        }
    }

    private final void initCollectResult(final long curUnixTime,
            final LocalDateTime localDateTime) {

        log.info(
                "initCollectResult begin for curUnixTime: {} localDateTime: {} printDelaySeconds: {} aheadSeonds: {} COLLECT_RESULT_MAP size: {}",
                curUnixTime, localDateTime, printDelaySeconds, aheadSeonds,
                RtcCache.COLLECT_RESULT_MAP.size());
        final long aheadUnixTime = curUnixTime + aheadSeonds;
        log.info("delExpiredResult aheadUnixTime: {} aheadLocalDateTime: {}", aheadUnixTime,
                RtcUtil.unixTime2LocalDateTime(aheadUnixTime));

        for (long i = curUnixTime - printDelaySeconds * 3; i <= aheadUnixTime; i++)
            RtcCache.COLLECT_RESULT_MAP.putIfAbsent(i,
                    new CollectResult(new AtomicInteger(0), new AtomicInteger(0),
                            new AtomicInteger(0), new AtomicInteger(0), new AtomicInteger(0),
                            new AtomicInteger(0)));

    }

    private final void delExpiredResult(final long curUnixTime, final LocalDateTime localDateTime) {

        log.info(
                "delExpiredResult begin for curUnixTime: {} localDateTime: {} expiredSeconds: {} COLLECT_RESULT_MAP size: {}",
                curUnixTime, localDateTime, expiredSeconds, RtcCache.COLLECT_RESULT_MAP.size());
        final long expiredUnixTime = curUnixTime - expiredSeconds;
        log.info("delExpiredResult expiredUnixTime: {} expiredLocalDateTime: {}", expiredUnixTime,
                RtcUtil.unixTime2LocalDateTime(expiredUnixTime));

        Iterator<Map.Entry<Long, CollectResult>> iterator = RtcCache.COLLECT_RESULT_MAP.entrySet()
                .iterator();
        while (iterator.hasNext()) {

            Map.Entry<Long, CollectResult> entry = iterator.next();
            if (entry.getKey() < expiredUnixTime)
                iterator.remove();
        }

        log.info("delExpiredResult end COLLECT_RESULT_MAP size: {}",
                RtcCache.COLLECT_RESULT_MAP.size());

    }

    public boolean isReady() {
        return isReady;
    }
}
