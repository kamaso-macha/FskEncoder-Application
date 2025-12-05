/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ApplicationResourcesTest.java
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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
// Created at 2025-10-25 16:35:37

class ApplicationResourcesTest {

	private static Logger LOGGER = null;
	
	private static ApplicationResources applicationResources;
	

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
		
		applicationResources = new ApplicationResources();
		
		LOGGER.info("applicationResources id: {}", System.identityHashCode(applicationResources)); 
		LOGGER.info("applicationResources : {}", ApplicationResources.asString()); 
		
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		
		ApplicationResources.setPath(null);
		applicationResources = null;
		
	}

	
	
	/**
	 * Test method for {@link application.ApplicationResources#ApplicationResources()}.
	 */
	@Test
	final void testApplicationResources() {
		LOGGER.info("testApplicationResources()");

		/*
		 * Nothing to test yet
		 */
		
		assertTrue(true);
				
	} // testApplicationResources()

	
	/**
	 * Test method for {@link application.ApplicationResources#setPath(java.lang.String)}.
	 */
	@Test
	final void testSetPath() {
		LOGGER.info("testSetPath()");

		/*
		 * Implicit tested by testDEFAULT_PROPERTY_FILE 
		 * and testEXTENSION_PROPERTY_FILE. 
		 */
		
		assertTrue(true);
				
	} // testSetPath()
	

	/**
	 * Test method for {@link application.ApplicationResources#DEFAULT_PROPERTY_FILE()}.
	 */
	@Test
	final void testDEFAULT_PROPERTY_FILE() {
		LOGGER.info("testDEFAULT_PROPERTY_FILE()");

		assertTrue(applicationResources != null);
		assertEquals("null/FskEncoder.properties", ApplicationResources.DEFAULT_PROPERTY_FILE());
		
		ApplicationResources.setPath("./foo/bar");
		assertEquals("./foo/bar/FskEncoder.properties", ApplicationResources.DEFAULT_PROPERTY_FILE());
				
	} // testDEFAULT_PROPERTY_FILE()
	

	/**
	 * Test method for {@link application.ApplicationResources#EXTENSION_PROPERTY_FILE()}.
	 */
	@Test
	final void testEXTENSION_PROPERTY_FILE() {
		LOGGER.info("testEXTENSION_PROPERTY_FILE()");

		assertTrue(applicationResources != null);
		assertEquals("null/Plugin.properties", ApplicationResources.EXTENSION_PROPERTY_FILE());
		
		ApplicationResources.setPath("./foo/bar");
		assertEquals("./foo/bar/Plugin.properties", ApplicationResources.EXTENSION_PROPERTY_FILE());
				
	} // testEXTENSION_PROPERTY_FILE()
	

	/**
	 * Test method for {@link application.ApplicationResources#asString()}.
	 */
//	@Test
	final void testAsString() {
		LOGGER.info("testAsString()");

		LOGGER.info("asString: {}", applicationResources.asString());

		assertTrue(
			applicationResources.asString().matches(
				"ApplicationResources [hashCode()=.*, null, null/FskEncoder.properties, null/Extension.properties]"
			)
		);
				
	} // testAsString()
	

	/**
	 * Test method for {@link application.ApplicationResources#getHashCode()}.
	 */
//	@Test
	final void testGetHashCode() {
		LOGGER.info("testGetHashCode()");

		fail("Not yet implemented"); // TODO
				
	} // testGetHashCode()
	

	/**
	 * Test method for {@link application.ApplicationResources#hashCode(byte[])}.
	 */
//	@Test
	final void testHashCodeByteArray() {
		LOGGER.info("testHashCodeByteArray()");

		fail("Not yet implemented"); // TODO
				
	} // testHashCodeByteArray()

	
} // ssalc
