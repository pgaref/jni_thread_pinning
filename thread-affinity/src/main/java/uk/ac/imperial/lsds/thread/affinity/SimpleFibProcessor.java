package uk.ac.imperial.lsds.thread.affinity;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentLinkedQueue;

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
	
	private static Logger LOG = LoggerFactory.getLogger(SimpleFibProcessor.class.getName());
	
	private ConcurrentLinkedQueue<BigInteger> queue;
	
	
	/* Some metrics */
	boolean monitor = true;
	private long count = 0L;
	private long start;
	private long  duration;

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
	
	public void setup(){
		
		if(pinned){
			TheCPU.getInstance().bind(coreID);
		}
		if (monitor) {
			/* Introduce latency measurements */
			start = System.currentTimeMillis();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		while (working) {
		
			setup();
			BigInteger toCompute = new BigInteger("30");
			
			
//			//Block and wait
//			while ((toCompute = queue.poll()) == null) {
//				LockSupport.parkNanos(1L);
//				// Thread.yield();
//			}
			
			
			
			LOG.debug("[Fib Runner] Started! Value="+toCompute);
			BigInteger tmp = fib(new BigInteger(String.valueOf(toCompute)));
			LOG.debug("[Fib Runner] Finished! Result=" + tmp);
			
			duration += System.currentTimeMillis()-start;
			this.count+=1;
			
			
			if ( monitor && ( count % 100 ==0 )) {
				LOG.info("[Stats] Core({})  Avg {} ms per Task", this.coreID , (duration/count));
			}
		}
		
	}
	

}
