/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Extensions
 * FILENAME      : Z80TraineerParameterDao.java
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

import extension.encoder.ByteOrder;
import extension.protocol.ChecksumCalculator;

/**
 * Responsibilities:<br>
 * 
 * 
 * <p>
 * Collaborators:<br>
 * 
 * 
 * <p>
 * Description:<br>
 * 
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC
// Created at 2026-04-08 10:04:53

public class Z80TrainerProtocolParameterDao {
	
	protected final int F_LOW;
	protected final int F_HIGH;
	
	protected final int NBR_STOP_BITS;
	
	protected final ByteOrder BYTE_ORDER;
	
	protected final ChecksumCalculator checksumCalculator;

	
	public Z80TrainerProtocolParameterDao(
		final int aFlow, final int aFhigh, 
		final int aNbrStopBits, 
		final ByteOrder aByteOrder,
		final ChecksumCalculator aChecksumCalculator
	) {
		
		if(aFlow <= 0 || aFhigh <= 0) 	throw new IllegalArgumentException("Invalid value(s) for aFlow and/or aFhigh.");
		if(aByteOrder          == null)	throw new IllegalArgumentException("aByteOrder can't be null.");
		if(aChecksumCalculator == null)	throw new IllegalArgumentException("aChecksumCalculator can't be null.");
		
		F_LOW  = aFlow;
		F_HIGH = aFhigh;
		
		NBR_STOP_BITS = aNbrStopBits;
		
		BYTE_ORDER = aByteOrder;
		
		checksumCalculator = aChecksumCalculator;
		
	} // Z80TrainerParameterDao()


	@Override
	public String toString() {
		return "Z80TrainerProtocolParameterDao [F_LOW=" + F_LOW + ", F_HIGH=" + F_HIGH + ", NBR_STOP_BITS="
				+ NBR_STOP_BITS + ", checksumCalculator=" + checksumCalculator + "]";
	}


} // ssalc
