/**
 * created on 2018年1月23日 下午1:26:07
 */
package cn.utstarcom.rtc.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author UTSC0167
 * @date 2018年1月23日
 *
 */
@Component
public class KafkaProducerImpl implements KafkaProducer {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
        super();
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topic, Integer partition, String key, String data) {

        this.kafkaTemplate.send(topic, partition, key, data);
    }
}
