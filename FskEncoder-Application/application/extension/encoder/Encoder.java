/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : Encoder.java
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
 * 	Encoding of different data types like
 * 	<ul>
 * 		<li>silence</>
 * 		<li>wave cycle</>
 * 		<li>bit</>
 * 		<li>byte</>
 * 		<li>byte buffer</>
 * 		<li>word</>
 *	</ul> 
 * 
 * <p>
 * Collaborators:<br>
 * 	SilenceEncoder
 * 	WaveCycle
 * 	BitEncoder or derived class
 * 
 * <p>
 * Description:<br>
 * 	Class must be initialized at construction time with an instance of a BitEncoder.
 *  It can be augmented with a Silence- and WaveCycleEncoder.
 *  Also for generation of envelopes the number and value of start and/or stop bits 
 *  can be configured.
 *  <p>
 *  The encoding of bytes can be configured to be done from LSB to MSB or vice versa
 *  and for the mode for encoding of a word can be set to LITTLE- or BIG ENDIAN  
 * 
 * <p>
 * @author Stefan
 *
 */

public class Encoder {

	private Logger logger = LogManager.getLogger(Encoder.class.getName());
	
	protected static final String SAMPLE_BUFFER_SIZE_NOT_SET = "Sample buffer size not set!";

	protected SilenceEncoder silenceEncoder;
	protected WaveCycleEncoder waveCycleEncoder;
	protected BitEncoder bitEncoder;
	
	protected ByteBuffer startBitSamples;
	protected ByteBuffer stopBitSamples;
	
	protected ByteBuffer sampleBuffer;
	
