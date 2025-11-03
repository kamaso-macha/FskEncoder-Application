/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : PlugInFactory.java
 *
 * PURPOSE       : what is it for?
 *
 * This file is part of the FSK-Encoder project. More information about
 * this project can be found here:  http://...
 * **********************************************************************
 *
 * Copyright (C) [2024] by Stefan Dickel, id4mqtt at gmx.de
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


package extension.factory;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Supporter class who creates the extension factories needed for the plug-in components.
 * 
 * <p>
 * Collaborators:<br>
 * InputReaderExtensionFactory, <br>
 * TargetSystemExtensionFactory, <br>
 * Java Reflection API
 * 
 * <p>
 * Description:<br>
 * All plug-in components are defined in the 'Extension.properties' file 
 * where a association is made between the extension provider name and
 * the provider factory class, e.g.
 * 
 * 		z80trainerIhx8.provider		= target.z80trainer.Z80TrainerExtension
 *		z80trainerIhx8.inputFormat	= source.ihx.x8.Ihx8ReaderExtension
 * <br>
 * The plug-in factory now creates an instance of the extension factory from the class name of the extension. 
 * 
 * <p>
 * @author Stefan
 *
 */

public class PlugInFactory {
	
	private static Logger logger = LogManager.getLogger(PlugInFactory.class.getName());
	
	private PlugInFactory() { /* empty */}


	/**
	 * Creates a TargetSystemExtensionFactory instance from the given class name.
	 * 
	 * @param aProviderClassName
	 * : the FQ class name of the factory provider.
	 * @return
	 * an instance of TargetSystemExtensionFactory
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * if Java Reflection can't instantiate the requested class.
	 * 
	 */
	public static TargetSystemExtensionFactory getTargetSystemExtensionFactory(final String aProviderClassName) 
			throws ClassNotFoundException, 
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
			NoSuchMethodException, SecurityException {
		
		if(aProviderClassName == null) throw new IllegalArgumentException("aProviderClassName cant be null");
		if(aProviderClassName.isBlank()) throw new IllegalArgumentException("aProviderClassName cant be blank");
		
		logger.trace("getTargetSystemExtensionFactory(): aProviderClassName = {}", aProviderClassName);
		
		Class<?> candidate = Class.forName(aProviderClassName);
		
		return (TargetSystemExtensionFactory) candidate.getConstructor().newInstance();
		
	} // getTargetSystemExtensionFactory()
	
	
	/**
	 * Creates a InputReaderExtensionFactory instance from the given class name.
	 * 
	 * @param aProviderClassName
	 * : the FQ class name of the factory provider.
	 * @return
	 * an instance of InputReaderExtensionFactory
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * if Java Reflection can't instantiate the requested class.
	 * 
	 */
	public static InputReaderExtensionFactory getInputReaderExtensionFactory(final String aProviderClassName) 
			throws ClassNotFoundException, 
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
			NoSuchMethodException, SecurityException {
		
		if(aProviderClassName == null) throw new IllegalArgumentException("aProviderClassName cant be null");
		if(aProviderClassName.isBlank()) throw new IllegalArgumentException("aProviderClassName cant be blank");
		
		logger.trace("getInputReaderExtensionFactory(): aProviderClassName = {}", aProviderClassName);
		
		Class<?> candidate = Class.forName(aProviderClassName);
		
		return (InputReaderExtensionFactory) candidate.getConstructor().newInstance();
		
	} // getTargetSystemExtensionFactory()
	
	
} // ssalc
