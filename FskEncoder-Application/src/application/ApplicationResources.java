/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : Applicationresources.java
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2024-06-08 16:49:06

public class ApplicationResources {
	
	private static Logger logger = LogManager.getLogger(ApplicationResources.class.getName());
	
	private static String path;
	
	/**
	 * 
	 */
	
	public static final String DEFAULT_OUTPUT_VOLUME =  "22";
	
	
	public ApplicationResources() { logger.info("ApplicationResources"); }
	
	public static void setPath(String aPath) { path = aPath; } 

	
	public static final String DEFAULT_PROPERTY_FILE()	 { logger.info("DEFAULT_PROPERTY_FILE()");
		return path + "/FskEncoder.properties"; }
	
	
	public static final String EXTENSION_PROPERTY_FILE() { logger.info("EXTENSION_PROPERTY_FILE()");
		return path + "/Plugin.properties";  }

	
	
	public static String asString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("ApplicationResources [hashCode()=" + getHashCode() + ", ");
		sb.append(path + ", ");
		sb.append(DEFAULT_PROPERTY_FILE() + ", ");
		sb.append(EXTENSION_PROPERTY_FILE());
		sb.append("]");
		
		return  sb.toString();
		
	}


	public static int getHashCode() {
		
		final int prime = 31;
		int result = 1;
		
		String defProp = DEFAULT_PROPERTY_FILE();
		String extProp = EXTENSION_PROPERTY_FILE();
		
		result = prime * result + ((path == null) ? 0 : hashCode(path.getBytes()));

		result = prime * result + ((defProp == null) ? 0 : hashCode(defProp.getBytes()));
		result = prime * result + ((extProp == null) ? 0 : hashCode(extProp.getBytes()));
		
		return result;
	}


    public static int hashCode(byte[] value) {
        int h = 0;
        for (byte v : value) {
            h = 31 * h + (v & 0xff);
        }
        return h;
    }
	
} // ssalc
