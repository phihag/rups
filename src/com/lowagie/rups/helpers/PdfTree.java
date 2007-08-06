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

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.nodetypes.PdfTrailerTreeNode;
import com.lowagie.text.pdf.PdfDictionary;

public class PdfTree extends JTree {

	/** a serial version UID */
	private static final long serialVersionUID = 7545804447512085734L;
	
	/** The root of the PDF tree. */
	protected PdfTrailerTreeNode root = new PdfTrailerTreeNode();
	
	/**
	 * Constructs a PDF tree.
	 */
	public PdfTree() {
		super();
		setCellRenderer(new PdfTreeCellRenderer());
		setModel(new DefaultTreeModel(root));
	}
	
	/**
	 * Sets or resets the root of this PDF tree.
	 */
	public void resetRoot(File file, TreeNodeFactory factory, PdfDictionary trailer) {
		root = new PdfTrailerTreeNode();
		root.setUserObject(file);
		root.setTrailer(trailer);
		factory.expandNode(root);
		setModel(new DefaultTreeModel(root));
		repaint();
	}
}
