package jacJarSoft.util;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

public class Property {
	private String key;
	private String value;

	@XmlAttribute
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
