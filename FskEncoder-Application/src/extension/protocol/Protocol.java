/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : Protocol.java
 *
 * PURPOSE       : what is it for?
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

package extension.protocol;

import java.nio.ByteBuffer;

import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Interface to be implemented on specific protocol handlers.
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * This interface must be implemented on specific protocol implementations.<br>
 * Please refer to the Protocol Implementation Guide for more information.
 * 
 * <p>
 * @author Stefan
 *
 */

public interface Protocol {

	/**
	 * Interface method which is expected from the application.
	 * 
	 * @param aDataBuffer
	 * A ByteBuffer containing the data to be encoded.
	 * 
	 * @return
	 * A ByteBuffer holding the sound samples.
	 */
	public ByteBuffer compile(final ByteBuffer aDataBuffer);

	
	/**
	 * Returns the audio format used for this protocol.
	 * 
	 * @return FskAudioFormat 
	 * The used audio format
	 */
	public FskAudioFormat getAudioFormat();
	
}
