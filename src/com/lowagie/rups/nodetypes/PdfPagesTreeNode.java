package com.lowagie.rups.nodetypes;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.lowagie.text.pdf.PdfDictionary;

public class PdfPagesTreeNode extends PdfObjectTreeNode {

	/** a serial version uid */
	private static final long serialVersionUID = 4527774449030791503L;

	/**
	 * Creates a tree node for a Pages dictionary.
	 * @param	object	a PdfDictionary of type pages.
	 */
	public PdfPagesTreeNode(PdfDictionary object) {
		super(object);
	}

    /**
     * Getter for the icon that is to be used for this tree node.
     * @return	the icon corresponding with the object
     */
    public Icon getIcon() {
		return new ImageIcon(PdfPagesTreeNode.class
			.getResource("icons/pages.png"));
    }
}
