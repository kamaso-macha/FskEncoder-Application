/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Z80Trainer.java
 *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Extensions
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

package target.z80trainer;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.BitEncoder;
import extension.encoder.BitOrder;
import extension.encoder.BitValue;
import extension.encoder.ByteOrder;
import extension.encoder.Encoder;
import extension.encoder.FskBitEncoder;
import extension.encoder.SilenceEncoder;
import extension.encoder.WaveCycleEncoder;
import extension.protocol.BackgroundTaskProtokol;
import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Encode a data buffer into Z80 Trainer specific sound samples.
 * 
 * 
 * <p>
 * Collaborators:<br>
 * <ul>
 * 	<li>DefaultChecksumCalculator</li>
 * 	<li>SilenceEncoder</li>
 * 	<li>WaveCycleEncoder</li>
 * 	<li>Encoder</li>
 * </ul>
 * 
 * <p>
 * Description:<br>
 * Implements the Z80 Trainer specific protocol required to convert a data buffer
 * into the correct sound samples that can be loaded into the target system. 
 * 
 * Provides the common functionality for both versions: TermIOS and Monitor.
 * 
 * @author Stefan
 *
 */

public abstract class Z80TrainerProtocolBase extends BackgroundTaskProtokol {

	private Logger logger = LogManager.getLogger(Z80TrainerProtocolBase.class.getName());
	
	/**
	 */
	
	/*
	 * FSK and envelope parameter definition
	 */
	protected final int F_LOW;
	protected final int F_HIGH;
	
	protected static final int NBR_START_BITS			=	1;
	protected static final BitValue START_BIT_VALUE		=	BitValue.LOW;
	
	protected final int NBR_STOP_BITS;
	protected static final BitValue STOP_BIT_VALUE		=	BitValue.HIGH;
	
	protected final int SAMPLING_RATE;

	protected final ByteOrder BYTE_ORDER;
	
	/*
	 * Common file structure
	 */
	protected static final int SILENCE_BLOCK		= 500;
	
	protected static final int LEAD_IN				= 12288;	// bits
	
	protected static final int MEASURE_0			= 1;		// bit
	protected static final int MEASURE_1			= 16;		// bits
	
	protected boolean haveProgramNbr = false;
	
	protected int startAdr;
	protected boolean haveStartAdr = false;
	
	
	/**
	 * Constructor.
	 */
	public Z80TrainerProtocolBase(Z80TrainerProtocolParameterDao aZ80TrainerProtocolParameterDao) {
		
		logger.trace("Z80Trainer(): aZ80TrainerProtocolParameterDao: {}", aZ80TrainerProtocolParameterDao);

		if(aZ80TrainerProtocolParameterDao == null)	throw new IllegalArgumentException("aZ80TrainerProtocolParameterDao can't be null.");
		
		F_LOW	= aZ80TrainerProtocolParameterDao.F_LOW;
		F_HIGH	= aZ80TrainerProtocolParameterDao.F_HIGH;
		
		SAMPLING_RATE	= F_HIGH * 8;
		
		NBR_STOP_BITS	= aZ80TrainerProtocolParameterDao.NBR_STOP_BITS;
		
		BYTE_ORDER		= aZ80TrainerProtocolParameterDao.BYTE_ORDER;
		
		checksumCalculator	= aZ80TrainerProtocolParameterDao.checksumCalculator;
		
		SilenceEncoder silenceEncoder = new SilenceEncoder(SAMPLING_RATE);
		WaveCycleEncoder waveCycleEncoder = new WaveCycleEncoder(SAMPLING_RATE);
		BitEncoder bitEncoder = new FskBitEncoder(F_LOW, F_HIGH, waveCycleEncoder);		

		encoder = new Encoder(bitEncoder, BYTE_ORDER, BitOrder.LSB_MSB)
			.withSilenceEncoder(silenceEncoder)
			.withWaveCycleEncoder(waveCycleEncoder)
			.withStartBits(NBR_START_BITS, START_BIT_VALUE)
			.withStopBits(NBR_STOP_BITS, STOP_BIT_VALUE)
			;
		
	} // Z80Trainer()

	
	/**
	 * Set the start address of the data block.
	 * 
	 * @param aStartAdr 
	 * The startAdr to be set
	 */
	@Override
	public void setStartAddress(long aStartAdr) {
		
		startAdr = (int)aStartAdr & 0x0FFFF;
		haveStartAdr = true;
		
	} // setStartAddress()


	/**
	 * Set the end address of the data block.
	 * 
	 * NOTE:<br>
	 * For a Z80 protocol this method does NOTHING because the Z80 protocol
	 * didn't need a end address.
	 * <br>
	 * BUT it must be implemented for the sake of completeness.
	 * 
	 * @param aEndAdr 
	 * This parameter is never used in Z80 protocol implementation.
	 */
	@Override
	public void setEndAddress(long aEndAdr) { /* empty */ }


