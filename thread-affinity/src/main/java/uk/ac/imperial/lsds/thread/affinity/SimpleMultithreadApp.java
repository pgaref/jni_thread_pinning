package uk.ac.imperial.lsds.thread.affinity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class SimpleMultithreadApp {
	
	private static int threads = Utils.WORKER_THREADS;
	private static int totalCores = 0;

    private static Logger LOG = LoggerFactory.getLogger(SimpleMultithreadApp.class.getName());
	
	public static void main( String[] args ){
		
		if(Utils.HYPER_THREAD)
			totalCores = TheCPU.getInstance().getNumCores();
		else
			totalCores = TheCPU.getInstance().getNumCores()/2;
		LOG.info("=== HyperThread: {} Cores: {} Workers: {} ===", Utils.HYPER_THREAD , totalCores, Utils.WORKER_THREADS);
		
		LOG.info( "Starting Main Application..." );
		Executor exc = Executors.newFixedThreadPool(threads);
		
		for(int i =0 ;i < threads; i++){
			LOG.info("Processor: "+ i);
			exc.execute( new SimpleFibProcessor(i%totalCores, true) );
		}
        
        LOG.info("Startup Done!");
    }
}
