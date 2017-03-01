package storm.sandbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.*;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.Arrays;
import java.util.UUID;

public class WordCountTopology
{
    // Create logger for this class
    private static final Logger logger = LogManager.getLogger(WordCountTopology.class);

    // Entry point for the topology
    public static void main(String[] args) throws Exception
    {
        logger.info("STARTING WORDCOUNT TOPOLOGY");

        // Used to build the topology
        TopologyBuilder builder = new TopologyBuilder();
        String topicName = "test";

        BrokerHosts hosts = new ZkHosts("localhost:2181");
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/kafka_tests", UUID.randomUUID().toString());
        spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        builder.setSpout("spout", new KafkaSpout(spoutConfig));
        builder.setBolt("split", new SplitSentence()).shuffleGrouping("spout");
        //builder.setBolt("count", new WordCount()).fieldsGrouping("split", new Fields("word"));

        // new configuration
        Config conf = new Config();

        // Set to false to disable debug information when
        // running in production on a cluster
        conf.setDebug(true);

        conf.put(Config.TOPOLOGY_DEBUG, true);
        //conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 2);
//        conf.put(Config.NIMBUS_SEEDS, nimbus_seeds);
//        conf.put(Config.NIMBUS_THRIFT_PORT, 6627);
        //conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        conf.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        conf.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList("localhost"));

        // If there are arguments, we are running on a cluster
        if (args != null && args.length > 0)
        {
            logger.info("Starting topology on cluster");

            // parallelism hint to set the number of workers
            conf.setNumWorkers(3);

            // submit the topology
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
        // Otherwise, we are running locally
        else
        {
            logger.info("Starting topology locally");

            // Cap the maximum number of executors that can be spawned
            // for a component to 3
            conf.setMaxTaskParallelism(1);


            // LocalCluster is used to run locally
            LocalCluster cluster = new LocalCluster();

            // submit the topology
            cluster.submitTopology("word-count", conf, builder.createTopology());

            // sleep
            logger.info("SLEEPING FOR 20s");
            Thread.sleep(120000);

            // shut down the cluster
            logger.info("-----------------------------------------------------------------------------------------");
            logger.info("-----------------------------------------------------------------------------------------");
            logger.info("-----------------------------------------------------------------------------------------");
            logger.info("-----------------------------------------------------------------------------------------");
            logger.info("                            SHUTTING DOWN TOPOLOGY");
            logger.info("-----------------------------------------------------------------------------------------");
            logger.info("-----------------------------------------------------------------------------------------");

            cluster.shutdown();
        }
    }
}