/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : InputReaderExtensionDao.java
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


package extension.model;

import extension.control.ReaderExtensionControl;
import extension.source.Reader;
import extension.view.gui.ExtensionGui;

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
// Created at 2024-05-13 12:56:59

public class InputReaderExtensionDao extends GuiExtensionDao {
	
	public final Reader READER;
	public final MemoryMap MEMORY_MAP;
	
	public final ReaderExtensionControl CONTROL;

	public InputReaderExtensionDao(final Reader aReader, final MemoryMap aMemoryMap, final ExtensionGui aGui, final ReaderExtensionControl aControl) {
		super(aGui);
		
		if(aReader == null) throw new IllegalArgumentException("aReader can't be null");
		if(aMemoryMap == null) throw new IllegalArgumentException("aMemoryMap can't be null");
		if(aControl == null) throw new IllegalArgumentException("aControl can't be null");
		
		READER = aReader;
		MEMORY_MAP = aMemoryMap;
		CONTROL = aControl;
		
	} // GuiExtension()

} // ssalc
