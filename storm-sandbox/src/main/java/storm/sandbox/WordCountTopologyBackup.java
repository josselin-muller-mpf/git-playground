//package storm.sandbox;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.storm.Config;
//import org.apache.storm.LocalCluster;
//import org.apache.storm.StormSubmitter;
//import org.apache.storm.kafka.bolt.KafkaBolt;
//import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
//import org.apache.storm.topology.TopologyBuilder;
//
//import java.util.Arrays;
//import java.util.Properties;
//
//public class WordCountTopologyBackup
//{
//    // Create logger for this class
//    private static final Logger logger = LogManager.getLogger(WordCountTopologyBackup.class);
//
//    // Entry point for the topology
//    public static void main(String[] args) throws Exception
//    {
//        logger.info("STARTING WORDCOUNT TOPOLOGY");
//
//        // Used to build the topology
////        TopologyBuilder builder = new TopologyBuilder();
////
////        // Add the spout, with a name of 'spout' and parallelism hint of 5 executors
//////        builder.setSpout("spout", new RandomSentenceSpout(), 5);
////
////        String zkIp = "localhost";
////        String nimbusHost = "localhost";
////        String zookeeperHost = zkIp +":2181";
////
////        //String zkConnString = "localhost:2181";
////        String topicName = "test";
////
////        BrokerHosts hosts = new ZkHosts(zookeeperHost);
////        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, UUID.randomUUID().toString());
////        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
////
////        builder.setSpout("spout", new KafkaSpout(spoutConfig));
////
////        // Add the storm.sandbox.SplitSentence bolt, with a name of 'split'
////        // and parallelism hint of 8 executors
////        // shufflegrouping subscribes to the spout, and equally distributes
////        // tuples (sentences) across instances of the storm.sandbox.SplitSentence bolt
////        builder.setBolt("split", new SplitSentence())
////                .shuffleGrouping("spout");
////
////        // Add the counter, with a name of 'count'
////        // and parallelism hint of 12 executors
////        // fieldsgrouping subscribes to the split bolt, and
////        // ensures that the same word is sent to the same instance (group by field 'word')
////        builder.setBolt("count", new WordCount())
////                .fieldsGrouping("split", new Fields("word"));
////
////        // new configuration
////        Config conf = new Config();
////
////        // Set to false to disable debug information when
////        // running in production on a cluster
////        //conf.setDebug(false);
////
////        //conf.put(Config.TOPOLOGY_DEBUG, false);
////        //conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 2);
////        conf.put(Config.NIMBUS_HOST, nimbusHost);
////        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
////        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
////        conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(zkIp));
////
////
////        //set producer properties.
////        Properties props = new Properties();
////        props.put("bootstrap.servers", "localhost:9092");
////        props.put("acks", "1");
////        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
////        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
////        props.put("batch.size", "0");
////
////        KafkaBolt bolt = new KafkaBolt()
////                .withProducerProperties(props)
////                .withTopicSelector(new DefaultTopicSelector("test_output"))
////                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("word", "count"));
////
////        builder.setBolt("forwardToKafka", bolt)
////                .shuffleGrouping("count");
//
//        TopologyBuilder builder = new TopologyBuilder();
//
////        Fields fields = new Fields("key", "message");
////        FixedTupleSpout spout = new FixedTupleSpout(fields, 4,
////                new Values("storm", "1"),
////                new Values("trident", "1"),
////                new Values("needs", "1"),
////                new Values("javadoc", "1")
////        );
////        spout.setCycle(true);
////        builder.setSpout("spout", spout, 5);
//
//        builder.setSpout("spout", new HarcodedTupleSpout(), 1);
//
//        //set producer properties.
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "sandbox.hortonworks.com:6667");
//        props.put("acks", "1");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        //props.put("metadata.fetch.timeout.ms", "5000");
//
//        KafkaBolt bolt = new KafkaBolt<String, String>()
//                .withProducerProperties(props)
//                .withTopicSelector(new DefaultTopicSelector("output"));
//                //.withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<String, String>());
//
//        builder.setBolt("forwardToKafka", bolt, 1)
//                .shuffleGrouping("spout");
//
//        Config conf = new Config();
//        conf.setDebug(true);
//        //conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 2);
//        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
//        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
//        conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList("localhost"));
//
//        // If there are arguments, we are running on a cluster
//        if (args != null && args.length > 0)
//        {
//            logger.info("Starting topology on cluster");
//
//            // parallelism hint to set the number of workers
//            conf.setNumWorkers(3);
//
//            // submit the topology
//            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
//        }
//        // Otherwise, we are running locally
//        else
//        {
//            logger.info("Starting topology locally");
//
//            // Cap the maximum number of executors that can be spawned
//            // for a component to 3
//            conf.setMaxTaskParallelism(1);
//
//
//            // LocalCluster is used to run locally
//            LocalCluster cluster = new LocalCluster();
//
//            // submit the topology
//            cluster.submitTopology("word-count", conf, builder.createTopology());
//
//            // sleep
//            logger.info("SLEEPING FOR 20s");
//            Thread.sleep(120000);
//
//            // shut down the cluster
//            logger.info("-----------------------------------------------------------------------------------------");
//            logger.info("-----------------------------------------------------------------------------------------");
//            logger.info("-----------------------------------------------------------------------------------------");
//            logger.info("-----------------------------------------------------------------------------------------");
//            logger.info("                            SHUTTING DOWN TOPOLOGY");
//            logger.info("-----------------------------------------------------------------------------------------");
//            logger.info("-----------------------------------------------------------------------------------------");
//
//            cluster.shutdown();
//        }
//    }
//}