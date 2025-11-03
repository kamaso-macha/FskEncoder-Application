/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : PlugInFactoryTest.java
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import control.WorkflowEngine;
import extension.control.StatusMessenger;
import extension.factory.PlugInFactory;
import application.support.TestInputReaderExtension;
import application.support.TestTargetSystemExtension;

/**
 * Responsibilities:<br>
 * JUnit test of the PlugInFactory
 * 
 * <p>
 * Collaborators:<br>
 * PlugInFactory
 * 
 * <p>
 * Description:<br>
 * Verifies the correct behavior of the PlugInFactory class.
 * 
 * <p>
 * @author Stefan
 *
 */

class PlugInFactoryTest {

	private static Logger LOGGER = null;
	
	protected StatusMessenger wfeMock = mock(WorkflowEngine.class);

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link extension.factory.PlugInFactory#getTargetSystemExtensionFactory(java.lang.String)}.
	 */	
	@Test
	final void testGetTargetSystemExtensionFactory() {
		LOGGER.info("testGetTargetSystemExtensionFactory()");
		
		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			
			PlugInFactory.getTargetSystemExtensionFactory(null);
			
		});
		assertEquals("aProviderClassName cant be null", thrown.getMessage());
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			
			PlugInFactory.getTargetSystemExtensionFactory(" ");
			
		});
		assertEquals("aProviderClassName cant be blank", thrown.getMessage());
		
		
		try {
			
			TestTargetSystemExtension result = (TestTargetSystemExtension) PlugInFactory.getTargetSystemExtensionFactory("application.support.TestTargetSystemExtension");
			
			assertTrue(result != null);
			LOGGER.info("result = {}", (result));
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
			
		} // yrt
		
	} // testGetTargetSystemExtensionFactory()


	/**
	 * Test method for {@link extension.factory.PlugInFactory#getInputReaderExtensionFactory(java.lang.String)}.
	 */
	@Test
	final void testGetInputReaderExtensionFactory() {
		LOGGER.info("testGetInputReaderExtensionFactory()");

		IllegalArgumentException thrown;
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			
			PlugInFactory.getInputReaderExtensionFactory(null);
			
		});
		assertEquals("aProviderClassName cant be null", thrown.getMessage());
		
		thrown = assertThrows(IllegalArgumentException.class, () -> {
			
			PlugInFactory.getInputReaderExtensionFactory(" ");
			
		});
		assertEquals("aProviderClassName cant be blank", thrown.getMessage());
		
		
		try {
			
			TestInputReaderExtension result = (TestInputReaderExtension) PlugInFactory.getInputReaderExtensionFactory("application.support.TestInputReaderExtension");
			
			assertTrue(result != null);
			LOGGER.info("result = {}", (result));
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			
			LOGGER.error("Unexpected exception caught: {}", e);
			fail("Unexpected exception caught: " + e);
			
		} // yrt
		
	} // testGetInputReaderExtensionFactory()


} // ssalc
