/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BitEncoder.java
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

/**
 * @author Stefan
 *
 */
package extension.encoder;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Responsibilities:<br>
 * 	Provides the common usable basic functionality for encoding a number of
 * 	bits in to their corresponding sound samples.
 * 
 * <p>
 * Collaborators:<br>
 * 	Superclass BitEncoder.
 * 
 * 
 * <p>
 * Description:<br>
 * 	This class provides the encodeLowBit and encodeHighBit methods which are required for the base class
 * 	BitEncoder.<br>
 * 	It also set a abstract stub method preparesampleBuffer which must be implemented in the derived subclass.
 * <p>
 * 	
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class FullCycleBitEncoder extends BitEncoder {
	
	private static Logger logger = LogManager.getLogger(FullCycleBitEncoder.class.getName());
	
	protected ByteBuffer lowBitSamples;
	protected ByteBuffer highBitSamples;
	
	
	/**
	 * Constructor.
	 * 
	 * @param aWaveCycleEncoder
	 * The desired sampling rate. It's not used locally but transferred to the superclass.
	 * 
	 */
	public FullCycleBitEncoder(final WaveCycleEncoder aWaveCycleEncoder) {
		super(aWaveCycleEncoder);
		
		/*
		 * Error handling is done in super class!
		 */
		
		logger.trace("FullCycleBitEncoder(): aWaveCycleEncoder = {}", aWaveCycleEncoder);
		
	} // BitEncoder(...)
	
	
	/**
	 * Returns the size of the sample buffer which is returned by encode() method.
	 * 
	 * @return the maximum buffer size needed to encode the lower frequency.
	 */
	public int getSampleBufferSize() {
		
		return Math.max(lowBitSamples.limit(), highBitSamples.limit());
		
	} // getSampleBufferSize()

	
	/**
	 * Does the encoding of a number of low bits (0) by copying the 
	 * earlier prepared sample buffer n times to the output buffer.
	 *  
	 * @param aCount
	 * Number of bits to encode
	 * 
	 * @return A ByteBuffer holding the requested sound samples.
	 * NOTE: The position attribute of the byte buffer is rewound to 0.
	 * 
	 */	
	protected ByteBuffer encodeLowBit(final int aCount) {
		logger.trace("encodeLowBit(): aCount = {}", aCount);
		
		return fillSampleBuffer(lowBitSamples, aCount);
		
	} // encodeLowBit()


	/**
	 * Does the encoding of a number of high bits (1) by copying the 
	 * earlier prepared sample buffer n times to the output buffer.
	 *  
	 * @param aCount
	 * Number of bits to encode
	 * 
	 * @return A ByteBuffer holding the requested sound samples.
	 * NOTE: The position attribute of the byte buffer is rewound to 0.
	 * 
	 */	
	protected ByteBuffer encodeHighBit(final int aCount) {
		logger.trace("encodeHighBit(): aCount = {}", aCount);
		
		return fillSampleBuffer(highBitSamples, aCount);
		
	} // encodeHighBit()
	
	
	/**
	 * Helper method who does the hard work.
	 * 
	 * @param aCount
	 * Number of bits to encode
	 * 
	 * @return A ByteBuffer holding the requested sound samples.
	 * NOTE: The position attribute of the byte buffer is rewound to 0.
	 */
	protected ByteBuffer fillSampleBuffer(final ByteBuffer aBuffer, final int aCount) {
		logger.trace("fillSampleBuffer():aBuffer.position = {}, aCount = {}", aBuffer.position(), aCount);

		int size = aCount * aBuffer.capacity();
		ByteBuffer buffer = ByteBuffer.allocate(size);
		
		for(int n = 0; n < aCount; n++) {
			logger.debug("buffer = {}, aBuffer = {}", buffer, aBuffer);
			aBuffer.rewind();
			buffer.put(aBuffer);
		}
		
		logger.debug("buffer = {}", buffer);
				
		return buffer;
		
	} // fillSampleBuffer(...)
	
	
    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "FullCycleBitEncoder [lowBitSamples=" + lowBitSamples + ", highBitSamples=" + highBitSamples 
				+ ", " + super.toString()
				+ "]";
	}


	/**
	 * Support function to prepare the required sound samples.<br>
	 * MUST be implemented in the specialized subclass.
	 * <p>
	 * This method MUST be called in the constructor of the specific subclass! 
	 */
	protected abstract void prepareSampleBuffers();

		
} // class

