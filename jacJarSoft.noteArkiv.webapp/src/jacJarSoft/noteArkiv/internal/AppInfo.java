package jacJarSoft.noteArkiv.internal;

import javax.xml.bind.annotation.XmlRootElement;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;

@XmlRootElement(name="appInfo")
public class AppInfo {
	private String version;
	private JSAppSettings appSettings;

	public AppInfo()
	{
		version = NoteArkivAppInfo.getVersion().toString();
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public JSAppSettings getAppSettings() {
		return appSettings;
	}
	public void setAppSettings(JSAppSettings jsAppSettings) {
		this.appSettings = jsAppSettings;
	}
}
