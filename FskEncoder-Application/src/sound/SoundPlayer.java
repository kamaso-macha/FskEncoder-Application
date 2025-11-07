/**
  *
  * **********************************************************************
  * PROJECT       : sound
  * FILENAME      : SoundPlayer.java
  *
  * This file is part of the FSK-Encoder project. More information about
  * this project can be found here:  http://to.be.provided.
  * **********************************************************************
  *
  * Copyright (C) [2023] by Stefan Dickel, 'fsk-encoder.sdi at gmx.de'
  *
  * This program is free software.
  * You can use, redistribute and/or modify it under the terms of 
  * the GNU Lesser General Public License as published by the 
  * Free Software Foundation, either version 3 of theLicense, 
  * or (at your option) any later version.
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


package sound;

import java.nio.ByteBuffer;
import java.util.ServiceConfigurationError;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.protocol.BackgroundTask;


/**
 * 
 * Responsibilities:<br>
 * Play back of sound samples.
 * 
 * <p>
 * Collaborators:<br>
 * ProgressIndicator
 * 
 * <p>
 * Description:<br>
 * A call to play() plays the sound samples which were given in a ByteBuffer
 * on the selected output device.
 * 
 * <p>
 * @author Stefan
 *
 */

public class SoundPlayer extends BackgroundTask<Void, Void> {

	private Logger logger = LogManager.getLogger(SoundPlayer.class.getName());
	
	protected SourceDataLine sourceDataLine;
	protected AudioFormat audioformat;
	
