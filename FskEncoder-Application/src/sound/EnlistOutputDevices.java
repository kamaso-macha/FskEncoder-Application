/**
  *
  * **********************************************************************
  * PROJECT       : sound
  * FILENAME      : OutputDeviceSelector.java
  *
  * This file is part of the FSK-Encoder project. More information about
  * this project can be found here:  http://to.be.provided.
  * **********************************************************************
  *
  * Copyright (C) [2023] by Stefan Dickel, 'fsk-encoder.sdi at gmx.de'
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


package sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * Responsibilities:<br>
 * Enlist all available sound output devices. 
 * Provides a list of all available output devices.
 * Provides a query to obtain a specific output device.
 * 
 * <p>
 * Collaborators:<br>
 * None.
 * 
 * <p>
 * Description:<br>
 * Builds a list of all available sound output devices.
 * 
 * <p>
 * @author Stefan
 *
 */

public class EnlistOutputDevices {

	private Logger logger = LogManager.getLogger(EnlistOutputDevices.class.getName());
	
	private List<Mixer.Info> outputDevices = new ArrayList<>();

	
	/**
	 * At the constructor call all known output devices are scanned and 
	 * - after applying a filter to get only 'direct sound output' devices -
	 * added to a list.
	 * 
	 */
	public EnlistOutputDevices() {
		logger.trace("OutputDeviceSelector()");
		
		collectOutputDevices();
		
	} // OutputDeviceSelector()
	
	
	/*
	 * Collects all input and output devices which are known to the Java sound system.
	 */
	protected void collectOutputDevices() {
		logger.trace("collectOutputDevices()");
		
    	Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    	
    	for(int i = 0; i < mixerInfo.length; i++) {
    		
    	    Mixer.Info info = mixerInfo[i];
    	    
    	    logger.debug("Mixer.Info: {}", info);
    	    
	    	logger.trace("found  mixer "
        	    	+ String.format("idx: %02d", i)
        	    	+ String.format("-> name: %-50s",		info.getName())
        	    	+ String.format(", vendor: %-25s",		info.getVendor())
        	    	+ String.format(", version: %-20s",		info.getVersion())
        	    	+ String.format(", description: %s",	info.getDescription())
        	    );
  	    	
    	    if(info.getDescription().matches(".*DirectSound Playback$")) {
    	    	outputDevices.add(info);

    	    	logger.info("adding mixer " 
          	    	+ String.format("idx: %02d", i)
          	    	+ String.format("-> name: %-50s",		info.getName())
          	    	+ String.format(", vendor: %-25s",		info.getVendor())
          	    	+ String.format(", version: %-20s",		info.getVersion())
          	    	+ String.format(", description: %s",	info.getDescription())
          	    );
    	    	
    	    } // fi
    	    
    	} // rof
    	
    	logger.debug("nbr of outDevices: " + outputDevices.size());
    	
	} // collectOutputDevices()

	
	/**
	 * Build and return a list of names of all known output devices.
	 * 
	 * @return
	 * List of names of known output devices.
	 * 
	 */
	public String[] getOutputDeviceNames() {
		logger.trace("getOutputDeviceNames()");
		
		String[] devices = new String[outputDevices.size()];
		
		for(int i = 0; i < outputDevices.size(); i++) {
			devices[i] = outputDevices.get(i).getName();
		}
		
		Arrays.sort(devices);
		
		return devices;
		
	} // getOutputDeviceNames()
	
	
	/**
	 * Returns the output device specified by a device index.
	 * 
	 * @param aDeviceIndex 
	 * the name of the requested device.
	 * 
	 * @return
	 * the requested output device.
	 */
	public Mixer.Info getOutputDevice(final int aDeviceIndex) {
		logger.trace("getOutputDevice(): aDeviceIndex = {}", aDeviceIndex);
		
		return outputDevices.get(aDeviceIndex);
		
	} // getOutputDevice()


	/**
	 * Returns the output device specified by a device name.
	 * 
	 * @param aDeviceName 
	 * the name of the requested device.
	 * 
	 * @return
	 * the requested output device or null if the requested device is unknown.
	 */
	public Mixer.Info getOutputDevice(final String aDeviceName) {
		logger.trace("getOutputDevice(): aDeviceName = {}", aDeviceName);
		
		for(Mixer.Info device : outputDevices) {
			
			if(device.getName().equals(aDeviceName)) {
				return device;
			}
			
		} // rof
		
		return null;
		
	} // getOutputDevice()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "EnlistOutputDevices [outputDevices=" + outputDevices + "]";
	}
	
	
} // OutputDeviceSelector
