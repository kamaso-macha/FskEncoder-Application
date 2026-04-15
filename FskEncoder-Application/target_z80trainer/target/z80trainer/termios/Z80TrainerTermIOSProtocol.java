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

package target.z80trainer.termios;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.encoder.BitValue;
import extension.encoder.ByteOrder;
import extension.sound.FskAudioFormat;
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
 * 
    Bit format
    --------------------------------------------------------------------------------
    
    '0'   1 cycle   595,95Hz (1678µs)
    '1'   1 cycle  1138,80Hz ( 878µs)

    
    Envelope (Byte format)
    --------------------------------------------------------------------------------
    
      1 start bit '0'
      8 data  bits, lsb first (b0 to b7)
      2 stop  bit '1'
    
    
    File format
    --------------------------------------------------------------------------------
    
     1.    12'288   bit '1'   Lead sync                       ->  leadIn
     
     2.         1   bit '0'   Measurement                     ->  syncPatern
     3.        16   bit '1'     for period length             +>

     4.         1   envlp     const '0x02'
     
     The program name can be encoded with 1 .. 8 envelopes
     5a.        1   envlp     Program name - MSB              ->  programName
     5b.        1   envlp     Program name                    +>  ASCII encoded
	 ...
     5x.        1   envlp     Program name - LSB              +>

	 ONLY If programName has less than 7 characters
     6.         1   envlp     const '0x00'                    ->  prg name terminator

     This sequence is repeated for 28 times.
     7a.        1   envlp     const '0xFF'                    ->  const sequence
     7b.        1   envlp     const '0xFE'                    +>  
     7c.        1   envlp     const '0xFD'                    +>  
     7d.        1   envlp     const '0xFB'                    +>  
     7e.        1   envlp     const '0xF7'                    +>  
     7f.        1   envlp     const '0xEF'                    +>  
     7g.        1   envlp     const '0xDF'                    +>  
     7h.        1   envlp     const '0xBF'                    +>  
     7i.        1   envlp     const '0x7F'                    +>  
 
     8a.        1   envlp     const '0xFF'                    ->  const sequence
     8b.        1   envlp     const '0xFE'                    +>  
     8c.        1   envlp     const '0xFD'                    +>  
     8d.        1   envlp     const '0xFB'                    +>  

     9.         1   envlp     const '0x00'

    10a         1   envlp     Start address - low byte        ->  startAddress
    10b         1   envlp     Start address - high byte       +>
              
    11a         1   envlp     Data block length - low byte    ->  dataBlockLength
    11b         1   envlp     Data block length - high byte   +>
            
    12.         n   envlp     Data block                      ->  dataBlock
    
    13a.        1   envlp     Data block checksum - LSB       ->  16 bit checksum
    13b.        1   envlp     Data block checksum - MSB       +>

    This sequence is repeated for 28 times.
    14a.       1   envlp     const '0xFF'                    ->  const sequence
    14b.       1   envlp     const '0xFE'                    +>  
    14c.       1   envlp     const '0xFD'                    +>  
    14d.       1   envlp     const '0xFB'                    +>  
    14e.       1   envlp     const '0xF7'                    +>  
    14f.       1   envlp     const '0xEF'                    +>  
    14g.       1   envlp     const '0xDF'                    +>  
    14h.       1   envlp     const '0xBF'                    +>  
    14i.       1   envlp     const '0x7F'                    +>  
 
    15a.       1   envlp     const '0xFF'                    ->  const sequence
    15b.       1   envlp     const '0xFE'                    +>  
    15c.       1   envlp     const '0xFD'                    +>  
    15d.       1   envlp     const '0xFB'                    +>  
    
 * </pre></tt></blockquote>
 * <p>
 * @author Stefan
 *
 */

public class Z80TrainerTermIOSProtocol extends Z80TrainerProtocolBase {

	private Logger logger = LogManager.getLogger(Z80TrainerTermIOSProtocol.class.getName());
	
	/**
	 */
	
	/*
	 * FSK and envelope parameter definition
	 */
	protected static final int F_LOW 					=    595;
	protected static final int F_HIGH					=	1139;
	
