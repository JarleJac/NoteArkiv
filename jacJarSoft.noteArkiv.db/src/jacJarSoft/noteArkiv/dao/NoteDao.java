package jacJarSoft.noteArkiv.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchList;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
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

	public SheetSearchList sheetSearch(SheetSearchParam param) {
		String from = "";
		String where = "";
		
		if (StringUtils.hasValue(param.getTitle())) {
			where = where + "where title like '%" + param.getTitle() + "%'";
		}
		@SuppressWarnings("unchecked")
		List<Note> sheets = (List<Note>) getEntityManager().createNativeQuery("select * from notes " + from + " " + where, Note.class).getResultList();
		
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


}
