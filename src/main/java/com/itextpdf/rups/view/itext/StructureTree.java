/*
 * $Id:  $
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
package com.itextpdf.rups.view.itext;

import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import com.itextpdf.rups.controller.PdfReaderController;
import com.itextpdf.rups.model.ObjectLoader;
import com.itextpdf.rups.model.TreeNodeFactory;
import com.itextpdf.rups.view.icons.IconTreeCellRenderer;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfTrailerTreeNode;
import com.itextpdf.rups.view.itext.treenodes.StructureTreeNode;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

/**
 * A JTree visualizing information about the structure tree of
 * the PDF file (if any).
 */
public class StructureTree extends JTree implements TreeSelectionListener, Observer {
	
	/** Nodes in the FormTree correspond with nodes in the main PdfTree. */
	protected PdfReaderController controller;

	public StructureTree(PdfReaderController controller) {
		super();
		this.controller = controller;
		setCellRenderer(new IconTreeCellRenderer());
		setModel(new DefaultTreeModel(new StructureTreeNode()));
		addTreeSelectionListener(this);
	}
	
	public void update(Observable observable, Object obj) {
		if (obj == null) {
			setModel(new DefaultTreeModel(new StructureTreeNode()));
			repaint();
			return;
		}
		if (obj instanceof ObjectLoader) {
			ObjectLoader loader = (ObjectLoader)obj;
			TreeNodeFactory factory = loader.getNodes();
			PdfTrailerTreeNode trailer = controller.getPdfTree().getRoot();
			PdfObjectTreeNode catalog = factory.getChildNode(trailer, PdfName.ROOT);
			PdfObjectTreeNode structuretree = factory.getChildNode(catalog, PdfName.STRUCTTREEROOT);
			if (structuretree == null) {
				return;
			}
			StructureTreeNode root = new StructureTreeNode();
			PdfObjectTreeNode kids = factory.getChildNode(structuretree, PdfName.K);
			loadKids(factory, root, kids);
			setModel(new DefaultTreeModel(root));
		}
	}

	@SuppressWarnings("unchecked")
	private void loadKids(TreeNodeFactory factory, StructureTreeNode structure_node, PdfObjectTreeNode object_node) {
		if (object_node == null) {
			return;
		}
		factory.expandNode(object_node);
		if (object_node.isDictionary()) {
			PdfDictionary dict = (PdfDictionary) object_node.getPdfObject();
			if (dict.checkType(PdfName.MCR)) {
				structure_node.add(new StructureTreeNode(factory.getChildNode(object_node, PdfName.MCID), "bullet_go.png"));
				return;
			}
			if (dict.checkType(PdfName.OBJR)) {
				structure_node.add(new StructureTreeNode(factory.getChildNode(object_node, PdfName.OBJ), "bullet_go.png"));
				return;
			}
			StructureTreeNode leaf = new StructureTreeNode(object_node, "chart_organisation.png");
			structure_node.add(leaf);
			PdfObjectTreeNode kids = factory.getChildNode(object_node, PdfName.K);
			loadKids(factory, leaf, kids);
		}
		else if (object_node.isArray()) {
			Enumeration<PdfObjectTreeNode> children = object_node.children();
			while (children.hasMoreElements()) {
				loadKids(factory, structure_node, children.nextElement());
			}
		}
		else if (object_node.isIndirectReference()) {
			loadKids(factory, structure_node, (PdfObjectTreeNode)object_node.getFirstChild());
		}
		else  {
			StructureTreeNode leaf = new StructureTreeNode(object_node, "bullet_go.png");
			structure_node.add(leaf);
		}
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		if (controller == null)
			return;
		StructureTreeNode selectednode = (StructureTreeNode)this.getLastSelectedPathComponent();
		PdfObjectTreeNode node = selectednode.getCorrespondingPdfObjectNode();
		if (node != null)
			controller.selectNode(node);
	}

	/** A Serial version UID */
	private static final long serialVersionUID = 4205940990483252858L;
}
