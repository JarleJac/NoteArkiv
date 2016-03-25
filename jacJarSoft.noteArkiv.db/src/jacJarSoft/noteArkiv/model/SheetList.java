package jacJarSoft.noteArkiv.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="LISTS")
@XmlRootElement(name="List")
public class SheetList extends RegisteredAwareEntity {
	@Id
	@GeneratedValue(generator="sqlite_lists")
	@TableGenerator(name="sqlite_lists", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="LISTS",
	    initialValue=1, allocationSize=1)	
	@Column(name="LIST_ID")
	private long listId;
	@Column(name="LIST_NAME")
    private String name;
	@Column(name = "LIST_DATE")
	private String listDateStr;
	@Transient
	private Date listDate;
	public long getListId() {
		return listId;
	}
	public void setListId(long listId) {
		this.listId = listId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getListDateStr() {
		return listDateStr;
	}
	public void setListDateStr(String listDateStr) {
		this.listDateStr = listDateStr;
		listDate = getDateFromDateStr(listDateStr);
	}
	public Date getListDate() {
		return listDate;
	}
	public void setListDate(Date listDate) {
		this.listDate = listDate;
		listDateStr = getDateStrFromDate(listDate);
	}
	@Override
	protected void postLoad() {
		listDate = getDateFromDateStr(listDateStr);
		super.postLoad();
	}
}
