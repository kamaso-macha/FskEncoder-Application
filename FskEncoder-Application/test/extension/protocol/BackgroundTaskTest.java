/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BackgroundTaskTest.java
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


package extension.protocol;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import extension.control.BackgroundExecutor;
import extension.control.StatusListener;

/**
 * Responsibilities:<br>
 * Functional testing of class BackgroundTask<T, V>. 
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Verifies all methods for correct functionality.
 * 
 * <p>
 * @author Stefan
 *
 */

class BackgroundTaskTest {

	private static Logger LOGGER = null;
	

	/**
	 *		Implementation of abstract test candidate. 
	 */
	
	class TestBackgroudTask<T, V> extends BackgroundTask<T, V> {

		@Override public T runBackgroundTask() { return null; }
		
		public void setRunflag(boolean aRunFlag) { isRunning = aRunFlag; }
		public PropertyChangeListener getPropertyChangeListener() { return propertyChangeListener; }
		
	} // ssalc
	
	
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
	 * Test method for {@link extension.protocol.BackgroundTask#BackgroundTask()}.
	 */
	@Test
	final void testBackgroundTask() {
		LOGGER.info("testBackgroundTask()");

		/*
		 * 		Nothing to test currently
		 */
		
		assertTrue(true);
		
	} // testBackgroundTask()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#addPropertyChangeListener(java.beans.PropertyChangeListener)}.
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void testAddPropertyChangeListener() {
		LOGGER.info("testAddPropertyChangeListener()");

		PropertyChangeListener propertyChangeListenerMock = mock(PropertyChangeListener.class);

		try(
				
			MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
				
		) {
			
			TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
			
			// test 1: NO PropertyChangeListener
			cut.execute();
			
			assertEquals(1, mcBackgroundExecutork.constructed().size());
			
			BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);
			
			verify(backgroundExecutorMock, times(1)).addPropertyChangeListener(null);
			verify(backgroundExecutorMock, times(1)).execute();
			
			
			// test 2: WITH PropertyChangeListener
			cut.addPropertyChangeListener(propertyChangeListenerMock);
			cut.execute();
			
			assertEquals(2, mcBackgroundExecutork.constructed().size());
			backgroundExecutorMock = mcBackgroundExecutork.constructed().get(1);

			verify(backgroundExecutorMock, times(1)).addPropertyChangeListener(propertyChangeListenerMock);
			
		} // yrt
		
	} // testAddPropertyChangeListener()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#runBackgroundTask()}.
	 */
	@Test
	final void testRunBackgroundTask() {
		LOGGER.info("testRunBackgroundTask()");

		/*
		 * 		Nothing to test here!
		 */
		
		assertTrue(true);
		
	} // testRunBackgroundTask()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#cancel(boolean)}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void testCancel() {
		LOGGER.info("testCancel()");

		try(
				
			MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
				
		) {
			
			TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
			
			cut.execute();
			
			assertEquals(1, mcBackgroundExecutork.constructed().size());
			BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);
			
