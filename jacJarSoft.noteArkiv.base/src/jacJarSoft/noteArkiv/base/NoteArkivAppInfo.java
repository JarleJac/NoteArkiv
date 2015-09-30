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
			version = new Version(versionStr);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during app initiation", e);
		}
	}
	public static Version getVersion() {
		return version;
	}
}
