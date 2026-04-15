/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : MainWindow.java
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


package view.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.ExitCodes;
import control.gui.InputFileController;
import control.gui.MainWindowController;
import control.gui.OutputDeviceController;
import control.gui.TargetSystemSelection;
import net.miginfocom.swing.MigLayout;

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
// Created at 2024-05-13 16:04:51

public class MainWindow extends JFrame {

	protected Logger logger = LogManager.getLogger(MainWindow.class.getName());

	private static final long serialVersionUID = 1L;
	
	protected JMenuBar menuBar;
	protected JFrame mainFrame;
	protected JPanel mainPanel;

	private MainWindowController controller;

	private JPanel readerExtensionPanel;
	private JPanel targetExtensionPanel;

	private JMenuItem mntmTargetSystem;
	private JMenuItem mntmOutputDevice;
	private JMenuItem mntmDefaultPath;

	private boolean autosaveFlag;

	private JCheckBoxMenuItem mntmAutoSave;

	
	/**
	 * @param aWorkFlowEngine 
	 * 
	 */
	public MainWindow(MainWindowController aController) {
	
		logger.trace("MainWindow()");
		
		mainFrame = this;

		controller = aController;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new MigLayout("wrap 1"));
		add(mainPanel);
		
		readerExtensionPanel = new JPanel();
		targetExtensionPanel = new JPanel();
		
		autosaveFlag = controller.getAutosaveFlag();
		
