package com.lowagie.swing.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * A Frame showing the progress of a certain action.
 */
public class ProgressFrame extends JFrame {

	/** a serial version uid. */
	private static final long serialVersionUID = -8286949678008659120L;
	/** label showing the message describing what's in progress. */
	protected JLabel message;
	/** the progress bar */
	protected JProgressBar progress;
	
	/**
	 * Creates a Progress frame displaying a certain message
	 * and a progressbar in indeterminate mode.
	 * @param	msg	the message that will be displayed.
	 */
	public ProgressFrame(String msg) {
		super("Progress...");
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		setLayout(layout);
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 0;
		constraints.gridy = 0;
	    message = new JLabel(msg);
	    getContentPane().add(message, constraints);
		constraints.gridy = 1;
	    progress = new JProgressBar();
	    progress.setIndeterminate(true);
	    getContentPane().add(progress, constraints);
	    
	    setSize(250, 100);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(screen.getWidth() * .5), (int)(screen.getHeight() * .5));

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
