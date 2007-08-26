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

package com.lowagie.rups.components;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import com.lowagie.rups.components.actions.StreamReplacementAction;
import com.lowagie.rups.components.io.TextAreaOutputStream;
import com.lowagie.rups.interfaces.StreamEditorInterface;
import com.lowagie.swing.helpers.Utilities;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfReader;

public class StreamEditor extends JFrame implements StreamEditorInterface {

	/** A serial version UID. */
	private static final long serialVersionUID = -7683769710481283193L;

	/** The Stream that has to be edited. */
	protected PRStream stream;
	/** The text area where the end user can edit the Stream. */
	protected JTextArea text;
	
	public StreamEditor(PRStream stream) {
		super();
		this.stream = stream;
		initialize();
		setVisible(true);
	}
	
	private void initialize() {
        // size and location
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screen.getWidth() * .60), (int)(screen.getHeight() * .60));
        setLocation((int)(screen.getWidth() * .150), (int)(screen.getHeight() * .150));
        setResizable(true);
        
		// title and menu
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Editing the Content Stream of a Form XObject");
        setJMenuBar(getMenu());
        
        text = new JTextArea();
        try {
			TextAreaOutputStream taos = new TextAreaOutputStream(text);
			taos.write(PdfReader.getStreamBytes(stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.getContentPane().add(Utilities.getScrollPane(text));
	}

	private JMenuBar getMenu() {
		JMenuBar bar = new JMenuBar();
        JMenu stream = new JMenu("Stream");
        JMenuItem save = new JMenuItem("Save");
        stream.add(save);
        save.setAction(new StreamReplacementAction(this));
        bar.add(stream);
		return bar;
	}

	public void edit() {
		stream.setData(text.getText().getBytes());
	}
}
