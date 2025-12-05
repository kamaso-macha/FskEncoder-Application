/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : OutputDeviceController.java
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


package control.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.management.InvalidAttributeValueException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.WorkflowEngine;
import extension.sound.FskAudioFormat;
import model.OutputDeviceControllerModel;
import sound.SoundPlayer;
import view.gui.DlgOutputDeviceSelection;
import view.gui.OutputDeviceGui;


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
// Created at 2024-05-09 09:47:06

public class OutputDeviceController implements ActionListener, ChangeListener, FocusListener {

	private Logger logger = LogManager.getLogger(OutputDeviceController.class.getName());
	
	protected JDialog dialog;

	protected WorkflowEngine workflowEngine;
	protected OutputDeviceControllerModel model;
	
	protected OutputDeviceGui outDevPanel;
	protected SoundPlayer soundPlayer;

	
	/**
	 * @param model 
	 * @param workFlowEngine
	 */
	public OutputDeviceController(WorkflowEngine aWorkFlowEngine, OutputDeviceControllerModel aModel) {
		logger.trace("OutputDeviceSelection(): aWorkFlowEngine = {}, aModel = {}", aWorkFlowEngine, aModel);
		
		if(aWorkFlowEngine == null) throw new IllegalArgumentException("aWorkflowEngine can't be null!");
		if(aModel == null) throw new IllegalArgumentException("aModel can't be null!");
		
		workflowEngine = aWorkFlowEngine;
		model = aModel;
		
		workflowEngine.registerCallback(this);

		try {
			
			outDevPanel = new OutputDeviceGui(this);
			
		} catch (InvalidAttributeValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setDefaults();
//		setOutputDevice();
		
	} // OutputDeviceSelection()
	

	@Override
	public void focusGained(FocusEvent e) {
		logger.trace("focusGained(), event: " + e.toString());

		// currently no actions here!
		
	} // focusGained()


	@Override
	public void focusLost(FocusEvent e) {
		logger.trace("focusLost(), event: " + e.toString());
		
		Object o = e.getSource();
		
		if(o instanceof JTextField) {
			
			dispatchTxtField(o, e.getSource());
			
		}
		else {
			logger.error("Unknown source " + e);
		}
		
		
	} // focusLost()
	

	@Override
	public void stateChanged(ChangeEvent e) {
		logger.trace("stateChanged(), event: " + e.toString());
		
		Object o = e.getSource();
		
		if(o instanceof JSlider) {
			JSlider s = (JSlider)o;
			int value = s.getValue();
			
			logger.debug("Slider name: " + s.getName() + ", value: " + value);
			outDevPanel.setTxtOutputVolume(value);
			try {
				
				if(soundPlayer != null) {
					soundPlayer.setOutputGain(value);
					workflowEngine.setOutputVolume(value);
				}
				
			} catch (LineUnavailableException e1) {
				// FIXME Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else {
			logger.error("Unknown source " + e);
		}
		
	} // stateChanged(...)


	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Output Device")) {
			handleDefaultOutputDevice();
		}
		else if(cmd.equals("comboBoxChanged")) {
			handleComboBoxEvent();
		}
		else if(cmd.equals("OK")) {
			handleDlgOutputDeviceOKEvent();
		}
		else if(cmd.equals("Cancel")) {
			handleDlgOutputDeviceCancelEvent();
		}
		else {
			
			try {
				int value = Integer.parseInt(cmd);
				outDevPanel.setSlOutputVolume(value);
			}
			catch(NumberFormatException exception) { throw new IllegalAccessError("Unknown Action Command!"); }
			
		} // esle

	} // actionPerformed()


	/**
	 * @return
	 */
	public String getOutputDeviceName() {
		logger.trace("getOutputDeviceName()");
		
		return model.getOutputDeviceName();
		
	} // getOutputDeviceName()

	
	/**
	 * @return
	 */
	public String[] getOutputDeviceNames() {
		logger.trace("getOutputDeviceNames()");
		
		return workflowEngine.getOutputDeviceNames();
		
	} // getOutputDeviceNames()


	/**
	 * @return
	 */
	public Point getCurrentPosition() {
		logger.trace("getCurrentPosition()");
		
		return new Point(100, 100);
		
	} // getCurrentPosition()


