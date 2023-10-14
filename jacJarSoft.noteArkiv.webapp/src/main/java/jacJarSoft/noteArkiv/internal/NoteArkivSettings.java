package jacJarSoft.noteArkiv.internal;

import jacJarSoft.util.PropertyList;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="NoteArkivSettings")
public class NoteArkivSettings {
	private JSAppSettings jsAppSettings = new JSAppSettings();
	private PropertyList persistenceProperties;
	private Integer chacheControlMaxAge = 3600;
	private String filesDirectory;
	
	public PropertyList getPersistenceProperties() {
		return persistenceProperties;
	}
	public void setPersistenceProperties(PropertyList persistenceProperties) {
		this.persistenceProperties = persistenceProperties;
	}
	public JSAppSettings getJsAppSettings() {
		return jsAppSettings;
	}
	public void setJsAppSettings(JSAppSettings jsAppSettings) {
		this.jsAppSettings = jsAppSettings;
	}
	public Integer getChacheControlMaxAge() {
		return chacheControlMaxAge;
	}
	public void setChacheControlMaxAge(Integer chacheControlMaxAge) {
		this.chacheControlMaxAge = chacheControlMaxAge;
	}
	public String getFilesDirectory() {
		return filesDirectory;
	}
	public void setFilesDirectory(String filesDirectory) {
		this.filesDirectory = filesDirectory;
	}
}
