package uk.ac.imperial.lsds.thread.affinity;

/**
 * @author pg1712
 *
 */
public class Utils {
	
	static String PROJECT_HOME;
	static boolean HYPER_THREAD=false;
	static int WORKER_THREADS = 8;
	
	
	public static String getProjectHome(){
		if(Utils.PROJECT_HOME == null)
			Utils.PROJECT_HOME = System.getProperty("user.dir");
		return Utils.PROJECT_HOME;
	}

}
