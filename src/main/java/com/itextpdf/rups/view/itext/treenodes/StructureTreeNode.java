package com.itextpdf.rups.view.itext.treenodes;

import com.itextpdf.rups.view.icons.IconTreeNode;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;

public class StructureTreeNode extends IconTreeNode {

	/** The corresponding tree node in the PdfTree. */
	protected PdfObjectTreeNode object_node;

	/**
	 * Creates the root node for the structure tree.
	 */
	public StructureTreeNode() {
		super("chart_organisation.png", "Structure Tree");
	}
	
	/**
	 */
	public StructureTreeNode(PdfObjectTreeNode node, String icon) {
		super(icon);
		this.object_node = node;
		if (node.isDictionary()) {
			PdfDictionary dict = (PdfDictionary)node.getPdfObject();
			if (dict.get(PdfName.TYPE) == null || dict.checkType(PdfName.STRUCTELEM)) {
				StringBuffer buf = new StringBuffer(dict.get(PdfName.S).toString());
				if (dict.get(PdfName.T) != null) {
					buf.append(" -> ");
					buf.append(dict.get(PdfName.T).toString());
				}
				this.setUserObject(buf.toString());
				return;
			}
		}
		this.setUserObject(node);
	}

    /**
     * Gets the node in the PdfTree that corresponds with this
     * OutlineTreeNode.
     * @return	a PdfObjectTreeNode in the PdfTree
     */
	public PdfObjectTreeNode getCorrespondingPdfObjectNode() {
		return object_node;
	}

	/** Serial version UID */
	private static final long serialVersionUID = 6822664148126160723L;

}
