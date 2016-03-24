package uk.ac.imperial.lsds.thread.affinity;

/**
 * @author pg1712
 *
 */
public class Utils {
	
	static String PROJECT_HOME;
	/* Experiments conf */
	static boolean CORE_PIN = false;
	static boolean HYPER_THREAD = true;
	static int WORKER_THREADS = 1;
	static int FIBONACCI_NUMBER = 35;
	static int EXPERIMENT_RUNTIME = 60; // in seconds
	/* Up to here */
	
	public static String getProjectHome(){
		if(Utils.PROJECT_HOME == null)
			Utils.PROJECT_HOME = System.getProperty("user.dir");
		return Utils.PROJECT_HOME;
	}

}
