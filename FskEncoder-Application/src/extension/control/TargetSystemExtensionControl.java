/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : ExtensionControl.java
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


package extension.control;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

import extension.protocol.Protocol;
import extension.sound.FskAudioFormat;

/**
 * Responsibilities:<br>
 * Base class for Target System Extension controller.
 * 
 * <p>
 * Collaborators:<br>
 * ActionListener
 * 
 * <p>
 * Description:<br>
 * Abstract base class for target system controller.
 * <p>
 * NOTE:<br>
 * Don't convert it to an interface!<br>
 * If it is an interface no typecast to the concrete implementation is possible!
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class TargetSystemExtensionControl implements ActionListener { // NOSONAR

	/**
	 * 
	 * Derived class must implement this method.<br>
	 * On invocation it must create and return a subclass of ExtensionGui containing the 
	 * extension specific GUI elements.
	 * 
	 * @return
	 * : a subclass of ExtensionGui, containing the extension specific GUI elements.
	 * 
	 */
	public abstract JPanel createLayout();

	
	/**
	 * 
	 * Derived class must implement this method.<br>
	 * On invocation it must return the FskAudioFormat which is defined in 
	 * the target system. 
	 *  
	 * @return
	 * : a instance of FskAudioFormat, which is defined in the target system. 
	 * 
	 */
	public abstract FskAudioFormat getAudioFormat();
	
	
	/**
	 * 
	 * Derived class must implement this method.<br>
	 * On invocation it must return a subclass of ExtensionGui containing the 
	 * extension specific GUI elements.
	 * 
	 * @return
	 * : a subclass of ExtensionGui, containing the extension specific GUI elements.
	 * 
	 */
	public abstract JPanel getGui();

	
	/**
	 * 
	 * Derived class must implement this method.<br>
	 * On invocation it must return the Protocol which is used by 
	 * the target system. 
	 *  
	 * @return
	 * : a instance of Protocol, which is used by the target system. 
	 * 
	 */
	public abstract Protocol getProtocol();
	
	
} // ssalc
