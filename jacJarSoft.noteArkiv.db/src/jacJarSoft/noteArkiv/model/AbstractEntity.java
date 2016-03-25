package jacJarSoft.noteArkiv.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;

import jacJarSoft.noteArkiv.db.SqliteDateTimeFormater;

@MappedSuperclass
public abstract class AbstractEntity {

	protected String getDateStrFromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
	    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
	    return ldt.format(SqliteDateTimeFormater.Formatter);
	}

	protected Date getDateFromDateStr(String dateStr) {
		LocalDateTime ldt = LocalDateTime.parse(dateStr, SqliteDateTimeFormater.Formatter);
	    return Date.from(ldt.toInstant(ZoneOffset.UTC));
		
	}
	@PostLoad
	protected void postLoad() {
	}

}
