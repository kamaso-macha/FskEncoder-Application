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
 * 	bits in to their sound samples.
 * 
 * <p>
 * Collaborators:<br>
 * 	WaveEncoder
 * 
 * 
 * <p>
 * Description:<br>
 * 	Abstract base class for all types of bit encoders.<br>
 * 	It acts as interface to the higher order encoder classes.
 * 	<p>
 * 	It's up to the implementation of a concrete bit encoder how a single bit - 0 or 1 bit -
 * 	is encoded into sound samples.<br>
 * 	Therefore the derived class has to implement the two methods
 * 	<ul>
 * 	  <li>ByteBuffer encodeLowBit(final int aCount)</li>
 * 	  <li>ByteBuffer encodeHighBit(final int aCount)</li>
 * 	</ul>
 * 	Examples how this can be done - also for a bit more complex implementation -
 * 	can be found in class target.z80trainer.FskBitEncoder (simple style) and target.microprofessor1.Mpf1BitEncoder.
 * <p>
 * @author Stefan
 *
 */

public abstract class BitEncoder {
	
	private static Logger logger = LogManager.getLogger(BitEncoder.class.getName());
	
	
	protected WaveCycleEncoder waveCycleEncoder;
	
	
	/**
	 * Constructor
	 * 
	 * @param aSamplingRate<br>
	 * The sampling rate to be used on creating wave samples. <br> 
	 * Must be greater or equal 4 times of the highest frequency used for sound samples.
	 * 
	 * @throws IllegalArgumentException<br>
	 * If sampling rate isn't greater 0.
	 */
	public BitEncoder(final WaveCycleEncoder aWaveCycleEncoder) {
		
		logger.trace("BitEncoder(): aSamplingRate = {}", aWaveCycleEncoder);
		
		if(aWaveCycleEncoder == null) throw new IllegalArgumentException("aWaveCycleEncoder can't be null!");
		
		waveCycleEncoder = aWaveCycleEncoder; 
		
	} // BitEncoder(...)
	
	
	/**
	 * Encodes aCount bits with the value given in aBitValue and returns the sound samples in a ByteBuffer.
	 * 
	 * @param aBitValue<br>
	 * The value of the bit to be encoded into sound samples.  It can be either 0 or 1.<br>
	 * Any other values are rejected.
	 * 
	 * @param aCount<br>
	 * Number if bits (with the same value) to be encoded.  It must be greater than 0.<br>
	 * Any other values are rejected.
	 * 
	 * @return<br>
	 * A byteBuffer containing the sound samples for the encoded bit(s).<p>
	 * The position attribute of the byte buffer must be reset to 0 by the concrete implementation!
	 * 
	 * @throws IllegalArgumentException<br>
	 * If aCount is less or equal 0 or if aBitValue is not in the range between 0 and 1.
	 * 
	 */
	public ByteBuffer encode(final int aBitValue, final int aCount) {
		logger.trace("encode(): aBitValue = {}, aCount = {}", aBitValue, aCount);
		
		if(aCount <= 0) throw new IllegalArgumentException("aCount must be greater than 0!");
		
		switch(aBitValue) {
		
		case 0:	return encodeLowBit(aCount);
		case 1: return encodeHighBit(aCount);
		
		default: throw new IllegalArgumentException("aBitValue must be in range 0 ... 1!");
		
		} // hctiws
		
	} // encode(...)
	
	
	public abstract int getSampleBufferSize();

	
    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "BitEncoder [waveCycleEncoder=" + waveCycleEncoder + "]";
	}


	protected abstract ByteBuffer encodeLowBit(final int aCount);
	protected abstract ByteBuffer encodeHighBit(final int aCount);

	
} // class

