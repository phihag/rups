
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

package com.lowagie.swing.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

/**
 * An informational dialog window showing the progress of a certain action.
 */
public class ProgressDialog extends JDialog {

	/** a serial version uid. */
	private static final long serialVersionUID = -8286949678008659120L;
	/** label showing the message describing what's in progress. */
	protected JLabel message;
	/** the progress bar */
	protected JProgressBar progress;
	/** the icon used for this dialog box. */
	public static final JLabel INFO = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));
	
	/**
	 * Creates a Progress frame displaying a certain message
	 * and a progressbar in indeterminate mode.
	 * @param	the parent frame of this dialog (used to position the dialog)
	 * @param	msg	the message that will be displayed.
	 */
	public ProgressDialog(JFrame parent, String msg) {
		super();
		this.setTitle("Progress...");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    setSize(300, 100);
	    this.setLocationRelativeTo(parent);
	    
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		getContentPane().add(INFO, constraints);
		constraints.gridheight = 1;
		constraints.gridx = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
	    message = new JLabel(msg);
	    getContentPane().add(message, constraints);
		constraints.gridy = 1;
	    progress = new JProgressBar();
	    progress.setIndeterminate(true);
	    getContentPane().add(progress, constraints);
	    
	    setVisible(true);
	}
	
	/**
	 * Changes the message describing what's in progress
	 * @param msg	the message describing what's in progress
	 */
	public void setMessage(String msg) {
		message.setText(msg);
	}

	/**
	 * Changes the value of the progress bar.
	 * @param value	the current value
	 */
	public void setValue(int value) {
		progress.setValue(value);
	}
	
	/**
	 * Sets the maximum value for the progress bar.
	 * If 0 or less, sets the progress bar to indeterminate mode.
	 * @param n	the maximum value for the progress bar
	 */
	public void setTotal(int n) {
		if (n > 0) {
			progress.setMaximum(n);
			progress.setIndeterminate(false);
			progress.setStringPainted(true);
		}
		else {
			progress.setIndeterminate(true);
			progress.setStringPainted(false);
		}
	}
}
