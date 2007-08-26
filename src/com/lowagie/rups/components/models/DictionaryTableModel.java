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

package com.lowagie.rups.components.models;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;

/**
 * A TableModel in case we want to show a PDF dictionary in a JTable.
 */
public class DictionaryTableModel extends AbstractTableModel {

	/** A serial version UID. */
	private static final long serialVersionUID = -8835275996639701776L;
	/** The PDF dictionary. */
	protected PdfDictionary dictionary;
	/** An ArrayList with the dictionary keys. */
	protected ArrayList<PdfName> keys = new ArrayList<PdfName>();

	/**
	 * Creates the TableModel.
	 * @param dictionary the dictionary we want to show
	 */
	public DictionaryTableModel(PdfDictionary dictionary) {
		this.dictionary = dictionary;
		for (Iterator i = dictionary.getKeys().iterator(); i.hasNext(); )
			this.keys.add((PdfName) i.next());
	}
	
	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 2;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return dictionary.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return keys.get(rowIndex);
		case 1:
			return dictionary.get(keys.get(rowIndex));
		default:
			return null;
		}
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Key";
		case 1:
			return "Value";
		default:
			return null;
		}
	}

}
