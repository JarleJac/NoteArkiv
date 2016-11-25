package jacJarSoft.noteArkiv.api;

public class SheetSearchParam {
	private String title;
    private String composedBy;
    private String arrangedBy;
    private int days;
    private Long listId;
    private String voices;
    private String tags;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComposedBy() {
		return composedBy;
	}
	public void setComposedBy(String composedBy) {
		this.composedBy = composedBy;
	}
	public String getArrangedBy() {
		return arrangedBy;
	}
	public void setArrangedBy(String arrangedBy) {
		this.arrangedBy = arrangedBy;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Long getListId() {
		return listId;
	}
	public void setListId(Long listId) {
		this.listId = listId;
	}
	public String getVoices() {
		return voices;
	}
	public void setVoices(String voices) {
		this.voices = voices;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}

}
