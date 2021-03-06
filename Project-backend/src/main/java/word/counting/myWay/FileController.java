package word.counting.myWay;

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

	@PostMapping("file/uploadFile")
	public FileEntity uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		String fileName = fileService.storeUploadedFile(file);

		if (fileName.isEmpty()) {
			return null;
		} else {

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileName).toUriString();

			return new FileEntity(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		}
	}

	@GetMapping("file/allFilesNames")
	public List<String> getListOfFiles() throws IOException {
		return fileService.getAllFilesNames();
	}

	@GetMapping("file/getFiles")
	public List<String> getFiles() throws IOException {
		return fileService.getUploadsFilesList();
	}

	@GetMapping("file/readFiles")
	public String readFiles() {
		return fileService.readLineByLine();
	}

	@GetMapping("file/countOccurencies")
	public List<String> count() {
		return fileService.countRepeatedWords();
	}

	@PostMapping("file/writeToFile")
	public List<FileWriter> writeToFile() throws IOException {
		if (fileService.writeToFile() == null) {
			throw new IOException();
		}
		return fileService.writeToFile();
	}

	@DeleteMapping("file/delete")
	public void deleteFiles() throws IOException {
		fileService.deleteFilesInAFolder();
	}

	@GetMapping("/downloadZip")
	public void downloadFile(HttpServletResponse response) throws Exception {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=download.zip");
		response.setStatus(HttpServletResponse.SC_OK);

		List<String> fileNames = fileService.getDownloadsFilesList();

		{

			try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
				for (String file : fileNames) {
					FileSystemResource resource = new FileSystemResource(file);

					ZipEntry e = new ZipEntry(resource.getFilename());
					e.setSize(resource.contentLength());
					e.setTime(System.currentTimeMillis());
					zippedOut.putNextEntry(e);
					StreamUtils.copy(resource.getInputStream(), zippedOut);
					zippedOut.closeEntry();
				}
				zippedOut.finish();
			} catch (Exception e) {
				throw e;
			}

		}
	}

}
