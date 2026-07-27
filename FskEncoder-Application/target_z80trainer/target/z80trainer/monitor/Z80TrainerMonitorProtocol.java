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

package target.z80trainer.monitor;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.BitValue;
import extension.encoder.ByteOrder;
import extension.protocol.Modulo256ChecksumCalculator;
import target.z80trainer.Z80TrainerProtocolBase;
import target.z80trainer.Z80TrainerProtocolParameterDao;

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
 * Implements the Z80 TGrainer specific protocol required to convert a data buffer
 * into the correct sound samples that can be loaded into the target system. 
 * 
 * <blockquote><tt><pre>
    Bit format
    --------------------------------------------------------------------------------
    
    '0'   1 cycle   568,18Hz (1760µs)
    '1'   1 cycle  1136,36Hz ( 880µs)

    
    Envelope (Byte format)
    --------------------------------------------------------------------------------
    
      1 start bit '0'
      8 data  bits, lsb first (b0 to b7)
      3 stop  bit '1'
    
    
    File format
    --------------------------------------------------------------------------------
    
     1.    12'288   bit '1'   Lead sync                         ->  leadIn
     
     2.         1   bit '0'   Measurement                       ->  syncPatern
     3.        16   bit '1'     for period length               +>
        
     4.         1   envlp     Program number - high byte       ->  programNumber
     5.         1   envlp     Program number - low byte        +>
              
     6.         1   envlp     Start address - high byte         ->  startAddress
     7.         1   envlp     Start address - low byte          +>
     8.         1   envlp     Start address checksum            +>
              
     6.         1   envlp     Data block length - high byte     ->  dataBlockLength
     7.         1   envlp     Data block length - low byte      +>
     8.         1   envlp     Data block length checksum        +>
        
     9.        16   bit '1'   Idle time for chksum calculation  ->  idleTime
        
    10.         n   envlp     Data block                        ->  dataBlock
    
    11.         1   envlp     Data block checksum               +>

 * </pre></tt></blockquote>
 * <p>
 * @author Stefan
 *
 */

public class Z80TrainerMonitorProtocol extends Z80TrainerProtocolBase {

	private Logger logger = LogManager.getLogger(Z80TrainerMonitorProtocol.class.getName());
	
	/**
	 */
	
	/*
	 * FSK and envelope parameter definition
	 */
	protected static final int F_LOW 					=    568;
	protected static final int F_HIGH					=	1136;
	
	protected static final int NBR_START_BITS			=	1;
	protected static final BitValue START_BIT_VALUE		=	BitValue.LOW;
	
	protected static final int NBR_STOP_BITS			=	3;
	protected static final BitValue STOP_BIT_VALUE		=	BitValue.HIGH;
	
	protected static final int SAMPLING_RATE			=	F_HIGH * 8;

	/*
	 * Structure of a complete file for upload
	 */
	
	protected static final int PRG_NBR_SIZE			= 2;		// bytes
	
	protected static final int START_ADR			= 1;		// word
	protected static final int START_ADR_CKS		= 1;		// byte
	
	protected static final int BLK_LEN				= 1;		// word
	protected static final int BLK_LEN_CKS			= 1;		// byte
	
	protected static final int CKS_IDLE_TIME		= 64;		// bits
	
	// 						byte[] data				= n 		// bytes TX-frames
	
	protected static final int CK_SUM				= 1;		// byte
	

	protected int programNbr;

	
	/**
	 * Constructor.
	 */
	public Z80TrainerMonitorProtocol() {
		super(
			new Z80TrainerProtocolParameterDao(
				F_LOW, F_HIGH, NBR_STOP_BITS, 
				ByteOrder.BIG_ENDIAN,
				new Modulo256ChecksumCalculator()
			)
		);
		
		logger.trace("Z80TrainerMonitorProtocol()");
		
	} // Z80TrainerMonitorProtocol()

	
	@Override
	public void setProgramNbr(final int aProgrammNbr) { 
	
		programNbr = aProgrammNbr;
		haveProgramNbr = true;
		
	} // setProgramNbr()

	

	@Override
	public void setProgramNbr(final String aProgramName) { 
		
		throw new IllegalAccessError("Method not supported.");
		
	} // setProgramNbr()

	

