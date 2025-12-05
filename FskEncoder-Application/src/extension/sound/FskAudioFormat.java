/**
  *
  * **********************************************************************
  * PROJECT       : FskEncoder-Application
  * FILENAME      : FskAudioFormat.java
  *
 * More information about this project can be found on Github
 * http://github.com/kamaso-macha/FskEncoder-Application
 *
 * **********************************************************************
 *
 * Copyright (C)2025 by Kama So Macha (http://github.com/kamaso-macha)
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


package extension.sound;

import javax.sound.sampled.AudioFormat;


/**
 * 
 * Responsibilities:<br>
 * Definition of basic sound parameter set
 * 
 * <p>
 * Collaborators:<br>
 * AudioFormat
 * 
 * <p>
 * Description:<br>
 * Defines the audio format parameter required to play the FSK-encoded
 * data as audio.
 * 
 * <p>
 * @author Stefan
 *
 */

public class FskAudioFormat extends AudioFormat {

	protected static final int SAMPLE_SIZE_IN_BITS 		= 8;
	protected static final int NBR_OF_CHANNELS			= 1;
	protected static final boolean IS_SIGNED_NUMBER		= true;
	protected static final boolean BIG_ENDIAN_FORMAT	= true;
	
	/**
	 * Constructor.
	 * 
	 * @param aSamplingRate
	 * The sampling rate to be used.
	 */
	public FskAudioFormat(final int aSamplingRate) {
		super(
			aSamplingRate, 
			SAMPLE_SIZE_IN_BITS, 
			NBR_OF_CHANNELS, 
			IS_SIGNED_NUMBER, 
			BIG_ENDIAN_FORMAT);
	}

	
	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "FskAudioFormat [encoding=" + encoding + ", sampleRate=" + sampleRate + ", sampleSizeInBits="
				+ sampleSizeInBits + ", channels=" + channels + ", frameSize=" + frameSize + ", frameRate=" + frameRate
				+ ", bigEndian=" + bigEndian + "]";
	}
	
	
} // FskAudioFormat
