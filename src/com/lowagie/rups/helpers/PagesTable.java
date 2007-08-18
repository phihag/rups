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

package com.lowagie.rups.helpers;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import com.lowagie.rups.interfaces.PdfTreeNodeSelector;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfPageTreeNode;
import com.lowagie.swing.helpers.JTableAutoModel;
import com.lowagie.swing.helpers.JTableAutoModelInterface;

public class PagesTable extends JTable implements JTableAutoModelInterface {

	private static final long serialVersionUID = -6523261089453886508L;

	/** A list with page nodes. */
	protected ArrayList<PdfPageTreeNode> list = new ArrayList<PdfPageTreeNode>();
	/** The tree that contains all the page nodes. */
	protected PdfTreeNodeSelector tree;
	
	/** Creates an empty pages table. */
	public PagesTable() {
	}
	
	/**
	 * Loads the pages from a PdfReader.
	 * @param tree			the tree with all the pdf object
	 * @param pagelabels	array with page labels
	 */
	public void loadPages(PdfTreeNodeSelector tree, String[] pagelabels) {
		if (tree == null) {
			list = new ArrayList<PdfPageTreeNode>();
		}
		else {
			this.tree = tree;
			int i = 0;
			Enumeration p = tree.getPages().depthFirstEnumeration();
			PdfObjectTreeNode  child;
			StringBuffer buf;
			while (p.hasMoreElements()) {
				child = (PdfObjectTreeNode)p.nextElement();
				if (child instanceof PdfPageTreeNode) {
					buf = new StringBuffer("Page ");
					buf.append(++i);
					if (pagelabels != null) {
						buf.append(" ( ");
						buf.append(pagelabels[i - 1]);
						buf.append(" )");
					}
					child.setUserObject(buf.toString());
					list.add((PdfPageTreeNode)child);
				}
			}
		}
		setModel(new JTableAutoModel(this));
	}

	/**
	 * Set the tree containing the page tree structure.
	 * @param	tree	a tree node selector instance
	 */
	public void setTree(PdfTreeNodeSelector tree) {
		this.tree = tree;
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
		return list.size();
	}

    /**
     * @see javax.swing.JTable#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
    	if (getRowCount() == 0) return null;
		switch (columnIndex) {
		case 0:
			return "Object " + list.get(rowIndex).getNumber();
		case 1:
			return list.get(rowIndex);
		}
		return null;
	}
	/**
	 * @see javax.swing.JTable#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Object";
		case 1:
			return "Page";
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
		if (tree != null && getRowCount() > 0)
			tree.selectNode(list.get(getSelectedRow()));
	}
}