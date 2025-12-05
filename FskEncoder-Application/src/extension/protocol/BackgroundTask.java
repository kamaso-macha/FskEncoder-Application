/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BackgroundTask.java
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


package extension.protocol;

import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.control.BackgroundExecutor;
import extension.control.StatusListener;


/**
 * Responsibilities:<br>
 * Handle interaction with the BackgroundExecutor,<br>
 * control the run-state of the task,<br>
 * handle the Progress status.
 * 
 * <p>
 * Collaborators:<br>
 * BackgroundExecutor,<br>
 * PropertyChangeListener.
 * 
 * <p>
 * Description:<br>
 * Provides basic functionality for all task implementations which should run as Swing background task. 
 * The background execution itself is handled by the BackgroundExecutor.
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class BackgroundTask<T, V> {
	
	private Logger logger = LogManager.getLogger(BackgroundTask.class.getName());

	private BackgroundExecutor<T, ?> backgroundExecutor;
	protected StatusListener statusListener;
	protected PropertyChangeListener propertyChangeListener;

	protected boolean isRunning;

	
	public BackgroundTask() {
		logger.trace("BackgroundTask()");
		
	} // BackgroundTask()
	
	
	/**
	 * 
	 * If the running task likes to drive a progress bar, then a PropertyChangeListener is needed.
	 * This property change listener is later on set on the backgroundExecutor.
	 * 
	 * @param aListener
	 * A object which implements the PropertyChangeListener interface.
	 * 
	 */
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
		logger.trace("addPropertyChangeListener(): aListener = {}", aListener);
		
		propertyChangeListener = aListener;
		
	} // addPropertyChangeListener()
	
	
	/**
	 * 
	 * This method is invoked on background execution but can also be used in situations where no background execution is applied.
	 * <br>
	 * It must be implemented in the derived sub-class to trigger all required actions.
	 * 
	 * @return An instance of the type T
	 */
	public abstract T runBackgroundTask();

	
	/**
	 * 
	 * A call to cancel() is transferred forward to the backgroundExecutor and requests the 
	 * cancellation of the currently running background task - if any is running.	 
	 *  
	 * @param mayInterruptIfRunning 
	 * if true tries to interrupt the background execution,<br
	 * if false no action is taken.
	 * 
	 * @return
	 * true if action was successful<br>
	 * false otherwise.
	 * 
	 */
	public boolean cancel(boolean mayInterruptIfRunning) {
		logger.trace("cancel()");
		
		return backgroundExecutor.cancel(mayInterruptIfRunning);
		
	} // cancel()
	
	
	/**
	 * 
	 * Informs a configured status listener that the execution of the background task is done.
	 * 
	 */
	public void done() {
		logger.trace("done()");

		if(statusListener != null) {
			statusListener.done();
		}
		
	} // done()
	
	
	/**
	 * Starts the background execution of the task.
	 * 
	 */
	public void execute() {
		logger.trace("execute()");
		
		backgroundExecutor = new BackgroundExecutor<>(this);
		backgroundExecutor.addPropertyChangeListener(propertyChangeListener);

		backgroundExecutor.execute();
		
	} // execute()

	
	/**
	 * 
	 * Method to read explicitly the current progress value/state.
	 * Returns the progress bound property.
	 *
	 * @return
	 * the progress bound property from SwingWorker.
	 * 
	 */
	public int getProgress() {
		logger.trace("getProgress(): {}", backgroundExecutor.getProgress());
		
		return backgroundExecutor.getProgress();
		
	} // getProgress()
	
	
	/**
	 * 
	 * Returns true if this task completed.Completion may be due to normal termination, 
	 * an exception, or cancellation -- in all of these cases, this method will return true.
	 * 
	 * @return
	 * true if this task has completed.
	 * 
	 */
	public boolean isDone() {
		logger.trace("isDone(): {}", backgroundExecutor.isDone());

		return backgroundExecutor.isDone();
		
	} // isDone()


	/**
	 * 
	 * Returns true if this task was cancelled before it completednormally.
	 * 
	 * @return
	 * true if this task was cancelled before it completed
	 * 
	 */
	public boolean isCancelled() {
		logger.trace("isCancelled(): {}", backgroundExecutor.isCancelled());

		return backgroundExecutor.isCancelled();
		
	} // isCancelled()
	
	
	/**
	 * Used to register a StatusListener.
	 * 
	 * @param aStatusListener
	 * the StatusListener instance to register.
	 * 
	 */
	public void registerStatusListener(StatusListener aStatusListener) {
		logger.trace("registerStatusListener(): aCallBack = {}", aStatusListener);
		
		statusListener = aStatusListener;
		
	} // registerStatusListener()
	
	
	/**
	 * 
	 * Query to get the current run-state.
	 * 
	 * @return
	 * The current run-state:<br>
	 * true if the task is running, false otherwise.
	 * 
	 */
	public boolean isRunning() {
		logger.trace("isRunning(): isRunning = {}", isRunning);
	
		return isRunning;
		
	} // isRunning()
	
	
	/**
	 * 
	 * Method to drive the progress bar.
	 * A call to this method is relayed to backgroundExecutor.stepOn().
	 * 
	 * @param aStep
	 * : a value to step on, 0 <= aStep <= 100
	 */
	protected void stepOn(int aStep) {
		logger.trace("stepOn(): aStep = {}", aStep);
		
		if(backgroundExecutor != null)
			backgroundExecutor.stepOn(aStep);
		
	} // stepOn()
	
	
	/**
	 * 
	 * Set the isRunning flag to false and forces the task to exit prematurely.
	 * 
	 */
	public void stop() {
		logger.trace("stop()");
		
		// See
		// https://stackoverflow.com/questions/21236289/swingworker-cancel-button-doesnt-work
		// https://docs.oracle.com/javase/tutorial/uiswing/concurrency/cancel.html
		
		isRunning = false;
		
	} // stop()


	/**
	 * Returns a string representation of the current state of this object.
	 * 
	 * @return
	 * : a string representation of the object.
	 */
	@Override
	public String toString() {
		
		return "BackgroundTask [backgroundExecutor=" + backgroundExecutor 
				+ ", isRunning=" + isRunning 
				+ "]";
		
	} // toString()
	
	
} // ssalc
