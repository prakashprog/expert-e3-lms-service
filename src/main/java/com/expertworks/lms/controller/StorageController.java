package com.expertworks.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.expertworks.lms.http.StorageDTO;
import com.expertworks.lms.service.StorageService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageService service;

	/*
	 * Working One
	 *
	 * @PostMapping("/upload") public ResponseEntity<String>
	 * uploadFile(@RequestParam(value = "file") MultipartFile file) {
	 * System.out.println("Request Recieved in uploadFile" + file.getName());
	 *
	 * //System.out.println("Request Recieved in storageDTO Name" +
	 * storageDTO.getName());
	 *
	 * return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK); }
	 */


	// https://medium.com/@pankajsingla_24995/multipart-request-with-request-body-using-spring-boot-and-test-using-postman-6ea46b71b75d


	@PostMapping(value = "/upload", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<StorageDTO> uploadFileWithForm(@RequestPart("user") String user,
			@RequestPart(value = "file", required = false) MultipartFile file)
	{

		System.out.println("Request Recieved in uploadFile" + file);
		String fileName = "No File";
		if (file != null)
			fileName = service.uploadFile(file);

		System.out.println("Request Recieved in storageDTO Namea : " + user);
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		//StorageDTO storageDTO = gson.fromJson(user, StorageDTO.class); // deserializes json into target2
		StorageDTO  storageDTO = new StorageDTO();

		storageDTO.setFileName(fileName);
		System.out.println("Request Recieved in storageDTO Name : " + storageDTO.getName());
		return new ResponseEntity<>(storageDTO, HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
		byte[] data = service.downloadFile(fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"").body(resource);
	}

	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
		return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
	}
}
