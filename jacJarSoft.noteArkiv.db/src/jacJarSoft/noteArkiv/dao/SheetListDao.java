package jacJarSoft.noteArkiv.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.SheetList;
import jacJarSoft.noteArkiv.model.SheetListNote;

@Component
public class SheetListDao extends AbstractDao {
	private static Logger logger = Logger.getLogger(SheetListDao.class.getName());
	
	public SheetList getList(long listId) {
		SheetList list = getEntityManager().find(SheetList.class, listId);
		return list;
	}
	public List<SheetList> getLists() {
		List<SheetList> lists = getEntityManager().createQuery("select l from SheetList l", SheetList.class).getResultList();
		return lists;
	}

	public SheetListNote insertLink(SheetListNote link) {
		getEntityManager().persist(link);
		return link;
	}
	public void deleteLink(SheetListNote link) {
		getEntityManager().remove(link);
	}

	@SuppressWarnings("unchecked")
	public Set<Long> getLinkedSheets(long listId) {
		List<SheetListNote> links = (List<SheetListNote>) getEntityManager()
				.createNativeQuery("select * from LIST_NOTES where LIST_ID = ?", SheetListNote.class)
				.setParameter(1, listId)
				.getResultList();
		return links.stream().map(link->{return link.getNoteId();}).collect(Collectors.toSet());
	}
	
	public SheetListNote getLink(long listId,  long sheetId) {
		SheetListNote.MyIdClass id = new SheetListNote.MyIdClass();
		id.listId = listId;
		id.noteId = sheetId;
		SheetListNote link = getEntityManager().find(SheetListNote.class, id);
		return link;
	}
	public SheetList updateList(SheetList list) {
		logger.fine("Updating list: " + list.getName()+"/" + list.getListDateStr());
		getEntityManager().merge(list);
		return list;
	}
	public SheetList insertList(SheetList list) {
		logger.fine("Insering list: " + list.getName()+"/" + list.getListDateStr());
		list.setRegisteredDate(new Date());
		list.setRegisteredBy(getCurrentUser());
		getEntityManager().persist(list);
		return list;
	}
	public void deleteList(SheetList list) {
		getEntityManager().remove(list);
	}
	
}
