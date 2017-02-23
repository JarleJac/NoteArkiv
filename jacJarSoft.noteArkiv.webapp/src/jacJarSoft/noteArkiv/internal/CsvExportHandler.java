package jacJarSoft.noteArkiv.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.CSVWriter;

import jacJarSoft.noteArkiv.api.ListSheet;
import jacJarSoft.noteArkiv.dao.NoteDao;
import jacJarSoft.noteArkiv.dao.TagsDao;
import jacJarSoft.noteArkiv.dao.VoiceDao;
import jacJarSoft.noteArkiv.model.Note;
import jacJarSoft.noteArkiv.model.NoteFile;
import jacJarSoft.util.StringUtils;

public class CsvExportHandler {
	private NoteDao noteDao;
	private VoiceDao voiceDao;
	private TagsDao tagsDao;

	private Path exportDirectory;
	private Map<Integer, String> voiceMap;
	private Map<Integer, String> tagsMap;

	@Autowired
	public CsvExportHandler(String filesDirectory, NoteDao noteDao, VoiceDao voiceDao, TagsDao tagsDao) {
		this.noteDao = noteDao;
		this.voiceDao = voiceDao;
		this.tagsDao = tagsDao;
		exportDirectory = Paths.get(filesDirectory,"export");
	}

	public void runExport() throws IOException {
		File dir = exportDirectory.toFile();
		if (!dir.exists())
			dir.mkdirs();

		getVoiceAndTagsMap();
		
		exportSheets();
		exportSheetFiles();
		exportLists();
	}

	private void exportLists() throws IOException {
		String[] headers = "ListeId;Navn;Dato;NoteId;Tittel".split(";");
		File file = exportDirectory.resolve("ListerInfo.csv").toFile();
		if (file.exists())
			file.delete();
		try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
			writer.writeNext(headers);
			List<ListSheet> listWithSheets = noteDao.getListWithSheets();
			for (ListSheet listSheet : listWithSheets) {
				String[] row = new String[headers.length];
				row[0] = String.valueOf(listSheet.getList().getListId());
				row[1] = listSheet.getList().getName();
				row[2] = listSheet.getList().getListDateStr();
				row[3] = String.valueOf(listSheet.getSheet().getNoteId());
				row[4] = listSheet.getSheet().getTitle();
				writer.writeNext(row);
			}
		}
	}

	private void exportSheetFiles() throws IOException {
		String[] headers = "NoteId;filnavn;Beskrivelse;RegDato;RegAv".split(";");
		File file = exportDirectory.resolve("NoteFilerInfo.csv").toFile();
		if (file.exists())
			file.delete();
		try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
			writer.writeNext(headers);
			List<NoteFile> sheetFiles = noteDao.getSheetFilesForExport();
			for (NoteFile sheetFile : sheetFiles) {
				String[] row = new String[headers.length];
				row[0] = String.valueOf(sheetFile.getNoteId());
				row[1] = sheetFile.getName();
				row[2] = sheetFile.getDescription();
				row[3] = sheetFile.getRegisteredDateStr();
				row[4] = sheetFile.getRegisteredBy();
				writer.writeNext(row);
			}
		}
	}

	private void exportSheets() throws IOException {
		String[] heders = "Id;Tittel;ArrangertAv;KomponertAv;Beskrivelse;Stemmer;Etiketter;RegDato;RegAv".split(";");
		
		File sheetsFile = exportDirectory.resolve("NoteInfo.csv").toFile();
		if (sheetsFile.exists())
			sheetsFile.delete();
		try (CSVWriter writer = new CSVWriter(new FileWriter(sheetsFile), ';')) {
			writer.writeNext(heders);
			List<Note> sheets = noteDao.getSheetsForExport();
			for (Note sheet : sheets) {
				String[] row = new String[heders.length];
				row[0] = String.valueOf(sheet.getNoteId());
				row[1] = sheet.getTitle();
				row[2] = sheet.getArrangedBy();
				row[3] = sheet.getComposedBy();
				row[4] = sheet.getDescription();
				row[5] = getVoicesStr(sheet.getVoices());
				row[6] = getTagsStr(sheet.getTags());
				row[7] = sheet.getRegisteredDateStr();
				row[8] = sheet.getRegisteredBy();
				writer.writeNext(row);
			}
		}
	}

	private void getVoiceAndTagsMap() {
		voiceMap = voiceDao.getVoices().stream()
				.collect(Collectors.toMap(voice -> voice.getId(), voice -> voice.getName()));
		tagsMap = tagsDao.getTags().stream()
				.collect(Collectors.toMap(tag -> tag.getId(), tag -> tag.getName()));
	}

	private String getTagsStr(String tags) {
		return getMappedString(tagsMap, tags);
	}

	private String getVoicesStr(String voices) {
		return getMappedString(voiceMap, voices);
	}
	private String getMappedString(Map<Integer, String> map, String values) {
		if (!StringUtils.hasValue(values))
			return "";
		String[] valueArr = values.split(",");
		StringBuilder builder = new StringBuilder();
		for (String value: valueArr) {
			if (!StringUtils.hasValue(value))
				continue;
			int id = Integer.parseInt(value);
			String name = map.get(id);
			if (name != null) {
				if (builder.length() > 0)
					builder.append(", ");
				builder.append(name);
			}
		}
		return builder.toString();
	}
}
