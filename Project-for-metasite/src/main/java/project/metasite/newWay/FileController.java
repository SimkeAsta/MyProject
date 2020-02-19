package project.metasite.newWay;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
	
//  @GetMapping("/downloadFile/{fileName:.+}")
//  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws FileNotFoundException {
//      // Load file as Resource
//      Resource resource = fileService.loadFileAsResource(fileName);
//
//      // Try to determine file's content type
//      String contentType = null;
//      try {
//          contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//      } catch (IOException ex) {
//         
//      }
//
//      // Fallback to the default content type if type could not be determined
//      if(contentType == null) {
//          contentType = "application/octet-stream";
//      }
//      
//      return ResponseEntity.ok()
//              .contentType(MediaType.parseMediaType(contentType))
//              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//              .body(resource);
//  }
//  
  @GetMapping("file/downloadFilesNames")
  public List<String> downloadFilesList(){
	  return fileService.getDownloadFilesNames();
  }
  
  @GetMapping("file/content")
  public List<String> getContent() throws IOException {
	  return fileService.getFileContent();
  }
  
//  @GetMapping("/file/download")
//	public ResponseEntity<byte[]> downloadFile() throws Exception {
//		String data = fileService.getFileContent();
//		ObjectMapper objectMapper = new ObjectMapper();
//		String text = objectMapper.writeValueAsString(data);
//		byte[] isr = text.getBytes();
//		String fileName = "A to G.txt";
//		HttpHeaders respHeaders = new HttpHeaders();
//		respHeaders.setContentLength(text.length());
//		respHeaders.setContentType(new MediaType("text", "txt"));
//		respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//		respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//		return new ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK);
//	}
  
  @GetMapping("/downloadZip")
  public void downloadFile(HttpServletResponse response) {

      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=download.zip");
      response.setStatus(HttpServletResponse.SC_OK);

      List<String> fileNames = fileService.getDownloadFilesNames();

      System.out.println("############# file size ###########" + fileNames.size());

      try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
          for (String file : fileNames) {
              FileSystemResource resource = new FileSystemResource(file);

              ZipEntry e = new ZipEntry(resource.getFilename());
              // Configure the zip entry, the properties of the file
              e.setSize(resource.contentLength());
              e.setTime(System.currentTimeMillis());
              // etc.
              zippedOut.putNextEntry(e);
              // And the content of the resource:
              StreamUtils.copy(resource.getInputStream(), zippedOut);
              zippedOut.closeEntry();
          }
          zippedOut.finish();
      } catch (Exception e) {
          // Exception handling goes here
      }
  }

}
