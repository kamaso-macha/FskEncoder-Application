/**
  *
  * **********************************************************************
  * PROJECT       : fskencoder
  * FILENAME      : DefaultChecksumCalculator.java
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Calculate a checksum over a sequence if values.
 * Applies a mask to bring the checksum into a specific data length (byte, word, ...).
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Default implementation which is used by FskEncoder as long as no specific one 
 * is provided.
 * <P>
 * Checksum is calculated on sum up integer values. 
 * On getCheckSum() the internal checksum is masked with the bit pattern which 
 * was given at initialization time.
 * 
 * <P>
 * @author Stefan
 * 
 */
public class DefaultChecksumCalculator extends ChecksumCalculator {
	
	private Logger logger = LogManager.getLogger(DefaultChecksumCalculator.class.getName());
	
	
	/**
	 * Constructor.
	 * 
	 * @param aMask
	 * A mask which will be applied on the calculated sum in method getCheckSum().
	 */
	public DefaultChecksumCalculator(final int aMask) {
		super(aMask);
		
	} // ChecksumCalculator()
	

	/**
	 * Add a value to the current check sum.
	 * If this is the first call to this method, the internal flag 'isInitialized' is 
	 * set to true.
	 * This indicates that a checksum can be obtained from calculator.
	 * 
	 * @param aValue
	 * A integer value to be added to the current check sum.
	 */
	@Override
	public void sumUp(int aValue) {
		logger.trace("sumUp(), aValue = " + String.format("%08X",  aValue));

		// Because we're on int, no mask is needed.
		chkSum += aValue; 
		
		isInitialized = true;
		
	} // sumUp()


} // ChecksumCalculator
