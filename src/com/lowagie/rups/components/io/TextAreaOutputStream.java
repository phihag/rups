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

package com.lowagie.rups.components.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.text.Document;

public class TextAreaOutputStream extends OutputStream {
	
	protected JTextArea text;
	protected Document document;
	protected int offset;
	
	public TextAreaOutputStream(JTextArea text) throws IOException {
		this.text = text;
		clear();
	}

	public void clear() {
		text.setText(null);
		document = text.getDocument();
		offset = 0;
	}
	
	@Override
	public void write(int i) throws IOException {
		byte[] b = { (byte)i };
		write(b, 0, 1);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		String snippet = new String(b, off, len);
		text.insert(snippet, offset);
		offset += len - off;
	}

	@Override
	public void write(byte[] b) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		byte[] snippet = new byte[1024];
		int bytesread;
		while ((bytesread = bais.read(snippet)) > 0) {
			write(snippet, 0, bytesread);
		}
	}
	
}