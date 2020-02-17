package project.metasite.newWay;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileController {

	@Autowired
	FileService fileService;

	@PostMapping("file/writeToFile")
	public List<FileWriter> writeToFile() throws IOException {
		return fileService.writeToFile();
	}

	@GetMapping("file/allFilesNames")
	public List<String> getListOfFiles() throws IOException {
		return fileService.getAllFilesNames();
	}
	
	@GetMapping("file/getFiles")
	public List<String> getFiles() throws IOException {
		return fileService.getFilesList();
	}

	@GetMapping("file/readFiles")
	public String readFiles() {
		return fileService.readLineByLine();
	}

	@GetMapping("file/countOccurencies")
	public List<String> count() {
		return fileService.countRepeatedWords();
	}

	@PostMapping("file/uploadFile")
	public FileEntity uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		String fileName = fileService.storeUploadedFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();

		return new FileEntity(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@DeleteMapping("file/delete")
	public void deleteFiles() throws IOException {
		fileService.deleteFilesInAFolder();
	}
	
  @GetMapping("/downloadFile/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
      // Load file as Resource
      Resource resource = fileService.loadFileAsResource(fileName);

      // Try to determine file's content type
      String contentType = null;
      try {
          contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (IOException ex) {
         
      }

      // Fallback to the default content type if type could not be determined
      if(contentType == null) {
          contentType = "application/octet-stream";
      }
      
      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
  }
  
  @GetMapping("file/download")
  public List<String> downloadFilesList(){
	  return fileService.getDownloadFilesNames();
  }

}
