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

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import com.lowagie.rups.Ruxfa;
import com.lowagie.rups.components.models.PdfTreeCellRenderer;
import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.interfaces.PdfTreeNodeSelector;
import com.lowagie.rups.interfaces.XfaInterface;
import com.lowagie.rups.nodetypes.FormTreeNode;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.XfaTreeNode;
import com.lowagie.text.pdf.PdfName;

/**
 * A JTree visualizing information about the Interactive Form of the
 * PDF file (if any). Normally shows a tree view of the field hierarchy
 * and individual XDP packets. 
 */
public class FormTree extends JTree implements TreeSelectionListener {

	/** A serial version UID. */
	private static final long serialVersionUID = -3584003547303700407L;

	/** Nodes in the FormTree correspond with nodes in the main PdfTree. */
	protected PdfTreeNodeSelector tree = null;
	/** Frame that show the XFA resource (if any) */
	protected Ruxfa ruxfa = null;
	
	/**
	 * Creates a new FormTree.
	 */
	public FormTree() {
		super();
		setCellRenderer(new PdfTreeCellRenderer());
		setModel(new DefaultTreeModel(new FormTreeNode()));
		addTreeSelectionListener(this);
	}

	/**
	 * Loads the fields of a PDF document into the FormTree.
	 * @param	factory	a factory that can produce new PDF object nodes
	 * @param	tree	the main PdfTree
	 */
	public void loadFields(TreeNodeFactory factory, PdfTreeNodeSelector tree) {
		this.tree = tree;
		if (tree == null) {
			setModel(new DefaultTreeModel(new FormTreeNode()));
			ruxfa = null;
		}
		else {
			PdfObjectTreeNode form = tree.getForm();
			if (form == null) {
				return;
			}
			FormTreeNode root = new FormTreeNode();
			PdfObjectTreeNode fields = factory.getChildNode(form, PdfName.FIELDS);
			if (fields != null) {
				FormTreeNode node = new FormTreeNode(fields);
				node.setUserObject("Fields");
				loadFields(factory, node, fields);
				root.add(node);
			}
			PdfObjectTreeNode xfa = factory.getChildNode(form, PdfName.XFA);
			if (xfa != null) {
				XfaTreeNode node = new XfaTreeNode(xfa);
				node.setUserObject("XFA");
				loadXfa(factory, node, xfa);
				root.add(node);
				ruxfa = new Ruxfa();
			}
			setModel(new DefaultTreeModel(root));
		}
	}

	/**
	 * Method that can be used recursively to load the fields hierarchy into the tree.
	 * @param	factory		a factory that can produce new PDF object nodes
	 * @param	form_node	the parent node in the form tree
	 * @param	object_node	the object node that will be used to create a child node
	 */
	private void loadFields(TreeNodeFactory factory, FormTreeNode form_node, PdfObjectTreeNode object_node) {
		if (object_node == null)
			return;
		factory.expandNode(object_node);
		if (object_node.isIndirectReference()) {
			loadFields(factory, form_node, (PdfObjectTreeNode)object_node.getFirstChild());
		}
		else if (object_node.isArray()) {
			Enumeration children = object_node.children();
			while (children.hasMoreElements()) {
				loadFields(factory, form_node, (PdfObjectTreeNode)children.nextElement());
			}
		}
		else if (object_node.isDictionary()) {
			FormTreeNode leaf = new FormTreeNode(object_node);
			form_node.add(leaf);
			PdfObjectTreeNode kids = factory.getChildNode(object_node, PdfName.KIDS);
			loadFields(factory, leaf, kids);
		}
	}

	/**
	 * Method that will load the nodes that refer to XFA streams.
	 * @param	form_node	the parent node in the form tree
	 * @param	object_node	the object node that will be used to create a child node
	 */
	private void loadXfa(TreeNodeFactory factory, XfaTreeNode form_node, PdfObjectTreeNode object_node) {
		if (object_node == null)
			return;
		factory.expandNode(object_node);
		if (object_node.isIndirectReference()) {
			loadXfa(factory, form_node, (PdfObjectTreeNode)object_node.getFirstChild());
		}
		else if (object_node.isArray()) {
			Enumeration children = object_node.children();
			PdfObjectTreeNode key;
			PdfObjectTreeNode value;
			while (children.hasMoreElements()) {
				key = (PdfObjectTreeNode)children.nextElement();
				value = (PdfObjectTreeNode)children.nextElement();
				if (value.isIndirectReference()) {
					factory.expandNode(value);
					value = (PdfObjectTreeNode)value.getFirstChild();
				}
				form_node.addPacket(key.getPdfObject().toString(), value);
			}
		}
		else if (object_node.isStream()) {
			form_node.addPacket("xdp", object_node);
		}
	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent evt) {
		if (tree == null)
			return;
		FormTreeNode selectednode = (FormTreeNode)this.getLastSelectedPathComponent();
		if (selectednode instanceof XfaInterface && ruxfa != null) {
			ruxfa.loadXfa((XfaInterface)selectednode);
			ruxfa.setVisible(true);
		}
		PdfObjectTreeNode node = selectednode.getCorrespondingPdfObjectNode();
		if (node != null)
			tree.selectNode(node);
	}
}
