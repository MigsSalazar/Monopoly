package monopoly7.io;

import lombok.extern.log4j.Log4j2;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

@Log4j2
public class ImageUtil {
	
	private static final String HOMEDIR = System.getProperty("user.dir");
	private static final String FILESEP = System.getProperty("file.separator");
	private static final String RESCDIR = HOMEDIR + FILESEP + HOMEDIR + "resource" + FILESEP;
	private static final String FILE404 = RESCDIR + "textures" + FILESEP + "404.png";
	
	public static Image openImage( String dir ){
		File f = new File( RESCDIR + dir );
		return openImage(f);
	}
	
	public static Image openImage( File f ){
		try{
			if( !f.exists() ){
				return ImageIO.read(f);		
			}
		} catch (IOException e) {
			log.error("Failed to load image. Returning 404 img", e);
		}
		try {
			return ImageIO.read( new File(FILE404) );
		} catch (IOException e1) {
			log.error( "Failed to find 404 img. POSSIBLY FATAL", e1 );
		}
		
		return null;
	}
	

}
