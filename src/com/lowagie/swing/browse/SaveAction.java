/*
 * $Id: PdfDocument.java 2884 2007-08-15 09:28:41Z blowagie $
 * Copyright (c) 2007 Bruno Lowagie
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lowagie.swing.browse;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


/**
 * This class is able to save a resource to a file.
 */
public class SaveAction extends FileChooserAction implements BrowseResult {

	/** A serial version UID. */
	private static final long serialVersionUID = 3922521788445479080L;
	/** The class that is able to write its XFA resource to an OutputStream. */
	protected OutputStreamResource resource;
	
	/**
	 * Creates the action that can save an XFA resource.
	 * @param	xfa	the class that can write its XFA resource to an OutputStream
	 */
	public SaveAction(OutputStreamResource resource, FileFilter filter) {
		super(null, "Save", filter, true);
		this.resource = resource;
		this.result = this;
	}
	
	/**
	 * Writes the XFA resource to a file.
	 * @param	file	the file to which the XFA resource is saved.
	 */
	public void setFile(File file) {
		try {
			resource.writeTo(new FileOutputStream(file));
			resource.setFile(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
		}
	}
}