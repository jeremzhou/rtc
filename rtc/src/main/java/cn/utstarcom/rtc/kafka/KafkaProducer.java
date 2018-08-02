/**
 * created on 2018年6月11日 上午11:09:09
 */
package cn.utstarcom.rtc.kafka;

/**
 * @author UTSC0167
 * @date 2018年6月11日
 *
 */
public interface KafkaProducer {

    void send(String topic, Integer partition, String key, String data);
}
