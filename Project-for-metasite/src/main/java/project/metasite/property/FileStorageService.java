//package project.metasite.property;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.DirectoryStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.stream.Stream;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class FileStorageService {
//
//	private final Path fileStorageLocation;
//	
//	final File folder = new File("/home/asta/Desktop/Project For Matesite/Uploads/");
//	
//	@Autowired
//	public FileStorageService(FileStorageProperty fileStorageProperty) {
//		this.fileStorageLocation = Paths.get(fileStorageProperty.getUploadDir()).toAbsolutePath().normalize();
//
//		try {
//			Files.createDirectories(this.fileStorageLocation);
//		} catch (Exception ex) {
//			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
//					ex);
//		}
//	}
//
//	@Transactional
//	public String storeFile(MultipartFile file) {
//		// Normalize file name
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//		try {
//			// Check if the file's name contains invalid characters
//			if (fileName.contains("..")) {
//				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//			}
//
//			// Copy file to the target location (Replacing existing file with the same name)
//			Path targetLocation = this.fileStorageLocation.resolve(fileName);
//			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//			return fileName;
//		} catch (IOException ex) {
//			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//		}
//	}
//
//	@Transactional
//	public Resource loadFileAsResource(String fileName) {
//		try {
//			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//			Resource resource = new UrlResource(filePath.toUri());
//			if (resource.exists()) {
//				return resource;
//			} else {
//				throw new MyFileNotFoundException("File not found " + fileName);
//			}
//		} catch (MalformedURLException ex) {
//			throw new MyFileNotFoundException("File not found " + fileName, ex);
//		}
//	}
//	
//	public void listFilesForFolder() throws IOException {
//		try (Stream<Path> paths = Files.walk(this.fileStorageLocation)) {
//		    paths
//		        .filter(Files::isRegularFile)
//		        .forEach(System.out::println);
//		} 
//	}
//	
//	@Transactional
//	public void readFromMultipleFiles() {
//		Path folderPath = fileStorageLocation;
//		
//		Map<String, List<String>> linesOfFiles = new TreeMap<String, List<String>>();
//		
//		List<String> fileNames = new ArrayList<>();
//        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
//            for (Path path : directoryStream) {
//                fileNames.add(path.toString());
//            }
//        } catch (IOException ex) {
//            System.err.println("Error reading files");
//            ex.printStackTrace();
//        }
//
//        for (String file : fileNames) {
//            try {
//                List<String> lines = Files.readAllLines(folderPath.resolve(file));
//                linesOfFiles.put(file, lines);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        linesOfFiles.forEach((String fileName, List<String> lines) -> {
//            System.out.println("Content of " + fileName + " is:");
//            lines.forEach((String line) -> {
//                System.out.println(line);
//            });
//            System.out.println("————————————————————————————————");
//        });
//	}
//	
//	
//}
//
//
//
//
