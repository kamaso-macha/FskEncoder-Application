/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : BackgroundExecutor.java
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


package extension.control;

import javax.swing.SwingWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.protocol.BackgroundTask;

/**
 * Responsibilities:<br>
 * Build the interface between Swing background execution and the application
 * 
 * <p>
 * Collaborators:<br>
 * BackgroundTask and it's subclasses
 * 
 * <p>
 * Description:<br>
 * BackgroundExecutor extends SwingWorker and acts as interface between BackgroundTask and Swing.<br>
 * <p>
 * BackgroundTask creates an instance of this class in the execute() method. After that, it invokes the 
 * execute() method of this class, which itself is inherited from SwingWorker.<br>
 * SwingWorker.execute() calls doInBackground() of this class. 
 * doInBackground() invokes BackgroundTask.runBackgroundTask() to start the background execution of the requested task.
 * <p>
 * NOTE:<br>
 * For each background execution a new instance of this class must be created.<br>
 * Please refer to Java SwingWorker documentation for more and detailed information.
 * <p>
 * @author Stefan
 * @param <T>
 * @param <V>
 *
 */

public class BackgroundExecutor<T, V> extends SwingWorker<T, V> {

	private Logger logger = LogManager.getLogger(BackgroundExecutor.class.getName());
	
	protected BackgroundTask<T, V> backgroundTask;
	
	
	public BackgroundExecutor(BackgroundTask<T, V> aBackgroundTask) {
		logger.trace("BackgroundExecutor(): aBackgroundTask = {}", aBackgroundTask);
		
		backgroundTask = aBackgroundTask;
		
	} // BackgroundExecutor(...)

	
	/**
	 * This method is called from SwingWorker after invocation of it's execute() method.
	 */
	@Override
	protected T doInBackground() throws Exception {
		logger.trace("doInBackground()");
		
		return (T) backgroundTask.runBackgroundTask();
		
	} // doInBackground() 
	
	
	/**
	 * Proxy method to signal done state to the background task.
	 */
	@Override
	public void done() {
		logger.trace("done()");

		backgroundTask.done();
		
	} // done()
	
	
	/**
	 * Drive a progress bar - if any is attached.
	 * 
	 * @param aProgress 
	 * a progress value to set. 0 <= aProgress <= 100.
	 */
	public void stepOn(int aProgress) {
		logger.trace("stepOn(): aProgress = {}", aProgress);
		
		setProgress(aProgress);
		
	} // stepOn()
	
	
	/**
	 * Returns a string representation of the current state of this object.
	 * 
	 * @return
	 * : a string representation of the object.
	 */
	@Override
	public String toString() {
		return "BackgroundExecutor []";
	}


} // ssalc
