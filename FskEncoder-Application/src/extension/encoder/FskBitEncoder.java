/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : DualFrequencyBitencoder.java
 *
 * PURPOSE       : Implementation of a FSK encoder. 
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

package extension.encoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * 	Encode a single 0 or 1 bit into their corresponding sound samples.<br>
 * 	Store the created sound samples in the byte buffers for low and high bit.<br>
 * 
 * <p>
 * Collaborators:<br>
 * 	Superclass FullCycleBitEncoder and BitEncoder
 * 
 * <p>
 * Description:<br>
 * 	Provides the sound samples needed to encode a single bit as a full wave cycle with
 * 	separate frequencies for each bit value as defined for FSK encoding.<br>
 * 	Therefore it uses the wave cycle encoder of the topmost base class BitEncoder which is called
 * 	with the necessary parameters for sampling rate and frequency.
 * <br>
 * 	The created sound samples are stored in two byte buffers, one for each bit value.
 * 
 * <p>
 * @author Stefan
 *
 */

public class FskBitEncoder extends FullCycleBitEncoder {
	
	private final Logger logger = LogManager.getLogger(FskBitEncoder.class.getName());
	
	protected int lowBitFrequency;
	protected int highBitFrequency;

	/**
	 * Depending of the required encoding, the low frequency can be used for either 0 or 1 value of a bit.<br>
	 * The high frequency represents always the encoding of the second bit value.
	 * 
	 * @param aLowBitFrerquency
	 * The specific frequency to be used for the LOW bit value. 
	 * 
	 * @param aHighBitFrequency
	 * The specific frequency to be used for the HIGH bit value.
	 * 
	 * @param aWaveCycleEncoder
	 * The sampling rate to be used for encoding.<br>
	 * NOTE: <br>
	 * <i>The sampling rate MUST be greater or equal to 4 * the higher frequency of the both mentioned above!<br>
	 * Sampling rates beyond that value are rejected when the encoding of the sound samples for aHighBitFrequency starts.</i>
	 * 
	 */
	public FskBitEncoder(final int aLowBitFrerquency, final int aHighBitFrequency, final WaveCycleEncoder aWaveCycleEncoder) {
		super(aWaveCycleEncoder);
		
		logger.trace("FskBitEncoder(): aLowBitFrerquency = {}, aHighBitFrequency = {}, aWaveCycleEncoder = {}", aLowBitFrerquency, aHighBitFrequency, aWaveCycleEncoder);
		
		if(aLowBitFrerquency == aHighBitFrequency) throw new IllegalArgumentException("aLowBitFrerquency can't be equal to aHighBitFrequency!");
		if(aLowBitFrerquency <= 0) throw new IllegalArgumentException("aLowBitFrerquency must be greater 0!");
		if(aHighBitFrequency <= 0) throw new IllegalArgumentException("aHighBitFrequency must be greater 0!");
		
		lowBitFrequency  = aLowBitFrerquency;
		highBitFrequency = aHighBitFrequency;
		
		prepareSampleBuffers();

	} // DualFrequencyBitencoder(...)


    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "FskBitEncoder [lowBitFrequency=" + lowBitFrequency + ", highBitFrequency=" + highBitFrequency + "]";
	}


	/**
	 * This method prepares the sound samples which are used later on to encode the bits.
	 */
	@Override
	protected void prepareSampleBuffers() {
		logger.trace("prepareSampleBuffers()");

		logger.trace("prepareSampleBuffers(): lowBitFrequency = {}, highBitFrequency = {}, waveCycleEncoder = {}", 
			lowBitFrequency, highBitFrequency, waveCycleEncoder);
		
		super.lowBitSamples  = super.waveCycleEncoder.encode(lowBitFrequency, 1);
		logger.trace("prepareSampleBuffers(): lowBitSamples = {}", lowBitSamples);

		super.highBitSamples = super.waveCycleEncoder.encode(highBitFrequency, 1);
		logger.trace("prepareSampleBuffers(): highBitSamples = {}", highBitSamples);
		
	} // prepareSampleBuffers()

	
} // class
