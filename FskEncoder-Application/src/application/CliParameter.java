/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : CliParameter.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Application
 *
 * **********************************************************************
 *
 * Copyright (C)2025 by Kama So Macha (http://github.com/kamaso-macha)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 *
 */


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
