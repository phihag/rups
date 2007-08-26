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

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;

import com.lowagie.rups.factories.IndirectObjectFactory;
import com.lowagie.rups.interfaces.PdfObjectRenderer;
import com.lowagie.swing.helpers.JTableAutoModel;
import com.lowagie.swing.helpers.JTableAutoModelInterface;
import com.lowagie.text.pdf.PdfNull;
import com.lowagie.text.pdf.PdfObject;

/**
 * A JTable that shows the indirect objects of a PDF xref table.
 */
public class XRefTable extends JTable implements JTableAutoModelInterface {

	/** A serial version UID. */
	private static final long serialVersionUID = -382184619041375537L;
	
	/** The factory that can produce all the indirect objects. */
	protected IndirectObjectFactory objects;
	/** The renderer that will render an object when selected in the table. */
	protected PdfObjectRenderer renderer;
	
	/** Creates a JTable visualizing xref table. */
	public XRefTable() {
		super();
	}
	
	/**
	 * Adds the indirect objects to the table.
	 * @param objects	the objects factory
	 */
	public void setObjects(IndirectObjectFactory objects, PdfObjectRenderer renderer) {
		this.objects = objects;
		this.renderer = renderer;
		if (objects == null) {
			renderer = null;
			repaint();
		}
		else {
			setModel(new JTableAutoModel(this));
			TableColumn col= getColumnModel().getColumn(0);
			col.setPreferredWidth(5);
		}
	}
	
	/**
	 * @see javax.swing.JTable#getColumnCount()
	 */
	public int getColumnCount() {
		return 2;
	}
	
	/**
	 * @see javax.swing.JTable#getRowCount()
	 */
	public int getRowCount() {
		if (objects == null) return 0;
		return objects.size();
	}

    /**
     * @see javax.swing.JTable#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getObjectReferenceByRow(rowIndex);
		case 1:
			return getObjectDescriptionByRow(rowIndex);
		default:
			return null;
		}
	}
	
	/**
	 * Gets the reference number of an indirect object
	 * based on the row index.
	 * @param rowIndex	a row number
	 * @return	a reference number
	 */
	protected int getObjectReferenceByRow(int rowIndex) {
		return objects.getRefByIndex(rowIndex);
	}
	
	/**
	 * Gets the object that is shown in a row.
	 * @param rowIndex	the row number containing the object
	 * @return	a PDF object
	 */
	protected String getObjectDescriptionByRow(int rowIndex) {
		PdfObject object = objects.getObjectByIndex(rowIndex);
		if (object instanceof PdfNull)
			return "Indirect object";
		return object.toString();
	}
	
	/**
	 * @see javax.swing.JTable#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Number";
		case 1:
			return "Object";
		default:
			return null;
		}
	}

	/**
	 * @see javax.swing.JTable#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent evt) {
		if (evt != null)
			super.valueChanged(evt);
		if (renderer != null)
			renderer.render(getObjectByRow(this.getSelectedRow()));
	}
	
	/**
	 * Gets the object that is shown in a row.
	 * @param rowIndex	the row number containing the object
	 * @return	a PDF object
	 */
	protected PdfObject getObjectByRow(int rowIndex) {
		return objects.loadObjectByReference(getObjectReferenceByRow(rowIndex));
	}
	
	/**
	 * Selects a row containing information about an indirect object.
	 * @param ref	the reference number of the indirect object
	 */
	public void selectRowByReference(int ref) {
		int row = objects.getIndexByRef(ref);
		setRowSelectionInterval(row, row);
		scrollRectToVisible(getCellRect(row, 1, true));
		valueChanged(null);
	}
}