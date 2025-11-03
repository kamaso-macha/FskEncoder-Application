/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : MemoryRegion.java
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


package extension.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Holds the contents of a continuous memory block with it's start address and size.
 * 
 * <p>
 * Collaborators:<br>
 * MemoryMap<br>
 * Any specific XxxMemoryRegionBuilder<br>
 * 
 * <p>
 * Description:<br>
 * MemoryRegion appends the payload of an given record to it's internal content buffer. 
 * On creation the start address is set to the value given in the constructor call. 
 * For each subsequent record it updates the size attribute with the length of the given payload. 
 * <br>
 * Buffer contents, start and end addresses as well as the current payload size can be obtained using getter methods.  
 * 
 * <p>
 * @author Stefan
 *
 */

// DOC

public class MemoryRegion {

	private Logger logger = LogManager.getLogger(MemoryRegion.class.getName());
	
	protected long startAddress;
	protected int size;
	protected List<byte[]> content;
	
	
	/**
	 * Standard constructor.
	 * 
	 * @param aStartAddress
	 * The base address of the memory image kept in the content buffer.
	 */
	public MemoryRegion(final long aStartAddress) {

		logger.trace("MemoryRegion(): aStartAddress = {}", aStartAddress);
		
		content = new ArrayList<>();
		startAddress = aStartAddress;
		
	} // MemoryRegion()
	
	
	/**
	 * Appends the payload buffer to the internal content buffer and updates the size attribute.
	 * 
	 * @param aContent aContent
	 * A byte buffer holding the payload.
	 * 
	 * @throws IllegalArgumentException
	 * If aContent is null or empty.
	 */
	public void addContent(final byte[] aContent) {
		logger.trace("addContent(): aContent = {}", aContent);
		
		if(aContent == null) throw new IllegalArgumentException("aContent can't be null");
		if(aContent.length == 0) throw new IllegalArgumentException("aContent can't be empty");
		
		content.add(aContent);
		size += aContent.length;
		
	} // addContent()
	
	
	/**
	 * @param startAddress2
	 */
	public void changeStartAddress(long aStartAddress) {
		logger.trace("changeStartAddress(): aStartAddress = {} ", String.format("0x%04X", aStartAddress));
		
		startAddress = aStartAddress;
		
	} //changeStartAddress()
	
	
	/**
	 * Returns the payload buffer.
	 * 
	 * @throws IllegalAccessError
	 * If the content buffer is empty.
	 * <br>
	 * A MemoryRegion should only be instantiated if - and only if - a data record is added immediately.
	 * There should be no need for an empty MemoryRegion!
	 */
	public ByteBuffer getContent() {
		logger.trace("getContent()");
		
		if(content.size() == 0) throw new IllegalAccessError("No content available");
		
		ByteBuffer result = ByteBuffer.allocate(getSize());
		
		for(byte[] region : content) {
			result.put(region);
		}
		
		return result;
		
	} // getContent()
	
	
	/**
	 * Returns the end address of the kept memory image.
	 * 
	 * @return the end address computed by startAddress + size - 1.
	 * <p>
	 * NOTE: The correction by -1 is done because 'start address' is the first address used:
	 * <br>
	 * start address = 0x0000, size = 0x40 -> 0x0000 + 0x40 = 0x0040.
	 * <br> 
	 * Because address 0x0000 is the first address byte which is used we have to count it in and therefore 
	 * subtract one from the calculated end address to get the correct end. 
	 */
	public long getEndAddress() { 
		logger.trace("getEndAddress()");
		
		if(size == 0) throw new IllegalAccessError("No content available");
		
		return startAddress + size - 1; 
		
	} //getEndAddress()
	

	/**
	 * Rreturns the size of the content buffer.
	 * 
	 * @return the size of the content buffer.
	 */
	public int getSize() { return size; }

	
	/**
	 * Returns the start address of the memory image.
	 * 
	 * @return
	 * the start address.
	 */
	public long getStartAddress() { return startAddress; }


	/**
	 * Returns a string representation of the instance.
	 * 
	 * @return
	 * a string representation of the instance.
	 */
	@Override
	public String toString() {
		
		return "MemoryRegion [" 
			+ "startAddress=" + String.format("0x%04X", startAddress)
			+ ", size=" + String.format("0x%02X", size) + " / " + size 
			+ "]";
		
	} // toString()


} // ssalc

