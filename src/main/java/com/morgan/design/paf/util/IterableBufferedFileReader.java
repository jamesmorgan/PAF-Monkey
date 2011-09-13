package com.morgan.design.paf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * @author James Edward Morgan
 */
public class IterableBufferedFileReader implements Iterable<String> {

	private final BufferedReader reader;

	public IterableBufferedFileReader(final String filePath) throws Exception {
		this.reader = Files.newReader(new File(filePath), Charsets.UTF_8);
	}

	public IterableBufferedFileReader(final File file) throws Exception {
		this.reader = Files.newReader(file, Charsets.UTF_8);
	}

	public void close() throws IOException {
		this.reader.close();
	}

	@Override
	public Iterator<String> iterator() {
		return new FileIterator();
	}

	private class FileIterator implements Iterator<String> {

		private String currentLine;
		private boolean assignedNextLine;

		public FileIterator() {
			//
		}

		@Override
		public boolean hasNext() {
			if (this.assignedNextLine) {
				return this.currentLine != null;
			}

			try {
				this.currentLine = IterableBufferedFileReader.this.reader.readLine();
			}
			catch (final Exception ex) {
				this.currentLine = null;
				ex.printStackTrace();
			}
			finally {
				this.assignedNextLine = true;
			}

			return this.currentLine != null;
		}

		@Override
		public String next() {
			this.assignedNextLine = false;
			return this.currentLine;
		}

		@Override
		public void remove() {
			//
		}
	}
}