	/**
	 * Set the program number that originally identified a unique block of data on tape.<br>
	 * 
	 * This application needs a program number for protocol purposes only.
	 * It can easily be set to a default value, e.g. 1. 
	 * 
	 * @param programNbr 
	 * The programNbr to set
	 */
	
//	public void setProgramNbr(int aProgrammNbr) { 
//		
//		programNbr = aProgrammNbr;
//		haveProgramNbr = true;
//		
//	}

	
	/**
	 * Starts the translation of the given data buffer by compiling common parts.
	 * 
	 * Translation is performed according to the defined protocol.
	 * A sound sample buffer is filled with all protocol elements and data and then returned.
	 * 
	 */
	public boolean compileCommonStart(final ByteBuffer aDataBuffer) {
		logger.trace("compileCommonStart(): aDataBuffer = {}", aDataBuffer);
		
		if(!haveProgramNbr) {
			logger.error("ERROR: aProgramNbr not set!");
			throw new IllegalAccessError("aProgramNbr not set!");
		}
		if(!haveStartAdr) {
			logger.error("ERROR: aStartAdr not set!");
			throw new IllegalAccessError("aStartAdr not set!");
		}
		
		isRunning = true;
		currentProgress = 0;
		setFullProgress(aDataBuffer.limit());
		
		calculateBufferSize(aDataBuffer);
		encoder.setBufferSize(soundSampleBufferSize);
		
		silence();
		progress(SILENCE_BLOCK);
		if(!isRunning) return false;
		
		leadIn();
		progress(LEAD_IN);
		if(!isRunning) return false;
		
		syncPatern();
		progress(MEASURE_0 + MEASURE_1);
		if(!isRunning) return false;
		
		return true;
		
	} // compileCommonStart()
	
	
	/**
	 * Does some common encodings at the end of compilation.
	 * 
	 * Translation is performed according to the defined protocol.
	 * A sound sample buffer is filled with all protocol elements and data and then returned.
	 * 
	 */
	public void compileCommonEnd() {
		logger.trace("compileCommonEnd()");
		
		silence();
		progress(SILENCE_BLOCK);
		
	} // compileCommonEnd()
	
	
	/*
	 * Calculates the amount of sound samples needed.
	 * 
	 * The encoder knows the size of the sound samples and provides a method to obtain that value.
	 * 
	 * Each protocol element is now multiplied by the number of items used for their encoding.
	 * Lastly a sum is calculated over all protocol elements representing the maximum number of 
	 * sound samples.
	 * 
	 */
	protected abstract void calculateBufferSize(final ByteBuffer aDataBuffer);	

	/*
	 * 
	 * The following helper methods are all self-explanatory and are not intended to be commented on in detail.
	 * 
	 * 
	 */
	
	
	protected void dataBlock(final ByteBuffer aDataBuffer) throws IllegalAccessException {
		logger.trace("dataBlock(): aDataBuffer = {}", aDataBuffer);
		
		encoder.encodeByteBuffer(aDataBuffer, true);
		
		aDataBuffer.rewind();
		
		for(int n = 0; n < aDataBuffer.limit(); n++) {
			checksumCalculator.sumUp((aDataBuffer.get(n) & 0x00FF));			
		} // rof
		
	} //dataBlock()


	protected void leadIn() {
		logger.trace("leadIn()");
		
		encoder.encodeBit((byte) 1, LEAD_IN);
		
	} //leadIn()


	protected void silence() {
		logger.trace("silence()");
		
		encoder.encodeSilence(SILENCE_BLOCK);
		
	} //silence()


	protected abstract void startAddress() throws IllegalAccessException;
	

	protected void syncPatern() {
		logger.trace("syncPatern()");
		
		encoder.encodeBit((byte) 0, 1);
		encoder.encodeBit((byte) 1, 16);
		
	} //syncPatern()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		
//		return "Z80TrainerProtocolBase [F_LOW=" + F_LOW + ", F_HIGH=" + F_HIGH + ", NBR_STOP_BITS=" + NBR_STOP_BITS
//				+ ", SAMPLING_RATE=" + SAMPLING_RATE + ", programNbr=" + programNbr + ", haveProgramNbr="
//				+ haveProgramNbr + ", startAdr=" + startAdr + ", haveStartAdr=" + haveStartAdr + ", "
//				+ super.toString()
//				+ "]";
		
		return "Z80TrainerProtocolBase [F_LOW=" + F_LOW + ", F_HIGH=" + F_HIGH + ", NBR_STOP_BITS=" + NBR_STOP_BITS
				+ ", SAMPLING_RATE=" + SAMPLING_RATE + ", haveProgramNbr="
				+ haveProgramNbr + ", startAdr=" + startAdr + ", haveStartAdr=" + haveStartAdr + ", "
				+ super.toString()
				+ "]";

	}// toString()


	/**
	 * Returns the audio format used for this protocol.
	 * 
	 * @return FskAudioFormat 
	 * The used audio format
	 */
	public FskAudioFormat getAudioFormat() { return new FskAudioFormat(SAMPLING_RATE); }


	/**
	 * @param programNumber
	 */
	public abstract void setProgramNbr(final int aProgramNumber);
	public abstract void setProgramNbr(final String aProgramName);


} // class