		createMenuBar();
		createMainFrame();
		
	} // MainWindow()
	

	/**
	 * 
	 */
	protected void createMenuBar() {
		
		// Menu Bar
		logger.trace("Menu Bar");
		
		JMenu mnFile = new JMenu("File");
		mnFile.setName("mnFile");
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setName("mntmExit");
		mntmExit.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { doExit(); }
		});
		mnFile.add(mntmExit);

		JMenu mnProperties = new JMenu("Preferences");
		mnProperties.setName("mnProperties");

		mntmTargetSystem = new JMenuItem("Target System");
		mntmTargetSystem.setName("mntmTargetSystem");
		mnProperties.add(mntmTargetSystem);
		
		mntmOutputDevice = new JMenuItem("Output Device");
		mntmOutputDevice.setName("mntmOutputDevice");
		mnProperties.add(mntmOutputDevice);
		
		mntmDefaultPath = new JMenuItem("Default Path");
		mntmDefaultPath.setName("mntmDefaultPath");
		mnProperties.add(mntmDefaultPath);
		
		mnProperties.add(new JSeparator());
		
		mntmAutoSave = new JCheckBoxMenuItem("Automatically save on exit");
		mntmAutoSave.setName("mntmAutoSave");
		mntmAutoSave.setSelected(autosaveFlag);
		mntmAutoSave.addActionListener(controller);
		
		mnProperties.add(mntmAutoSave);
		
		menuBar =  new JMenuBar();
		menuBar.add(mnFile);
		menuBar.add(mnProperties);
		setJMenuBar(menuBar);
		
	} // createMenuBar()


	private WindowListener createExitListener() {
	
	return new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			
			doExit();
			
		} // windowClosing()
		
	}; // WindowAdapter()

} // createExitListener()


	/**
	 * 
	 */
	private void createMainFrame() {
		logger.trace("createMainFrame()");
				
		setAlwaysOnTop(false);
		setResizable(false);
		setLocationOnscreen();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(createExitListener());            
		
		setLayout(new MigLayout("wrap 1"));	// NOSONAR
		
	} // createMainFrame()
	
	
	/**
	 * 
	 */
	protected void doExit() {
		logger.trace("doExit()");

		if( ! autosaveFlag) {
			int confirm = JOptionPane.showOptionDialog(
					mainPanel, 
					"Save current settings?",
					"Exit Confirmation", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					null, 
					null
				);
	
			logger.trace("Option: {}", confirm);
			
			if (confirm == 0) {
	
				doSaveSettings();
				
			}

		} // fi(! autosaveFlag)
		else {
			
			doSaveSettings();
			
		}
		
		System.exit(ExitCodes.EOK);
				
	} // doExit()


	/**
	 * 
	 */
	protected void doSaveSettings() {
		logger.trace("doSaveSettings()");

		Point location = mainFrame.getLocationOnScreen();
		controller.setCurrentPosition(location);
		controller.saveSetings();
		
	} // doSaveSettings()

	
	/**
	 * @return 
	 * 
	 */
	public boolean getAutosaveFlag() {
		logger.trace("getAutosaveFlag()");

		return autosaveFlag;
		
	} // getAutosaveFlag()
	
	
	/**
	 * @return
	 */
	public JPanel getMainPanel() {
		logger.trace("getMainPanel()");
		
		return mainPanel;
		
	} // setTargetExtensionPanel()


	/**
	 * @param memoryMapPanel2
	 */
	public void setReaderExtensionPanel(JPanel aExtensionPanel) {
		logger.trace("setReaderExtensionPanel(): aExtensionPanel = {}", aExtensionPanel);
		
		readerExtensionPanel.removeAll();
		readerExtensionPanel.add(aExtensionPanel);
		readerExtensionPanel.validate();
		pack();
		
	} // setReaderExtensionPanel()
	
	
	/**
	 * @param memoryMapPanel2
	 */
	public void setTargetExtensionPanel(JPanel aExtensionPanel) {
		logger.trace("setTargetExtensionPanel(): aExtensionPanel = {}", aExtensionPanel);
		
		targetExtensionPanel.removeAll();
		targetExtensionPanel.add(aExtensionPanel);
		targetExtensionPanel.validate();
		pack();
		
	} // setTargetExtensionPanel()


	/**
	 * 
	 */
	public void setAutosaveFlag(final boolean aAutosaveFlag) {
		logger.trace("setAutosaveFlag(): aAutosaveFlag = {}", aAutosaveFlag);

		autosaveFlag = aAutosaveFlag;
		mntmAutoSave.setSelected(autosaveFlag);
		
	} // setAutosaveFlag()
	
	
	/**
	 * 
	 */
	public void setExtensionPanels() {
		logger.trace("setExtensionPanels()");
		
		mainPanel.add(readerExtensionPanel);
		mainPanel.add(targetExtensionPanel);
		
	} // getGui()


	/**
	 * @param targetSystemSelection
	 */
	public void setMnuTargetSystemController(TargetSystemSelection targetSystemSelection) {
		logger.trace("setMnuTargetSystemController()");
		
		mntmTargetSystem.addActionListener(targetSystemSelection);
		
	} // setMnuTargetSystemController()


	/**
	 * @param outputDeviceController
	 */
	public void setMnuOutputDeviceController(OutputDeviceController outputDeviceController) {
		logger.trace("setMnuOutputDeviceController()");
		
		mntmOutputDevice.addActionListener(outputDeviceController);
		
	} // setMnuOutputDeviceController()


	protected void setLocationOnscreen() {
		logger.trace("setLocationOnscreen()");
		
		Point currentLocation = controller.getCurrentPosition();
		
        if(currentLocation != null) {
        	
        	mainFrame.setLocation(currentLocation);
        	
        }
        else {
        	
        	mainFrame.setLocationRelativeTo(null);
        	
        }
        
	} // setLocationOnscreen()


	/**
	 * @param inputFileController
	 */
	public void setMnuDefaultPathController(InputFileController inputFileController) {
		logger.trace("setMnuDefaultPathController()");
		
		mntmDefaultPath.addActionListener(inputFileController);
		
	} // setMnuDefaultPathController()


	/**
	 * @return
	 */
	public boolean getAutosaveMode() {
		logger.trace("getAutosaveMode()");
		
		return mntmAutoSave.isSelected();
		
	} // getAutosaveMode()
	
	
} // ssalc
