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

import javax.swing.table.AbstractTableModel;

/**
 * The table model used for the XRefTable.
 */
public class XRefTableModel extends AbstractTableModel {

	/** A serial version UID. */
	private static final long serialVersionUID = 2286342108489097006L;
	/** The table for which this is the model. */
	protected XRefTable table;
	
	/**
	 * Creates the XRefTableModel.
	 * @param table	the table for which this model is used.
	 */
	public XRefTableModel(XRefTable table) {
		this.table = table;
	}
	
    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     * @return int the number of columns
     */
    public int getColumnCount() {
		return table.getColumnCount();
	}

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     * @return int the number of rows
     */
    public int getRowCount() {
		return table.getRowCount();
	}

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     * @param rowIndex int		a row number
     * @param columnIndex int	a column number
     * @return Object	an object corresponding with a cell in the table
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
    	return table.getValueAt(rowIndex, columnIndex);
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 * @param columnIndex a column number.
	 * @return the name of the column
	 */
	public String getColumnName(int columnIndex) {
		return table.getColumnName(columnIndex);
	}
}