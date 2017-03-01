package storm.sandbox.topologies;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import storm.sandbox.bolts.SplitSentenceBolt;
import storm.sandbox.bolts.WordCountBolt;
import storm.sandbox.utils.StormHelpers;

import java.util.UUID;

public class FromKafkaTopology
{
    // Create logger for this class
    private static final Logger logger = LogManager.getLogger(FromKafkaTopology.class);

    // Entry point for the topology
    public static void main(String[] args) throws Exception
    {
        logger.info("STARTING FROM_KAFKA TOPOLOGY");

        TopologyBuilder builder = new TopologyBuilder();

        String topicName = "sandbox_output";

        BrokerHosts hosts = new ZkHosts("localhost:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
        SplitSentenceBolt splitBolt = new SplitSentenceBolt();
        WordCountBolt countBolt = new WordCountBolt();

        builder.setSpout("spout", kafkaSpout);
        builder.setBolt("split", splitBolt).shuffleGrouping("spout");
        builder.setBolt("count", countBolt).fieldsGrouping("split", new Fields("word"));

        Config conf = new Config();
        conf.setDebug(true);
        conf.put(Config.TOPOLOGY_DEBUG, true);

        StormHelpers.SubmitTopology(builder, conf, args);
    }
}
