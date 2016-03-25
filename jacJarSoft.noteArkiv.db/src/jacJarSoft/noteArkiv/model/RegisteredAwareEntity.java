package jacJarSoft.noteArkiv.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;


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
	    registeredDateStr = getDateStrFromDate(registeredDate);
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
	    registeredDate = getDateFromDateStr(registeredDateStr);
	}

	@Override
	protected void postLoad() {
	    setRegDateFromStr();
	    super.postLoad();
	}

	public String getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}

}