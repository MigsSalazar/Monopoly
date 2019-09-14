package monopoly7.io;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lombok.extern.flogger.Flogger;

/**
 * In a lot of ways, this is a logging and directory managing
 * wrapper class for ImageIO. This class handles try/catches for
 * ImageIO's functions as well as defining needed directories
 * used for the project.
 * 
 * @author Miguel Salazar
 *
 */
@Flogger
public class ImageUtil {
	
	/**
	 * Copies directly from the "user.dir" property within the System Properties
	 */
	public static String HOMEDIR = System.getProperty("user.dir");
	/**
	 * Copies directly from the "file.separator" property within the System Properties
	 */
	public static String FILESEP = System.getProperty("file.separator");
	/**
	 * Short for "resource directory", it defines the resource directory from within the home directory
	 */
	public static String RESCDIR = HOMEDIR + "resources" + FILESEP;
	/**
	 * Appends to the resource directory the texture directory used for defining different textures
	 */
	public static String TEXTURES = RESCDIR + "textures" + FILESEP;
	/**
	 * Selects the 404 image file used when an image cannot be found given the path
	 */
	public static String FILE404 = TEXTURES + "404.png";
	
	public static Image openImage( String... dir ){
		StringBuilder sb = new StringBuilder();
		//sb.append(TEXTURES);
		for( int i=0; i<dir.length; i++ ){
			sb.append(dir[i]);
			if( i != dir.length - 1 ){
				sb.append(FILESEP);
			}
		}
		log.atInfo().log("Final destination: %s", sb.toString());
		File f = new File( sb.toString() );
		return openImage(f);
	}
	
	/**
	 * Parses the passed in path into a File object and then calls {@link #openImage(File)}
	 * @param dir	an array of a separated file path. This is done to handle file path separators
	 * without worrying about the OS
	 * @return	the requested image from the passed in file
	 */
	public static Image openImageInTextures( String... dir ){
		String[] maker = new String[ dir.length+1 ];
		maker[0] = TEXTURES;
		for( int i=0; i<dir.length; i++ ){
			maker[i+1] = dir[i];
		}
		return openImage( maker );
	}
	
	/**
	 * Generates an Image file from the passed in file. Upon failure, an exception
	 * is thrown and a 404 image is returned instead.
	 * @param f	file of the image to generate
	 * @return	the requested image. If reading the file fails, a 404 image is returned instead
	 */
	public static Image openImage( File f ){
		log.atInfo().log("Open Image: file to load="+f.getAbsolutePath());
		try{
			log.atFinest().log("currently in first try: "+f.getCanonicalPath());
			log.atFinest().log("file exists: "+ f.exists());
			if( f.exists() ){
				return ImageIO.read(f);		
			}
		} catch (IOException e) {
			log.atWarning().withCause(e).log("Failed to load image. Returning 404 img", e);
		}
		try {
			return ImageIO.read( new File(FILE404) );
		} catch (IOException e1) {
			log.atSevere().withCause(e1).log( "Failed to find 404 img. POSSIBLY FATAL", e1 );
		}
		
		return null;
	}
	

}
