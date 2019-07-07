package monopoly7.IOUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HighFidelityFileInputStream extends FileInputStream {
	
	public HighFidelityFileInputStream( File f ) throws FileNotFoundException {
		super(f);
	}

	public HighFidelityFileInputStream(FileDescriptor fdObj) {
		super(fdObj);
	}
	
	public HighFidelityFileInputStream( String s ) throws FileNotFoundException {
		super(s);
	}
	
	/*
	 * LEAVE THESE METHODS SEPARATE
	 * DESPITE THE REPEATED CODE, SINCE IT'S ALL BITWISE
	 * OPERATIONS WITH DIFFERENT PRIMITIVE TYPES, MIXING
	 * AND MATCHING DIFFERENT DATA TYPES WILL CORRUPT
	 * THE DATA. KEEP THEM SEPARATE FOR SAFETY
	 */
	
	public short readInShort() throws IOException {
		short ret = 0;
		byte[] bytes = new byte[2];
		read(bytes);
		
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		
		return ret;
	}

	public long readInLong() throws IOException {
		long ret = 0;
		byte[] bytes = new byte[8];
		read(bytes);
		
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		
		return ret;
	}
	
	public int readInInt() throws IOException {
		int ret = 0;
		byte[] bytes = new byte[4];
		read(bytes);
		
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		
		return ret;
	}

}
