/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : DataRecord.java
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


package extension.source;

/**
 * Responsibilities:<br>
 * Interface for handling data of the source file, structured in records.<br>
 * Provides the required basic functionality to access the basic attributes of a data record.<br>
 * 
 * <p>
 * Collaborators:<br>
 * Specific implementation of XxxLexer, <br>
 * specific implementation of XxxParser, <br>
 * specific implementation of XxxMemoryRegionBuilder, <br>
 * 
 * <p>
 * Description:<br>
 * This interface acts between the specific Lexer, Parser and MemoryRegionBuilder.
 * 
 * <p>
 * @author Stefan
 *
 */

public interface DataRecord {

	/**
	 * Returns the internal buffer.
	 * 
	 *  @return the internal buffer.
	 */
	public byte[] getData();
	
	/**
	 * Returns the record number which was set on constructor call.
	 * 
	 * @return the record number.
	 */
	public int getRecordNumber();
	
	/**
	 * Return the offset value which was calculated by the parse() method.
	 * 
	 * @return the offset value.
	 */
	public long  getOffset();
	
} // ssalc
