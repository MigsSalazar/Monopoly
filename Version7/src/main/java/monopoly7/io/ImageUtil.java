package monopoly7.io;

import lombok.extern.flogger.Flogger;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@Flogger
public class ImageUtil {
	
	public static final String HOMEDIR = System.getProperty("user.dir");
	public static final String FILESEP = System.getProperty("file.separator");
	public static final String RESCDIR = HOMEDIR + FILESEP + "resources" + FILESEP;
	public static final String TEXTURES = RESCDIR + "textures" + FILESEP;
	public static final String FILE404 = RESCDIR + "textures" + FILESEP + "404.png";
	
	public static Image openImage( String dir ){
		File f = new File( TEXTURES + dir );
		return openImage(f);
	}
	
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
