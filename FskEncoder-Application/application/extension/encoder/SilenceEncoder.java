/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : SilenceEncoder.java
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

package extension.encoder;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Creates sound samples needed to encode the requested duration of silence.
 * The sound samples contain all the integer value 0.
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Given the sampling rate and requested duration in milliseconds the number of samples is calculated
 * with the formula <br>
 * (sampling rate [smpl/s] / 1000) * duration [ms].<br>
 * 
 * Teh created samples are returned in a ByteBuffer.
 * 
 * <p>
 * @author Stefan
 *
 */

public class SilenceEncoder {

	private Logger logger = LogManager.getLogger(SilenceEncoder.class.getName());
	
	protected int samplingRate;
	
	/**
	 * @param aSamplingRate
	 * The sampling rate to be used.
	 */
	public SilenceEncoder(int aSamplingRate) {
		
		logger.trace("SilenceEncoder(): aSamplingRate = {}", aSamplingRate);
		
		
		if(aSamplingRate <= 0) throw new IllegalArgumentException("aSamplingRate must be greater than 0!");
		if((aSamplingRate / 1000) < 1) throw new IllegalArgumentException("aSamplingRate / 1000 must be greater or equal 1!");
		
		samplingRate = aSamplingRate; 
		
	}
	
	
	/**
	 * Creates the required amount of samples to fill aDuration seconds with silence.
	 * 
	 * @param aDuration
	 * Lengths of silence in number of milliseconds.
	 * 
	 * @return
	 * A ByteBuffer containing the samples with pointer set to 0.
	 * 
	 */
	public ByteBuffer encode(final int aDuration) {
		logger.trace("encode(): aDuration = {}", aDuration);

		if(aDuration <= 0) throw new IllegalArgumentException("aDuration must be greater than 0!");

		int size = (samplingRate / 1000) * aDuration;
		
		ByteBuffer buffer = ByteBuffer.allocate(size);
		
		for(int n = 0; n < size; n++) {
			buffer.put((byte) 0);
		}
		
		logger.debug("buffer.limit = {}, buffer.position = {}", buffer.limit(), buffer.position());
		
		buffer.flip();
		logger.debug("buffer.limit = {}, buffer.position = {}", buffer.limit(), buffer.position());
		
		return buffer;
		
	} // encode


    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "SilenceEncoder [samplingRate=" + samplingRate + "]";
	}


} // class
