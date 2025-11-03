/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : BitValue.java
 *
 * PURPOSE       : For type safety.
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

/**
 * @author Stefan
 *
 */
package extension.encoder;

/**
 * 
 * Responsibilities:<br>
 * 	Enumerate the two possible bit values.
 * 
 * <p>
 * Collaborators:<br>
 * 	None.
 * 
 * <p>
 * Description:<br>
 * 	For sake of type safety this enum encodes the valid values for a single bit.<br>
 * 	It should be used for all situations of control and not for encoding of data. 
 * 
 * <p>
 * @author Stefan
 *
 */

public enum BitValue {
	
	LOW,
	HIGH
	;

} // class

