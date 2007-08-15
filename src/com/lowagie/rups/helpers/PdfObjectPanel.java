/*
 * $Id: AbstractTool.java 49 2007-05-19 19:24:42Z chammer $
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

package com.lowagie.rups.helpers;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import com.lowagie.rups.interfaces.PdfObjectRenderer;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

public class PdfObjectPanel extends JPanel implements PdfObjectRenderer {

	/** a serial version id. */
	private static final long serialVersionUID = 1302283071087762494L;

	/** The layout that will show the info about the PDF object that is being analyzed. */
	protected CardLayout layout = new CardLayout();

	/** Label that shows a value. */
	JLabel value_rendering = new JLabel();
	/** Table with dictionary entries. */
	JTable dictionary_rendering = new JTable();
	/** The text pane with the info about a PDF object in the bottom panel. */
	JTextPane stream_rendering = new JTextPane();
	
	/** Creates a PDF object panel. */
	public PdfObjectPanel() {
		setLayout(layout);

		// empty
		JPanel empty_panel = new JPanel();
		add(empty_panel, "empty");
		
		// string, number, boolean
		JPanel value_panel = new JPanel();
		value_panel.setLayout(new BorderLayout());
		JScrollPane value_scrollpane = new JScrollPane();
		value_scrollpane.setViewportView(value_rendering);
		value_panel.add(value_scrollpane, java.awt.BorderLayout.CENTER);
		add(value_panel, "value");
		
		// dictionary / array
		JScrollPane dict_scrollpane = new JScrollPane();
		dict_scrollpane.setViewportView(dictionary_rendering);
		add(dict_scrollpane, "dictionary");
		
		// stream
		JScrollPane text_scrollpane = new JScrollPane();
		text_scrollpane.setViewportView(stream_rendering);
		add(text_scrollpane, "stream");
	}
	
	/**
	 * Clear ths object panel.
	 */
	public void clear() {
		layout.show(this, "empty");
	}
	
	/**
	 * @see com.lowagie.rups.interfaces.PdfObjectRenderer#render(com.lowagie.text.pdf.PdfObject)
	 */
	public void render(PdfObject object) {
		switch(object.type()) {
		case PdfObject.DICTIONARY:
			dictionary_rendering.setModel(new DictionaryTableModel((PdfDictionary)object));
			layout.show(this, "dictionary");
			this.repaint();
			break;
		case PdfObject.ARRAY:
			dictionary_rendering.setModel(new PdfArrayTableModel((PdfArray)object));
			layout.show(this, "dictionary");
			this.repaint();
			break;
		case PdfObject.STREAM:
			PRStream stream = (PRStream)object;
			String string;
			try {
				string = new String(PdfReader.getStreamBytes(stream));
			}
			catch(IOException e) {
				string = "The stream could not be read: " + e.getMessage();
			}
			stream_rendering.setText(string);
			layout.show(this, "stream");
			this.repaint();
			stream_rendering.repaint();
			break;
		default:
			value_rendering.setText(object.toString());
			layout.show(this, "value");
			break;
		}
	}
}
