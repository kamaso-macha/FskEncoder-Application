/**
  *
  * **********************************************************************
  * PROJECT       : FskEncoder-Application
  * FILENAME      : Z80ChecksumCalculator.java
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

package target.z80trainer.termios;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import extension.protocol.ChecksumCalculator;

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
 * needed e.g. for SEL Z80 Trainer (as described in �2.5 of it's Users Manual).
 * 
 * <p>
 * @author Stefan
 * 
 */

public class Z80TrainerTermIOSChecksumCalculator extends ChecksumCalculator {

	private Logger logger = LogManager.getLogger(Z80TrainerTermIOSChecksumCalculator.class.getName());
	
	/**
	 * Constructor who initializes the superclass with the mask to be applied.
	 */
	public Z80TrainerTermIOSChecksumCalculator() {
		super(0x00FFFF);
		
		logger.trace("Z80TrainerTermIOSChecksumCalculator()");

	} // ChecksumCalculator()
	

	/**
	 * Specific algorithm:
	 * 1. The given integer is truncated to a byte value by applying the mask 0x0FFFF.
	 * 2. The resulting value is added to the current value of chkSum.
	 * 3. The new checkSum is truncated to a byte value by applying the mask 0x0FFSFF.
	 */
	@Override
	public void sumUp(int aValue) {
		logger.trace("me: {}", this);
		
		if(aValue <= 0x00FF) {
			chkSum += aValue;
			logger.debug(String.format("aValue: 0x%04X, chkSum: 0x%04X", aValue, chkSum));
		}
		else {
			int loByte = (aValue & 0x00FF);
			chkSum += loByte; 
			logger.debug(String.format("aValue: 0x%04X, loByte: 0x%04X chkSum: 0x%04X", aValue, loByte, chkSum));
			
			int hiByte = ((aValue >> 8) & 0x00FF);
			chkSum += hiByte;
			logger.debug(String.format("aValue: 0x%04X, hiByte: 0x%04X chkSum: 0x%04X", aValue, hiByte, chkSum));
			
		}
				
		isInitialized = true;
		
	} // sumUp()


} // ChecksumCalculator
