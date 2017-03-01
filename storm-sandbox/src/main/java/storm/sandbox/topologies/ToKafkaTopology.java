package storm.sandbox.topologies;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.topology.TopologyBuilder;
import storm.sandbox.spouts.HarcodedTupleSpout;
import storm.sandbox.utils.StormHelpers;

import java.util.Properties;

public class ToKafkaTopology
{
    // Create logger for this class
    private static final Logger logger = LogManager.getLogger(ToKafkaTopology.class);

    // Entry point for the topology
    public static void main(String[] args) throws Exception
    {
        logger.info("STARTING TO_KAFKA TOPOLOGY");

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new HarcodedTupleSpout(), 1);

        //set producer properties.
        Properties props = new Properties();
        props.put("bootstrap.servers", "sandbox.hortonworks.com:6667");
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaBolt bolt = new KafkaBolt<String, String>()
                .withProducerProperties(props)
                .withTopicSelector(new DefaultTopicSelector("sandbox_output"));
                //.withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper<String, String>());

        builder.setBolt("forwardToKafka", bolt, 1)
                .shuffleGrouping("spout");

        Config conf = new Config();
        conf.setDebug(true);

        StormHelpers.SubmitTopology(builder, conf, args);
    }
}
