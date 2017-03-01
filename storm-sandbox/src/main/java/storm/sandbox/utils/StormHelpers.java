package storm.sandbox.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

public class StormHelpers
{
    private static final Logger logger = LogManager.getLogger(StormHelpers.class);

    public static void SubmitTopology(TopologyBuilder builder, Config conf, String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException, InterruptedException
    {
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
