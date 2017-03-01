package storm.sandbox;

import java.text.BreakIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.Constants;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

// There are a variety of bolt types. In this case, use BaseBasicBolt
public class SplitSentence extends BaseBasicBolt
{
    private static final Logger logger = LogManager.getLogger(WordCount.class);

    // Execute is called to process tuples
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector)
    {
        // Ignore tick
        if (tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID))
            return;

        for (Object field : tuple.getValues())
        {
            logger.info("Received");
            logger.info(field);

            // Get the sentence content from the tuple
            String sentence = field.toString();

            // An iterator to get each word
            BreakIterator boundary = BreakIterator.getWordInstance();

            //Give the iterator the sentence
            boundary.setText(sentence);

            // Find the beginning first word
            int start = boundary.first();

            // Iterate over each word and emit it to the output stream
            for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next())
            {
                // get the word
                String word = sentence.substring(start, end);

                // If a word is whitespace characters, replace it with empty
                word = word.replaceAll("\\s+", "");

                // if it's an actual word, emit it
                if (!word.equals(""))
                {
                    logger.info("Emitting: " + word);
                    collector.emit(new Values(word));
                }
            }
        }
    }

    // Declare that emitted tuples will contain a word field
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer)
    {
        declarer.declare(new Fields("word"));
    }
}