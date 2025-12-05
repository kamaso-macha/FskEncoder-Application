/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : Record.java
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


package extension.source;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Abstract base class for all data records used by the Lexer and it's subclasses. <br>
 * Holding and returning the base set of attributes. <br>
 * 
 * <p>
 * Collaborators:<br>
 * Different specific Lexer and Parser implementations<br>
 * MemoryregionBuilder.
 * 
 * <p>
 * Description:<br>
 * This abstract class provides the basic functionality of a data record.
 * <p>
 * A concrete subclass must implement the parseContent() method which is capable for parsing a given String into it's 
 * byte representation. This method MUST be called in the constructor of the derived class!
 * <p>
 * Each record has an unique recordNumber which can be used as line counter for error analysis and detection. 
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class Record implements DataRecord {

	private Logger logger = LogManager.getLogger(Record.class.getName());
	
	protected int recordNumber;
	protected int offset;
	protected byte[] dataBuffer;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param aRecordNumber 
	 * Should be a unique number like e.g. the line or record number of a source file or alike.
	 * 
	 */
	public Record(final int aRecordNumber) {
	
		logger.trace("Record(): aRecordNumber = {}", aRecordNumber);

		recordNumber = aRecordNumber;
		
	} // Record(...)
	
	
	/**
	 * Returns the internal buffer.
	 * 
	 *  @return the internal buffer.
	 */
	public byte[] getData() { return dataBuffer; }
	
	
	/**
	 * Returns the record number which was set on constructor call.
	 * 
	 * @return the record number.
	 */
	public int getRecordNumber() { return recordNumber; }
	
	
	/**
	 * Return the offset value which was calculated by the parse() method.
	 * 
	 * @return the offset value.
	 */
	public long  getOffset() { return offset; }

	
	/**
	 * Must implement a parser which takes a string and transforms it into the Record structures.
	 * It must provide values at least for the dataBuffer and offset attributes.
	 * 
	 * @param aContent <br>
	 * A string representing the information read from source file.
	 * 
	 * @throws Exception <br>
	 * Throws a format specific exception like IhxException or alike if the given content can't be parsed.
	 */
	protected abstract void parseContent(Object aContent) throws Exception;


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representation of the current instance.
	 */
	@Override
	public String toString() {
		return "Record [recordNumber=" + recordNumber + ", offset=" + offset + ", dataBuffer="
				+ Arrays.toString(dataBuffer) + "]";
	}
	
	
} // ssalc
