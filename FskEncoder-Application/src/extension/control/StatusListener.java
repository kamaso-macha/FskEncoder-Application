/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : TaskCallBack.java
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


package extension.control;

/**
 * Responsibilities:<br>
 * Interface between a controller or GUI and the BackgroundTask.
 * 
 * <p>
 * Collaborators:<br>
 * Any controller implementation,<br>
 * BackgroundTask
 * 
 * <p>
 * Description:<br>
 * BackgroundTask holds a instance of a controller by it's StatusListener interface.
 * 
 * <p>
 * @author Stefan
 *
 */

public interface StatusListener {

	/**
	 * Signal from background execution (via BackgroundTask/BackgroundExecutor) that
	 * the background execution is finish.
	 */
	void done();
	
	
	/**
	 * A background task can send notifications to the implementor of this interface to inform e.g. 
	 * about process state.
	 * 
	 * @param aNotification
	 * a object containing the notification.
	 */
	void notification(Object aNotification);

}
