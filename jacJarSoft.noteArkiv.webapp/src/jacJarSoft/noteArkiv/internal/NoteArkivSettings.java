package jacJarSoft.noteArkiv.internal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="NoteArkivSettings")
public class NoteArkivSettings {
	private String backgroundImageUrl;
	private String applicationTitle;
	private String applicationWelcomeTitle;
	private String applicationWelcomeHtml;
	
	public String getBackgroundImageUrl() {
		return backgroundImageUrl;
	}
	public void setBackgroundImageUrl(String backgroundImageUrl) {
		this.backgroundImageUrl = backgroundImageUrl;
	}
	public String getApplicationTitle() {
		return applicationTitle;
	}
	public void setApplicationTitle(String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}
	public String getApplicationWelcomeTitle() {
		return applicationWelcomeTitle;
	}
	public void setApplicationWelcomeTitle(String applicationWelcomeTitle) {
		this.applicationWelcomeTitle = applicationWelcomeTitle;
	}
	public String getApplicationWelcomeHtml() {
		return applicationWelcomeHtml;
	}
	public void setApplicationWelcomeHtml(String applicationWelcomeHtml) {
		this.applicationWelcomeHtml = applicationWelcomeHtml;
	}
}
