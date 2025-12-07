/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BackgroundTaskProtokol.java
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

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.Encoder;
import extension.execution.BackgroundTask;

/**
 * Responsibilities:<br>
 * Provide the functionality for background execution with SwingWorker.
 * 
 * <p>
 * Collaborators:<br>
 * BackgroundTask and derived classes.
 * 
 * <p>
 * Description:<br>
 * This is the base class of all protocol implementations.
 * <br>
 * It provides common used attributes and methods as well as some 
 * abstract methods which are required for a proper work.
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class BackgroundTaskProtokol extends BackgroundTask<Void, Void> implements Protocol {
	
	private Logger logger = LogManager.getLogger(BackgroundTaskProtokol.class.getName());

	protected ChecksumCalculator checksumCalculator;
	protected Encoder encoder;

	protected ByteBuffer dataBuffer;
	protected ByteBuffer soundsampleBuffer;
	protected int soundSampleBufferSize;
	
	protected int fullProgress;
	protected int currentProgress;

	
	/**
	 * Returns the buffer holding the sound samples.
	 * 
	 * @return
	 * A ByteBuffer filled with the sound samples.
	 * @throws IllegalAccessException 
	 */
	public ByteBuffer getSoundSampleBuffer() throws IllegalAccessException {
		
		if(soundsampleBuffer == null) throw new IllegalAccessError("Nothing has been compiled so far.");
		return soundsampleBuffer; 
		
	} // getSoundSampleBuffer()


	/**
	 * This method is invoked on background execution but can also be used in situations where no background execution is applied.
	 * <br>
	 * It starts the compilation of the sound samples and set the result ready to be retrieved.
	 * <br>
	 * Make sure to call setDataBuffer(...) before invocation.
	 * 
	 */
	@Override
	public Void runBackgroundTask() {
		logger.trace("runBackgroundTask(): dataBuffer = {}", dataBuffer);
		
		if(dataBuffer == null) throw new IllegalAccessError("No data buffer set!");

		soundsampleBuffer = compile(dataBuffer);
		logger.trace("runBackgroundTask(): soundsampleBuffer = {}", soundsampleBuffer);
		
		return null;
		
	} // runBackgroundTask()
	
	
	/**
	 * Direct invocation of the compilation process.
	 * <p>
	 * NOTE:
	 * <br>
	 * This method can be set to protected in further releases of the project.<br>
	 * So any call the this method should be avoided and executed shall be used instead. 
	 */
	public abstract ByteBuffer compile(final ByteBuffer aDataBuffer);

	
	/**
	 * It calculates the over all progress on each call and should update the progress bar
	 * with a call to the method progressIndicator.stepProgress(...)
	 * 
	 * @param aStep <br>
	 * The amount of progress achieved since the last invocation.
	 */
	protected void progress(final int aStep) {
		logger.trace("progress(): aStep = {}", aStep);

		currentProgress += aStep;
		
		int progress = (currentProgress * 100) /fullProgress;
		
		logger.trace("fullProgress = {}, currentProgress =  {}, progress = {}", 
				fullProgress, currentProgress, progress);

		stepOn(Math.min(progress, 100));
			
	} // progress()
	
	
	/**
	 * Set the byte buffer to be used as input for the compilation process.
	 * 
	 * @param aDataBuffer
	 * A ByteBuffer holding the data bytes to be compiled.
	 */
	public void setDataBuffer(final ByteBuffer aDataBuffer) {
		
		if(aDataBuffer == null) throw new IllegalArgumentException("aDataBuffer can't be null!");
		dataBuffer = aDataBuffer;
		
	} // setDataBuffer()


	public void setEndAddress(long endAddress) {
		throw new IllegalAccessError("Unsupported method");	// NOSONAR
	}

	
	/**
	 * Set the field fullProgress which marks the maximum progress (100%). 
	 * The progress is calculated by the formula currentProgress * 100 / fullProgress
	 * 
	 * @param final int aBufferSize 
	 * size of the data buffer
	 */
	protected abstract void setFullProgress(final int aBufferSize);
	
	
	public void setSize(long startAddress) {
		throw new IllegalAccessError("Unsupported method");	// NOSONAR
	}


	/**
	 * @param startAddress
	 */
	public void setStartAddress(long startAddress) {
		throw new IllegalAccessError("Unsupported method");	// NOSONAR
	}
	
	
	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "BackgroundTaskProtokol [checksumCalculator=" + checksumCalculator + ", encoder=" + encoder
				+ ", dataBuffer=" + dataBuffer + ", soundsampleBuffer=" + soundsampleBuffer + ", soundSampleBufferSize="
				+ soundSampleBufferSize + ", isRunning=" + isRunning + ", currentProgress=" + currentProgress 
				+ ", " + super.toString()
				+ "]";
	}


} // ssalc
