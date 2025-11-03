/**
  *
  * **********************************************************************
  * PROJECT       : fskencoder
  * FILENAME      : Z80ChecksumCalculator.java
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

package extension.protocol;

/**
 * 
 * Responsibilities:<br>
 * Calculate a one byte checksum with an MOD 256 algorithm.
 * 
 * <p>
 * Collaborators:<br>
 * Super class.
 * 
 * <p>
 * Description:<br>
 * ChecksumCalculator implementation which is calculating a MOD 256 checksum,
 * needed e.g. for SEL Z80 Trainer (as described in §2.5 of it's Users Manual).
 * 
 * <p>
 * @author Stefan
 * 
 */

public class Modulo256ChecksumCalculator extends ChecksumCalculator {

	
	/**
	 * Constructor who initializes the superclass with the mask to be applied.
	 */
	public Modulo256ChecksumCalculator() {
		super(0x00FF);
		
	} // ChecksumCalculator()
	

	/**
	 * Specific algorithm:
	 * 1. The given integer is truncated to a byte value by applying the mask 0x0FF.
	 * 2. The resulting value is added to the current value of chkSum.
	 * 3. The new checkSum is truncated to a byte value by applying the mask 0x0FF.
	 */
	@Override
	public void sumUp(int aValue) { 
	
		chkSum += (aValue & mask); 
		chkSum &= mask;
		
		isInitialized = true;
		
	} // sumUp()


} // ChecksumCalculator
