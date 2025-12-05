/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : OutputDeviceController.java
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


package control.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.WorkflowEngine;
import extension.control.StatusListener;
import extension.model.MemoryRegion;
import extension.protocol.BackgroundTask;
import extension.protocol.BackgroundTaskProtokol;
import extension.protocol.Protocol;
import sound.SoundPlayer;
import view.gui.CompileAndUploadGui;


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

public class CompileAndUploadAction implements StatusListener, ActionListener, PropertyChangeListener {

	private Logger logger = LogManager.getLogger(CompileAndUploadAction.class.getName());
	
	private WorkflowEngine workflowEngine;	
	private CompileAndUploadGui compileAndUploadPanel;

	private BackgroundTaskProtokol protocol;
	private BackgroundTask<Void, Void> bgCompiler;

	private SoundPlayer soundPlayer;
	private BackgroundTask<Void, Void> bgPlayer;
	
	private BackgroundTask<Void, Void> currentTask;

	private List<MemoryRegion> selectedRegions;

	private int currentRegion;

	
	/**
	 * @param aWorkFlowEngine
	 * @param ompileAndUploadGui 
	 */
	public CompileAndUploadAction(WorkflowEngine aWorkFlowEngine) {
		logger.trace("CompileAndUploadAction(): aWorkFlowEngine = {}", aWorkFlowEngine);
		
		workflowEngine = aWorkFlowEngine;
		
		compileAndUploadPanel = new CompileAndUploadGui(this);

		setProtocol(workflowEngine.getProtocol());
		setSoundPlayer(workflowEngine.getSoundPlayer());
		
	} // CompileAndUploadAction()
	

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		String actionCommand = e.getActionCommand();
		
