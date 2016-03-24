package uk.ac.imperial.lsds.thread.affinity.out;

import java.io.FileNotFoundException;
import java.io.OutputStream;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.imperial.lsds.thread.affinity.Utils;


/**
 * @author pg1712
 *
 */
public class LogWriter {
	
	private String filename;
	private String path;
	private HistogramLogWriter logWriter;
	private static Logger LOG = LoggerFactory.getLogger(LogWriter.class.getName());
	
	public LogWriter(String path, String filename){
		this.path = Utils.getProjectHome() +"/"+ path +"/";
		this.filename = filename;
		
		try {
			this.logWriter = new HistogramLogWriter(this.path+ this.filename);
		} catch (FileNotFoundException e) {
			LOG.error("Could not persist HDR log {} ", filename);
			LOG.error("Exception {}", e);
		}
		// headers etc.
		this.setup();
	}
	
	public void setup(){
		// Some log header bits
		this.logWriter.outputComment("[Logged with " + LogWriter.class.getName() + "]");
		this.logWriter.outputLogFormatVersion();
		this.logWriter.outputStartTime(System.currentTimeMillis());
		this.logWriter.setBaseTime(System.currentTimeMillis());
		
	}
	
	public void histoDump(Histogram histo){
		LOG.info("Persisting histo file {}", this.path + this.filename);
		// Every once in a while, we just drop some histograms
		this.logWriter.outputComment("[ Custom Summary Average 90th 99th \t"+
				histo.getMean()/1000000.0 + "\t"+
				histo.getValueAtPercentile(90)/1000000.0 + "\t"+
				histo.getValueAtPercentile(99)/1000000.0 + "]");
		this.logWriter.outputLegend();
		this.logWriter.outputIntervalHistogram(histo);
	}

}
