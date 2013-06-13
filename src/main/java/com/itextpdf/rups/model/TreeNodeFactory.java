/*
 * $Id$
 *
 * Copyright 2007 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.itextpdf.rups.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNull;
import com.itextpdf.text.pdf.PdfObject;

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
		if (node.getChildCount() > 0) {
			return;
		}
		PdfObject object = node.getPdfObject();
		switch (object.type()) {
		case PdfObject.INDIRECT:
			PdfIndirectReference ref = (PdfIndirectReference)object;
			PdfObjectTreeNode leafI = getNode(ref.getNumber());
			addNodes(node, leafI);
			expandNode(leafI);
			return;
		case PdfObject.ARRAY:
			PdfArray array = (PdfArray)object;
			PdfObjectTreeNode leafA;
			for (Iterator<PdfObject> it = array.listIterator(); it.hasNext(); ) {
				leafA = PdfObjectTreeNode.getInstance(it.next());
				addNodes(node, leafA);
				expandNode(leafA);
			}
			return;
		case PdfObject.DICTIONARY:
		case PdfObject.STREAM:
			expandDictionaryNode(node);
			return;
		}
	}
	
	/**
	 * Expands a dictionary node.
	 * @param node	the parent node
	 */
	private void expandDictionaryNode(PdfObjectTreeNode node) {
		PdfDictionary dict = new PdfDictionary();
		dict.putAll((PdfDictionary)node.getPdfObject());
		getPagesFirst(node, dict);
		PdfObjectTreeNode leaf;
		for (PdfName element : dict.getKeys()) {
			leaf = PdfObjectTreeNode.getInstance(dict, element);
			addNodes(node, leaf);
			expandNode(leaf);
		}
	}
	
	/**
	 * Processes the page tree first.
	 * @param node	the parent node
	 * @param dict	a copy of the dictionary object in the parent node
	 */
	private void getPagesFirst(PdfObjectTreeNode node, PdfDictionary dict) {
		PdfDictionary pages = dict.getAsDict(PdfName.PAGES);
		if (pages != null) {
			PdfObjectTreeNode leaf = PdfObjectTreeNode.getInstance(dict, PdfName.PAGES);
			addNodes(node, leaf);
			expandNode(leaf);
			dict.remove(PdfName.PAGES);
		}
	}

	/**
	 * Finds a specific child of dictionary node.
	 * @param	node	the node with a dictionary among its children
	 * @param	key		the key of the item corresponding with the node we need
	 */
	@SuppressWarnings("unchecked")
    public PdfObjectTreeNode getChildNode(PdfObjectTreeNode node, PdfName key) {
		Enumeration<PdfObjectTreeNode> children = node.breadthFirstEnumeration();
		PdfObjectTreeNode child;
		while (children.hasMoreElements()) {
			child = children.nextElement();
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
