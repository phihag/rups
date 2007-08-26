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

package com.lowagie.rups.interfaces;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An object that implements the XfaInterface can write (part of) an
 * XFA resource to an OutputStream.
 */
public interface XfaInterface {
	/**
	 * Writes (part of) an XFA resource to an OutputStream.
	 * If key is <code>null</code>, the complete resource is written;
	 * if key refers to an individual package, this package only is
	 * written to the OutputStream.
	 * @param os	the OutputStream to which the XML is written.
	 * @param key	the key of an individual package (can be null if the complete XML file is needed)
	 * @throws IOException	usual exception when there's a problem writing to an OutputStream
	 */
	public void writeTo(OutputStream os, String key) throws IOException;
}
