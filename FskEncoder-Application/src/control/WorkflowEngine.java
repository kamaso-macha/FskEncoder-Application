/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : WorkFlowEngine.java
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


package control;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.sound.sampled.Mixer.Info;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.gui.InputFileController;
import control.gui.MainWindowController;
import control.gui.OutputDeviceController;
import control.gui.StatusBarUpdate;
import extension.control.ReaderExtensionControl;
import extension.control.StatusMessenger;
import extension.control.TargetSystemExtensionControl;
import extension.factory.InputReaderExtensionFactory;
import extension.factory.PlugInFactory;
import extension.factory.TargetSystemExtensionFactory;
import extension.model.InputReaderExtensionDao;
import extension.model.MemoryRegion;
import extension.model.TargetSystemExtensionDao;
import extension.protocol.Protocol;
import extension.sound.FskAudioFormat;
import model.FskUploaderModel;
import sound.EnlistOutputDevices;
import sound.SoundPlayer;
import view.gui.EmptyExtensionGui;

/**
 * Responsibilities:<br>
 * Handle all 'inter-controller' actions,<br>
 * do orchestration for complex tasks.
 * 
 * <p>
 * Collaborators:<br>
 * FskUploaderModel,<br>
 * EnlistOutputDevices,<br>
 * MainWindowController,<br>
 * OutputDeviceController,<br>
 * InputFileController,<br>
 * ReaderExtensionControl,<br>
 * TargetSystemExtensionControl,<br>
 * StatusBarUpdate.
 * 
 * <p>
 * Description:<br>
 * WorkflowEngine is the common controller for all tasks which are distributed over more than one controller or 
 * which needs more effort to get done.
 * 
 * <p>
 * @author Stefan
 *
 */

public class WorkflowEngine implements StatusMessenger {

	private Logger logger = LogManager.getLogger(WorkflowEngine.class.getName());
	
	protected static final String EMPTY_STATUS_BAR = ""; 						// NOSONAR
	protected static final String NOT_DEFINED = FskUploaderModel.NOT_DEFINED;	// NOSONAR

	protected FskUploaderModel model;
	protected EnlistOutputDevices enlistOutputDevices;
	protected InputReaderExtensionDao inputReaderExtensionDao;
	protected TargetSystemExtensionDao targetSystemExtensionDao;
	protected String selectedFileName;
	
	protected MainWindowController mainWindowCallback;

	protected OutputDeviceController outputDeviceSelectionCallback;
	protected InputFileController inputFileController;
	protected ReaderExtensionControl readerController;
	protected TargetSystemExtensionControl targetController;
	protected StatusBarUpdate statusBarCallback;
		
	
	/**
	 * 
	 * Constructor.
	 * 
	 * @param aModel
	 * : instance of FskUploaderModel, holding all data needed to run the application. 
	 * 
	 */
	public WorkflowEngine(FskUploaderModel aModel) {
	
		logger.trace("WorkFlowEngine(): aModel = {}", aModel);
		
		if(aModel == null) throw new IllegalArgumentException("aModel can't be null");
		
		model = aModel;
		
		enlistOutputDevices = new EnlistOutputDevices();
		
	} // WorkFlowEngine()


	/**
	 * 
	 * Obtains a list of output devices found on the PC or laptop.
	 * 
	 * @return
	 * Array of device name strings.
	 * 
	 */
	public String[] getOutputDeviceNames() {
		logger.trace("getOutputDeviceNames()");

		return enlistOutputDevices.getOutputDeviceNames();
		
	} // setTargetSystem()


	/**
	 * 
	 * Returns the filename of the currently selected input file.
	 * 
	 * @return
	 * the fully qualified file path.
	 * 
	 */
	public String getSelectedFileName() {
		logger.trace("getSelectedFileName()");

		return selectedFileName;
		
	} // getSelectedFileName()


