package jacJarSoft.util;

import java.util.Properties;

import javax.xml.bind.annotation.XmlTransient;

public class PropertyList {
	private Property property[];

	public Property[] getProperty() {
		return property;
	}

	public void setProperty(Property property[]) {
		this.property = property;
	}
	
	@XmlTransient
	public java.util.Properties getAsProperties()
	{
		java.util.Properties props = new Properties();
		for (int i=0; i <property.length; i++) {
			Property theProp = property[i];
			props.setProperty(theProp.getKey(), theProp.getValue());
		}
		return props;
	}
}
