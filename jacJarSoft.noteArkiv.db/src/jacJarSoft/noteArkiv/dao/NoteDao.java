package jacJarSoft.noteArkiv.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchList;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.db.SqliteDateTimeFormater;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.util.StringUtils;

@Component
public class NoteDao extends AbstractDao {
	
	public Note getNote(long noteId) {
		Note note = getEntityManager().find(Note.class, noteId);
		return note;
	}

	public Note insertNote(Note sheet) {
		sheet.setRegisteredDate(new Date());
		sheet.setRegisteredBy(getCurrentUser());
		getEntityManager().persist(sheet);
		return sheet;
	}
	public Note updateNote(Note sheet) {
		getEntityManager().merge(sheet);
		return sheet;
	}

	private String addWhere(String where, String clauseToAdd)
	{
		if (where.length() == 0)
			where = "where " + clauseToAdd;
		else
			where = where + " and " + clauseToAdd;
		return where;
	}
	public SheetSearchList sheetSearch(SheetSearchParam param) {
		String from = "";
		String where = "";
		
		if (StringUtils.hasValue(param.getTitle())) {
			where = addWhere(where,"title like '%" + param.getTitle() + "%'");
		}
		if (param.getDays() > 0) {
			LocalDateTime localDateFrom = LocalDateTime.now().minusDays(param.getDays());
			
			//Date dateFrom = Date.from(localDateFrom.atZone(ZoneId.systemDefault()).toInstant());
			where = addWhere(where,"registered_date >= datetime('" +localDateFrom.format(SqliteDateTimeFormater.Formatter) + "')");
		}

		@SuppressWarnings("unchecked")
		List<Note> sheets = (List<Note>)getEntityManager().createNativeQuery("select * from notes " + from + " " + where + " order by title", Note.class).getResultList();
		
		SheetSearchList result = new SheetSearchList();
		List<SheetParam> sheetList = result.getSheetList();
		
		for (Note sheet : sheets) {
			SheetParam sheetData = getSheetData(sheet);
			sheetList.add(sheetData);
		}
			
		return result;
	}

	public SheetParam getSheetData(Note sheet) {
		SheetParam sheetData = new SheetParam();
		sheetData.setSheet(sheet);
		return sheetData;
	}

	public void deleteNote(Note note) {
		getEntityManager().remove(note);
		
	}


}