	/**
	 * 
	 * Retrieve a list of currently selected MemoryRegions from the ReaderExtensionControl
	 * 
	 * @return
	 * a list of MemoryRegion objects.
	 * 
	 */
	public List<MemoryRegion> getSelectedMemoryRegions() {
		logger.trace("getSelectedMemoryRegions()");
		
		return readerController.getSelectedMemoryRegions(); 
		
	} // getSelectedEntries()


	/**
	 * 
	 * returns the Mixer.Info object which is associated to the given output device name.
	 * 
	 * @param aOutDevName
	 * the output device name for which the output device should be returned.
	 * 
	 * @return
	 * the Mixer.Info object which is associated to the given output device name.
	 */
	public Info getOutputDevice(String aOutDevName) {
		logger.trace("getOutputDevice(): aOutDevName = {}", aOutDevName);
		
		return enlistOutputDevices.getOutputDevice(aOutDevName);
		
	} // getOutputDevice()


	/**
	 * 
	 * @param aOutDevName
	 * @return
	 */
	public int getOutputVolume() {
		logger.trace("getOutputVolume()");
		
		return model.getOutputVolume();
		
	} // getOutputVolume()


	/**
	 * 
	 * Triggers the ReaderExtensionControl to load the selected input file.
	 * 
	 */
	public void loadCandidateFile() {
		logger.trace("loadCandidateFile()");
		
		readerController.load();
		
		// Note: This code is necessary for a GUI update after loading a file.
		// Otherwise the input reader extension panel is left empty or incorrect
		JPanel panel = readerController.createLayout();
		mainWindowCallback.setReaderExtensionPanel(panel);
		
	} // loadCandidateFile()
	
	
	/**
	 * 
	 * Register the given controller as call back.
	 * 
	 * @param aInputFileController
	 * : the controller which must be registered as callback.
	 */
	public void registerCallback(InputFileController aInputFileController) {
		logger.trace("registerCallback(): aInputFileController = {}", aInputFileController);
		
		if(aInputFileController == null) throw new IllegalArgumentException("aInputFileController can't be null.");
		
		inputFileController = aInputFileController;
		
	} // registerCallback()


	/**
	 * 
	 * Register the given controller as call back.
	 * 
	 * @param aMainWindowController
	 * : the controller which must be registered as callback.
	 */
	public void registerCallback(MainWindowController aMainWindowController) {
		logger.trace("registerCallback(): aMainWindowController = {}", aMainWindowController);
		
		if(aMainWindowController == null) throw new IllegalArgumentException("aMainWindowController can't be null.");
				
		mainWindowCallback = aMainWindowController;
		
	} // registerCallback()


	/**
	 * 
	 * Register the given controller as call back.
	 * 
	 * @param aOutputDeviceSelection
	 * : the controller which must be registered as callback.
	 */
	public void registerCallback(OutputDeviceController aOutputDeviceSelection) {
		logger.trace("registerCallback(): aOutputDeviceSelection = {}", aOutputDeviceSelection);
		
		if(aOutputDeviceSelection == null) throw new IllegalArgumentException("aOutputDeviceSelection can't be null.");
				
		outputDeviceSelectionCallback = aOutputDeviceSelection;
		
	} // registerCallback()


	/**
	 * 
	 * Register the given controller as call back.
	 * 
	 * @param aStatusBarUpdate
	 * : the controller which must be registered as callback.
	 */
	public void registerCallback(StatusBarUpdate aStatusBarUpdate) {
		logger.trace("registerCallback(): aStatusBarUpdate = {}", aStatusBarUpdate);
		
		if(aStatusBarUpdate == null) throw new IllegalArgumentException("aStatusBarUpdate can't be null.");
				
		statusBarCallback = aStatusBarUpdate;
		
	} // registerCallback()


	/**
	 * @param value
	 */
	public void setOutputVolume(int aValue) {
		logger.trace("setOutputVolume(): aValue = {}", aValue);
		
		model.setOutputVolume(aValue);
		
	} //setOutputVolume(...)


