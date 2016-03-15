package uk.ac.imperial.lsds.thread.affinity;

/**
 * @author pg1712
 *
 */
public class TheCPU {
	
	private static final String cpuLibrary = 
		Utils.getProjectHome() + "/clib/libCPU.so";
	
	private static final TheCPU cpuInstance = new TheCPU ();
	
	static {
		try {
			System.load (cpuLibrary);
		} catch (final UnsatisfiedLinkError e) {
			System.err.println(e.getMessage());
			System.err.println("error: failed to load CPU library");
			System.exit(1);
		}
	}
	
	public static TheCPU getInstance () { return cpuInstance; }
	
	/**
	 * Thread affinity functions - Implemented using JNI
	 * 
	 */
	
	/* Returns the Total number of CPU Cores (including HTs if available) */
	public native int getNumCores ();
	/* Binds Current Process with the specified core ID  */
	public native int bind (int cpu);
	/* Un-binds all the CPU Cores */
	public native int unbind ();
	/* Returns the CPU id that was LAST binded */
	public native int getCpuId ();
}