package project.metasite.newWay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileService {

	private Path fileStorageLocation;
	
	private Path fileDownloadLocation;

	@Autowired
	public FileService() throws IOException {
		this.fileStorageLocation = Files.createDirectories(Paths.get("/tmp/Uploads/"));
		this.fileDownloadLocation = Files.createDirectories(Paths.get("/tmp/Downloads/"));
	}

	@Transactional
	public List<String> getAllFilesNames() {
		List<String> results = new ArrayList<String>();
		File[] files = new File("/tmp/Uploads/").listFiles();

		for (File file : files) {
			if (file.isFile()) {
				results.add(file.getName());
			}
		}
		return results;
	}
	
	@Transactional
	public List<String> getFilesList() {
		List<String> results = new ArrayList<String>();
		try (Stream<Path> walk = Files.walk(Paths.get("/tmp/Uploads/"))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            results = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return results;
	}
	
	@Transactional
	public List<String> getDownloadFilesNames() {
		List<String> results = new ArrayList<String>();
		try (Stream<Path> walk = Files.walk(Paths.get("/tmp/Downloads/"))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            results = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return results;
	}
	
	@Transactional
	public String storeDownloadedFile(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			Path targetLocation = this.fileDownloadLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw ex;
		}
	}

	@Transactional
	public String storeUploadedFile(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw ex;
		}
	}
	
	@Transactional
	public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
		try {
			Path filePath = this.fileDownloadLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new FileNotFoundException();
		}
	}
	
	@Transactional
	public String readLineByLine() {
		List<String> filePaths = getFilesList();
		StringBuilder contentBuilder = new StringBuilder();
		String filePath = "";
		String wholeText = " ";
		for (String string : filePaths) {
			filePath = string;
			try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
				stream.forEach(s -> contentBuilder.append(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
			contentBuilder.toString();
			wholeText = new StringBuilder().append(wholeText).append(contentBuilder).toString();
			
		}
		return wholeText;
		}

	@Transactional
	public List<String> countRepeatedWords() {
		String result = "";
		List<String> finalResult = new ArrayList<String>();
		String text = readLineByLine().toLowerCase();
		List<String> list = Arrays.asList(text.split("\\.|\\s|,"));
		Set<String> uniqueWords = new HashSet<String>(list);
		for (String word : uniqueWords) {
			result = word + ": " + Collections.frequency(list, word);
			finalResult.add(result);
		}
		Collections.sort(finalResult);
		return finalResult;
	}

	@Transactional
	public List<FileWriter> writeToFile() throws IOException {
		List<FileWriter> filesWithContent = new ArrayList<>();
		List<String> list = countRepeatedWords();
		FileWriter writer = new FileWriter(new File("/tmp/Downloads/", "A to G.txt"));
		FileWriter writerTwo = new FileWriter(new File("/tmp/Downloads/", "H to N.txt"));
		FileWriter writerThree = new FileWriter(new File("/tmp/Downloads/", "O to U.txt"));
		FileWriter writerFour = new FileWriter(new File("/tmp/Downloads/", "V to Z.txt"));
		for (String str : list) {
			char c = str.charAt(0);
			if (c >= 'a' && c <= 'g') {
				writer.write(str + System.lineSeparator());
			} else if (c >= 'h' && c <= 'n') {
				writerTwo.write(str + System.lineSeparator());
			} else if (c >= 'o' && c <= 'u') {
				writerThree.write(str + System.lineSeparator());
			} else if (c >= 'v' && c <= 'z') {
				writerFour.write(str + System.lineSeparator());
			}
			filesWithContent.add(writer);
			filesWithContent.add(writerTwo);
			filesWithContent.add(writerThree);
			filesWithContent.add(writerFour);
		}
		writer.close();
		writerTwo.close();
		writerThree.close();
		writerFour.close();
		return filesWithContent;
	}
	
	@Transactional
	public void deleteFilesInAFolder() throws IOException {
		FileUtils.cleanDirectory(new File("/tmp/Uploads/"));
	}
	
	@Transactional
	public List<String> getFileContent() throws IOException {
		List<String> contentFiles = new ArrayList<String>();
		String file = "";
		List<String> filesWithContent = getDownloadFilesNames();
		String newContent = null;
		for (String string : filesWithContent) {
			file = string;
			BufferedReader reader = new BufferedReader(new FileReader(file));
		    String         line = null;
		    StringBuilder  stringBuilder = new StringBuilder();
		    String tab = " ";
		    String coma = ",";

		    try {
		        while((line = reader.readLine()) != null) {
		            stringBuilder.append(line);
		            stringBuilder.append(coma);
		            stringBuilder.append(tab);
		        }

		       newContent = stringBuilder.toString();
		       contentFiles.add(newContent);
		    } finally {
		        reader.close();
		    }
		}
		return contentFiles;
	}
	
//	public ResponseEntity<List<byte[]>> downloadingFile() throws IOException {
//		List<String> data = getFileContent();
//		String singleContent = "";
//		String fileName = "";
//		ResponseEntity<List<byte[]>> newList = null;
//		for (String string : data) {
//			singleContent = string;
//			char c = singleContent.charAt(0);
//			if (c >= 'a' && c <= 'g') {
//				fileName = "A to G.txt";
//				ObjectMapper objectMapper = new ObjectMapper();
//				String text = objectMapper.writeValueAsString(singleContent);
//				byte[] isr = text.getBytes();
//				HttpHeaders respHeaders = new HttpHeaders();
//				respHeaders.setContentLength(text.length());
//				respHeaders.setContentType(new MediaType("text", "txt"));
//				respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//				respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//				newList.getBody
//						new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);
//				
//			} else if (c >= 'h' && c <= 'n') {
//				fileName = "H to N.txt";
//				ObjectMapper objectMapper = new ObjectMapper();
//				String text = objectMapper.writeValueAsString(singleContent);
//				byte[] isr = text.getBytes();
//				HttpHeaders respHeaders = new HttpHeaders();
//				respHeaders.setContentLength(text.length());
//				respHeaders.setContentType(new MediaType("text", "txt"));
//				respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//				respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//				newList.add(new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK));
//			} else if (c >= 'o' && c <= 'u') {
//				fileName = "O to U.txt";
//				ObjectMapper objectMapper = new ObjectMapper();
//				String text = objectMapper.writeValueAsString(singleContent);
//				byte[] isr = text.getBytes();
//				HttpHeaders respHeaders = new HttpHeaders();
//				respHeaders.setContentLength(text.length());
//				respHeaders.setContentType(new MediaType("text", "txt"));
//				respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//				respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//				newList.add(new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK));
//			} else if (c >= 'v' && c <= 'z') {
//				fileName = "V to U.txt";
//				ObjectMapper objectMapper = new ObjectMapper();
//				String text = objectMapper.writeValueAsString(singleContent);
//				byte[] isr = text.getBytes();
//				HttpHeaders respHeaders = new HttpHeaders();
//				respHeaders.setContentLength(text.length());
//				respHeaders.setContentType(new MediaType("text", "txt"));
//				respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//				respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//				newList.add(new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK));
//			}
//			
//		}
//		return newList;
//		
//	}
}
