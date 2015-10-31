package jacJarSoft.util;

public class Version {
	private int major;
	private int minor;
	private int fix;
	private int build;
	private String versionText;

	public Version(int major, int minor, int fix, int build) {
		this.major = major;
		this.minor = minor;
		this.fix = fix;
		this.build = build;
	}
	public Version(String version) {
		String[] mainSplit = version.split("-");
		if (mainSplit.length > 1)
			versionText = mainSplit[1];
		String[] split = mainSplit[0].split("\\.");
		if (split.length != 4)
			throw new IllegalArgumentException("version string must be in the format <major>.<minor>.<fix>.<build>[-<buildInfo>]");
		major = Integer.valueOf(split[0]);
		minor = Integer.valueOf(split[1]);
		fix = Integer.valueOf(split[2]);
		build = Integer.valueOf(split[3]);
	}
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public int getFix() {
		return fix;
	}
	public void setFix(int fix) {
		this.fix = fix;
	}
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}	

	@Override
	public String toString() {
		return major + "." + minor + "." + fix + "." + build + (versionText == null ? "" : "-" + versionText) ;
	}
	public boolean isEarlier(Version version) {
		// TODO Auto-generated method stub isEarlier
		return false;
	}
}
