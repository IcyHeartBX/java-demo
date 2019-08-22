package com.pix.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

public class CloseUtils {

	public static void close(InputStream is) {
		if(is == null)
			return;
		
		try {
			is.close();
		} catch (Exception e) {

		}
	}

	public static void close(OutputStream os) {
		if(os == null)
			return;
		
		try {
			os.flush();
			os.close();
		} catch (Exception e) {

		}
	}

	public static void close(RandomAccessFile randomAccessFile) {
		if (randomAccessFile == null)
			return;
		
		try {
			randomAccessFile.close();
		} catch (Exception e) {

		}
	}
	
	public static void close(Writer writer) {
		if (writer == null)
			return;
		
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {

		}
	}

	public static void close(Reader reader) {
		if(reader == null)
			return;
		
		try {
			reader.close();
		} catch (Exception e) {

		}
	}
}
