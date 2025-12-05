/**
 *
 * **********************************************************************
 * PROJECT       : FskEncoder-Application
 * FILENAME      : Lexer.java
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


package extension.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsibilities:<br>
 * Read from a line structured source file,<br>
 * create a specific DataRecord from each line read
 * 
 * <p>
 * Collaborators:<br>
 * Specific implementation of Lexer, <br>
 * specific implementation of Parser.
 * 
 * <p>
 * Description:<br>
 * On creation it receives a Constructor<?> object which is used later on in the getRecord() method 
 * to create the correct type of returned DataRecord.
 * <p>
 * Because we don't want and can't to relay on the DataRecord interface at this point we use Java generics.<p>
 * The Constructor object given in the constructor call must be the default constructor of that class which is 
 * given in the type argument for the Lexer class.
 * <p>
 * Detailed information for the usage can be obtained e.g. from class source.ihx8.IhxLexer.
 * 
 * <p>
 * @author Stefan
 *
 */

public abstract class Lexer<T> {

	private Logger logger = LogManager.getLogger(Lexer.class.getName());
	
	/**
	 * Type T must be hold for usage in getRecord.
	 */
	protected T t;
	protected Constructor<?> constructor;
		
	protected BufferedReader reader;
	protected int recordIndex;
	
	
	/**
	 * Default constructor.
	 * 
	 * @param aConstructor
	 * the constructor of that class which is given as Type parameter from user.
	 *  
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * If the aConstructor parameter contains an invalid signature and the class T can't be instantiated.
	 * 
	 */
	public Lexer(Constructor<?> aConstructor) { 
		
		logger.trace("Lexer(): aConstructor = {}", aConstructor);

		if(aConstructor == null) throw new IllegalArgumentException("aConstructor cant be null!");
		
		constructor = aConstructor;
		
	} // Lexer()
	
	
	/**
	 * Set the name of the source file to be processed.
	 * 
	 * @param aFileName
	 * the name and path of the source file
	 * 
	 * @throws FileNotFoundException
	 * if the source file can't be found.
	 */
	public void setFile(final String aFileName) throws FileNotFoundException { 
		logger.trace("setFile(): aFile = {}", aFileName);
		
		if(aFileName == null) throw new IllegalArgumentException("aFile cant be null!");
		if(aFileName.isBlank()) throw new IllegalArgumentException("aFile cant be blank nor empty!");
		
		FileInputStream inputStream = new FileInputStream(aFileName);
		reader = new BufferedReader(new InputStreamReader(inputStream));
		
		recordIndex = 01;
		
	} // setFile()
	
	
	/**
	 * Reads a single line from source file and creates a specific DataRecord object
	 * with an unique recordIndex and the data read from file. 
	 * 
	 * @return
	 * the specific DataRecord object currently created.
	 * 
	 * @throws IOException
	 * if the source file can't be accessed.
	 * 
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * if no DataRecord object can be created due of an invalid constructor.
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * If the created object is unable to parse the given line correctly.
	 */
	@SuppressWarnings("unchecked")
	public T getRecord() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.trace("getrecord()");
		
		String line = null;
		
		do {
			
			line = reader.readLine();
			recordIndex++;
			
		} while((line != null) && (line.isBlank()));
		
		if(line != null) {
			
			t = (T) constructor.newInstance(new Object[] { recordIndex, line });
			
			return t;
			
		}
		
		return null;
		
	} // getrecord()


	/**
	 * Returns a string representation of the current instance.
	 * 
	 * @return
	 * a string representing the current state of this object. 
	 */
	@Override
	public String toString() {
		return "Lexer [t=" + t + ", constructor=" + constructor + ", reader=" + reader + ", recordIndex=" + recordIndex
				+ "]";
	}
	

} // ssalc
