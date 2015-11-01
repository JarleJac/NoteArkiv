package jacJarSoft.noteArkiv.model;

import javax.xml.bind.annotation.XmlRootElement;

import jacJarSoft.noteArkiv.base.NoteArkivAppInfo;

@XmlRootElement(name="appInfo")
public class AppInfo {
	private String version;

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
}
