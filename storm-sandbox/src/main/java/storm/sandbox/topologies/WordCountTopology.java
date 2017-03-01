package storm.sandbox.topologies;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import storm.sandbox.bolts.SplitSentenceBolt;
import storm.sandbox.spouts.RandomSentenceSpout;
import storm.sandbox.bolts.WordCountBolt;
import storm.sandbox.utils.StormHelpers;

public class WordCountTopology
{
    // Create logger for this class
    private static final Logger logger = LogManager.getLogger(WordCountTopology.class);

    // Entry point for the topology
    public static void main(String[] args) throws Exception
    {
        logger.info("STARTING WORD_COUNT TOPOLOGY");

        TopologyBuilder builder = new TopologyBuilder();

        RandomSentenceSpout sentenceSpout = new RandomSentenceSpout();
        SplitSentenceBolt splitBolt = new SplitSentenceBolt();
        WordCountBolt countBolt = new WordCountBolt();

        builder.setSpout("spout", sentenceSpout);
        builder.setBolt("split", splitBolt).shuffleGrouping("spout");
        builder.setBolt("count", countBolt).fieldsGrouping("split", new Fields("word"));

        Config conf = new Config();
        conf.setDebug(true);
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 2);

        StormHelpers.SubmitTopology(builder, conf, args);
    }
}