	protected BitOrder bitOrder;
	protected ByteOrder byteOrder;
	
	
	/**
	 * Constructor.
	 * <br>
	 * Creates the encoder instance with the minimum parameters for initialization.
	 * 
	 * @param aBitEncoder
	 * Specific encoder to encode single bits.
	 * 
	 * @param aByteOrder
	 * Sets the byte order of data type WORD. Can be one of LITTLE_ENDIAN and BIG_ENDIAN.
	 * 
	 * @param aBitOrder
	 * Sets the bit order for encoding a byte into bits. The value is one of LSB_MSB if LSB should be encoded (and 
	 * transfered first) or MSB_LSB if the opposite direction should be used.
	 */
	public Encoder(final BitEncoder aBitEncoder, final ByteOrder aByteOrder, final BitOrder aBitOrder) {
	
		logger.trace("Encoder()");
		
		if(aBitEncoder       == null) throw new IllegalArgumentException("aBitEncoder can't be null!");
		if(aByteOrder        == null) throw new IllegalArgumentException("aByteOrder can't be null!");
		if(aBitOrder         == null) throw new IllegalArgumentException("aBitOrder can't be null!");
		
		bitEncoder       = aBitEncoder;
		byteOrder		 = aByteOrder;
		bitOrder		 = aBitOrder;
				
	} // Encoder(...)
	
	
	/**
	 * Set an additional SilenceEncoder.<br>
	 * A SilenceEncoder can be used to generate a number of silence samples e.g. as header 
	 * and/or trailer of a sound buffer.
	 * 
	 * @param aSilenceEncoder
	 * Instance of a SilenceEncoder.<br>
	 * This SilenceEncoder MUST be initialized with the sampling rate as the BitEncoder!
	 * 
	 * @return
	 * The instance of the encoder itself (this).
	 */
	public Encoder withSilenceEncoder(SilenceEncoder aSilenceEncoder) {
		logger.debug("withSilenceEncoder(): aSilenceEncoder = {}", aSilenceEncoder);
		
		if(aSilenceEncoder == null) throw new IllegalArgumentException("aSilenceEncoder can't be null!");

		silenceEncoder = aSilenceEncoder;
		
		return this;
		
	} // withSilenceEncoder(...)
	
	
	/**
	 * Set an additional WaveCycle.<br>
	 * A WaveCycle can be used to generate a number of wave cycle samples e.g. as header 
	 * and/or trailer of a sound buffer.
	 * 
	 * @param aWaveCycleEncoder
	 * Instance of a WaveCycle.<br>
	 * This WaveCycle SHOULD be the same instance as the one for the BitEncoder.
	 * 
	 * @return
	 * The instance of the encoder itself (this).
	 */
	public Encoder withWaveCycleEncoder(WaveCycleEncoder aWaveCycleEncoder) {
		logger.debug("withWaveCycleEncoder(): aWaveCycleEncoder = {}", aWaveCycleEncoder);
		
		if(aWaveCycleEncoder == null) throw new IllegalArgumentException("aWaveCycleEncoder can't be null!");

		waveCycleEncoder = aWaveCycleEncoder;
		
		return this;
		
	} // withWaveCycleEncoder(...)
	
	
	/**
	 * Set the number and value of start bits if the data should be packed into an envelope.
	 * 
	 * @param aNbrStartBits
	 * The desired number of start bits.
	 * 
	 * @param aStartBitValue
	 * The value of the start bit, one of HIGH or LOW.
	 * 
	 * @return
	 * The instance of the encoder itself (this).
	 */
	public Encoder withStartBits(final int aNbrStartBits, final BitValue aStartBitValue) {
		logger.debug("withStartBits(): aNbrStartBits = {}, aStartBitValue = {}", aNbrStartBits, aStartBitValue);
		
		startBitSamples = bitEncoder.encode(aStartBitValue.ordinal(), aNbrStartBits);

		logger.trace("bitValue = {}, aNbrStartBits = {}, startBitSamples = {}, bitEncoder = {}", 
				aStartBitValue, aNbrStartBits, startBitSamples, bitEncoder);
		
		return this;
		
	} // withChecksumCalculator (...)

	
	/**
	 * Set the number and value of stop bits if the data should be packed into an envelope.
	 * 
	 * @param aNbrStartBits
	 * The desired number of stop bits.
	 * 
	 * @param aStartBitValue
	 * The value of the stop bit, one of HIGH or LOW.
	 * 
	 * @return
	 * The instance of the encoder itself (this).
	 */
	public Encoder withStopBits(final int aNbrStopBits,  final BitValue aStopBitValue) {
		logger.debug("withStopBits(): aNbrStopBits = {}, aStopBitValue = {}", aNbrStopBits, aStopBitValue);
		
		stopBitSamples = bitEncoder.encode(aStopBitValue.ordinal(), aNbrStopBits);

		logger.trace("bitValue = {}, aStopBitValue = {}, stopBitSamples = {}, bitEncoder = {}", 
				aStopBitValue, aStopBitValue, stopBitSamples, bitEncoder);
		
		return this;
			
	} // withStopBits(...)
	
	
	/**
	 * Get the size of size of the buffer to hold one sample.
	 * 
	 * @return
	 * The size of a single sample in bytes.
	 */
	public int getSampleSize() { return bitEncoder.getSampleBufferSize(); }
	
	
	public void setBufferSize(final int aBufferSize) {
		logger.debug("setBufferSize(): aBufferSize: {}", aBufferSize);

		sampleBuffer = ByteBuffer.allocate(aBufferSize);

	} // setBufferSize()
	
	
	/**
	 * Returns the sample buffer.
	 * The buffer position is set to 0 position.
	 * 
	 * @return
	 * Buffer containing the encoded sound samples.
	 * 
	 */
	public ByteBuffer getSampleBuffer() { 
		logger.trace("getSampleBuffer()");
		
		if(sampleBuffer == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);

		logger.trace("getSampleBuffer(): " + sampleBuffer);
		
		rewindBuffer(sampleBuffer);
		logger.trace("getSampleBuffer(): " + sampleBuffer);

		return sampleBuffer;
		
	} //getSampleBuffer()
	
	
	/**
	 * Clears the sound sample buffer.
	 */
	public void clearSampleBuffer() { 
		logger.debug("clearSampleBuffer()");
		
		if(sampleBuffer == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);

		sampleBuffer.clear(); 
		
	} // clearSampleBuffer()
	

