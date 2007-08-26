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

package com.lowagie.rups.factories;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfPagesTreeNode;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNull;
import com.lowagie.text.pdf.PdfObject;

/**
 * A factory that creates TreeNode objects corresponding with PDF objects.
 */
public class TreeNodeFactory {

	/** The factory that can produce all indirect objects. */
	protected IndirectObjectFactory objects;
	/** An list containing the nodes of every indirect object. */
	protected ArrayList<PdfObjectTreeNode> nodes = new ArrayList<PdfObjectTreeNode>();
	
	/**
	 * Creates a factory that can produce TreeNode objects
	 * corresponding with PDF objects.
	 * @param objects	a factory that can produce all the indirect objects of a PDF file.
	 */
	public TreeNodeFactory(IndirectObjectFactory objects) {
		this.objects = objects;
		for (int i = 0; i < objects.size(); i++) {
			int ref = objects.getRefByIndex(i);
			nodes.add(PdfObjectTreeNode.getInstance(PdfNull.PDFNULL, ref));
		}
	}
	
	/**
	 * Gets a TreeNode for an indirect objects.
	 * @param ref	the reference number of the indirect object.
	 * @return	the TreeNode representing the PDF object
	 */
	public PdfObjectTreeNode getNode(int ref) {
		int idx = objects.getIndexByRef(ref);
		PdfObjectTreeNode node = nodes.get(idx);
		if (node.getPdfObject().isNull()) {
			node = PdfObjectTreeNode.getInstance(objects.loadObjectByReference(ref), ref);
			nodes.set(idx, node);
		}
		return node;
	}
	
	/**
	 * Creates the Child TreeNode objects for a PDF object TreeNode.
	 * @param node	the parent node
	 */
	public void expandNode(PdfObjectTreeNode node) {
		if (node.getChildCount() > 0)
			return;
		PdfObject object = node.getPdfObject();
		PdfObjectTreeNode leaf;
		switch (object.type()) {
		case PdfObject.INDIRECT:
			PdfIndirectReference ref = (PdfIndirectReference)object;
			leaf = getNode(ref.getNumber());
			addNodes(node, leaf);
			if (leaf instanceof PdfPagesTreeNode)
				expandNode(leaf);
			return;
		case PdfObject.ARRAY:
			PdfArray array = (PdfArray)object;
			for (Iterator it = array.getArrayList().iterator(); it.hasNext(); ) {
				leaf = PdfObjectTreeNode.getInstance((PdfObject)it.next());
				addNodes(node, leaf);
				expandNode(leaf);
			}
			return;
		case PdfObject.DICTIONARY:
		case PdfObject.STREAM:
			PdfDictionary dict = (PdfDictionary)object;
			for (Iterator it = dict.getKeys().iterator(); it.hasNext(); ) {
				leaf = PdfObjectTreeNode.getInstance(dict, (PdfName)it.next());
				addNodes(node, leaf);
				expandNode(leaf);
			}
			return;
		}
	}
	
	/**
	 * Finds a specific child of dictionary node.
	 * @param	node	the node with a dictionary among its children
	 * @param	key		the key of the item corresponding with the node we need
	 */
	public PdfObjectTreeNode getChildNode(PdfObjectTreeNode node, PdfName key) {
		Enumeration children = node.breadthFirstEnumeration();
		PdfObjectTreeNode child;
		while (children.hasMoreElements()) {
			child = (PdfObjectTreeNode)children.nextElement();
			if (child.isDictionaryNode(key)) {
				if (child.isIndirectReference()) {
					expandNode(child);
					child = (PdfObjectTreeNode)child.getFirstChild();
				}
				expandNode(child);
				return child;
			}
		}
		return null;
	}
	
	/**
	 * Tries adding a child node to a parent node without
	 * throwing an exception. Normally, if the child node is already
	 * added as one of the ancestors, an IllegalArgumentException is
	 * thrown (to avoid an endless loop). Loops like this are allowed
	 * in PDF, not in a JTree.
	 * @param parent	the parent node
	 * @param child		a child node
	 */
	private void addNodes(PdfObjectTreeNode parent, PdfObjectTreeNode child) {
		try {
			parent.add(child);
		}
		catch(IllegalArgumentException iae) {
			parent.setRecursive(true);
		}
	}
}