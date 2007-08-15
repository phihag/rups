package com.lowagie.rups.factories;

import javax.swing.SwingWorker;

import com.lowagie.rups.Rups;
import com.lowagie.swing.ui.ProgressFrame;
import com.lowagie.text.pdf.PdfReader;

/**
 * This class reads the indirect objects of a PDF file into an IndirectObjectStore.
 * It also updates the components of the RUSP Viewer.
 * The class extends SwingWorker because reading the XRef table of some PDF documents
 * (for instance the PDF Reference Manual) is a long running task.
 * While the XRef table is being read, the end user needs to see a progress bar.
 * Without using SwingWorker, this progress bar doesn't show up properly.
 * You can use only one instance of PdfWorker at a time.
 */
public class PdfWorker extends SwingWorker {

	/** Static variable that indicates if the PdfWorker is busy. */
	protected static boolean busy = false;
	/** The rups application using this RupsComponents class. */
	protected Rups rups;
	/** The object store */
	IndirectObjectStore objects;
	/** Keeps track of the progress */
	protected ProgressFrame progress;
	
	/**
	 * Creates a PdfWorker instance.
	 * @param	rups	the RUPS application that needs the PdfWorker
	 * @param	reader	the reader with the PDF document should be read
	 */
	private PdfWorker(Rups rups, PdfReader reader) {
		this.rups = rups;
		progress = new ProgressFrame("Reading PDF file");
		objects = new IndirectObjectStore(reader);
	}
	
	/**
	 * Reads the XRef table in background.
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Object doInBackground() throws Exception {
		int n = objects.getXRefMaximum();
		progress.setMessage("Reading the Cross-Reference table");
		progress.setTotal(n);
		while (objects.registerNextObject()) {
			progress.setValue(objects.getCurrent());
		}
		progress.setTotal(0);
		return null;
	}

	/**
	 * Updates the components in the RUPS viewer, using
	 * the indirect objects store.
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		progress.setMessage("Updating viewer");
		rups.update(objects);
		progress.dispose();
		busy = false;
	}

	/**
	 * Loads a PDF into a RUPS application.
	 * Note that each JVM can read only one PDF at a time.
	 */
	public static boolean loadPdf(Rups rups, PdfReader reader) {
		if (busy) return false;
		busy = true;
		new PdfWorker(rups, reader).execute();
		return true;
	}

}