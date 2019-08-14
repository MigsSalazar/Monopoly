package monopoly7.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * To anyone that finds this class and wants to refactor it, PLEASE BE VERY CAREFUL.
 * Yes, I know this looks very refactorable as there is indeed a lot of repeated code.
 * HOWEVER, Java types are very rigid with the bits used for each type so casting
 * between types can lead to unexpected consequences. This is why, although all the code
 * looks repeated (because it is) I kept them separate such that I wouldn't mess up the
 * true values of the data being passed. Besides that, this is a simple library that reads
 * in and writes out data from file IO streams. 
 * @author Miguel Salazar
 *
 */
public class BinaryIOUtil {

	/**
	 * Read in a character's or short's worth of bytes (2 bytes) from a FileInputStream.
	 * @param fis A binary input that contains at least 2 bytes to read
	 * @return a character found within the FileInputStream. Cast if a short is desired
	 * @throws IOException
	 */
	public static char readInChar( FileInputStream fis ) throws IOException{
		byte[] bytes = new byte[2];
		fis.read(bytes);
		//parse the bytes into a ints
		int upperInt = (((int)bytes[0] ) & 255 ) << 8;
		int lowerInt = ((int)bytes[1] ) & 255;
		//parse the ints to a char
		return (char)(upperInt | lowerInt);
	}
	
	/**
	 * Read in an integer's worth of bytes (4 bytes) from a FileInputStream.
	 * @param fis A binary input that contains at least 4 bytes to read
	 * @return an integer found within the FileInputStream
	 * @throws IOException
	 */
	public static int readInInt( FileInputStream fis ) throws IOException{
		int ret = 0;
		byte[] bytes = new byte[4];
		fis.read(bytes);
		//DO NOT REFACTOR OUT WITH OTHER METHODS
		//types in Java are very rigid. Despite being repeated code,
		//they can't be merged as that would sacrifice type integrity
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		return ret;
	}
	
	/**
	 * Read in a long's worth of bytes (8 bytes) from a FileInputStream.
	 * @param fis A binary input that contains at least 8 bytes to read
	 * @return a long found within the FileInputStream
	 * @throws IOException
	 */
	public static long readInLong( FileInputStream fis ) throws IOException{
		long ret = 0L;
		byte[] bytes = new byte[8];
		fis.read(bytes);
		//DO NOT REFACTOR OUT WITH OTHER METHODS
		//types in Java are very rigid. Despite being repeated code,
		//they can't be merged as that would sacrifice type integrity
		for( int i=0; i<bytes.length; i++ ){
			ret |= ( (((int)bytes[i]) & 255) << (8*(bytes.length - i - 1)));
		}
		return ret;
	}
	
	public static void writeOutChar( char data, FileOutputStream fos) throws IOException{
		int padding = (data < 256 && data > 0) ? 0 : 1;
		int pushables = (data < 256 && data > 0) ? 1 : 2;
		for( int i=0; i<padding; i++ ){
			fos.write('\0');
		}
		
		for( int i=pushables; i>0; i--){
			int pushing = ((byte)(data >> (8*(i-1)))) & 255;
			//log.atFinest().log("i:"+i+",data:"+data+",pushing:"+pushing);
			fos.write( pushing );
		}
		
	}

	public static void writeOutInt( int data, FileOutputStream fos) throws IOException {
		//log.atInfo().log("Beginning writeOutInt");
		int padding = 0;
		int pushables = 0;
		
		if( data < 256 && data > 0 ){
			padding = 3;
			pushables = 1;
		}else if( data < 65536 && data > 0 ){
			padding = 2;
			pushables = 2;
		}else{
			pushables = 4;
		}
		
		for( int i=0; i<padding; i++ ){
			fos.write('\0');
		}
		
		for( int i=pushables; i>0; i--){
			int pushing = ((byte)(data >> (8*(i-1)))) & 255;
			//log.atFinest().log("i:"+i+",data:"+data+",pushing:"+pushing);
			fos.write( pushing );
		}
	}

}
