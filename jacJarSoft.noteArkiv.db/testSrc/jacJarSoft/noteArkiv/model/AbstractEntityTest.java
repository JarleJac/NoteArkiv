package jacJarSoft.noteArkiv.model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class AbstractEntityTest {

	@Test
	public void testDateMethods() {
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.MILLISECOND,0);
		Date dateNow = new Date(cal.getTimeInMillis());
		String dateStr = AbstractEntity.getDateStrFromDate(dateNow);
		Date dateFromDateStr = AbstractEntity.getDateFromDateStr(dateStr);
		
		assertEquals(dateNow, dateFromDateStr);
	}

}
