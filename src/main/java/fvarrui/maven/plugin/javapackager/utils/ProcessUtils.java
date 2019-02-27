package fvarrui.maven.plugin.javapackager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

public class ProcessUtils {
	
	public static String exec(Log log, File dir, String... command) throws MojoExecutionException {
		StringBuffer outputBuffer = new StringBuffer();
		try { 
			if (dir == null) dir = new File(".");
			Process process = Runtime.getRuntime().exec(command, null, dir);
			if (log != null) log.info("Command line: " + StringUtils.join(command, " "));
			BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while (process.isAlive()) {
				if (output.ready()) {
					String line = output.readLine();
					if (log != null) log.info(line);
					outputBuffer.append(line);
				}
				if (log != null && error.ready()) {
					log.error(error.readLine());
				}
			}
			output.close();
			error.close();
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
		return outputBuffer.toString();
	}
	
	public static String exec(Log log, String... command) throws MojoExecutionException {
		return exec(log, null, command);
	}

	public static String exec(File dir, String... command) throws MojoExecutionException {
		return exec(null, dir, command);
	}
	
	public static String exec(String... command) throws MojoExecutionException {
		return exec(null, null, command);
	}

}