/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : FskUploaderModel.java
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


package model;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.ApplicationResources;

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
// Created at 2024-05-11 09:18:56

public class FskUploaderModel implements

	InputFileControllerModel,
	MainWindowControllerModel,
	OutputDeviceControllerModel,
	TargetSystemSelectionModel

	{

	public static final String NOT_DEFINED = "Not defined";
	
	
	protected static final String AUTOSAVE_FLAG			= "SaveOnExit";
	
	protected static String DEFAULT_PROPERTY_FILE;		// NOSONAR
	protected static String EXTENSION_PROPERTY_FILE;	// NOSONAR

	protected static final String PTY_CURRENT_LOCATION_X	= "currentLocationX";
	protected static final String PTY_CURRENT_LOCATION_Y	= "currentLocationY";
	protected static final String PTY_DEFAULT_DIRECTORY		= "defaultDirectory";
	protected static final String PTY_OUTPUT_DEVICE			= "defaultOutputDeviceName";
	protected static final String PTY_OUTPUT_VOLUME			= "defaultOutputVolume";
	protected static final String PTY_TARGET_SYSTEM			= "defaultTargetSystemName";

	private Logger logger = LogManager.getLogger(FskUploaderModel.class.getName());

	protected Properties defaultProperties		= new Properties();
	protected Properties extensionProperties	= new Properties();

	
	/**
	 * 
	 */
	public FskUploaderModel() {
		logger.trace("FskUploaderModel()");
		
		DEFAULT_PROPERTY_FILE	= ApplicationResources.DEFAULT_PROPERTY_FILE();
		EXTENSION_PROPERTY_FILE	= ApplicationResources.EXTENSION_PROPERTY_FILE();
		
		loadProperties();
		
	} // FskUploaderModel()
	
	
	@Override
	public Point getCurrentPosition() {
		logger.trace("getCurrentPosition()");
		
		String xPos = (String) defaultProperties.getOrDefault(PTY_CURRENT_LOCATION_X, "0");
		String yPos = (String) defaultProperties.getOrDefault(PTY_CURRENT_LOCATION_Y, "0");
		
		int ptX = Integer.parseInt(xPos);
		int ptY = Integer.parseInt(yPos);
		
		logger.debug("x: {}, y: {}", ptX, ptY);
				
		return new Point(ptX, ptY);
		
	} // getCurrentPosition()
	
	
	/**
	 * @return
	 */
	@Override
	public String getDefaultDirectory() {
		logger.trace("getDefaultDirectory()");
		
		return (String) defaultProperties.getOrDefault(PTY_DEFAULT_DIRECTORY, "");	// NOSONAR
		
	} // getDefaultDirectory()


	/**
	 * @return
	 */
	public String getInputReaderProviderClassName(final String aTargetSystemName) {
		logger.trace("getInputReaderProviderClassName(): aTargetSystemName = {}", aTargetSystemName);

		// z80trainer.name		= SEL Z80-Trainer
		String unique_system_name_name = getKeyByValue(aTargetSystemName, ".name", extensionProperties);		// NOSONAR
		logger.trace("unique_system_name_name: {}", unique_system_name_name);
		
		if(unique_system_name_name == null) {
			logger.info("Target System Name '{}' is unknown in the current configuration.", aTargetSystemName);
			return NOT_DEFINED;
		}
				
		// e.g. z80trainer
		String unique_system_name = unique_system_name_name.replace(".name", "");		// NOSONAR
		logger.trace("unique_system_name: {}", unique_system_name);

		// e.g. z80trainer.inputFormat	= source.ihx8.IhxReader
		String inputReaderProviderClassNeme = (String) extensionProperties.get(unique_system_name + ".inputFormat");		// NOSONAR
		logger.trace("inputReaderProviderClassNeme: '{}'", inputReaderProviderClassNeme);
		
		if(inputReaderProviderClassNeme == null) {
			logger.info("Input format for target {} is unknown in the current configuration.", unique_system_name);
			return NOT_DEFINED;
		}
		
		return inputReaderProviderClassNeme;
		
	} // getInputReaderProviderClassName()
	
	
	protected String getKeyByValue(final String aValue, final String aKeyFilter, final Properties aPropertySet) {
		logger.trace("getByValue(): aValue = {}", aValue);
		
		Set<Entry<Object, Object>> entrySet = aPropertySet.entrySet();
		Object key;
		Object value;
		
		for(Entry<Object, Object> entry : entrySet) {	// NOSONAR
			
			key = entry.getKey();
			value = entry.getValue();
			
			if(
				((String)value).equals(aValue)	// NOSONAR
			&& ((String)key).contains(aKeyFilter)	// NOSONAR
			
			) {
				
				return ((String)key);	// NOSONAR
				
			}
			
		} // rof
		
		return null;
		
	} // getByValue()
	
	
	/**
	 * @return
	 */
	@Override
	public String getOutputDeviceName() {
		logger.trace("getOutputDeviceName()");
		
		return (String) defaultProperties.getOrDefault(PTY_OUTPUT_DEVICE, NOT_DEFINED);	// NOSONAR
		
	} // getOutputDeviceName()
	
	
	/**
	 * @param aValue
	 */
	public int getOutputVolume() {
		logger.trace("getOutputVolume()");
		
		String outputVolum = (String) defaultProperties.getOrDefault(PTY_OUTPUT_VOLUME, ApplicationResources.DEFAULT_OUTPUT_VOLUME);	// NOSONAR
		return Integer.parseInt(outputVolum);
		
	} //getOutputVolume(...)


	/**
	 * @return
	 */
	@Override
	public String getTargetSystemName() {
		logger.trace("getTargetSystemName()");
		
		return (String) defaultProperties.getOrDefault(PTY_TARGET_SYSTEM, NOT_DEFINED);	// NOSONAR
		
	} // getTargetSystemName()
	

	@Override
	public String[] getTargetSystemNames() {
		logger.trace("getTargetSystemNames()");
		
		List<String> targetSystemNames = new ArrayList<>();
		
		Set<Entry<Object, Object>> entrySet = extensionProperties.entrySet();
		Object key;
		Object value;
		
		for(Entry<Object, Object> entry : entrySet) {	// NOSONAR
			
			key = entry.getKey();
			value = entry.getValue();
			
			logger.trace("key: {}, value: {}", key, value);

			if(((String)key).endsWith(".name")) {	// NOSONAR
				targetSystemNames.add((String)value);
			}
			
		} // rof
		
		Collections.sort(targetSystemNames);
		logger.trace("targetSystemNames: {}", targetSystemNames);
		
		return targetSystemNames.toArray(new String[0]);
		
	} // getTargetSystemNames()
	
	
	/**
	 * @return
	 */
	public String getTargetSystemProviderClassName(final String aTargetSystemName) {
		logger.trace("getTargetSystemProviderClassName(): aTargetSystemName = {}", aTargetSystemName);
		
		// z80trainer.name		= SEL Z80-Trainer
		String unique_system_name_name = getKeyByValue(aTargetSystemName, ".name", extensionProperties);		// NOSONAR
		logger.trace("unique_system_name_name: {}", unique_system_name_name);
		
		if(unique_system_name_name == null) {
			logger.info("Target System Name '{}' is unknown in the current configuration.", aTargetSystemName);
			return NOT_DEFINED;
		}
		
		// e.g. z80trainer
		String unique_system_name = unique_system_name_name.replace(".name", "");		// NOSONAR
		logger.trace("unique_system_name: {}", unique_system_name);

		// e.g. z80trainer.provider		= target.z80trainer.Z80TrainerExtension
		String targetSystemProviderClassName = (String) extensionProperties.get(unique_system_name + ".provider");		// NOSONAR
		logger.trace("targetSystemProviderClassName: {}", targetSystemProviderClassName);
		
		if(targetSystemProviderClassName == null) {
			logger.info("Target provider for system {} is unknown in the current configuration.", unique_system_name_name);
			return NOT_DEFINED;
		}
		
		return targetSystemProviderClassName;
		
	} // getTargetSystemProviderClassName()

	
	protected void loadProperties() {
		logger.trace("loadProperties()");
		
		logger.debug("defaultProperties	= {}, extensionProperties = {}", defaultProperties.toString(), extensionProperties.toString());
		logger.debug("ApplicationResources: {}", new ApplicationResources().toString());
		try {

			logger.debug("Current directory is '{}'", System.getProperty("user.dir"));
			
			logger.info("Try to load default property file '{}' ...", DEFAULT_PROPERTY_FILE);
			File defaultFile = new File(DEFAULT_PROPERTY_FILE);
			
			if(defaultFile.exists()) {
				
				FileReader defaultReader= new FileReader(defaultFile);
				defaultProperties.load(defaultReader);
				
				defaultReader.close();
				defaultReader = null;
				defaultFile = null;

				logger.info("Default property file '{}' successfully loaded.", DEFAULT_PROPERTY_FILE);

			}
			else {
				logger.info("File not found, no defaults loaded.");
			}
			
			
			logger.info("Try to load extension property file '{}' ...", EXTENSION_PROPERTY_FILE);
			File extensionFile = new File(EXTENSION_PROPERTY_FILE);
			
			if(extensionFile.exists()) {
				
				FileReader extensionReader= new FileReader(extensionFile);
				extensionProperties.load(extensionReader);
				
				extensionReader.close();
				extensionReader = null;
				extensionFile = null;
				
				logger.info("Extension property file '{}' successfully loaded.", EXTENSION_PROPERTY_FILE);

			}
			else {
				logger.info("File not found, no extensions loaded.");
			}
			
		} catch (FileNotFoundException e) {
			logger.error("Property file not found!", e);
		} catch (IOException e) {
			logger.error("Property file not accessible!", e);
		}

		logger.trace("Extension properties: {}", extensionProperties);

	} // loadDefaults()


	@Override
	public void setCurrentPosition(Point aPosition) {
		logger.trace("setCurrentPosition(): " + aPosition);
		
		Long xPos = (long) aPosition.getX();
		Long yPos = (long)aPosition.getY();
		
		
		defaultProperties.put(PTY_CURRENT_LOCATION_X, xPos.toString());
		defaultProperties.put(PTY_CURRENT_LOCATION_Y, yPos.toString());

	} // setCurrentPosition()
	
	
	/**
	 * @param absolutePath
	 */
	@Override
	public void setDefaultDirectory(String aAbsolutePath) {
		logger.trace("setDefaultDirectory(): aAbsolutePath = {}", aAbsolutePath);
		
		defaultProperties.put(PTY_DEFAULT_DIRECTORY, aAbsolutePath);
		
	} // setDefaultDirectory()


	/**
	 * @param index
	 */
	@Override
	public void setOutputDeviceName(String aNname) {
		logger.trace("setOutputDeviceName(): aNname = {}", aNname);
		
		defaultProperties.put(PTY_OUTPUT_DEVICE, aNname);
		
	} // setOutputDeviceName()


	/**
	 * @param aValue
	 */
	public void setOutputVolume(int aValue) {
		logger.trace("setOutputVolume(): aValue = {}", aValue);
		
		defaultProperties.put(PTY_OUTPUT_VOLUME, Integer.toString(aValue));
		
	} //setOutputVolume(...)


	/**
	 * @param aName
	 */
	public void setTargetSystemName(String aName) {
		logger.trace("setTargetSystemName(): aName = {}", aName);
		
		defaultProperties.put(PTY_TARGET_SYSTEM, aName);
		
	} // setTargetSystemName()


	/**
	 * 
	 */
	@Override
	public void saveSetings() {
		logger.trace("saveSetings()");
		
		File file;
		FileOutputStream fos;
		
		try {

			file = new File(DEFAULT_PROPERTY_FILE);
			fos = new FileOutputStream(file);
			
			logger.debug("file: {}", file.toString());
			logger.debug("fos: {}", fos.toString());
			
			logger.debug("default properties: {}", defaultProperties.toString());
			
			defaultProperties.store(fos,
				" ------------------------------------------------------------------------------\r\n" + 
				"#\r\n" + 
				"#	Defaults\r\n" + 
				"#\r\n" 
			);

			fos.close();
			fos = null;
			file = null;

		} catch (FileNotFoundException e) {
			logger.error("Property file not found!", e);
		} catch (IOException e) {
			logger.error("Property file not accessible!", e);
		} finally {
			
		}
		
	} // saveSetings()


	/**
	 * @return
	 */
	@Override
	public boolean getAutosaveFlag() {
		logger.trace("getAutosaveFlag()");
		
		String autoSaveMode = (String)defaultProperties.getOrDefault(AUTOSAVE_FLAG, "false");	// NOSONAR
		logger.trace("AUTOSAVE_FLAG = {}", autoSaveMode);
		
		return Boolean.parseBoolean(autoSaveMode);
		
	} // getAutosaveFlag()


	/**
	 * @param saSate
	 */
	@Override
	public void setAutosaveFlag(Boolean aState) {
		logger.trace("setAutosaveFlag(): aState = {}", aState);
		
		defaultProperties.put(AUTOSAVE_FLAG, aState.toString());
		
	} // setAutosaveFlag()


} // ssalc
