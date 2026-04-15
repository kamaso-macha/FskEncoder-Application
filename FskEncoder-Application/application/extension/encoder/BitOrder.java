/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : BitOrder.java
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

package extension.encoder;

/**
 * Responsibilities:<br>
 * Enumerates the two possible directions for encoding the bits of a byte.
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * LSB_MSB defines the the least significant bit (b0) is encoded - and therefore 
 * transmitted first while<br
 * MSB_LSB indicates the opposite direction (from b7 ... b0)
 * 
 * <p>
 * @author Stefan
 *
 */

public enum BitOrder {

	LSB_MSB,
	MSB_LSB,
	;
	
}
