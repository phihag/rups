/*
 * $Id$
 *
 * Copyright 2007 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.itextpdf.rups.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;


/**
 * Wrapper for both iText's PdfReader (referring to a PDF file to read)
 * and SUN's PDFFile (referring to the same PDF file to render).
 */
public class PdfFile {

	// member variables
	
	/** The directory where the file can be found (if the PDF was passed as a file). */
	protected File directory = null;
	
	/** The original filename. */
	protected String filename = null;
	
	/** The PdfReader object. */
	protected PdfReader reader = null;
	
	/** The file permissions */
	protected Permissions permissions = null;
	
	// constructors
	/**
	 * Constructs a PdfFile object.
	 * @param	file	the File to read
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public PdfFile(File file) throws IOException, DocumentException {
		if (file == null)
			throw new IOException("No file selected.");
		directory = file.getParentFile();
		filename = file.getName();
		try {
			readFile(new FileInputStream(file), false);
		}
		catch(BadPasswordException bpe) {
			readFile(new FileInputStream(file), true);
		}
	}
	
	/**
	 * Constructs a PdfFile object.
	 * @param	file	the byte[] to read
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public PdfFile(byte[] file) throws IOException, DocumentException {
		try {
			readFile(new ByteArrayInputStream(file), false);
		}
		catch(BadPasswordException bpe) {
			readFile(new ByteArrayInputStream(file), true);
		}
	}
	
	/**
	 * Does the actual reading of the file into PdfReader and PDFFile.
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected void readFile(InputStream fis, boolean checkPass) throws IOException, DocumentException {
		// reading the file into PdfReader
		permissions = new Permissions();
		if (checkPass) {
		    final JPasswordField passwordField = new JPasswordField(32);

		    JOptionPane pane = new JOptionPane(passwordField, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
                @Override
                public void selectInitialValue() {
                    passwordField.requestFocusInWindow();
                }
            };

            pane.createDialog(null, "Enter the User or Owner Password of this PDF file").setVisible(true);

		    byte[] password = new String(passwordField.getPassword()).getBytes();
		    reader = new PdfReader(fis, password);
		    permissions.setEncrypted(true);
		    permissions.setCryptoMode(reader.getCryptoMode());
		    permissions.setPermissions(reader.getPermissions());
		    if (reader.isOpenedWithFullPermissions()) {
		    	permissions.setOwnerPassword(password);
		    	permissions.setUserPassword(reader.computeUserPassword());
		    }
		    else {
		    	JOptionPane.showMessageDialog(null, "You opened the document using the user password instead of the owner password.");
		    }
		}
		else {
			reader = new PdfReader(fis);
			permissions.setEncrypted(false);
		}
	}

	/**
	 * Getter for iText's PdfReader object.
	 * @return	a PdfReader object
	 */
	public PdfReader getPdfReader() {
		return reader;
	}
	
	/**
	 * Getter for the filename
	 * @return the original filename
	 * @since 5.0.3
	 */
	public String getFilename(){
	    return filename;
	}
}