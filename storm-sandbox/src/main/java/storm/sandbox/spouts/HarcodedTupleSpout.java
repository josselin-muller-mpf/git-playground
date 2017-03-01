package storm.sandbox.spouts;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

public class HarcodedTupleSpout extends BaseRichSpout
{
    // Collector used to emit output
    SpoutOutputCollector _collector;

    // Used to generate a random number
    Random _rand;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector)
    {
        // Set the instance collector to the one passed in
        _collector = collector;

        // For randomness
        _rand = new Random();
    }

    @Override
    public void nextTuple()
    {
        _collector.emit(new Values("test", "aaaarg"));

        // Sleep for a bit
        Utils.sleep(10000);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("key", "message"));
    }
}
