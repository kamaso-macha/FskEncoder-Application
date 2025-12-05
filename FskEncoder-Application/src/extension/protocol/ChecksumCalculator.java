/**
  *
  * **********************************************************************
  * PROJECT       : FskEncoder-Application
  * FILENAME      : ChecksumCalculator.java
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

package extension.protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * 
 * Responsibilities:<br>
 * Base class for specific implementations.
 * Return the currently calculated checksum.
 * Return the currently set and applied mask for calculation. 
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Abstract base class for any type of checksum calculators.
 * <P>
 * Derived implementations have only to implement the desired algorithm 
 * in the sumUp() method.
 * <P>
 * NOTE:<br>
 * The derived class must set the 'isInitialized' flag to true as soon as a 
 * valid checksum can be obtained.
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class ChecksumCalculator {
	
	private Logger logger = LogManager.getLogger(ChecksumCalculator.class.getName());
	
	protected int mask;
	protected int chkSum;
	
	protected boolean isInitialized = false;
	
	
	/**
	 * Constructor.
	 * 
	 * @param aMask	
	 * A mask to apply to the parameter of sumUp.
	 * <br>
	 * If aMask is 0 it will be set to -1 internally because a mask of 0
	 * will never return a check sum.
	 * <P>
	 * If checksum is calculated on <br>
	 * * byte basis, set aValueMask to 0x000FF <br> 
	 * * word basis, set aValueMask to 0x0FFFF <br>
	 * 
	 */
	
	public ChecksumCalculator(final int aMask) {
		
		if(aMask == 0) {
			mask = -1;
		}
		else {
			mask = aMask;
		}
		
	} // ChecksumCalculator()
	
	
	/**
	 * Method who is calculating the checksum.<br>
	 * 
	 * Must implement the algorithm to be applied on checksum calculation.
	 * 
	 * @param aValue
	 * Value to sum up to the check sum.
	 * <P>
	 * NOTE:<br>
	 * The implementation of this method must set 'isInitialized' flag to true 
	 * as soon as a valid checksum can be obtained.
	 * 
	 */
	public abstract void sumUp(final int aValue);
	
	
	/**
	 * Returns the checksum calculated from previous calls to sumUp().<br>
	 * the return value is masked with the mask which has been set on construction time.
	 * 
	 * @return
	 * The calculated checksum.<br>
	 * The return value is calculated using the formula <i>chkSum & mask; </i>
	 * <P> 
	 * A appropriated mask and/or cast must be applied to bring the checksum to 
	 * the desired data type.
	 * 
	 * @throws IllegalAccessException
	 * If nothing was calculated so far. That means, it must have been at least 
	 * one call to sumUp(). 
	 * 
	 */
	public int getCheckSum() throws IllegalAccessException {
		logger.debug("getCheckSum() protocol, chkSum = " + String.format("%04X", chkSum & mask));
	
		if(! isInitialized) {
			throw new IllegalAccessException("Can't return a checksum, nothing was calculated so far!");
		}
		
		return chkSum & mask; 
		
	} // getCheckSum()
	
	
	/**
	 * Returns the mask which has been set in constructor call.
	 * 
	 * @return
	 * The mask currently set.
	 */
	public int getValueMask() { return mask; }


	/**
	 * set the checksum to 0.
	 */
	public void clear() { chkSum = 0; }


    /**
     * Returns a string summarizing the state of this object.
     *
     * @return  A summary string
     */
	@Override
	public String toString() {
		return "ChecksumCalculator [mask=" + mask + ", chkSum=" + chkSum + ", isInitialized=" + isInitialized + "]";
	}
	

} // ChecksumCalculator