	protected static final int NBR_START_BITS			=	1;
	protected static final BitValue START_BIT_VALUE		=	BitValue.LOW;
	
	protected static final int NBR_STOP_BITS			=	2;
	protected static final BitValue STOP_BIT_VALUE		=	BitValue.HIGH;
	
	protected static final int SAMPLING_RATE			=	F_HIGH * 8;

	/*
	 * Structure of a complete file for upload
	 */
	protected static final int SILENCE_BLOCK		= 500;
	
	protected static final int LEAD_IN				= 12288;	// bits
	
	protected static final int MEASURE_0			= 1;		// bit
	protected static final int MEASURE_1			= 16;		// bits
	
	protected static final int[] CONST_PATTERN_1 	= new int[] 
		{0xFF,  0xFE, 0xFD, 0xFB, 0xF7, 0xEF, 0xDF, 0xBF, 0x7F};
	
	protected static final int[] CONST_PATTERN_2 	= new int[] 
			{0xFF,  0xFE, 0xFD, 0xFB};
		
	protected static final int START_ADR			= 2;		// bytes
	
	protected static final int BLK_LEN				= 2;		// bytes
	protected static final int BLK_LEN_CKS			= 2;		// byte
	
	// 						byte[] data				= n 		// bytes TX-frames
	
	protected static final int CK_SUM				= 2;		// bytes
	
	protected static final int MAX_PRG_NAME_LEN		= 8;		// max 8 characters
	protected String programName;

	
	/**
	 * Constructor.
	 */
	public Z80TrainerTermIOSProtocol() {
		super(
				new Z80TrainerProtocolParameterDao(
					F_LOW, F_HIGH, NBR_STOP_BITS, 
					ByteOrder.LITTLE_ENDIAN,
					new Z80TrainerTermIOSChecksumCalculator()
				)
			);
		
		logger.trace("Z80TrainerTermIOSProtocol()");
		
	} // Z80TrainerTermIOSProtocol()


	
	@Override
	public void setProgramNbr(int aProgramNumber) {
		
		throw new IllegalAccessError("Method not supported.");
		
	} // setProgramNbr(int)



	@Override
	public void setProgramNbr(String aProgramName) {
		
		programName = aProgramName;
		haveProgramNbr = true;
		
	} // setProgramNbr(String)


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

			// TODO: Test coverage

			if(!compileCommonStart(aDataBuffer))
				return null;	
			
			programName();
			progress(programName.length());
			if(!isRunning) return null;
			
			constSequence();
			progress(CONST_PATTERN_1.length);
			progress(CONST_PATTERN_2.length);
			if(!isRunning) return null;
			
			checksumCalculator.clear();

			startAddress();
			progress(START_ADR);
			if(!isRunning) return null;
			
			dataBlockLength(aDataBuffer);
			progress(BLK_LEN + BLK_LEN_CKS);
			if(!isRunning) return null;
						
			dataBlock(aDataBuffer);
			progress(aDataBuffer.limit());
			if(!isRunning) return null;
			
			checkSum();
			progress(CK_SUM);
			if(!isRunning) return null;
			
			constSequence();
			progress(CONST_PATTERN_1.length);
			progress(CONST_PATTERN_2.length);
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
			// item						size	samples
			= SILENCE_BLOCK						* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			
			+ LEAD_IN							* sampleSize	// bits	
			+ MEASURE_0							* sampleSize	// bits
			+ MEASURE_1							* sampleSize	// bits
			
			+ programName.length()				* envelopeSize	// bytes
			
			+ CONST_PATTERN_1.length 	* 28	* envelopeSize	// bytes
			+ CONST_PATTERN_2.length			* envelopeSize	// bytes
			
			+ START_ADR					* 2		* envelopeSize	// bytes
			+ BLK_LEN					* 2		* envelopeSize	// bytes
			
			+ aDataBuffer.limit()				* envelopeSize	// bytes
			
			+ CK_SUM					* 2		* envelopeSize	// bytes
			
			+ CONST_PATTERN_1.length	* 28	* envelopeSize	// bytes
			+ CONST_PATTERN_2.length			* envelopeSize	// bytes
			