	protected ByteBuffer soundsampleBuffer;

	
	/**
	 * Constructor.
	 * 
	 * @param aLine
	 * A instance of javax.sound.sampled.SourceDataLine to be used for playback.
	 * 
	 * @param aAudioFormat
	 * A instance of javax.sound.sampled.SourceDataLine.
	 * 
	 * @throws IllegalArgumentException
	 * if aLine is null or aAudioFormat is null
	 * 
	 */
	public SoundPlayer(final SourceDataLine aLine, final AudioFormat aAudioFormat) {
		
		logger.trace("SoundPlayer(): aLine = {}, aAudioFormat = {}", aLine, aAudioFormat);
		
		if(aLine == null) {
			throw new IllegalArgumentException("aLine can't be null!");
		}
		
		if(aAudioFormat == null) {
			throw new IllegalArgumentException("aAudioFormat can't be null!");
		}

		sourceDataLine = aLine;
		audioformat = aAudioFormat;
		
	} // SoundPlayer(...)
	
	
	/**
	 * Starts the execution as background task under control of a SwingWorker.
	 * <p>
	 * Usually this method isn't invoked directly ba an application process. It should use play() instead.
	 */
	@Override
	public Void runBackgroundTask() {
		logger.trace("runBackgroundTask()");
		
		if(soundsampleBuffer == null) throw new IllegalAccessError("No sound sample buffer set!");
		
		try { play(soundsampleBuffer); } 
		catch (LineUnavailableException e) {
			logger.fatal("No output device available! Execution aborted. {}", e);
			throw new ServiceConfigurationError("No output line available");
		}
		
		return null;
		
	} // runBackgroundTask()
	
	
	/**
	 * Plays the sound samples given in the ByteBuffer.
	 * <br>
	 * If this method is invoked directly than the playback is done in foreground and blocks the current process till
	 * playback is done completely.
	 * <br>
	 * The play back can be stopped by invocation of method stop().
	 * <br>
	 * The output line is drained and closed correctly after termination of play back.  
	 * 
	 * @param aSoundSampleBuffer
	 * A ByteBuffer which held the sound samples to play.
	 * 
	 * @throws LineUnavailableException
	 * If the given SourceDataLine can't be opened or accessed in any way.
	 */
	public void play(final ByteBuffer aSoundSampleBuffer) throws LineUnavailableException {
		logger.trace("play(): aSoundSampleBuffer = {}", aSoundSampleBuffer);

		isRunning = true;
		
		int chunkSize = sourceDataLine.getBufferSize();
		byte[] lineBuffer = new byte[chunkSize];
		
		logger.trace("0 chunkSize = {}, lineBuffer.length = {}", chunkSize, lineBuffer.length);
		
		int guardTime = (int)(sourceDataLine.getFormat().getSampleRate() * 5 / 1000);
		int progress = 0;
		
    	if(! sourceDataLine.isOpen()) {
    		sourceDataLine.open(audioformat);
    	}

		sourceDataLine.start();

		int nbrOfSamplesLeft = aSoundSampleBuffer.limit() - aSoundSampleBuffer.position();

		do {
			
			chunkSize = (nbrOfSamplesLeft < chunkSize) ? nbrOfSamplesLeft : chunkSize;
			
			logger.trace("1 nbrOfSamples: " + aSoundSampleBuffer.limit() + ", done: " + aSoundSampleBuffer.position() + ", left: " + nbrOfSamplesLeft);						
			aSoundSampleBuffer.get(lineBuffer, 0, chunkSize);
			sourceDataLine.write(lineBuffer, 0, chunkSize);

			progress = aSoundSampleBuffer.position() * 100 / aSoundSampleBuffer.limit();
			stepOn(progress);
			
			logger.info("1 written: chunkSize = {}, lineBuffer.length = {}, progress = {}, isRunning = {}",
					chunkSize, lineBuffer.length, progress, isRunning
			);
			
			while (sourceDataLine.getBufferSize() / 2 < sourceDataLine.available()) {
				
				/*
				
					No coverage, can't be tested!
				
					Mockito 5 says:
						It is not possible to mock static methods of java.lang.Thread 
						to avoid interfering with class loading what leads to infinite loops
				
				 */
				
				try {
					
					Thread.sleep((long)(chunkSize / guardTime));
					
				} catch (Exception e) {
					
					Thread.currentThread().interrupt();
					
					isRunning = false;
					break;
				}
				
			} // elihw

			nbrOfSamplesLeft = aSoundSampleBuffer.limit() - aSoundSampleBuffer.position();
		
		} while(isRunning && (nbrOfSamplesLeft > 0));
		
		logger.trace("closing line ...");

		sourceDataLine.drain();
		sourceDataLine.stop(); 
		sourceDataLine.close();
		
		logger.trace("DONE...");
		
	} // play()
	
	
	/**
	 * Set the sound sample buffer to be played back.
	 * 
	 * @param aSoundSampleBuffer 
	 * aByteBuffer containing the sound samples.
	 * 
	 */
	public void setSoundBuffer(ByteBuffer aSoundSampleBuffer) {
		logger.trace("setSoundBuffer(): aSoundSampleBuffer = {}", aSoundSampleBuffer);
		
		if(aSoundSampleBuffer == null) throw new IllegalArgumentException("aSoundbufffer can't be null!");
		soundsampleBuffer = aSoundSampleBuffer;
		
	} // setSoundBuffer()
	
	
	/**
	 * Set the isRunning flag to false and forces the play loop to exit prematurely.
	 * <P>
	 * The output line is drained and closed correctly after termination of play back.  
	 */
	@Override
	public void stop() { isRunning = false; }


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "SoundPlayer [sourceDataLine=" + sourceDataLine + ", audioformat=" + audioformat + ", soundsampleBuffer="
				+ soundsampleBuffer + ", isRunning=" + isRunning 
				+ "]";
	}


	/**
	 * @param aValue
	 * The requested gain in the range from 0 ... 100
	 * @throws LineUnavailableException 
	 */
	public void setOutputGain(int aValue) throws LineUnavailableException {
		logger.trace("setOutputGain(): aValue = {}", aValue);
		
		if(aValue < 0) throw new IllegalArgumentException("aValue can't be less than 0.");
		if(aValue > 100) throw new IllegalArgumentException("aValue can't be greater than 100.");
		
//    	https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/javax/sound/sampled/FloatControl.html
		
		/*
		 * The current implementation uses a linear scaling for the output volume.
		 * 
		 * 		float level = (float) (-80.0 + (0.86 * value))
		 * 
		 * where 0 equals to -80cB and 100 to +6dB in steps of 0.86dB.
		 * 
		 * 
		 * A logarithmic scale can be set by using the formula
		 * 
		 * 		float level = Math.max(-80, (-80.0 + (86 / 2) * Math.log10(value))
		 * 
		 * where 0 equals to -80dB and 100 to +6dB in logarithmic steps.
		 * 1 == -80dB, 2 == -67dB, 3 == -59dB, 61 == -3dB, 71 == 0dB, 83 == +3dB and 100 == +6dB.
		 */
		
		float level = (float) (-80.0 + (0.86 * aValue));

		logger.trace("value = {}, lin.level = {}, log(value) = {}, log.level = {}", 
			aValue, level, 
			Math.log10(aValue), Math.max(-80, (-80.0 + (86 / 2) * Math.log10(aValue)))
		);
		
        try {
        
        	if(! sourceDataLine.isOpen()) {
        		sourceDataLine.open(audioformat);
        	}

        	setGain(level);
            
        }
        catch (LineUnavailableException e) { 
        	logger.error("Unexpected exception caught: {}", e);
        	throw e;
        }
        catch(java.lang.IllegalArgumentException e) { 
        	logger.error("Unexpected exception caught: {}", e); 
        	throw e;
        }
 		
	} // setOutputGain()


	/**
	 * Set the gain level of the output line.
	 * 
	 * @param level
	 * the gain level to be set.
	 */
	protected void setGain(float level) {
		logger.trace("setGain(): level = {}dB", level);
		
		FloatControl control = (FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
		control.setValue(limit(control,level));

	} // setGain()


	/**
	 * Retrieves the valid minimum and maximum values for the used control.
	 * 
	 * @param control
	 * the control who is currently used.
	 * 
	 * @param level
	 * the gain level to be checked.
	 * 
	 * @return a valid minimum or maximum
	 */
	protected float limit(FloatControl control, float level) {
		logger.trace("limit(): level = {}, min = {}, max = {}", level, control.getMaximum(), control.getMinimum());
		
		return Math.min(control.getMaximum(), Math.max(control.getMinimum(), level));
		
	} // limit()


} // SoundPlayer
