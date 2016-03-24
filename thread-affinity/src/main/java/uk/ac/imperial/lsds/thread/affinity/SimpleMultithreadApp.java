package uk.ac.imperial.lsds.thread.affinity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.HdrHistogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.imperial.lsds.thread.affinity.out.LogWriter;

/**
 * Hello world!
 *
 */
public class SimpleMultithreadApp {
	
	private static int threads = Utils.WORKER_THREADS;
	private static int totalCores = 0;
	private List<SimpleFibProcessor> runnables;
	private static Logger LOG = LoggerFactory.getLogger(SimpleMultithreadApp.class.getName());
	private  ExecutorService excService;
	
	public SimpleMultithreadApp(){
		runnables = new ArrayList<SimpleFibProcessor>();
	}
	
    private void setup(){
    	/* Use HyperThreads or Not */
		if(Utils.HYPER_THREAD)
			totalCores = TheCPU.getInstance().getNumCores();
		else
			totalCores = TheCPU.getInstance().getNumCores()/2;
		LOG.info("=== CORE Pin: {} HyperThread: {} Total Cores: {} Workers: {} ===",
				Utils.CORE_PIN, Utils.HYPER_THREAD , SimpleMultithreadApp.totalCores, SimpleMultithreadApp.threads);
		
		LOG.debug( "Setting up Application..." );
		excService = Executors.newFixedThreadPool(threads);
		for(int i =0 ;i < threads; i++){
			LOG.debug("Processor: "+ i);
			SimpleFibProcessor newFib =  new SimpleFibProcessor(i%totalCores, Utils.CORE_PIN);
			this.runnables.add( newFib );
			excService.execute( newFib );
		}		
        LOG.debug("Setup Done!");
	}
    
    public void run_experiment(){
    	long start = System.currentTimeMillis();
		while((System.currentTimeMillis() - start) < (Utils.EXPERIMENT_RUNTIME*1000) ){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Histogram allLatencies = new Histogram(TimeUnit.SECONDS.toNanos(10), 3);
		for(SimpleFibProcessor tmp : this.runnables){
			tmp.stopWorking();
			allLatencies.add(tmp.getLatencies());
			LOG.debug("Thread working: {} Stats Size in Bytes: {}", tmp.isWorking(), tmp.getLatencies().getEstimatedFootprintInBytes());
		}
		this.excService.shutdown();
		
		LOG.debug("Summary Stats Size: {}", allLatencies.getEstimatedFootprintInBytes());
		//Latency in X divide nanos by ?? => Miliseconds are enough for this experiment
		//allLatencies.outputPercentileDistribution(System.out, 1000000.0);
		LOG.info("Workers {} Summary Average 90th 99th {}\t{}\t{}",
				SimpleMultithreadApp.threads,
				allLatencies.getMean()/1000000.0,
				allLatencies.getValueAtPercentile(90)/1000000.0,
				allLatencies.getValueAtPercentile(99)/1000000.0);
		
		LogWriter histoWrite  = new LogWriter("data", 
				"Fib_" + Integer.toString(Utils.FIBONACCI_NUMBER)
				+"_Pin-" + Utils.CORE_PIN
				+"_Workers-" + SimpleMultithreadApp.threads
				+"_Cores-" + totalCores+".log");
		histoWrite.histoDump(allLatencies);
    }
    
    
    
	public static void main( String[] args ){
		SimpleMultithreadApp app = new SimpleMultithreadApp();
		
		for (int i = 1; i < 11; i++){
			SimpleMultithreadApp.threads = i;
			app.setup();
			app.run_experiment();
		}
		LOG.info("All Done!!");
	
    }
}
