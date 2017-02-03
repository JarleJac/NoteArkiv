package jacJarSoft.noteArkiv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import jacJarSoft.noteArkiv.model.Tag;

@Component
public class TagsDao extends AbstractDao {

	@SuppressWarnings("unchecked")
	public List<Tag> getTags() {
		return (List<Tag>) getEntityManager().createNativeQuery("select * from tags order by tag_name collate nocase", Tag.class).getResultList();
	}
	@SuppressWarnings("unchecked")
	public boolean doesTagWithNameExist(String name) {
		List<Tag> list = (List<Tag>) getEntityManager().createNativeQuery("select * from tags where tag_name = ?", Tag.class)
				.setParameter(1, name)
				.getResultList();
		return list.size() != 0;
	}
	public Tag insertTag(Tag tag) {
		getEntityManager().persist(tag);
		return tag;
	}
	public void deleteTag(Tag tag) {
		getEntityManager().remove(tag);
	}
	public Tag getTag(int tagId) {
		Tag tag = getEntityManager().find(Tag.class, tagId);
		return tag;
	}
}
