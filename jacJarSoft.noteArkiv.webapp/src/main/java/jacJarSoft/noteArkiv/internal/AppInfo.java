package jacJarSoft.noteArkiv.internal;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="appInfo")
public class AppInfo {
	private String version;
	private JSAppSettings appSettings;
	private String copyrightMsg;
	
	public AppInfo()
	{
		version = NoteArkivAppInfo.getVersion().toString();
		copyrightMsg = NoteArkivAppInfo.COPYRIGHT;
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
	public String getCopyrightMsg() {
		return copyrightMsg;
	}
	public void setCopyrightMsg(String copyrightMsg) {
		this.copyrightMsg = copyrightMsg;
	}
}
