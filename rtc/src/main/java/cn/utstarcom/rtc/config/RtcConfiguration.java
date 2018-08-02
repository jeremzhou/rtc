/**
 * created on 2018年6月8日 下午2:20:34
 */
package cn.utstarcom.rtc.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import cn.utstarcom.rtc.common.RtcBeanNames;

/**
 * @author UTSC0167
 * @date 2018年6月8日
 *
 */
@Configuration(RtcBeanNames.RTCCONFIGURATION)
public class RtcConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RtcConfiguration.class);

    private final String collectStatisticsTaskCronExpression;

    @Value("${rtc.collect.statistics.task.print-delay-seconds:2}")
    private int collectTaskPrintDelaySeconds;

    @Value("${rtc.collect.is-print.uri:false}")
    private volatile boolean collectPrintUri;

    @Value("${rtc.collect.is-print.handled-data:false}")
    private volatile boolean collectPrintSentData;

    @Value("${rtc.http.server.address:0.0.0.0}")
    private String httpServerIp;

    @Value("${rtc.http.server.port:8082}")
    private int httpServerPort;

    @Value("${rtc.http.server.thread.numbers.bossGroup:1}")
    private int bossGroupThreadNumbers;

    @Value("${rtc.http.server.thread.numbers.workerGroup:16}")
    private int workerGroupThreadNumbers;

    @Value("${rtc.http.server.channelInitializer.eventExecutorGroup.thread.numbers:256}")
    private int eventGroupThreadNumbers;

    @Value("${rtc.http.server.loglevel:INFO}")
    private String httpServerLoglevel;

    @Value("${rtc.http.server.channelOption.soBacklog:1024}")
    private int channelOptionSoBacklog;

    @Value("${rtc.http.server.channelOption.connectTimeoutMills:3000}")
    private int channelOptionConnectTimeoutMills;

    @Value("${rtc.http.server.channelInitializer.readTimeoutHandler.timeoutSeconds:180}")
    private int readTimeoutHandlerTimeoutSeconds;

    @Value("${rtc.http.server.channelInitializer.httpObjectAggregator.maxContentLength:1048576}")
    private int httpObjectAggregatorMaxContentLength;

    @Value("${rtc.task.scheduler.pool-size:10}")
    private int taskSchedulerPoolSize;

    private final String rtcTopicName;

    @Value("${spring.kafka.rtc.topic.num-partitions:12}")
    private int rtcTopicNumPartitions;

    @Value("${spring.kafka.rtc.topic.replication-factor:2}")
    private short rtcTopicReplicationFactor;

    public RtcConfiguration(
            @Value("${rtc.collect.statistics.task.cron-expression:0/1 * * * * ?}") String collectStatisticsTaskCronExpression,
            @Value("${spring.kafka.rtc.topic.name:sparkStreaming1}") String rtcTopicName) {
        this.collectStatisticsTaskCronExpression = collectStatisticsTaskCronExpression;
        this.rtcTopicName = rtcTopicName;
    }

    @PostConstruct
    private void init() {

        log.info("RtcConfiguration load configuration: {}", this);
    }

    public int getCollectTaskPrintDelaySeconds() {
        return collectTaskPrintDelaySeconds;
    }

    public void setCollectTaskPrintDelaySeconds(int collectTaskPrintDelaySeconds) {
        this.collectTaskPrintDelaySeconds = collectTaskPrintDelaySeconds;
    }

    public boolean isCollectPrintUri() {
        return collectPrintUri;
    }

    public void setCollectPrintUri(boolean collectPrintUri) {
        this.collectPrintUri = collectPrintUri;
    }

    public boolean isCollectPrintSentData() {
        return collectPrintSentData;
    }

    public void setCollectPrintSentData(boolean collectPrintSentData) {
        this.collectPrintSentData = collectPrintSentData;
    }

    public String getHttpServerIp() {
        return httpServerIp;
    }

    public void setHttpServerIp(String httpServerIp) {
        this.httpServerIp = httpServerIp;
    }

    public int getHttpServerPort() {
        return httpServerPort;
    }

    public void setHttpServerPort(int httpServerPort) {
        this.httpServerPort = httpServerPort;
    }

    public int getBossGroupThreadNumbers() {
        return bossGroupThreadNumbers;
    }

    public void setBossGroupThreadNumbers(int bossGroupThreadNumbers) {
        this.bossGroupThreadNumbers = bossGroupThreadNumbers;
    }

    public int getWorkerGroupThreadNumbers() {
        return workerGroupThreadNumbers;
    }

    public void setWorkerGroupThreadNumbers(int workerGroupThreadNumbers) {
        this.workerGroupThreadNumbers = workerGroupThreadNumbers;
    }

    public int getEventGroupThreadNumbers() {
        return eventGroupThreadNumbers;
    }

    public void setEventGroupThreadNumbers(int eventGroupThreadNumbers) {
        this.eventGroupThreadNumbers = eventGroupThreadNumbers;
    }

    public String getHttpServerLoglevel() {
        return httpServerLoglevel;
    }

    public void setHttpServerLoglevel(String httpServerLoglevel) {
        this.httpServerLoglevel = httpServerLoglevel;
    }

    public int getChannelOptionSoBacklog() {
        return channelOptionSoBacklog;
    }

    public void setChannelOptionSoBacklog(int channelOptionSoBacklog) {
        this.channelOptionSoBacklog = channelOptionSoBacklog;
    }

    public int getChannelOptionConnectTimeoutMills() {
        return channelOptionConnectTimeoutMills;
    }

    public void setChannelOptionConnectTimeoutMills(int channelOptionConnectTimeoutMills) {
        this.channelOptionConnectTimeoutMills = channelOptionConnectTimeoutMills;
    }

    public int getReadTimeoutHandlerTimeoutSeconds() {
        return readTimeoutHandlerTimeoutSeconds;
    }

    public void setReadTimeoutHandlerTimeoutSeconds(int readTimeoutHandlerTimeoutSeconds) {
        this.readTimeoutHandlerTimeoutSeconds = readTimeoutHandlerTimeoutSeconds;
    }

    public int getHttpObjectAggregatorMaxContentLength() {
        return httpObjectAggregatorMaxContentLength;
    }

    public void setHttpObjectAggregatorMaxContentLength(int httpObjectAggregatorMaxContentLength) {
        this.httpObjectAggregatorMaxContentLength = httpObjectAggregatorMaxContentLength;
    }

    public int getTaskSchedulerPoolSize() {
        return taskSchedulerPoolSize;
    }

    public void setTaskSchedulerPoolSize(int taskSchedulerPoolSize) {
        this.taskSchedulerPoolSize = taskSchedulerPoolSize;
    }

    public int getRtcTopicNumPartitions() {
        return rtcTopicNumPartitions;
    }

    public void setRtcTopicNumPartitions(int rtcTopicNumPartitions) {
        this.rtcTopicNumPartitions = rtcTopicNumPartitions;
    }

    public short getRtcTopicReplicationFactor() {
        return rtcTopicReplicationFactor;
    }

    public void setRtcTopicReplicationFactor(short rtcTopicReplicationFactor) {
        this.rtcTopicReplicationFactor = rtcTopicReplicationFactor;
    }

    public String getCollectStatisticsTaskCronExpression() {
        return collectStatisticsTaskCronExpression;
    }

    public String getRtcTopicName() {
        return rtcTopicName;
    }

    @Override
    public String toString() {
        return "RtcConfiguration [collectStatisticsTaskCronExpression="
                + collectStatisticsTaskCronExpression + ", collectTaskPrintDelaySeconds="
                + collectTaskPrintDelaySeconds + ", collectPrintUri=" + collectPrintUri
                + ", collectPrintSentData=" + collectPrintSentData + ", httpServerIp="
                + httpServerIp + ", httpServerPort=" + httpServerPort + ", bossGroupThreadNumbers="
                + bossGroupThreadNumbers + ", workerGroupThreadNumbers=" + workerGroupThreadNumbers
                + ", eventGroupThreadNumbers=" + eventGroupThreadNumbers + ", httpServerLoglevel="
                + httpServerLoglevel + ", channelOptionSoBacklog=" + channelOptionSoBacklog
                + ", channelOptionConnectTimeoutMills=" + channelOptionConnectTimeoutMills
                + ", readTimeoutHandlerTimeoutSeconds=" + readTimeoutHandlerTimeoutSeconds
                + ", httpObjectAggregatorMaxContentLength=" + httpObjectAggregatorMaxContentLength
                + ", taskSchedulerPoolSize=" + taskSchedulerPoolSize + ", rtcTopicName="
                + rtcTopicName + ", rtcTopicNumPartitions=" + rtcTopicNumPartitions
                + ", rtcTopicReplicationFactor=" + rtcTopicReplicationFactor + "]";
    }
}