			+ SILENCE_BLOCK						* (int)(SAMPLING_RATE * SILENCE_BLOCK / 1000) + 1	// samples
			;
		
		logger.info("aDataBuffer = {}, soundSampleBufferSize = {}", aDataBuffer, soundSampleBufferSize);
		
	} //calculateBufferSize()	
	

	/*
	 * 
	 * The following helper methods are all self-explanatory and are not intended to be commented on in detail.
	 * 
	 * 
	 */
	
	
	protected void checkSum() throws IllegalAccessException {
		logger.trace("checkSum()");
	
		int chkSum = checksumCalculator.getCheckSum();
		
		encoder.encodeWord(chkSum, true);
		
	} // checkSum()
	
	
	protected void dataBlockLength(final ByteBuffer aDataBuffer) throws IllegalAccessException {
		logger.trace("dataBlockLength(): aDataBuffer = {}", aDataBuffer);
		
		int bufferSize = aDataBuffer.limit();
		
		checksumCalculator.sumUp(bufferSize);
		
		encoder.encodeWord(bufferSize, true);

	} //dataBlockLength()


	protected void programName() {
		logger.trace("programName()");
		
		ByteBuffer buffer = ByteBuffer.allocate(programName.length() + 2);
		
		buffer.put((byte) 0x02);
		buffer.put(programName.getBytes());
		
		if(programName.length() < MAX_PRG_NAME_LEN) {
			buffer.put((byte) 0x00);
		}
		else {
			buffer.limit(buffer.limit() - 1);
		}
		
		encoder.encodeByteBuffer(buffer, true);
		
	} //programName()
	
	
	protected void constSequence() {
		logger.trace("constSequence()");
		
		int buffersize = CONST_PATTERN_1.length * 28 + CONST_PATTERN_2.length + 1;
		
		ByteBuffer buffer = ByteBuffer.allocate(buffersize);

		logger.trace("buffer.capacity: {}", buffer.capacity());

		for(int n = 0; n < 28; n++ ) {
			fillPatternBuffer(buffer, CONST_PATTERN_1);
		}
		
		fillPatternBuffer(buffer, CONST_PATTERN_2);
		
		buffer.put((byte) 0x00);
		
		encoder.encodeByteBuffer(buffer, true);
		
	} // constSequence()
	
	
	/**
	 * @param buffer
	 */
	private void fillPatternBuffer(ByteBuffer aBuffer, final int[] aPattern) {
//		logger.trace("fillPatternBuffer(): aPattern.length: {}", aPattern.length);
		
		for(int i = 0; i < aPattern.length; i++) 
			aBuffer.put((byte) ((byte) aPattern[i] & 0x00FF));
		
	} // fillPatternBuffer()



	protected void startAddress() throws IllegalAccessException {
		logger.trace(String.format("startAddress(): startAdr: 0x%04X", startAdr));
		
		checksumCalculator.sumUp(startAdr);
		
		encoder.encodeWord(startAdr, true);

	} //startAddress()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "Z80TrainerProtocol [programName=" + programName + ", haveProgramNbr=" + haveProgramNbr + ", startAdr="
				+ startAdr + ", haveStartAdr=" + haveStartAdr 
				+ ", " + super.toString()
				+ "]";
	}


	/**
	 * Returns the audio format used for this protocol.
	 * 
	 * @return FskAudioFormat 
	 * The used audio format
	 */
	public FskAudioFormat getAudioFormat() { return new FskAudioFormat(SAMPLING_RATE); }


	@Override
	protected void setFullProgress(final int aBufferSize) {
		logger.trace("setFullProgress()");

		fullProgress = 
		// size of item 
		  SILENCE_BLOCK
		  
		+ LEAD_IN
		+ MEASURE_0
		+ MEASURE_1
		
		+ programName.length()	// bytes
		
		+ CONST_PATTERN_1.length * 28
		+ CONST_PATTERN_2.length
		
		+ START_ADR
		+ BLK_LEN

		+ aBufferSize
		
		+ CK_SUM
		
		+ CONST_PATTERN_1.length * 28
		+ CONST_PATTERN_2.length
		
		+ SILENCE_BLOCK
		;
		
	} // setFullProgress()


} // class
