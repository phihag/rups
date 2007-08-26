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

import java.awt.CardLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.lowagie.rups.components.actions.StreamEditorAction;
import com.lowagie.rups.components.io.TextAreaOutputStream;
import com.lowagie.rups.components.models.DictionaryTableModel;
import com.lowagie.rups.components.models.PdfArrayTableModel;
import com.lowagie.rups.interfaces.PdfObjectRenderer;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

public class InfoPanel extends JPanel implements PdfObjectRenderer {

	/** a serial version id. */
	private static final long serialVersionUID = 1302283071087762494L;

	/** Name of a panel in the CardLayout. */
	private static final String TEXT = "text";
	/** Name of a panel in the CardLayout. */
	private static final String TABLE = "table";
	
	/** The layout that will show the info about the PDF object that is being analyzed. */
	protected CardLayout layout = new CardLayout();

	/** Table with dictionary entries. */
	JTable table = new JTable();
	/** The text pane with the info about a PDF object in the bottom panel. */
	JTextArea text = new JTextArea();
	
	/** Creates a PDF object panel. */
	public InfoPanel() {
		// layout
		setLayout(layout);

		// dictionary / array
		JScrollPane dict_scrollpane = new JScrollPane();
		dict_scrollpane.setViewportView(table);
		add(dict_scrollpane, TABLE);
		
		// stream / number / string / ...
		JScrollPane text_scrollpane = new JScrollPane();
		text_scrollpane.setViewportView(text);
		add(text_scrollpane, TEXT);
	}
	
	/**
	 * Clear ths object panel.
	 */
	public void clear() {
		text.setText(null);
		layout.show(this, TEXT);
	}
	
	/**
	 * @see com.lowagie.rups.interfaces.PdfObjectRenderer#render(com.lowagie.text.pdf.PdfObject)
	 */
	public void render(PdfObject object) {
		if (object == null) {
			text.setText(null);
			layout.show(this, TEXT);
			this.repaint();
			text.repaint();
		}
		if (text.getMouseListeners().length > 0)
			text.removeMouseListener(text.getMouseListeners()[0]);
		switch(object.type()) {
		case PdfObject.DICTIONARY:
			table.setModel(new DictionaryTableModel((PdfDictionary)object));
			layout.show(this, TABLE);
			this.repaint();
			break;
		case PdfObject.ARRAY:
			table.setModel(new PdfArrayTableModel((PdfArray)object));
			layout.show(this, TABLE);
			this.repaint();
			break;
		case PdfObject.STREAM:
			PRStream stream = (PRStream)object;
			try {
				TextAreaOutputStream taos = new TextAreaOutputStream(text);
				taos.write(PdfReader.getStreamBytes(stream));
				text.addMouseListener(new StreamEditorAction(stream));
			}
			catch(IOException e) {
				text.setText("The stream could not be read: " + e.getMessage());
			}
			layout.show(this, TEXT);
			this.repaint();
			text.repaint();
			break;
		default:
			text.setText(object.toString());
			layout.show(this, TEXT);
			break;
		}
	}
}
