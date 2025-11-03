package application;

import org.kohsuke.args4j.Option;

@SuppressWarnings("java:S1118")
public final class CliParameter {

	public CliParameter() { 
		// empty constructor
	}

	/*
	 * Possible annotations:
	 * 
	 * https://args4j.kohsuke.org/apidocs/org/kohsuke/args4j/Option.html
	 * 	
	 */
	@SuppressWarnings("java:S1444")
	@Option(name="-c", usage="path to property file", required = true)
	public static String cfgPath = null;

}
