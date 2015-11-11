package jacJarSoft.util;

public class Version implements Comparable<Version>{
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
		return toVersionString(true);
	}
	public String toVersionString(boolean includeText) {
		return major + "." + minor + "." + fix + "." + build + (versionText == null || !includeText ? "" : "-" + versionText) ;
	}
	public boolean isEarlier(Version other) {
		return compareTo(other) < 0;
	}
	@Override
	public int compareTo(Version other) {
		int ret = major - other.major;
		if (ret == 0)
			ret = minor - other.minor;
		if (ret == 0)
			ret = fix - other.fix;
		if (ret == 0)
			ret = build - other.build;
		return ret;
	}
}