	/**
	 * Starts the translation of the given data buffer and returns the generated sound samples.
	 * 
	 * Translation is performed according to the defined protocol.
	 * A sound sample buffer is filled with all protocol elements and data and then returned.
	 * 
	 */
	@Override
	public ByteBuffer compile(final ByteBuffer aDataBuffer) {
		logger.trace("compile(): aDataBuffer = {}", aDataBuffer);
		
		try {

			if(!compileCommonStart(aDataBuffer))
				return null;
			
			programNumber();
			progress(PRG_NBR_SIZE);
			if(!isRunning) return null;
			
			startAddress();
			progress(START_ADR + START_ADR_CKS);
			if(!isRunning) return null;
			
			dataBlockLength(aDataBuffer);
			progress(BLK_LEN + BLK_LEN_CKS);
			if(!isRunning) return null;
			
			idleTime();
			progress(CKS_IDLE_TIME);
			if(!isRunning) return null;
			
			checksumCalculator.clear();

			dataBlock(aDataBuffer);
			progress(aDataBuffer.limit());
			if(!isRunning) return null;
			
			checkSum();
			progress(CK_SUM);
			if(!isRunning) return null;

			compileCommonEnd();
		}
		catch(IllegalAccessException e) {
			logger.error("Unexpected exception caught:", e);
		}
		
		return encoder.getSampleBuffer();
		
	} // compile()


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
	protected void calculateBufferSize(final ByteBuffer aDataBuffer) {
		logger.trace("calculateBufferSize(): aDataBuffer = {}", aDataBuffer);
		
		int sampleSize = encoder.getSampleSize();
		int envelopeSize = 
				(NBR_START_BITS * sampleSize) 
			  + (8 * sampleSize) 
			  + (NBR_STOP_BITS * sampleSize)
			  ;
		
		/*
		 * For a better readability a tabular form is used.
		 */
		soundSampleBufferSize
			// item					mult	samples
			= SILENCE_BLOCK				* 1		* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			+ LEAD_IN				* 1		* sampleSize	// bits	
			+ MEASURE_0				* 1		* sampleSize	// bits
			+ MEASURE_1				* 1		* sampleSize	// bits
			+ PRG_NBR_SIZE				* 2		* envelopeSize	// word
			+ START_ADR				* 2		* envelopeSize	// word
			+ START_ADR_CKS			* 1		* envelopeSize	// bytes
			+ BLK_LEN				* 2		* envelopeSize	// word
			+ BLK_LEN_CKS			* 1		* envelopeSize	// bytes
			+ CKS_IDLE_TIME			* 1		* sampleSize	// bits
			+ aDataBuffer.limit()	* 1		* envelopeSize	// bytes
			+ CK_SUM				* 1		* envelopeSize	// bytes
			+ SILENCE_BLOCK				* 1		* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			;
		
		logger.trace("soundSampleBufferSize = {}", soundSampleBufferSize);
		
	} //calculateBufferSize()	
	

	/*
	 * 
	 * The following helper methods are all self-explanatory and are not intended to be commented on in detail.
	 * 
	 * 
	 */
	
	
	/**
	 * @throws IllegalAccessException
	 */
	private void checkSum() throws IllegalAccessException {
		logger.trace("checkSum()");

		int chkSum = checksumCalculator.getCheckSum();	
		encoder.encodeByte((byte)chkSum, true);
		
	} // checkSum()
	
	
	protected void dataBlockLength(final ByteBuffer aDataBuffer) throws IllegalAccessException {
		logger.trace("dataBlockLength(): aDataBuffer = {}", aDataBuffer);
		
		int bufferSize = aDataBuffer.limit();
		
		byte loByte = (byte)(bufferSize & 0x00FF);
		byte hIbyte = (byte)((bufferSize >> 8) & 0x00FF);

		checksumCalculator.clear();
		checksumCalculator.sumUp(loByte);
		checksumCalculator.sumUp(hIbyte);
		
		encoder.encodeWord(bufferSize, true);

		byte chkSum = (byte)checksumCalculator.getCheckSum();
		encoder.encodeByte(chkSum, true);
	
	} //dataBlockLength()


	protected void idleTime() {
		logger.trace("idleTime()");
		
		encoder.encodeBit((byte) 1, CKS_IDLE_TIME);
		
	} //idleTime()


	protected void startAddress() throws IllegalAccessException {
		logger.trace("startAddress()");
		
		byte loByte = (byte)(startAdr & 0x00FF);
		byte hIbyte = (byte)((startAdr >> 8) & 0x00FF);
		
		checksumCalculator.clear();
		checksumCalculator.sumUp(loByte);
		checksumCalculator.sumUp(hIbyte);
		
		encoder.encodeWord(startAdr, true);

		byte chkSum = (byte)checksumCalculator.getCheckSum();
		encoder.encodeByte(chkSum, true);
		
	} //startAddress()


	protected void programNumber() {
	logger.trace("programNumber()");
	
	encoder.encodeWord(programNbr, true);
	
} //programNumber()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		
		return "Z80TrainerMonitorProtocol [programNbr=" + programNbr 
			+ ", " + super.toString() + "]";
		
	}// toString()


	@Override
	protected void setFullProgress(final int aBufferSize) {
		logger.trace("setFullProgress()");
		
		fullProgress = 
		// item					mult
		  SILENCE_BLOCK			* 1
		+ LEAD_IN				* 1	
		+ MEASURE_0				* 1
		+ MEASURE_1				* 1
		+ PRG_NBR_SIZE			* 2		// word
		+ START_ADR				* 2		// word
		+ START_ADR_CKS			* 1		// bytes
		+ BLK_LEN				* 2		// word
		+ BLK_LEN_CKS			* 1		// bytes
		+ CKS_IDLE_TIME			* 1		// bits
		+ aBufferSize			* 1		// bytes
		+ CK_SUM				* 1		// bytes
		+ SILENCE_BLOCK			* 1
		;
		
	} // setFullProgress()


} // class