	/**
	 * Encodes the given duration as silence samples and put them into the sample buffer.
	 * 
	 * @param aDuration
	 * The duration of silence in milliseconds.
	 * 
	 */
	public void encodeSilence(final int aDuration) {
		logger.debug("encodeSilence(): aDuration = {}", aDuration);
		
		if(silenceEncoder == null) throw new IllegalAccessError("No SilenceEncoder configured!");
		if(sampleBuffer   == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);
		logger.trace("encodeSilence(): sampleBuffer = {}", sampleBuffer);
		
		sampleBuffer.put(silenceEncoder.encode(aDuration));
		logger.trace("encodeSilence(): sampleBuffer = {}", sampleBuffer);
		
	} // encodeSilence(...)

	
	/**
	 * Encodes the given number of wave cycles and put them into the sample buffer.
	 * 
	 * @param aFrequency
	 * The frequency to be used.
	 * 
	 * @param aCycleCount
	 * The number of wave cycles to encode.
	 * 
	 */
	public void encodeWaveCycle(final int aFrequency, final int aCycleCount) {
		logger.debug("encodeWaveCycle(): aFrequency = {}, aCycleCount = {}", aFrequency, aCycleCount);
		
		if(waveCycleEncoder == null) throw new IllegalAccessError("No WaveCycleEncoder configured!");
		if(sampleBuffer     == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);
		logger.trace("encodeWaveCycle(): sampleBuffer = {}", sampleBuffer);

		sampleBuffer.put(waveCycleEncoder.encode(aFrequency, aCycleCount));
		logger.trace("encodeWaveCycle(): sampleBuffer = {}", sampleBuffer);
		
	} // encodeWaveCycle(...)
	
	
	/**
	 * Encodes a number of bits into sound samples.
	 * 
	 * @param aBitValue
	 * The value to be encoded.
	 * 
	 * @param aCount
	 * The number of bits to be encoded.
	 * 
	 */
	public void encodeBit(final byte aBitValue, final int aCount) {
		logger.debug("encodeBit(): aBitValue = {}, aCount = {}", aBitValue, aCount);
		
		if(sampleBuffer   == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);
		logger.trace("encodeBit(): sampleBuffer = {}", sampleBuffer);

		ByteBuffer buffer = bitEncoder.encode(aBitValue, aCount);
		buffer.rewind();
		logger.trace("encodeBit(): buffer = {}, bitEncoder = {}", buffer, bitEncoder);
		
		sampleBuffer.put(buffer);
		logger.trace("encodeBit(): sampleBuffer = {}", sampleBuffer);
		
	} // encodeBit(...)
	
	
	/**
	 * Encodes a byte using the byte order given at initialization time.
	 * The generated sound samples are appended to the sample buffer.
	 * 
	 * @param aDataByte
	 * The byte to be encoded.
	 * 
	 * @param withTxFrame
	 * Set to true if the byte should be put in a envelope.<br>
	 * If an envelope is requested than the values for start and stop bits are applied.
	 * So make sure that the correct parameters are set!. Otherwise this parameter is silently ignored.
	 * 
	 */
	public void encodeByte(byte aDataByte, final boolean withTxFrame) {
		logger.debug("encodeByte(): aDataByte = {}, withTxFrame = {}",
			String.format("%02X", aDataByte),
			withTxFrame
		);   // NOSONAR
		
		if(sampleBuffer == null) throw new IllegalAccessError(SAMPLE_BUFFER_SIZE_NOT_SET);
		logger.trace("encodeByte(): sampleBuffer = {}", sampleBuffer);  // NOSONAR

		if(withTxFrame) {
			logger.trace("encodeByte(): add start bits");
			startBitSamples.rewind();
			sampleBuffer.put(startBitSamples);
		}
		logger.trace("encodeByte(): sampleBuffer = {}", sampleBuffer);  // NOSONAR
		
		// shortcut:
		     if(aDataByte == (byte)0x000) encodeBit((byte) 0, 8);
		else if(aDataByte == (byte)0x0FF) encodeBit((byte) 1, 8);
		else {

			byte bitValue = 0; 
			logger.trace("n = {}, aDataByte = {}, bitValue = {}", "-", String.format("%02X", aDataByte), bitValue);
			
			for(int n = 0; n < 8; n++) {

				switch(bitOrder) { // NOSONAR
				case LSB_MSB:
//					logger.trace("n = {}, (aDataByte & 0x01) = {}, bitValue = {}", n, String.format("%02X", (byte)(aDataByte & 0x01)), bitValue);
					bitValue = (byte)(aDataByte & 0x01);
					aDataByte >>= 1;
					break;
					
				case MSB_LSB:
//					logger.trace("n = {}, (aDataByte & 0x80) = {}, bitValue = {}", n, String.format("%02X", (byte)(aDataByte & 0x80)), bitValue);
					bitValue = (byte) ((aDataByte & 0x80) == 0x80 ? 1 : 0);
					aDataByte <<= 1;
					break;
					
				} // hcitws

				logger.trace("n = {}, aDataByte = {}, bitValue = {}", n, String.format("%02X", aDataByte), bitValue);
				
				encodeBit(bitValue, 1);
				
			} // rof
			
		} // esle
		     
		if(withTxFrame) {
			logger.trace("encodeByte(): add stop bits");
			stopBitSamples.rewind();
			sampleBuffer.put(stopBitSamples);
		}
		logger.trace("encodeByte(): sampleBuffer = {}", sampleBuffer);
		     
	} // encodeByte(...)
	

