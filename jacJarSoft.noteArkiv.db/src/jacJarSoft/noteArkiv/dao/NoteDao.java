package jacJarSoft.noteArkiv.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.api.SheetParam;
import jacJarSoft.noteArkiv.api.SheetSearchParam;
import jacJarSoft.noteArkiv.db.SqliteDateTimeFormater;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.User;
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
	public List<Note> sheetSearch(SheetSearchParam param) {
		String from = "";
		String where = "";
		
		if (StringUtils.hasValue(param.getTitle())) {
			where = addWhere(where,"title like '%" + param.getTitle() + "%'");
		}
		if (StringUtils.hasValue(param.getArrangedBy())) {
			where = addWhere(where,"arranged_by like '%" + param.getArrangedBy() + "%'");
		}
		if (StringUtils.hasValue(param.getComposedBy())) {
			where = addWhere(where,"composed_by like '%" + param.getComposedBy() + "%'");
		}
		if (param.getDays() > 0) {
			LocalDateTime localDateFrom = LocalDateTime.now().minusDays(param.getDays());
			
			//Date dateFrom = Date.from(localDateFrom.atZone(ZoneId.systemDefault()).toInstant());
			where = addWhere(where,"registered_date >= datetime('" +localDateFrom.format(SqliteDateTimeFormater.Formatter) + "')");
		}

		if (param.getListId() != null) {
			from = "join LIST_NOTES LN on LN.LIST_ID = " + param.getListId().toString() + 
					" AND LN.NOTE_ID = N.NOTE_ID ";
		}
		
		String sql = "select * from NOTES N " + from + " " + where + " order by title";
		@SuppressWarnings("unchecked")
		List<Note> sheets = (List<Note>)getEntityManager().createNativeQuery(sql, Note.class).getResultList();
		
		Set<String> paramTagsSet = new HashSet<>();
		Set<String> paramVoicesSet = new HashSet<>();
		if (StringUtils.hasValue(param.getTags()))
			paramTagsSet.addAll(Arrays.asList(param.getTags().split(",")));
		if (StringUtils.hasValue(param.getVoices()))
			paramVoicesSet.addAll(Arrays.asList(param.getVoices().split(",")));
		if (paramTagsSet.size() > 0 || paramVoicesSet.size() > 0)
		{
			List<Note> origSheets = sheets;
			sheets = new ArrayList<>();
			for (Note sheet : origSheets)
			{
				boolean keep = true;
				if (!isInSet(paramTagsSet, sheet.getTags()))
					keep = false;
				if (!isInSet(paramVoicesSet, sheet.getVoices()))
					keep = false;
				if (keep)
					sheets.add(sheet);
			}
		}
			
		return sheets;
	}

	private boolean isInSet(Set<String> paramSet, String values) {
		if (paramSet.size() == 0)
			return true;
		Set<String> valueSet = new HashSet<>();
		if (StringUtils.hasValue(values))
			valueSet.addAll(Arrays.asList(values.split(",")));

		valueSet.retainAll(paramSet);
		return valueSet.size() == paramSet.size();
	}

	public SheetParam getSheetData(Note sheet, boolean inCurrent) {
		SheetParam sheetData = new SheetParam();
		sheetData.setSheet(sheet);
		sheetData.setInCurrent(inCurrent);
		return sheetData;
	}

	public void deleteNote(Note note) {
		getEntityManager().remove(note);
		
	}

	public List<Note> getSheetsForExport() {
		return getEntityManager().createQuery("select note from Note note order by note.noteId", Note.class).getResultList();		
	}


}
