package com.lowagie.rups.nodetypes;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;

public class OutlineTreeNode extends DefaultMutableTreeNode {

	/** A serial version uid */
	private static final long serialVersionUID = 5437651809665762952L;

	/** The corresponding tree node in the PdfTree. */
	protected PdfObjectTreeNode outline;
	
	public OutlineTreeNode() {
		super("Bookmarks");
	}
	
	public OutlineTreeNode(PdfObjectTreeNode node) {
		super();
		this.outline = node;
		PdfDictionary dict = (PdfDictionary)node.getPdfObject();
		this.setUserObject(dict.get(PdfName.TITLE));
	}

    /**
     * Getter for the icon that is to be used for this tree node.
     * @return	the icon corresponding with the object
     */
    public static Icon getIcon() {
		return new ImageIcon(OutlineTreeNode.class
			.getResource("icons/outline.png"));
    }

	public PdfObjectTreeNode getOutline() {
		return outline;
	}
}
