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

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import com.lowagie.rups.components.models.PdfTreeCellRenderer;
import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.interfaces.PdfTreeNodeSelector;
import com.lowagie.rups.nodetypes.OutlineTreeNode;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.text.pdf.PdfName;

/**
 * A JTree visualizing information about the outlines (aka bookmarks) of
 * the PDF file (if any).
 */
public class OutlineTree extends JTree implements TreeSelectionListener {

	/** A serial version uid. */
	private static final long serialVersionUID = 5646572654823301007L;

	/** Nodes in the OutlineTree correspond with nodes in the main PdfTree. */
	PdfTreeNodeSelector tree = null;
	
	/** Creates a new outline tree. */
	public OutlineTree() {
		super();
		setCellRenderer(new PdfTreeCellRenderer());
		setModel(new DefaultTreeModel(new OutlineTreeNode()));
		addTreeSelectionListener(this);
	}

	/**
	 * Loads the outlines of a PDF document into the OutlineTree.
	 * @param	factory	a factory that can produce new PDF object nodes
	 * @param	tree	the main PdfTree
	 */
	public void loadOutlines(TreeNodeFactory factory, PdfTreeNodeSelector tree) {
		this.tree = tree;
		if (tree == null) {
			setModel(new DefaultTreeModel(new OutlineTreeNode()));
		}
		else {
			PdfObjectTreeNode outline = tree.getOutlines();
			if (outline == null) {
				return;
			}
			OutlineTreeNode root = new OutlineTreeNode();
			loadOutline(factory, root, factory.getChildNode(outline, PdfName.FIRST));
			setModel(new DefaultTreeModel(root));
		}
	}
	
	/**
	 * Method that can be used recursively to load the outline hierarchy into the tree.
	 */
	private void loadOutline(TreeNodeFactory factory, OutlineTreeNode parent, PdfObjectTreeNode child) {
		OutlineTreeNode childnode = new OutlineTreeNode(child);
		parent.add(childnode);
		PdfObjectTreeNode first = factory.getChildNode(child, PdfName.FIRST);
		if (first != null) {
			loadOutline(factory, childnode, first);
		}
		PdfObjectTreeNode next = factory.getChildNode(child, PdfName.NEXT);
		if (next != null) {
			loadOutline(factory, parent, next);
		}
	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent evt) {
		if (tree == null)
			return;
		OutlineTreeNode selectednode = (OutlineTreeNode)this.getLastSelectedPathComponent();
		PdfObjectTreeNode node = selectednode.getCorrespondingPdfObjectNode();
		if (node != null)
			tree.selectNode(node);
	}
}
