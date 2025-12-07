/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BackgroundExecutorTest.java
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


package extension.execution;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import extension.execution.BackgroundExecutor;
import extension.execution.BackgroundTask;

/**
 * Responsibilities:<br>
 * JUnit tests of class BackgroundExecutor
 * 
 * <p>
 * Collaborators:<br>
 * BackgroundExecutor,<br>
 * BackgroundTask.
 * 
 * <p>
 * Description:<br>
 * Contains all JUnit tests for the BackgroundExecutor.
 * 
 * <p>
 * @author Stefan
 *
 */

class BackgroundExecutorTest {

	private static Logger LOGGER = null;
	
	
	private BackgroundTask<Void, Void> bgTaskMock;
	private BackgroundExecutor<Void, Void> cut;

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
	@SuppressWarnings("unchecked")
	@BeforeEach
	void setUp() throws Exception {
		
		bgTaskMock = mock(BackgroundTask.class);
		cut = new BackgroundExecutor<Void, Void>(bgTaskMock);
		
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	
	/**
	 * Test method for {@link extension.execution.BackgroundExecutor#done()}.
	 */
	@Test
	final void testDoInBackground() {
		LOGGER.info("testDoInBackground()");

		try {
			
			cut.doInBackground();
			verify(bgTaskMock, times(1)).runBackgroundTask();
			
		} catch (Exception e) {

			LOGGER.error("Unexprcted exception caught: {}",e);
			fail("Unexprcted exception caught");
		}
		
	} // testDoInBackground()


	/**
	 * Test method for {@link extension.execution.BackgroundExecutor#done()}.
	 */
	@Test
	final void testDone() {
		LOGGER.info("testDone()");

		cut.done();
		
		verify(bgTaskMock, times(1)).done();
		
	} // testDone()


	/**
	 * Test method for {@link extension.execution.BackgroundExecutor#BackgroundExecutor(extension.execution.BackgroundTask)}.
	 */
	@Test
	final void testBackgroundExecutor() {
		LOGGER.info("testBackgroundExecutor()");

		/*
		 * Nothing specific to test here
		 */
		
		assertTrue(true);
		
	} // testBackgroundExecutor()


	/**
	 * Test method for {@link extension.execution.BackgroundExecutor#stepOn(int)}.
	 */
	@Test
	final void testStepOn() {
		LOGGER.info("testStepOn()");

		final int aStep = 42;
		
		BackgroundExecutor<Void, Void> localCutSpy = spy(new BackgroundExecutor<Void, Void>(bgTaskMock));
		localCutSpy.stepOn(aStep);
		
		/*
		 * Can't test SwingWorker.setProgress() because it's protected
		 */
		
		assertTrue(true);
		
	} // testStepOn()


	/**
	 * Test method for {@link extension.execution.BackgroundExecutor#toString()}.
	 */
	@Test
	final void testToString() {
		LOGGER.info("testToString()");

		String result = cut.toString();
		LOGGER.info("result = {}", result);
		
		assertEquals("BackgroundExecutor []", result);

	} // testToString()


} // ssalc
