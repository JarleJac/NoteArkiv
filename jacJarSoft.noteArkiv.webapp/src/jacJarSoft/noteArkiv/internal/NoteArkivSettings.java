package jacJarSoft.noteArkiv.internal;

import javax.xml.bind.annotation.XmlRootElement;

import jacJarSoft.util.PropertyList;

@XmlRootElement (name="NoteArkivSettings")
public class NoteArkivSettings {
	private String backgroundImageUrl;
	private String applicationTitle;
	private String applicationWelcomeTitle;
	private String applicationWelcomeHtml;
	private String applicationId;
	private PropertyList loggingProperties;
	private PropertyList persistenceProperties;
	
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
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public PropertyList getLoggingProperties() {
		return loggingProperties;
	}
	public void setLoggingProperties(PropertyList loggingProperties) {
		this.loggingProperties = loggingProperties;
	}
	public PropertyList getPersistenceProperties() {
		return persistenceProperties;
	}
	public void setPersistenceProperties(PropertyList persistenceProperties) {
		this.persistenceProperties = persistenceProperties;
	}
}
