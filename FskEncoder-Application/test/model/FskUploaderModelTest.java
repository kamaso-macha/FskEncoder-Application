/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : FskUploaderModelTest.java
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


package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

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
// Created at 2024-06-07 18:54:18

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FskUploaderModelTest {

	private static Logger LOGGER = null;
	
	private ApplicationResources appRes;
	
	private String defaultPropertyFileName		= "./test-cfg/FskEncoder.properties";
	private String extensionPropertyFileName	= "./test-cfg/Plugin.properties";

	private Properties defaultPropertiesMock;
	private Properties extensionPropertiesMock;
	
	private FskUploaderModel cut;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	    System.setProperty("log4j.configurationFile","./test-cfg/log4j2.xml");
		LOGGER = LogManager.getLogger();

	}


	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		LOGGER.info("setUp()");

		removePropertyFiles();
		
		appRes = new ApplicationResources();
		ApplicationResources.setPath("./test-cfg");
		LOGGER.debug("setUp() -> appRes = {}", appRes.toString());
		
	} // setUp()


	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		
	}

	
	
	/**
	 * Test method for {@link model.FskUploaderModel#FskUploaderModel()}.
	 */
	@Test
	@Order(1)
	final void testFskUploaderModel() {
		LOGGER.info("testFskUploaderModel()");

	
		try {
			
			// No property files available:
			createOrRenewCut();
			LOGGER.debug("defaultPropertiesMock	= {}, extensionPropertiesMock = {}", defaultPropertiesMock.toString(), extensionPropertiesMock.toString());

			verify(defaultPropertiesMock,   times(0)).load(any(Reader.class));
			verify(extensionPropertiesMock, times(0)).load(any(Reader.class));
			
			
			clearInvocations(defaultPropertiesMock);
			clearInvocations(extensionPropertiesMock);

			
			// Property files available:
			createPropertyFile(defaultPropertyFileName,   new String[] {"default   = test"});
			createPropertyFile(extensionPropertyFileName, new String[] {"extension = test"});

			LOGGER.debug("appRes: {}", appRes.toString());
			createOrRenewCut();
			LOGGER.debug("defaultPropertiesMock	= {}, extensionPropertiesMock = {}", defaultPropertiesMock.toString(), extensionPropertiesMock.toString());

			LOGGER.debug("defaultPropertyFile	= {}, extensionPropertyFile = {}", ApplicationResources.DEFAULT_PROPERTY_FILE(), ApplicationResources.EXTENSION_PROPERTY_FILE());
			cut.loadProperties();
			
			/*
			 * Don't know why the readers are invoked two times.
			 * In the source there is only one invocation ...
			 */
			verify(defaultPropertiesMock,   times(2)).load(any(Reader.class));
			verify(extensionPropertiesMock, times(2)).load(any(Reader.class));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} // testFskUploaderModel()


	/**
	 * Test method for {@link model.FskUploaderModel#getCurrentPosition()}.
	 */
	@Test
	final void testGetCurrentPosition() {
		LOGGER.info("testGetCurrentPosition()");

		// currently no position is stored and we receive '0.0' as default value

		FskUploaderModel localCut = new FskUploaderModel();	
		Point result = localCut.getCurrentPosition();
		
		assertEquals(0, (int)result.getX());
		assertEquals(0, (int)result.getY());
		
		// more tests on getPositions are done in setCurrentPosition test
		
	} // testGetCurrentPosition()


	/**
	 * Test method for {@link model.FskUploaderModel#getDefaultDirectory()}.
	 */
	@Test
	final void testGetDefaultDirectory() {
		LOGGER.info("testGetDefaultDirectory()");

		// currently no default directory is set up and we receive an empty string
		
		FskUploaderModel localCut = new FskUploaderModel();	
		String result = localCut.getDefaultDirectory();
		
		assertEquals("", result);
		
	} // testGetDefaultDirectory()


	/**
	 * Test method for {@link model.FskUploaderModel#getInputReaderProviderClassName(java.lang.String)}.
	 */
	@Test
	final void testGetInputReaderProviderClassName_Default() {
		LOGGER.info("testGetInputReaderProviderClassName_Default()");
		
		// currently there is no Input Reader defined and we receive NOT_DEFINED as result.
		FskUploaderModel localCut = new FskUploaderModel();	
		String result = localCut.getInputReaderProviderClassName("System Q");
		
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetInputReaderProviderClassName_Default()


	@Test
	final void testGetInputReaderProviderClassName() {
		LOGGER.info("testGetInputReaderProviderClassName()");
		
		createTestPropertyFile();
		
		FskUploaderModel localCut = new FskUploaderModel();

		String result = localCut.getInputReaderProviderClassName("System 1 A");
		assertEquals("source.ReaderA", result);

		result = localCut.getInputReaderProviderClassName("System 2");
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
		result = localCut.getInputReaderProviderClassName("System 3");
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetInputReaderProviderClassName()


	/**
	 * Test method for {@link model.FskUploaderModel#getKeyByValue(java.lang.String, java.lang.String, java.util.Properties)}.
	 */
	@Test
	final void testGetKeyByValue() {
		LOGGER.info("testGetKeyByValue()");

		createOrRenewCut();

		Properties properties = new Properties();
		
		properties.put("key.foo", "value_foo");
		properties.put("key.bar", "value_bar");
		properties.put("key.baz", "value_baz");
		
		String result = cut.getKeyByValue("value_bar", "bar", properties);
		
		assertEquals("key.bar", result);
		
		
	} // testGetKeyByValue()


	/**
	 * Test method for {@link model.FskUploaderModel#getOutputDeviceName()}.
	 */
	@Test
	final void testGetOutputDeviceName() {
		LOGGER.info("testGetOutputDeviceName()");

		// test the default value
		FskUploaderModel localCut = new FskUploaderModel();	
		String result = localCut.getOutputDeviceName();
		
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetOutputDeviceName()


	/**
	 * Test method for {@link model.FskUploaderModel#getOutputVolume()}.
	 */
	@Test
	final void testGetOutputVolume() {
		LOGGER.info("testGetOutputVolume()");

		// test the default value
		FskUploaderModel localCut = new FskUploaderModel();	
		int result = localCut.getOutputVolume();
		
		// this is the hardcoded default value
		assertEquals(Integer.parseInt(ApplicationResources.DEFAULT_OUTPUT_VOLUME), result);
		
		localCut.setOutputVolume(42);
		result = localCut.getOutputVolume();
		
		assertEquals(42, result);
		
	} // testGetOutputVolume()


	/**
	 * Test method for {@link model.FskUploaderModel#getTargetSystemName()}.
	 */
	@Test
	final void getTargetSystemName() {
		LOGGER.info("testGetTargetSystemName()");

		// test the default value
		FskUploaderModel localCut = new FskUploaderModel();	
		String result = localCut.getOutputDeviceName();
		
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetTargetSystemName()


	/**
	 * Test method for {@link model.FskUploaderModel#getTargetSystemNames()}.
	 */
	@Test
	final void testGetTargetSystemNames() {
		LOGGER.info("testGetTargetSystemNames()");
		
		String[] extensions = new String[] {
				"z80trainerIhx8.name			= SEL Z80-Trainer / IHX8",
				"z80trainerIhx8.provider		= target.z80trainer.Z80TrainerExtension",
				"z80trainerIhx8.inputFormat	= source.ihx.x8.Ihx8ReaderExtension",
				
				"z80trainerBin.name			= SEL Z80-Trainer / BIN",
				"z80trainerBin.provider		= target.z80trainer.Z80TrainerExtension",
				"z80trainerBin.inputFormat	= source.bin.BinReaderExtension",
				
				"mpf1Ihx8.name			= Multitech Microprofessor I / IHX8",
				"mpf1Ihx8.provider		= target.microprofessor1.Mpf1Extension",
				"mpf1Ihx8.inputFormat	= source.ihx.x8.Ihx8ReaderExtension"
				
		};

//		createPropertyFile(defaultPropertyFileName, new String[] {"default", "test"});
		createPropertyFile(extensionPropertyFileName, extensions);

		// test the default value
		ApplicationResources appres = new ApplicationResources();
		ApplicationResources.setPath("./test-cfg");
		
		FskUploaderModel localCut = new FskUploaderModel();	
		String[] result = localCut.getTargetSystemNames();
		
		for(String s : result) {
			
			LOGGER.info("target: '{}'", s);
			
		}
		
		assertEquals(3, result.length);
		
	} // testGetTargetSystemNames()


	/**
	 * Test method for {@link model.FskUploaderModel#getTargetSystemProviderClassName(java.lang.String)}.
	 */
	@Test
	final void testGetTargetSystemProviderClassName_Default() {
		LOGGER.info("testGetTargetSystemProviderClassName_Default()");

		// test the default value
		FskUploaderModel localCut = new FskUploaderModel();	
		String result = localCut.getTargetSystemProviderClassName("Test");
		
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetTargetSystemProviderClassName_Default()


	/**
	 * Test method for {@link model.FskUploaderModel#getTargetSystemProviderClassName(java.lang.String)}.
	 */
	@Test
	final void testGetTargetSystemProviderClassName() {
		LOGGER.info("testGetTargetSystemProviderClassName()");

		createTestPropertyFile();
		
		FskUploaderModel localCut = new FskUploaderModel();
		
		
		String result = localCut.getTargetSystemProviderClassName("System 1 A");
		assertEquals("target.System1", result);
		
		result = localCut.getTargetSystemProviderClassName("System 2");
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
		result = localCut.getTargetSystemProviderClassName("System 3");
		assertEquals(FskUploaderModel.NOT_DEFINED, result);
		
	} // testGetTargetSystemProviderClassName()


	/**
	 * Test method for {@link model.FskUploaderModel#loadProperties()}.
	 */
	@Test
	final void testLoadProperties() {
		LOGGER.info("testLoadProperties()");

		/*
		 * Tested by constructor test
		 */
		
		assertTrue(true);
		
	} // testLoadProperties()


	/**
	 * Test method for {@link model.FskUploaderModel#setCurrentPosition(java.awt.Point)}.
	 */
	@Test
	final void testSetCurrentPosition() {
		LOGGER.info("testSetCurrentPosition()");

		int x = 42;
		int y = 88;
		
		Point location = new Point(x, y); // x, y
		
		String resX = Integer.toString((int)location.getX());
		String resY = Integer.toString((int)location.getY());
		
		createOrRenewCut();
		cut.setCurrentPosition(location);
		
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.PTY_CURRENT_LOCATION_X), eq(resX));
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.PTY_CURRENT_LOCATION_Y), eq(resY));
		
		FskUploaderModel localCut = new FskUploaderModel();	
		localCut.setCurrentPosition(location);
		Point result = localCut.getCurrentPosition();
		
		assertEquals(location,  result);
		
	} // testSetCurrentPosition()


	/**
	 * Test method for {@link model.FskUploaderModel#setDefaultDirectory(java.lang.String)}.
	 */
	@Test
	final void testSetDefaultDirectory() {
		LOGGER.info("testSetDefaultDirectory()");

		String defDirectory = "/default/directory/name";
		
		createOrRenewCut();
		cut.setDefaultDirectory(defDirectory);
		
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.PTY_DEFAULT_DIRECTORY), eq(defDirectory));
		
		FskUploaderModel localCut = new FskUploaderModel();	
		localCut.setDefaultDirectory(defDirectory);
		String result = localCut.getDefaultDirectory();
		
		assertEquals(defDirectory,  result);
		
	} // testSetDefaultDirectory()


	/**
	 * Test method for {@link model.FskUploaderModel#setOutputDeviceName(java.lang.String)}.
	 */
	@Test
	final void testSetOutputDeviceName() {
		LOGGER.info("testSetOutputDeviceName()");

		String outDevName = "outputDevice";
		
		createOrRenewCut();
		cut.setOutputDeviceName(outDevName);
		
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.PTY_OUTPUT_DEVICE), eq(outDevName));
		
		FskUploaderModel localCut = new FskUploaderModel();	
		localCut.setOutputDeviceName(outDevName);
		String result = localCut.getOutputDeviceName();
		
		assertEquals(outDevName,  result);
		
	} // testSetOutputDeviceName()


	/**
	 * Test method for {@link model.FskUploaderModel#getOutputVolume()}.
	 */
	@Test
	final void testSetOutputVolume() {
		LOGGER.info("testSetOutputVolume()");

		/*
		 * Implicit tested by testGetOutputVolume()
		 */
		
	} // testSetOutputVolume()


	/**
	 * Test method for {@link model.FskUploaderModel#setTargetSystemName(java.lang.String)}.
	 */
	@Test
	final void testSetTargetSystemName() {
		LOGGER.info("testSetTargetSystemName()");

		String targetSysName = "targetSystem";
		
		createOrRenewCut();
		cut.setTargetSystemName(targetSysName);
		
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.PTY_TARGET_SYSTEM), eq(targetSysName));
		
		FskUploaderModel localCut = new FskUploaderModel();	
		localCut.setTargetSystemName(targetSysName);
		String result = localCut.getTargetSystemName();
		
		assertEquals(targetSysName,  result);
		
	} // testSetTargetSystemName()


	/**
	 * Test method for {@link model.FskUploaderModel#saveSetings()}.
	 */
	@Test
	final void testSaveSetings() {
		LOGGER.info("testSaveSetings()");

		try {

			createOrRenewCut();
			cut.saveSetings();
			
			verify(defaultPropertiesMock, times(1)).store(any(OutputStream.class), any(String.class));
			
		} catch (IOException e) {
			LOGGER.error("IO exception while saving properties: ", e);
		}
		
	} // testSaveSetings()


	/**
	 * Test method for {@link model.FskUploaderModel#getAutosaveFlag()}.
	 */
	@Test
	final void testGetAutosaveFlag() {
		LOGGER.info("testGetAutosaveFlag()");

		// test the default value
		FskUploaderModel localCut = new FskUploaderModel();	
		boolean result = localCut.getAutosaveFlag();
		
		assertEquals(false, result);
		
	} // testGetAutosaveFlag()


	/**
	 * Test method for {@link model.FskUploaderModel#setAutosaveFlag(java.lang.Boolean)}.
	 */
	@Test
	final void testSetAutosaveFlag() {
		LOGGER.info("testSetAutosaveFlag()");

		boolean asFlag = true;
		
		createOrRenewCut();
		cut.setAutosaveFlag(asFlag);
		
		verify(defaultPropertiesMock, times(1)).put(eq(FskUploaderModel.AUTOSAVE_FLAG), eq(Boolean.toString(asFlag)));
		
		FskUploaderModel localCut = new FskUploaderModel();	
		localCut.setAutosaveFlag(asFlag);
		boolean result = localCut.getAutosaveFlag();
		
		assertEquals(asFlag,  result);

	} // testSetAutosaveFlag()


	//##################################################################################################################
	
	
	/**
	 * 
	 */
	protected void createOrRenewCut() {
		LOGGER.info("createOrRenewCut()");
		
		try(
				
			MockedConstruction<Properties> mcProperties = mockConstruction(Properties.class);
				
			MockedStatic<ApplicationResources> appProps = mockStatic(ApplicationResources.class)
			
		)
		{
			
			appProps.when(ApplicationResources::DEFAULT_PROPERTY_FILE).thenReturn(defaultPropertyFileName);
			appProps.when(ApplicationResources::EXTENSION_PROPERTY_FILE).thenReturn(extensionPropertyFileName);
			
			cut = new FskUploaderModel();
			assertTrue(cut != null);
			
			assertEquals(2, mcProperties.constructed().size());
			
			defaultPropertiesMock	= mcProperties.constructed().get(0);
			extensionPropertiesMock	= mcProperties.constructed().get(1);
			
			assertTrue(defaultPropertiesMock != null);
			assertTrue(extensionPropertiesMock != null);
			assertTrue(defaultPropertiesMock != extensionPropertiesMock);
			
		} // yrt
		
	} // createOrRenewCut()


	private void createPropertyFile(final String aFileName, final String[] aPropertyList) {
		LOGGER.info("createPropertyFile()");
	
		try {

			File file = new File(aFileName);
			FileOutputStream fos = new FileOutputStream(file);
			
			Properties properties = new Properties();
			
			
			String[] kv = null;
			
			for(String p : aPropertyList) {
				
				kv = p.split("=");
				LOGGER.trace("kv: '{}' -> '{}'", kv[0], kv[1]);

				properties.put(kv[0].replaceAll("\t", ""), kv[1].replaceAll("\t", "").stripLeading());
			}
			
			properties.store(fos,
				" ------------------------------------------------------------------------------\r\n" + 
				"#\r\n" + 
				"#	<type>\r\n" + 
				"#\r\n" 
			);
			
			properties = null;
			
			fos.flush();
			fos.close();
			
		} catch (FileNotFoundException e) {
			LOGGER.error("Property file not found!", e);
		} catch (IOException e) {
			LOGGER.error("Property file not accessible!", e);
		}
		
	} // createPropertyFile()
	

	private void createTestPropertyFile() {
		LOGGER.info("createTestPropertyFile()");
	
		try {

			File file = new File(extensionPropertyFileName);
			FileOutputStream fos = new FileOutputStream(file);
			
			Properties properties = new Properties();
			
			properties.put("system1a.name",			"System 1 A");
			properties.put("system1a.provider",		"target.System1");
			properties.put("system1a.inputFormat",	"source.ReaderA");

			properties.put("system1b.name",			"System 1 B");
			properties.put("system1b.provider",		"target.System1");
			properties.put("system1b.inputFormat",	"source.ReaderB");

			properties.put("system2.name",			"System 2");
			
			properties.store(fos, "Test properties");
			
			properties = null;
			
			fos.flush();
			fos.close();
			
		} catch (FileNotFoundException e) {
			LOGGER.error("Property file not found!", e);
		} catch (IOException e) {
			LOGGER.error("Property file not accessible!", e);
		}
		
	} // createTestPropertyFile()
	

	/**
	 * 
	 */
	private void removePropertyFiles() {
		LOGGER.info("removePropertyFiles()");
		
		try {

			LOGGER.info("deleting file {}", defaultPropertyFileName);
			new File(defaultPropertyFileName).delete();
			LOGGER.info("defaultPropertyFileName existing: {}",  new File(defaultPropertyFileName).exists());

		} catch (Exception e) {
			LOGGER.error("Can't delete file {}, reason:", defaultPropertyFileName, e);
		}

		try {
	
			LOGGER.info("deleting file {}", extensionPropertyFileName);
			new File(extensionPropertyFileName).delete();
			LOGGER.info("extensionPropertyFileName existing: {}",  new File(extensionPropertyFileName).exists());
			
//			Path path = Paths.get(extensionPropertyFileName);
//			Files.delete(path);
	
		} catch (Exception e) {
			LOGGER.error("Can't delete file {}, reason:", extensionPropertyFileName, e);
		}
		
	} // removePropertyFiles()


} // ssalc
