package uk.ac.imperial.lsds.thread.affinity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.HdrHistogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pg1712
 *
 */
public class SimpleFibProcessor implements Runnable {

	/* Thread contol state */
	private boolean working = false;
	/* Core Pinned or not*/
	private boolean pinned;
	/* coreID */
	private int coreID;
	private ConcurrentLinkedQueue<BigInteger> queue;
	
	private static Logger LOG = LoggerFactory.getLogger(SimpleFibProcessor.class.getName());
	
	
	/* Some metrics */
	boolean monitor = true;
	private long count = 0L;
	private long start;
	private long  duration;
	// Max recorded value, Decimal places precision
	private final Histogram LATENCY_HISTO = new Histogram(TimeUnit.SECONDS.toNanos(10), 3);
	


	public static BigInteger fib(BigInteger n) {
	    if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0 ) return n;
	    else 
	        return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
	}
	
	
	public SimpleFibProcessor(int coreID, boolean pinned){
		this.coreID = coreID;
		this.pinned = pinned;
		this.working = true;
		this.queue = new ConcurrentLinkedQueue<BigInteger>();
		this.LATENCY_HISTO.reset();
	}
	
	public void stopWorking(){
		this.working = false;
	}
	
	public void startWorking(){
		this.working = true;
	}
	
	/**
	 * @return the queue
	 */
	public ConcurrentLinkedQueue<BigInteger> getQueue() {
		return queue;
	}

	public void enableMonitoring () {
		this.monitor = true;
	}
	
	public void disableMonitoring () {
		this.monitor = false;
	}
	
	public boolean isWorking(){
		return this.working == true;
	}
	/**
	 * @return the latencies
	 */
	public Histogram getLatencies() {
		return LATENCY_HISTO;
	}
	
	public void setup(){
		if(pinned){
			TheCPU.getInstance().bind(coreID);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		setup();
		while (working) {
			
			if (monitor) {
				/* Introduce latency measurements */
				start = System.nanoTime();
			}
			BigInteger toCompute = new BigInteger(Integer.toString(Utils.FIBONACCI_NUMBER));
			
//			//Block and wait
//			while ((toCompute = queue.poll()) == null) {
//				LockSupport.parkNanos(1L);
//				// Thread.yield();
//			}
			
			LOG.debug("[Fib Runner] Started! Value="+toCompute);
			BigInteger tmp = fib(new BigInteger(String.valueOf(toCompute)));
			LOG.debug("[Fib Runner] Finished! Result=" + tmp);
			long took = System.nanoTime() - start;
			
			LATENCY_HISTO.recordValue(took);
			duration += took;
			
			this.count+=1;
			
			
			if ( monitor && ( count % 100 ==0 )) {
				LOG.debug("[Stats] Core({})  Avg {} ns per Task", this.coreID , (duration/count));
			}
		}
		
	}
	

}
