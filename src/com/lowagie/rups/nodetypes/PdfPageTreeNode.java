package com.lowagie.rups.nodetypes;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.lowagie.text.pdf.PdfDictionary;

public class PdfPageTreeNode extends PdfObjectTreeNode {

	/**	A serial version uid */
	private static final long serialVersionUID = 3747496604295843783L;

	/**
	 * Creates a tree node for a Pages dictionary.
	 * @param	object	a PdfDictionary of type pages.
	 */
	public PdfPageTreeNode(PdfDictionary object) {
		super(object);
	}

    /**
     * Getter for the icon that is to be used for this tree node.
     * @return	the icon corresponding with the object
     */
    public Icon getIcon() {
		return new ImageIcon(PdfPageTreeNode.class
			.getResource("icons/page.png"));
    }
}
