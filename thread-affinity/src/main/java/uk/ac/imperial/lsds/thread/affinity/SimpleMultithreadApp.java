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
		
		/* Use HyperThreads or Not */
		if(Utils.HYPER_THREAD)
			totalCores = TheCPU.getInstance().getNumCores();
		else
			totalCores = TheCPU.getInstance().getNumCores()/2;
		
		LOG.info("=== CORE Pin: {} HyperThread: {} Total Cores: {} Workers: {} ===",Utils.CORE_PIN, Utils.HYPER_THREAD , totalCores, Utils.WORKER_THREADS);
		
		LOG.info( "Starting Main Application..." );
		Executor exc = Executors.newFixedThreadPool(threads);
		
		for(int i =0 ;i < threads; i++){
			LOG.info("Processor: "+ i);
			exc.execute( new SimpleFibProcessor(i%totalCores, Utils.CORE_PIN) );
		}
        
        LOG.info("Startup Done!");
    }
}
