package jacJarSoft.noteArkiv.internal;


public class FileList {
	public static class FileInfo {
		private String description; 
		public String getDescription() {
			return description;
		}
		public void setDescription(String decription) {
			this.description = decription;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		private String fileName;		
	}
	private FileInfo[] fileInfo;
	public FileInfo[] getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(FileInfo[] fileInfo) {
		this.fileInfo = fileInfo;
	}
};
