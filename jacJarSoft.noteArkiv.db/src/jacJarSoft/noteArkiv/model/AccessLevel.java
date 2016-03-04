package jacJarSoft.noteArkiv.model;

public enum AccessLevel {
	SYSADMIN(100),
	ADMIN(50),
	AUTHOR(40),
	READER(10),
	DISABLED(0);
	private int level;
	private AccessLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
}
