package monopoly7.IOUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HighFidelityFileOutputStream extends FileOutputStream {
	
	public HighFidelityFileOutputStream( File f ) throws FileNotFoundException {
		super(f);
	}
	
	public HighFidelityFileOutputStream( File f, boolean b ) throws FileNotFoundException {
		super(f, b);
	}

	public HighFidelityFileOutputStream(FileDescriptor fdObj) {
		super(fdObj);
	}
	
	public HighFidelityFileOutputStream( String s ) throws FileNotFoundException{
		super(s);
	}

	public HighFidelityFileOutputStream( String s, boolean b ) throws FileNotFoundException{
		super(s, b);
	}
	
	public void writeOutData( int padding, int pushables, long data ) throws IOException {
		for( int i=0; i<padding; i++ ){
			write('\0');
		}
		
		for( int i=pushables; i>0; i--){
			int pushing = ((byte)(data >> (8*(i-1)))) & 255;
			//System.out.println("i:"+i+",data:"+data+",pushing:"+pushing);
			write( pushing );
		}
	}
	
	public void writeOutLong( long data ) throws IOException {
		if( data < 256 && data > 0 ){
			writeOutData( 7, 1, data );
		}else if( data < 65536 && data > 0 ){
			writeOutData( 6, 2, data );
		}else if( data < 4294967296L && data > 0 ){
			writeOutData( 4, 4, data );
		}else{
			writeOutData( 0, 8, data );
		}
	}
	
	public void writeOutShort( short data ) throws IOException {
		if( data < 256 && data > 0 ){
			writeOutData( 1, 1, data );
		}else{
			writeOutData( 0, 2, data );
		}
	}
	
	public void writeOutInt( int data ) throws IOException {
		if( data < 256 && data > 0 ){
			writeOutData( 3, 1, data );
		}else if( data < 65536 && data > 0 ){
			writeOutData( 2, 2, data );
		}else{
			writeOutData( 0, 4, data );
		}
	}
}
