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

package com.lowagie.rups.nodetypes;

import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

import com.lowagie.rups.interfaces.TreeNodeWithIcon;

public class XdpTreeNode extends DefaultMutableTreeNode
	implements TreeNodeWithIcon {

	/** A serial version UID. */
	private static final long serialVersionUID = -6431790925424045933L;

	public XdpTreeNode(Node node) {
		super(node);
		if (node instanceof Element) {
			Element element = (Element)node;
			addChildNodes(element.attributes());
		}
		if (node instanceof Branch) {
			Branch branch = (Branch) node;
			addChildNodes(branch.content());
		}
	}

	private void addChildNodes(List list) {
		for (Iterator i = list.iterator(); i.hasNext(); ) {
			Node n = (Node)i.next();
			if (n instanceof Namespace) continue;
			if (n instanceof Comment) continue;
			this.add(new XdpTreeNode(n));
		}
	}

	public Node getNode() {
    	return (Node)getUserObject();
	}
	
	public String toString() {
		Node node = getNode();
		if (node instanceof Element) {
			Element e = (Element)node;
			return e.getName();
		}
		if (node instanceof Attribute) {
			Attribute a = (Attribute)node;
			StringBuffer buf = new StringBuffer();
			buf.append(a.getName());
			buf.append("=\"");
			buf.append(a.getValue());
			buf.append("\"");
			return buf.toString();
		}
		if (node instanceof Text) {
			Text t = (Text)node;
			return t.getText();
		}
		if (node instanceof ProcessingInstruction) {
			ProcessingInstruction pi = (ProcessingInstruction)node;
			StringBuffer buf = new StringBuffer("<?");
			buf.append(pi.getName());
			buf.append(" ");
			buf.append(pi.getText());
			buf.append("?>");
			return buf.toString();
		}
		if (node instanceof Document) {
			return "XFA Document";
		}
		return getNode().toString();
	}
	
    /**
     * Getter for the icon that is to be used for this tree node.
     * @return	the icon corresponding with the object
     */
    public Icon getIcon() {
    	Node node = getNode();
    	if (node instanceof Attribute) {
    		return new ImageIcon(XdpTreeNode.class.getResource("icons/attribute.png"));
    	}
    	if (node instanceof Text) {
    		return new ImageIcon(XdpTreeNode.class.getResource("icons/text.png"));
    	}
    	if (node instanceof ProcessingInstruction) {
    		return new ImageIcon(XdpTreeNode.class.getResource("icons/pi.png"));
    	}
    	if (node instanceof Document) {
    		return new ImageIcon(XdpTreeNode.class.getResource("icons/xfa.png"));
    	}
		return new ImageIcon(XdpTreeNode.class.getResource("icons/tag.png"));
    }
}