		if(actionCommand.equals("Do Upload")) {
			handleButtonDoUpload();
		}
		else if(actionCommand.equals("Abort")) {
			handleButtonABORT();
		}
		else {
			throw new IllegalAccessError("Unknown Action Command!");
		}
		
	} // actionPerformed()


	/**
	 * 
	 */
	protected void compileNextRegion() {
		logger.trace("compileNextRegion(): currentRegion = {}, selectedRegions.size() = {}", currentRegion, selectedRegions.size());
		
		currentRegion++;
		
		if(currentRegion < selectedRegions.size()) {
			runCompile(selectedRegions.get(currentRegion));
		}

	} // compileNextRegion()


	@Override
	public void done() {
		logger.trace("done()");

		compileAndUploadPanel.setProgress(100);
		
	} // done()


	/**
	 * @return
	 */
	public JPanel getGui() {
		logger.trace("getGui()");
		
		return compileAndUploadPanel;
		
	} // getGui()


	/**
	 * 
	 */
	private void handleButtonABORT() {
		logger.trace("handleButtonABORT()");
				
		logger.trace("currentTask = {}",  currentTask);
		currentTask.stop();

	} // handleButtonABORT()


	/**
	 * 
	 */
	private void handleButtonDoUpload() {
		logger.trace("handleButtonDoUpload()");
		
		selectedRegions = workflowEngine.getSelectedMemoryRegions();
		logger.trace("handleButtonDoUpload(): selectedRegions.size = {}", selectedRegions.size());
		
		if(selectedRegions != null) {
			
			// runCompile() does a pre increment!
			currentRegion = -1;
			
			if(selectedRegions.size() == 0) {
				
				workflowEngine.setStatusMessage("Please select one or more memory regions for upload");
				
			}
			else {
	
				compileNextRegion();
				
			}
			
		} // fi(selectedRegions != null)
			
	} // handleButtonDoUpload()


	@Override
	public void notification(Object aNotification) {
		logger.trace("notification(): aNotification = {}", aNotification);
		
	} // notification()


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.trace("propertyChange(): evt = {}, currentTask = {}", evt, currentTask);
		
		final String STATE = "state"; // NOSONAR

		logger.trace("compileAndUploadPanel = {}", compileAndUploadPanel);
		
		String propertyName = evt.getPropertyName();
		Object propertyValue = evt.getNewValue();

		logger.debug("propertyName = {}, propertyValue = {}", propertyName, propertyValue);

		// from BackgroundExecutor<T, V> extends SwingWorker<T, V>
		if ("progress" == propertyName) {
			
			int progress = (Integer)propertyValue;
			
			logger.trace("progress = {}", progress);
			compileAndUploadPanel.setProgress(progress);

		}
		// from ???
		else if(bgCompiler == currentTask
				&& STATE == propertyName
				&&  "STARTED"  == (String) propertyValue.toString()) {
			
			logger.trace("propertyChange(): compiler = STARTED ...");
						
		}
		else if(bgCompiler == currentTask
			&& STATE == propertyName
			&&  "DONE"  == (String) propertyValue.toString()) {
				
			logger.trace("propertyChange(): compiler = DONE!");
			runUpload();
						
		}
		else if(bgPlayer == currentTask
			&& STATE == propertyName
			&&  "STARTED"  == (String) propertyValue.toString()) {
				
			logger.trace("propertyChange(): player = STARTED ...");

		}
		else if(bgPlayer == currentTask
			&& STATE == propertyName
			&&  "DONE"  == (String) propertyValue.toString()) {
			
			logger.trace("propertyChange(): player = DONE!");

			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
			
			workflowEngine.setStatusMessage("");
			workflowEngine.setUploadTimestamp(timeStamp);
			compileNextRegion();
				
		}
		else {
			logger.error("unhandled propertyChange event");
		}
		
	} // propertyChange()
	

	/**
	 * @param aCandidate
	 */
	protected void runCompile(MemoryRegion aCandidate) {
		logger.trace("runCompile(): currentRegion = {}, aCandidate = {}", currentRegion, aCandidate);

		String regionInfo = String.format("Compiling region %d: Start: 0x%04X, End: 0x%04X, Size. 0x%04X", 
			currentRegion + 1,
			aCandidate.getStartAddress(),
			aCandidate.getEndAddress(),
			aCandidate.getSize()
		); 
			
		logger.info(regionInfo);
		workflowEngine.setStatusMessage(regionInfo);

		ByteBuffer dataBuffer = aCandidate.getContent();
		dataBuffer.rewind();
		protocol.setStartAddress(aCandidate.getStartAddress());
		protocol.setEndAddress(aCandidate.getEndAddress());
		protocol.setDataBuffer(dataBuffer);
		
		bgCompiler = protocol;	
		bgCompiler.addPropertyChangeListener(this);
		bgCompiler.registerStatusListener(this);

		currentTask = bgCompiler;			
		
		logger.trace("dataBuffer = {}, currentTask = {}",  dataBuffer, currentTask);
		
		bgCompiler.execute();

		logger.trace("currentTask = {}",  currentTask);

	} // runCompile()


	/**
	 * @param aCandidate
	 */
	protected void runUpload() {
		logger.trace("runUpload()");

		compileAndUploadPanel.setProgress(0);

		String regionInfo = String.format("Pending upload of region %d", currentRegion + 1 ); 
		logger.info(regionInfo);
		workflowEngine.setStatusMessage(regionInfo);
				
		try {

//			int shallPlay = JOptionPane.showConfirmDialog(
			OptionPane optionPane = new OptionPane();
			int shallPlay = optionPane.showConfirmDialog(
				new JFrame(),
				"Start Upload of region " + (currentRegion + 1) + "?", 
				"Upload to target system",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
			
			if(shallPlay == 0) {

				regionInfo = String.format("Upload of region %d", currentRegion + 1 ); 
				logger.info(regionInfo);
				workflowEngine.setStatusMessage(regionInfo);

				ByteBuffer soundSampleBuffer = protocol.getSoundSampleBuffer();	
				soundSampleBuffer.rewind();
				soundPlayer.setSoundBuffer(soundSampleBuffer);
		
				bgPlayer = soundPlayer;
				bgPlayer.addPropertyChangeListener(this);
				bgPlayer.registerStatusListener(this);
	
				currentTask = bgPlayer;
	
				logger.trace("soundSampleBuffer = {}, currentTask = {}",  soundSampleBuffer, currentTask);
	
				bgPlayer.execute();
				
			}
			else {
				compileNextRegion();
			}
	
		} catch (IllegalAccessException e) {
			logger.error("Unexpected exception while executing DoUpload: {}", e);
		}
		
	} // runUpload()


	/**
	 * @param aProtocol
	 */
	public void setProtocol(Protocol aProtocol) {
		logger.trace("setGui(): setProtocol = {}", aProtocol);
		
		protocol = (BackgroundTaskProtokol)aProtocol;
		
	} // setProtocol()


	/**
	 * @param soundPlayer
	 */
	public void setSoundPlayer(SoundPlayer aSoundPlayer) {
		logger.trace("setSoundPlayer(): aSoundPlayer = {}", aSoundPlayer);

		soundPlayer = aSoundPlayer;
		
	} // setSoundPlayer()


} // ssalc
