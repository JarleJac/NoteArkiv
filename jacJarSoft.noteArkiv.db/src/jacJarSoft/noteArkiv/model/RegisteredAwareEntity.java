package jacJarSoft.noteArkiv.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import jacJarSoft.noteArkiv.db.SqliteDateTimeFormater;


@MappedSuperclass
public abstract class RegisteredAwareEntity extends AbstractEntity {

	@Column(name = "REGISTERED_DATE")
	private String registeredDateStr;
	@Transient
	private Date registeredDate;
	@Column(name = "REGISTERED_BY")
	private String registeredBy;

	public RegisteredAwareEntity() {
		super();
	}

	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
		Instant instant = Instant.ofEpochMilli(registeredDate.getTime());
	    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
	    registeredDateStr = ldt.format(SqliteDateTimeFormater.Formatter);
	}

	@XmlTransient
	public String getRegisteredDateStr() {
		return registeredDateStr;
	}

	public void setRegisteredDateStr(String registeredDateStr) {
		this.registeredDateStr = registeredDateStr;
	    setRegDateFromStr();
	}

	private void setRegDateFromStr() {
		LocalDateTime ldt = LocalDateTime.parse(registeredDateStr, SqliteDateTimeFormater.Formatter);
	    registeredDate = Date.from(ldt.toInstant(ZoneOffset.UTC));
	}

	@PostLoad
	protected void postLoad() {
	    setRegDateFromStr();
	}

	public String getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}

}