	/**
	 * 
	 * Create and set the plug-in for the input reader and triggers the update of the main window so that the
	 * correct input reader panel is displayed.
	 * <br>
	 * The input reader type is determined by the currently set or selected target system.
	 * 
	 * @param aTargetSystemName
	 * : the name of the selected target system.
	 */
	public void setReaderPlugin(String aTargetSystemName) {
		logger.trace("setReaderPlugin(): aTargetSystemName = {}", aTargetSystemName);
		
		String inputReaderProviderClassNeme = model.getInputReaderProviderClassName(aTargetSystemName);
		
		logger.trace("inputReaderProviderClassNeme = {}", inputReaderProviderClassNeme);
		
		try {
			
			InputReaderExtensionFactory factory = PlugInFactory.getInputReaderExtensionFactory(inputReaderProviderClassNeme); // NOSONAR
			
			inputReaderExtensionDao = factory.getInputReaderExtensions((StatusMessenger)this);	// NOSONAR			
			
			readerController = inputReaderExtensionDao.CONTROL;	
			readerController.initialize(inputReaderExtensionDao, (StatusMessenger)this);

			// Note: This code provides an empty input reader panel!
			JPanel memoryMapPanel = readerController.createLayout();		// NOSONAR
			mainWindowCallback.setReaderExtensionPanel(memoryMapPanel);
			
		} catch (
				ClassNotFoundException 
			  | InstantiationException 
			  | IllegalAccessException 
			  | IllegalArgumentException
			  | InvocationTargetException 
			  | NoSuchMethodException 
			  | SecurityException e) {
			statusBarCallback.setStatusMessage("Can't set up reader, see logfile!");
			logger.error("Can't set up reader, reason: {}", e);
		} catch (Exception e) {		// NOSONAR
			statusBarCallback.setStatusMessage("Can't set up reader, see logfile!");
			logger.error("Can't set up reader, reason: {}", e);
		}
		
	} // setReaderPlugin()


	/**
	 * 
	 * Set the selected input file path to internal data model.
	 * 
	 * @param aSelectedFilePath
	 * : the full qualified pathname of the currently selected file.
	 * 
	 */
	public void setSelectedFileName(String aSelectedFilePath) {
		logger.trace("setSelectedFileName(): aSelectedFilePath = {}", aSelectedFilePath);

		selectedFileName = aSelectedFilePath;
		
		readerController.setFileName(selectedFileName);
		
	} // setSelectedFileName()


	/**
	 * 
	 * Triggers the statusBar to display the given message.
	 * 
	 * @param aStatusMessage
	 * : the message to be displayed.
	 * 
	 */
	@Override
	public void setStatusMessage(String aStatusMessage) {
		logger.trace("setStatusMessage(): aStatusMessage = {}", aStatusMessage);
		
		statusBarCallback.setStatusMessage(aStatusMessage);
		
		// TODO Display status message for a while than erase it.
		
	} // setStatusMessage()


	/**
	 * 
	 * Create and set the plug-in for the target system and triggers the update of the main window so that the
	 * correct target system panel is displayed.
	 * <br>
	 * The target system type is determined by the currently set or selected onem.
	 * 
	 * @param aTargetSystemName
	 * : the name of the selected target system.
	 */
	public void setTargetPlugin(String aTargetSystemName) {
		logger.trace("setTargetPlugin(): aTargetSystemName = {}", aTargetSystemName);
		
		String targetSystemProviderClassName = model.getTargetSystemProviderClassName(aTargetSystemName);
		logger.trace("targetSystemProviderClassName = {}", targetSystemProviderClassName);
		
		try {
			
			TargetSystemExtensionFactory factory = PlugInFactory.getTargetSystemExtensionFactory(targetSystemProviderClassName); // NOSONAR
			targetSystemExtensionDao = factory.getTargetSystemExtension((StatusMessenger)this); // NOSONAR
			
			targetController = targetSystemExtensionDao.CONTROL;
			
			JPanel targetSystemPanel = targetController.createLayout(); // NOSONAR
			mainWindowCallback.setTargetExtensionPanel(targetSystemPanel);
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			statusBarCallback.setStatusMessage("Can't set up target system, see logfile!");
			logger.error("Can't set up target system, reason:", e);
		}
		
	} // setTargetPlugin()


