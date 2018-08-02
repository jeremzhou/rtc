/**
 * created on 2018年6月9日 下午4:30:03
 */
package cn.utstarcom.rtc.config;

/**
 * @author UTSC0167
 * @date 2018年6月9日
 *
 */
//@Component
public class KafkaAdminClientConfiguration {

//    private static final Logger log = LoggerFactory.getLogger(KafkaAdminClientConfiguration.class);
//
//    private final KafkaAdmin kafkaAdmin;
//
//    private final RtcConfiguration rtcConfiguration;
//
//    public KafkaAdminClientConfiguration(KafkaAdmin kafkaAdmin, RtcConfiguration rtcConfiguration) {
//        super();
//        this.kafkaAdmin = kafkaAdmin;
//        this.rtcConfiguration = rtcConfiguration;
//    }
//
//    @PostConstruct
//    private void init() {
//
//        final String rtcTopicName = rtcConfiguration.getRtcTopicName();
//        final int rtcTopicNumPartitions = rtcConfiguration.getRtcTopicNumPartitions();
//        final short rtcTopicReplicationFactor = rtcConfiguration.getRtcTopicReplicationFactor();
//
//        log.info(
//                "KafkaAdminClientConfiguration begin to init. the rtcTopicName: {} rtcTopicNumPartitions: {} rtcTopicReplicationFactor: {}",
//                rtcTopicName, rtcTopicNumPartitions, rtcTopicReplicationFactor);
//        AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig());
//        ListTopicsResult listTopicsResult = adminClient.listTopics();
//        try {
//            Set<String> nameSet = listTopicsResult.names().get();
//            
//            boolean isExisting = false;
//            for (String existTopicName : nameSet) {
//                log.info("KafkaAdminClientConfiguration existTopicName: {}",existTopicName);
//                if (rtcTopicName.equals(existTopicName)) {
//                    log.info("KafkaAdminClientConfiguration the rtcTopicName: {} has exist",rtcTopicName);
//                    isExisting = true;
//                    break;
//                }
//            }
//            
//            if (nameSet == null || nameSet.size() == 0 || !isExisting) {
//                
//                log.info("KafkaAdminClientConfiguration begin to create rtcTopicName: {}",rtcTopicName);
//                NewTopic newTopic = new NewTopic(rtcTopicName, rtcTopicNumPartitions,rtcTopicReplicationFactor);
//                List<NewTopic> topics = new LinkedList<>();
//                topics.add(newTopic);
//                CreateTopicsResult createTopicsResult = adminClient.createTopics(topics);
//                KafkaFuture<Void> kafkaFuture = createTopicsResult.all();
//                kafkaFuture.get();
//                
//                if (!kafkaFuture.isDone()) {
//                    log.info("KafkaAdminClientConfiguration create topic result isn't done, exit!");
//                    LogToConsole.err(this.getClass().getName(),
//                            "KafkaAdminClientConfiguration create topic isn't done, exit!");
//                    System.exit(2);
//                }
//                log.info("KafkaAdminClientConfiguration end to create rtcTopicName: {}",rtcTopicName);
//            } else {
//                log.info("KafkaAdminClientConfiguration the rtcTopicName: {} has exist, do nothing.",rtcTopicName);
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            log.info("KafkaAdminClientConfiguration create topic generate Exception:",
//                    e);
//            log.info("KafkaAdminClientConfiguration create topic failed because exception, exit!");
//            LogToConsole.err(this.getClass().getName(),
//                    "KafkaAdminClientConfiguration create topic failed because exception, exit!");
//            System.exit(2);
//        }
//        log.info("KafkaAdminClientConfiguration end to init.");
//    }
}
