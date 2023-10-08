package jacJarSoft.noteArkiv.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MESSAGES")
@XmlRootElement(name = "Messages")
public class Message extends AbstractEntity {
	@Id
	@GeneratedValue(generator = "sqlite_messages")
	@TableGenerator(name = "sqlite_messages", table = "sqlite_sequence", pkColumnName = "name", 
					valueColumnName = "seq", pkColumnValue = "MESSAGES", initialValue = 1, allocationSize = 1)
	@Column(name = "MESSAGE_ID")
	private long id;
	@Column(name = "HEADING")
	private String heading;
	@Column(name = "MESSAGE")
	private String message;
	@Column(name = "MESSAGE_TYPE")
	@Enumerated(EnumType.STRING)
	private MessageType messageType;
	@Column(name = "AUTO_EXPIRE_DATE")
	private String autoExpireDateStr;
	@Column(name = "EXPIRED")
	private int expiredInt;

	
	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Date getAutoExpireDate() {
		return getDateFromDateStr(autoExpireDateStr);
	}

	public void setAutoExpireDate(Date autoExpireDate) {
		autoExpireDateStr = getDateStrFromDate(autoExpireDate);
	}

	public boolean isExpired() {
		return expiredInt == 1;
	}

	public void setExpired(boolean expired) {
		this.expiredInt = expired ? 1 : 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