	private void dispatchTxtField(final Object o, final Object aSource) {
		logger.trace("dispatchTxtField()");
		
		JTextField t = (JTextField)o;
		
		if(t.getName().equals("txtOutputVolume")) {
			
			int value = Integer.parseInt(t.getText());
			
			logger.debug("Textfield name: " + t.getName() + ", value: " + value);
			outDevPanel.setSlOutputVolume(value);
			
		}
		else {
			logger.error("Unknown control " + aSource);
		}
		
	} // dispatchTxtField()

	
	protected void handleDefaultOutputDevice() {
		logger.trace("handleOutputDevice()");
		
		dialog = new DlgOutputDeviceSelection(this);
		dialog.setVisible(true);
				
	} // handleOutputDevice()


	protected void handleDlgOutputDeviceCancelEvent() {
		logger.trace("handleDlgOutputDeviceCancelEvent()");
		
		dialog.setVisible(false);
		dialog.dispose();
		
	} // handleDlgOutputDeviceCancelEvent()


	protected void handleDlgOutputDeviceOKEvent() {
		logger.trace("handleDlgOutputDeviceOKEvent()");

		String name = ((DlgOutputDeviceSelection) dialog).getSelectedDeviceName();
		model.setOutputDeviceName(name);
		
		setOutputDevice();
		
		handleDlgOutputDeviceCancelEvent();
		
	} // handleDlgOutputDeviceOKEvent()


	protected void handleComboBoxEvent() {
		logger.trace("handleComboBoxEvent()");
		
	
	} // handleComboBoxEvent()


	/**
	 * @throws LineUnavailableException 
	 * 
	 */
	public void setOutputDevice() {
		logger.trace("setOutputDevice()");
		
		String outDevName = model.getOutputDeviceName();
		outDevPanel.setTxtOutputDevice(outDevName);

		try {
			
			FskAudioFormat fskAudioFormat = workflowEngine.getAudioFormat();
			
			if(fskAudioFormat == null) {
				return;
			}
			
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, fskAudioFormat);

			Mixer.Info outputDevice = workflowEngine.getOutputDevice(outDevName);	
			
			if(!AudioSystem.isLineSupported(dataLineInfo)) {
				logger.error("Unsupported output line: " + dataLineInfo);
				throw new LineUnavailableException("Unsupported output line: " + outputDevice);
			}

//		https://docs.oracle.com/javase%2F8%2Fdocs%2Fapi%2F%2F/javax/sound/sampled/SourceDataLine.html
		
			Mixer mixer = AudioSystem.getMixer(outputDevice);
			
			SourceDataLine sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo); 				
			
			soundPlayer = new SoundPlayer(sourceDataLine, fskAudioFormat);
			
		}
		catch(LineUnavailableException e) {logger.fatal("Unexpected exception caught: {}", e); }
		
	} // setOutputDevice()


	/**
	 * 
	 */
	public void setDefaults() {
		logger.trace("setDefaults()");
		
		setOutputDevice();
		
		int outputVolume = workflowEngine.getOutputVolume();

		logger.trace("outputVolume: {}", outputVolume);

		outDevPanel.setTxtOutputVolume(outputVolume);
		outDevPanel.setSlOutputVolume(outputVolume);
		
		try {
			
			if(soundPlayer != null)
				soundPlayer.setOutputGain(outputVolume);
			
				logger.trace("soundPlayer: {}, outputVolume: {}", soundPlayer, outputVolume);
			
		} catch (LineUnavailableException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
		
	} // setDefaults()


	/**
	 * @return
	 */
	public JPanel getGui() {
		logger.trace("getGui()");
		
		return outDevPanel;
		
	} // getGui()


	/**
	 * 
	 */
	public int getOutputVolume() {
		logger.trace("getOutputVolume()");
		
		return workflowEngine.getOutputVolume();
		
	} // getOutputVolume
	
	
	/**
	 * @return
	 */
	public SoundPlayer getSoundPlayer() {
		logger.trace("getSoundPlayer()");
		
		return soundPlayer;
		
	} // getGui()


} // ssalc