	/**
	 * 
	 * Set the name of the currently selected target system.
	 * 
	 * @param aName
	 * : name of the currently selected target system.
	 * 
	 */
	public void setTargetSystem(String aName) {
		logger.trace("setTargetSystem(): aName = {}", aName);

		String targetSystemName;
		
		if(aName == null) {
			
			targetSystemName = model.getTargetSystemName();

		}
		else {
			
			model.setTargetSystemName(aName);
			targetSystemName = aName;
			
		}
					
		
		if( ! targetSystemName.equals(NOT_DEFINED)) {
			setReaderPlugin(targetSystemName);
			setTargetPlugin(targetSystemName);
			
			if(outputDeviceSelectionCallback != null) {
				outputDeviceSelectionCallback.setOutputDevice();
			}
			
			setStatusMessage("");
		}
		else {
			// TODO: set GUI state
//			mainWindowCallback.setGuiState(GuiState.INACTIVE);			
		}
		
		mainWindowCallback.setTitle();
		
	
	} // setTargetSystem()


	/**
	 * 
	 * Set the timestamp of an executed upload by triggering the required action on InputFileController
	 * 
	 * @param aTimeStamp 
	 * 
	 */
	public void setUploadTimestamp(String aTimeStamp) {
		logger.trace("setUploadTimestamp()");

		inputFileController.setLastUploadTime(aTimeStamp);
		
	} // setUploadTimestamp()


	/**
	 * 
	 * Requests the FskAudioFormat from current instance of the TargetSystemExtensionControl.
	 * 
	 * @return
	 * : the FskAudioFormat defined by the current instance of the TargetSystemExtensionControl.
	 * 
	 */
	public FskAudioFormat getAudioFormat() {
		logger.trace("getAudioFormat()");

		if(targetController == null) {
			return null;
		}
		else {
			return targetController.getAudioFormat();
		}
		
	} // setTargetSystem()


	/**
	 * 
	 * Retrieves the input file reader GUI extension 
	 * 
	 * @return
	 * : the panel of the input reader UI
	 * 
	 */
	public JPanel getReaderGui() {
		logger.trace("getReaderGui()");
		
		if(inputReaderExtensionDao != null) {
			return inputReaderExtensionDao.GUI;
		}
		else {
			return new EmptyExtensionGui();
		}
		
	} // getReaderGui()



	/**
	 * 
	 * Retrieves the target system GUI extension 
	 * 
	 * @return
	 * : the panel of the target system UI
	 * 
	 */
	public JPanel getTargetGui() {
		logger.trace("getTargetGui()");
		
		if(targetSystemExtensionDao != null) {
			return targetSystemExtensionDao.GUI;
		}
		else {
			return new EmptyExtensionGui();
		}

	} // getTargetGui()


	/**
	 * 
	 * Returns the active instance of the target system protocol.
	 * 
	 * @return
	 * : the active instance of the target system protocol.
	 * 
	 */
	public Protocol getProtocol() {
		logger.trace("getProtocol()");
		
		if(targetSystemExtensionDao != null) {
			return targetSystemExtensionDao.PROTOCOL;
		}
		else {
			return null;
		}
		
	} // getProtocol()


	/**
	 * 
	 * Returns the instance of the sound player.
	 * 
	 * @return
	 * : the instance of the sound player.
	 * 
	 */
	public SoundPlayer getSoundPlayer() {
		logger.trace("getSoundPlayer()");
		
		return outputDeviceSelectionCallback.getSoundPlayer();
		
	} // setAutosaveFlag()


	/**
	 * Returns the FileNameExtensionFilter by a request 
	 * 
	 * @return
	 */
	public FileNameExtensionFilter getFileNameExtensionFilter() {
		logger.trace("getFileNameExtensionFilter(): inputReaderExtensionDao = {}", inputReaderExtensionDao);
		
		return inputReaderExtensionDao.READER.getFileNameExtensionFilter();
		
	} // getFileNameExtensionFilter()


} // ssalc
