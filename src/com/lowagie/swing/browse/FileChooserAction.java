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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Allows you to browse the file system and forwards the file
 * to the object that is waiting for you to choose a file.
 */
public class FileChooserAction extends AbstractAction {

	/** A serial version UID. */
	private static final long serialVersionUID = 2225830878098387118L;
	/** An object that is expecting the result of the file chooser action. */
	protected BrowseResult result;
	/** A file filter to apply when browsing for a file. */
	protected FileFilter filter;
	/** Indicates if you're browsing to create a new or an existing file. */
	protected boolean newFile;
	
	/**
	 * Creates a new file chooser action.
	 * @param result	the object waiting for you to select file
	 * @param caption	a description for the action
	 * @param filter	a filter to apply when browsing
	 * @param newFile	indicates if you should browse for a new or existing file
	 */
	public FileChooserAction(BrowseResult result, String caption, FileFilter filter, boolean newFile) {
		super(caption);
		this.result = result;
		this.filter = filter;
		this.newFile = newFile;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		if (filter != null) {
			fc.setFileFilter(filter);
		}
		int okCancel;
		if (newFile) {
			okCancel = fc.showSaveDialog(null);
		}
		else {
			okCancel = fc.showOpenDialog(null);
		}
		if (okCancel == JFileChooser.APPROVE_OPTION)
			result.setFile(fc.getSelectedFile());
	}

}