package jacJarSoft.noteArkiv.base;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jacJarSoft.util.Version;

public class NoteArkivAppInfo {
	private static Logger logger = Logger.getLogger(NoteArkivAppInfo.class.getName());
	private static Version version;
	static {
		Properties props = new Properties();
		try {
			props.load(NoteArkivAppInfo.class.getResourceAsStream("/version.properties"));
			String versionStr = props.getProperty("NoteArkiv.version");
			try {
				version = new Version(versionStr);
			} catch (IllegalArgumentException e) { //Probably running from eclipse, try the other props
				Properties eclipseProps = new Properties();
				eclipseProps.load(NoteArkivAppInfo.class.getResourceAsStream("/eclipseVersion.properties"));
				versionStr = eclipseProps.getProperty("NoteArkiv.version");
				version = new Version(versionStr);
			}
			logger.info("Version: " + version.toString());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during app initiation", e);
		}
	}
	public static Version getVersion() {
		return version;
	}
}
