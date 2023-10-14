package jacJarSoft.noteArkiv.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import jacJarSoft.noteArkiv.db.util.SqliteDateTimeFormater;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;

@MappedSuperclass
public abstract class AbstractEntity {

	protected static String getDateStrFromDate(Date date) {
		if (date == null)
			return null;
		Instant instant = Instant.ofEpochMilli(date.getTime());
	    LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	    return ldt.format(SqliteDateTimeFormater.Formatter);
	}

	protected static Date getDateFromDateStr(String dateStr) {
		if (dateStr == null)
			return null;
		LocalDateTime ldt = LocalDateTime.parse(dateStr, SqliteDateTimeFormater.Formatter);
	    return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
	@PostLoad
	protected void postLoad() {
	}

}
