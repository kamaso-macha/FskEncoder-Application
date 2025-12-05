/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : WaveCycle.java
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
 *	Responsibilities:<br>
 *		Encodes a number of wave cycles of frequency f with the sampling rate
 *		given in constructor call.<br>
 *		<br>
 *		The encoded wave cycle(s) are stored as sound samples in a buffer and returned.<br>
 *		<p>
 *
 *	Collaborators:<br>
 *		None<br>
 *		<p>
 *
 *	Description:<br>
 *	
 * 		Encodes a number of full wave cycles to sound samples of full signal strength 
 * 		(+127 ... 0 ... -127).
 * 		<p>
 * 		Every buffer starts with a '0' value (zero-crossing of the wave) and ends with 
 * 		the last non-zero sample before the closing zero-crossing of the wave.  It's size is
 * 		calculated by sampleRate/aFrequency.
 * 		<p<>
 * 		Example of one full wave cycle:
 * 		<p>
 * 		Sampling rate = 16000 samples / second  <br>
 * 		frequency     =  2000 Hz (1 / second) <br>
 * 		buffer size   = 16000/2000 = 8 samples
 * 		buffer content: 0, 89, 127, 89, 0, -89, -127, -89
 * 		<p>
 * 		Sampling rate = 16000 samples / second  <br>
 * 		frequency     =  1000 Hz (1 / second) <br>
 * 		buffer size   = 16000/1000 = 16 samples.
 * 		buffer content: 0, 48, 89, 117, 127, 117, 89, 48, 0, -48, -89, -117, -127, -117, -89, -48
 * 		<p>
 *		At construction time the desired sampling rate is given as constructor parameter.
 *		Sampling rate is checked against and rejected on values less or equal 0.
 *		<p>
 *		A call of encode(...) method starts the creation of sound samples which are 
 *		(in respect to the chosen sampling rate) are more or less sine waves.
 *		<p>
 * 		A trailing '0' byte should be appended as the very last byte of a fully assembled 
 * 		sound buffer while nothing must be appended between two subsequent compilation steps.
 *  
 *<p>
 * @author Stefan
 * 
 */

public class WaveCycleEncoder {
	
	private Logger logger = LogManager.getLogger(WaveCycleEncoder.class.getName());
	
	
	protected final int samplingRate;
	
	/**
	 * 
	 * @param int aSampleRate<br>
	 * A value which defines the desired sampling rate.<br>
	 * This value must be <u>at least 4 times of the maximum frequency</u> which has to be encoded
	 * and should always be a multiple of the highest frequency to encode.<br>
	 * Good values are 8 or 16 times f-max for a acceptable compromise between harmonic distortion
	 * and buffer size.
	 * <p>
	 * A later call to encode() method verifies that the desired sampling rate is applicable on 
	 * the frequency to encode. 
	 * 
	 * @throws: IllegalArgumentException if the sampling rate is less or equal 0.
	 * 
	 */
	public WaveCycleEncoder(final int aSamplingRate) {
		
		logger.trace("WaveCycle(): aSamplingRate = {}", aSamplingRate);
		
		if(aSamplingRate <= 0) throw new IllegalArgumentException("aSamplingRate must be greater than 0");
		
		samplingRate = aSamplingRate;
		
	} // WaveCycle(...)
	
	
	/**
	 * Encodes the requested number of wave cycles of specified frequency to sound samples.
	 * <p>
	 * @param aFrequency<br>
	 * The frequency to encode as sound samples.
	 * 
	 * @param aCycleCount<br>
	 * Number of full wave cycles to be encoded.
	 * 
	 * @return<br>
	 * A ByteBuffer containing the created sound samples.
	 * 
	 * @throws IllegalArgumentException if the sampling rate is less than 4 times of aFrequency
	 * 
	 */
	public ByteBuffer encode(final int aFrequency, final int aCycleCount) {
		
		logger.debug("encode(): freq = {}, aCycleCount = {}, sampleRate = {} ",aFrequency, aCycleCount, samplingRate);
		
		if(samplingRate < (4 * aFrequency)) throw new IllegalArgumentException("Given sample rate is not suitable for the requested frequency!");
		
		int samples = (samplingRate/aFrequency);
		int buffSize = samples * aCycleCount;
		
		ByteBuffer result = ByteBuffer.allocate(buffSize);
		logger.debug("samples / cycle = {}, buffer.capacity = {}, buffer.position = {}", samples, result.capacity(), result.position());
		
		byte value = 0;
		
		for(int n = 0; n < aCycleCount; n++) {

			for (int i = 0; i < samples; i++) {
	
				double angle = 2.0 * Math.PI * i / samples;
				value = (byte) (Math.sin(angle) * 127f);
	
				result.put(value);
	
				logger.trace("n: {}, i: {}, angle: {}, value: {}", n, i, angle, value);
				
			} // one cycle
		
		} // for(cycleCount)
		
		logger.debug("buffer.capacity = {}, buffer.position = {}", result.capacity(), result.position());
		result.position(0);
		return result;
		
	} // encode(...)


    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "WaveCycle [samplingRate=" + samplingRate + "]";
	}


} // class

