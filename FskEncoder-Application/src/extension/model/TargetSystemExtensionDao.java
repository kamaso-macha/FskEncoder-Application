/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder
 * FILENAME      : GuiExtension.java
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

import extension.control.TargetSystemExtensionControl;
import extension.protocol.Protocol;
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
// Created at 2024-05-13 10:11:37

public class TargetSystemExtensionDao extends GuiExtensionDao {
	
	public final Protocol PROTOCOL;
	
	public final TargetSystemExtensionControl CONTROL;
	
	
	/**
	 * 
	 */
	public TargetSystemExtensionDao(final Protocol aProtocol, final ExtensionGui aGui, final TargetSystemExtensionControl aControl) {
		super(aGui);
		
		if(aProtocol == null) throw new IllegalArgumentException("aProtocol can't be null");
		if(aControl == null) throw new IllegalArgumentException("aControl can't be null");
		
		PROTOCOL = aProtocol;
		CONTROL = aControl;
		
	} // GuiExtension()
	

} // ssalc
