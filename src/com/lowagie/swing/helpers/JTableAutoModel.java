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

package com.lowagie.swing.helpers;

import javax.swing.table.AbstractTableModel;

/**
 * A reusable TableModel class for tables that implement the
 * JTableAutoModelInterface.
 */
public class JTableAutoModel extends AbstractTableModel {
	
	/** The serial version uid. */
	private static final long serialVersionUID = -2229431581745521537L;
	/** The table that knows how to model itself. */
	protected JTableAutoModelInterface table;

	/**
	 * Constructs an auto model for a JTable.
	 * @param	table	a JTable that knows information about its model.
	 */
	public JTableAutoModel(JTableAutoModelInterface table) {
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
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 * @param columnIndex a column number.
	 * @return the name of the column
	 */
	public String getColumnName(int columnIndex) {
		return table.getColumnName(columnIndex);
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
}