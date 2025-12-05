/**
 *
 * **********************************************************************
 * PROJECT       : ScratchPad
 * FILENAME      : DataFileController.java
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import control.WorkflowEngine;
import model.InputFileControllerModel;
import view.gui.InputFileLoadGui;
import view.gui.InputFileSelectionGui;


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
// Created at 2024-05-09 09:47:19

public class InputFileController implements ActionListener {

	private Logger logger = LogManager.getLogger(InputFileController.class.getName());

	private WorkflowEngine workflowEngine;
	private InputFileControllerModel model;
	
	private InputFileSelectionGui inputFileSelectionPanel;
	private InputFileLoadGui inputFileLoadPanel;
	private JPanel parentFrame;
	
	/**
	 * @param aWorkFlowEngine
	 * @param aParentPanel 
	 */
	public InputFileController(WorkflowEngine aWorkFlowEngine, model.InputFileControllerModel aModel, JPanel aParentPanel) {
		logger.trace("DefaultPathSelection(): aWorkFlowEngine = {}, aModel = {}", aWorkFlowEngine, aModel);
		
		workflowEngine = aWorkFlowEngine;
		model = aModel;
		parentFrame = aParentPanel;
		
		workflowEngine.registerCallback(this);
		
		inputFileSelectionPanel	= new InputFileSelectionGui(this);
		inputFileLoadPanel		= new InputFileLoadGui(this);
		
	} // DefaultPathSelection()
	

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.trace("actionPerformed(): e = {}", e);
		
		String cmd = e.getActionCommand();
		
		if(cmd.contentEquals("Default Path")) {
			doSetDefaultPath();
		}
		else if(cmd.contentEquals("Select File")) {
			doSelectFile();
		}
		else if(cmd.contentEquals("Load File")) {
			doLoadFile();
		}
		else {
			logger.error("Unknown event " + e);
		}
		
		
	} // actionPerformed()
	

	/**
	 * 
	 */
	protected void doLoadFile() {
		logger.trace("doLoadFile()");
		
        String filePath = workflowEngine.getSelectedFileName();
        logger.debug("Selected file:" + filePath);
        
        if(filePath != null) {
        
	        workflowEngine.loadCandidateFile();
	        setFileStamp(filePath);
	        
        }
        else {
        	
        	workflowEngine.setStatusMessage("Please select an input file first");
        	
        }
        
	} // doLoadFile()


	protected void doSetDefaultPath() {
		logger.trace("doSetDefaultPath()");

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        String defaultDirectory = model.getDefaultDirectory(); 
        
        final File file = new File(defaultDirectory);
        chooser.setCurrentDirectory(file);
        final int result = chooser.showDialog(parentFrame, "Select");

        logger.trace("result: {}", result);
        if (result == JFileChooser.APPROVE_OPTION) {
        	
            File selectedDirectory = chooser.getSelectedFile();
            logger.trace("selectedDirectory: " + selectedDirectory);
            
            model.setDefaultDirectory(selectedDirectory.getAbsolutePath());

        }
        
        chooser.setVisible(false);

	} // doSetDefaultPath()


	protected void doSelectFile() {
		logger.trace("doSelectFile()");

        String defaultPath = model.getDefaultDirectory(); 
        defaultPath = defaultPath == null ? "/" : defaultPath;
        
        final File file = new File(defaultPath);

        FileNameExtensionFilter filter = workflowEngine.getFileNameExtensionFilter();

        final JFileChooser chooser = new JFileChooser("Verzeichnis w�hlen");
        chooser.setFileFilter(filter);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileHidingEnabled(true);                
        chooser.setCurrentDirectory(file);
        
        chooser.setVisible(true);
        
        final int result = chooser.showOpenDialog(parentFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
        	
            String selectedFilePath = chooser.getSelectedFile().getAbsolutePath();
            inputFileSelectionPanel.setTxtSelectedFilePath(selectedFilePath);
            workflowEngine.setSelectedFileName(selectedFilePath);
            
            doLoadFile();
            
        }
        
	} // doSelectFile()
	

	/**
	 * @param filePath
	 */
	public void setFileStamp(String aFilePath) {
		logger.trace("setFileStamp(): aFilePath = {}", aFilePath);
		
		File selectedFile = new File(aFilePath);
		
		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond((selectedFile.lastModified() / 1000), 0, ZoneOffset.UTC);       
		logger.trace("last modified: " + selectedFile.lastModified() + ", date: " + localDateTime.toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		inputFileLoadPanel.setTxtFileStamp(localDateTime.format(formatter));
		
	} // setFileStamp()


	/**
	 * @return
	 */
	public Component getFileSelectGui() {
		logger.trace("getFileSelectGui()");
		
		return inputFileSelectionPanel;
		
	} // getFileSelectGui()


	/**
	 * @return
	 */
	public Component getFileLoadGui() {
		logger.trace("getFileLoadGui()");
		
		return inputFileLoadPanel;
		
	} // getFileLoadGui()


	/**
	 * @param aTimeStamp
	 */
	public void setLastUploadTime(String aTimeStamp) {
		logger.trace("setLastUploadTime()");
		
		inputFileLoadPanel.setLastUploadTime(aTimeStamp);
		
	} // setLastUploadTime()


} // ssalc