			cut.cancel(true);
			verify(backgroundExecutorMock, times(1)).cancel(true);
			
		} // yrt
		
	} // testCancel()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#done()}.
	 */
	@Test
	final void testDone() {
		LOGGER.info("testDone()");

		StatusListener statusListenerMock = mock(StatusListener.class);
		
		TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
		
		cut.execute();			
		cut.registerStatusListener(statusListenerMock);
		
		cut.done();
		verify(statusListenerMock, times(1)).done();
			
	} // testDone()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#execute()}.
	 */
	@Test
	final void testExecute() {
		LOGGER.info("testExecute()");

		/*
		 * 		Implicitly tested by other tests.
		 */
		
		assertTrue(true);
		
	} // testExecute()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#getProgress()}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void testGetProgress() {
		LOGGER.info("testGetProgress()");

		try(
				
			MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
				
		) {
			
			TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
			
			cut.execute();
			
			assertEquals(1, mcBackgroundExecutork.constructed().size());			
			BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);
			
			/*
			 * NOTE:
			 * backgroundExecutor.getProgress() is called twice:
			 * 	1. in the logging statement at method entry and
			 * 	2. on the target itself.
			 */
			cut.getProgress();
			verify(backgroundExecutorMock, times(2)).getProgress();
			
		} // yrt
		
	} // testGetProgress()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#isDone()}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void testIsDone() {
		LOGGER.info("testIsDone()");

		try(
				
			MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
				
		) {
			
			TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
			
			cut.execute();
			
			assertEquals(1, mcBackgroundExecutork.constructed().size());			
			BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);
			
			/*
			 * NOTE:
			 * backgroundExecutor.isDone() is called twice:
			 * 	1. in the logging statement at method entry and
			 * 	2. on the target itself.
			 */
			cut.isDone();
			verify(backgroundExecutorMock, times(2)).isDone();
			
		} // yrt
		
	} // testIsDone()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#isCancelled()}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void testIsCancelled() {
		LOGGER.info("testIsCancelled()");

		try(
				
			MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
				
		) {
			
			TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
			
			cut.execute();
			
			assertEquals(1, mcBackgroundExecutork.constructed().size());			
			BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);
			
			/*
			 * NOTE:
			 * backgroundExecutor.isCancelled() is called twice:
			 * 	1. in the logging statement at method entry and
			 * 	2. on the target itself.
			 */
			cut.isCancelled();
			verify(backgroundExecutorMock, times(2)).isCancelled();
			
		} // yrt
		
	} // testIsCancelled()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#registerStatusListener(extension.control.StatusListener)}.
	 */
	@Test
	final void testRegisterStatusListener() {
		LOGGER.info("testRegisterStatusListener()");

		StatusListener statusListenerMock = mock(StatusListener.class);
		
		TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
		cut.execute();			

		cut.done();
		verify(statusListenerMock, times(0)).done();

		cut.registerStatusListener(statusListenerMock);
		
		cut.done();
		verify(statusListenerMock, times(1)).done();
			
	} // testRegisterStatusListener()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#isRunning()}.
	 */
	@Test
	final void testIsRunning() {
		LOGGER.info("testIsRunning()");

		TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
		
		assertEquals(false, cut.isRunning());
		
		cut.setRunflag(true);
		assertEquals(true, cut.isRunning());
		
	} // testIsRunning()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#stop()}.
	 */
	@Test
	final void testStepOn() {
		LOGGER.info("testStepOn()");

		try(
				
				@SuppressWarnings("rawtypes")
				MockedConstruction<BackgroundExecutor> mcBackgroundExecutork = mockConstruction(BackgroundExecutor.class);
					
			) {
				
				TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
				
				cut.stepOn(0);
				assertEquals(0, mcBackgroundExecutork.constructed().size());			

				cut.execute();
				assertEquals(1, mcBackgroundExecutork.constructed().size());			
				@SuppressWarnings("unchecked")
				BackgroundExecutor<Void, Void> backgroundExecutorMock = mcBackgroundExecutork.constructed().get(0);

				cut.stepOn(0);
				verify(backgroundExecutorMock, times(1)).stepOn(0);
				
			} // yrt

	} // testStepOn()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#stop()}.
	 */
	@Test
	final void testStop() {
		LOGGER.info("testStop()");

		TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
		cut.setRunflag(true);
		assertEquals(true, cut.isRunning());

		cut.stop();
		assertEquals(false, cut.isRunning());

	} // testStop()


	/**
	 * Test method for {@link extension.protocol.BackgroundTask#toString()}.
	 */
	@Test
	final void testToString() {
		LOGGER.info("testToString()");

		TestBackgroudTask<Void, Void> cut = new TestBackgroudTask<Void, Void>();
		String result = cut.toString();

		LOGGER.info(result);
		
		assertEquals(
			"BackgroundTask [backgroundExecutor=null, isRunning=false]" 
			, result
		);
		
	} // testToString()


} // ssalc