	/**
	 * Encodes the content of the given byte buffer.
	 * 
	 * @param aDataByteBuffer
	 * Buffer holding the bytes to encode.
	 * 
	 * @param withTxFrame
	 * Set to true if each byte should be put in an envelope.<br>
	 * If an envelope is requested than the values for start and stop bits are applied.
	 * So make sure that the correct parameters are set!. Otherwise this parameter is silently ignored.
	 * 
	 */
	public void encodeByteBuffer(final ByteBuffer aDataByteBuffer, final boolean withTxFrame) {
		logger.debug("encodeByte(): aDataByteBuffer = {}, withTxFrame = {}",
			aDataByteBuffer, withTxFrame
		);
		logger.trace("encodeByteBuffer(): sampleBuffer = {}", sampleBuffer);
		
		for(int n = 0; n < aDataByteBuffer.limit(); n++) {
			encodeByte(aDataByteBuffer.get(n), withTxFrame);
		}
		logger.trace("encodeByteBuffer(): sampleBuffer = {}", sampleBuffer);
		
	} // encodeByteBuffer(...)
	

	/**
	 * Encodes a WORD (16 bit value) using the endian mode which was set before in the constructor call.
	 * 
	 * @param aDataWord
	 * The word to be encoded.
	 * 
	 * 
	 * @param withTxFrame
	 * Set to true if the byte should be put in a envelope.
	 * <br>
	 * If an envelope is requested than the values for start and stop bits are applied.
	 * So make sure that the correct parameters are set!. Otherwise this parameter is silently ignored.
	 * 
	 */
	public void encodeWord(final int aDataWord, final boolean withTxFrame) {
		logger.debug("encodeWord(): aDataWord = {}, withTxFrame = {}",
				String.format("%04X", aDataWord),
				withTxFrame
			);
		
		logger.trace("encodeWord(): sampleBuffer = {}", sampleBuffer);

		byte highByte = (byte)((aDataWord & 0x0FF00) >> 8);
		byte lowByte  = (byte) (aDataWord & 0x0FF);
		
		switch(byteOrder) { // NOSONAR
		case BIG_ENDIAN: 
			encodeByte(highByte, withTxFrame);
			encodeByte(lowByte, withTxFrame);
			break;
			
		case LITTLE_ENDIAN: 
			encodeByte(lowByte, withTxFrame);
			encodeByte(highByte, withTxFrame);
			break;
			
		}
		
		logger.trace("encodeWord(): sampleBuffer = {}", sampleBuffer);

	} // encodeWord(...)


    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "Encoder [silenceEncoder=" + silenceEncoder 
				+ ", waveCycleEncoder=" + waveCycleEncoder 
				+ ", bitEncoder=" + bitEncoder 
				+ ", startBitSamples=" + startBitSamples 
				+ ", stopBitSamples=" + stopBitSamples
				+ ", sampleBuffer=" + sampleBuffer 
				+ ", bitOrder=" + bitOrder 
				+ ", byteOrder=" + byteOrder + "]";
	}
	

	/**
	 * Set the limit to capacity if limit == position
	 */
	protected void rewindBuffer(ByteBuffer aByteBuffer) {
		logger.trace("resizeBuffer(): aByteBuffer = {}", aByteBuffer);
		
		int position = aByteBuffer.position();
		int limit    = aByteBuffer.limit();
		
		if(position > 0) {
			
			if(position != limit) {
				aByteBuffer.flip();
			}
			
			aByteBuffer.rewind();

		}
		
		logger.trace("resizeBuffer(): aByteBuffer = {}", aByteBuffer);
		
	} // resizeBuffer()
	

} // class
