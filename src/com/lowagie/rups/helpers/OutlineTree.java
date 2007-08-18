package com.lowagie.rups.helpers;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.interfaces.PdfTreeNodeSelector;
import com.lowagie.rups.nodetypes.OutlineTreeNode;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.text.pdf.PdfName;

public class OutlineTree extends JTree implements TreeSelectionListener {

	/** A serial version uid. */
	private static final long serialVersionUID = 5646572654823301007L;

	PdfTreeNodeSelector tree = null;
	OutlineTreeNode root = new OutlineTreeNode();
	
	public OutlineTree() {
		super();
		setCellRenderer(new PdfTreeCellRenderer());
		setModel(new DefaultTreeModel(root));
		addTreeSelectionListener(this);
	}

	public void loadOutlines(TreeNodeFactory factory, PdfTreeNodeSelector tree) {
		this.tree = tree;
		if (tree == null) {
			root = new OutlineTreeNode();
			setModel(new DefaultTreeModel(root));
		}
		else {
			PdfObjectTreeNode outline = tree.getOutlines();
			if (outline == null) {
				return;
			}
			root = new OutlineTreeNode();
			loadOutline(factory, root, factory.getChildNode(outline, PdfName.FIRST));
			setModel(new DefaultTreeModel(root));
		}
	}
	
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

	public void valueChanged(TreeSelectionEvent evt) {
		if (tree == null) return;
		OutlineTreeNode selectednode = (OutlineTreeNode)this.getLastSelectedPathComponent();
		PdfObjectTreeNode node = selectednode.getOutline();
		if (node != null)
			tree.selectNode(node);
	}
}
