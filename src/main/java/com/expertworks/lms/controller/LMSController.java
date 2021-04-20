package com.expertworks.lms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.expertworks.lms.model.TeamCourses;
import com.expertworks.lms.model.UserVideos;
import com.expertworks.lms.model.VideoLink;
import com.expertworks.lms.service.StorageService;

@RestController
@Component
public class LMSController {
	
	

    @Autowired
    private StorageService service;
	
	
	AWSCredentials credentials = new BasicAWSCredentials(
			  "<AWS accesskey>", 
			  "<AWS secretkey>"
			);
   
	@GetMapping
    @RequestMapping({ "/" })
    public String lmsPage() {
        return "Hello LMS Service";
    }


	@GetMapping
    @RequestMapping({ "/lmsvideo" })
    public void lmsVideos() throws Exception {
		
		// SECTION 1 OPTION 1: Create a S3 client with in-program credential
					//
					BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAZTGOLXTIJKYEGYJZ", "dQpVi/VwGUeydrMVzGyUUWKg885goDEFe+fitch/");
					// us-west-2 is AWS Oregon
					AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").withCredentials(new AWSStaticCredentialsProvider(awsCreds))
							.build();

					// SECTION 1 OPTION 2: Create a S3 client with the aws credentials set in OS (require config and crendentials in .aws folder.) Demonstrate at the end of this video.
					//
//					AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

					// SECTION 2: Put file in S3 bucket
					//
					String bucketName = "expertlmsbucket";
					String folderName = "folder1";
					//String fileNameInS3 = "javaFile123.txt";
					String fileNameInS3 = "sample-mp4-file.mp4";
					
					String fileNameInLocalPC = "file1.txt";
					String path = System.getProperty("user.dir");
					
					System.out.println("user.dir  : "  + path);
					
					System.out.println("File : "  + new File(fileNameInLocalPC).getAbsolutePath());
					File file1 = new File("javaFile123.txt");  
		            if (file1.createNewFile()) {  
		                System.out.println("New File is created!");  
		            } else {  
		                System.out.println("File already exists.");  
		            }  

//					PutObjectRequest request = new PutObjectRequest(bucketName, folderName + "/" + fileNameInS3, file1);
//					s3Client.putObject(request);
//					System.out.println("--Uploading file done");
					
					// SECTION 3: Get file from S3 bucket
					//
//					S3Object fullObject;
//					fullObject = s3Client.getObject(new GetObjectRequest(bucketName, folderName + "/" + fileNameInS3));
//					System.out.println("--Downloading file done");
//					// Print file content line by line
//					InputStream is = fullObject.getObjectContent();
//					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//					String line;
//					System.out.println("--File content:");
//					while ((line = reader.readLine()) != null) {
//						System.out.println(line);
//					}
					
					
					
					S3Objects.inBucket(s3Client, "expertlmsbucket").forEach((S3ObjectSummary objectSummary) -> {
					    // TODO: Consume `objectSummary` the way you need
						
						
					    System.out.println("=======================================");
			
					    System.out.println(objectSummary.getKey());
					    
					  
					 
	                    
	                    
					    
					    
					    
					});
					
			
//					   ObjectMetadata objectMetadata = s3Client.getObjectMetadata(bucketName, "test/lambda-java.m3u8");
//	                    Map userMetadataMap = objectMetadata.getUserMetadata();
//	                    
//	                    System.out.println(userMetadataMap);
//	                    
//	                    Map rowMetadataMap = objectMetadata.getRawMetadata();

	
    }
	
	
	
	
	@GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
	

	
	public static void main(String[] args) throws Exception {
		
		LMSController lmsController= new LMSController();
		
		lmsController.lmsVideos();
		
		
		
	}
	
	
	
	@GetMapping("/content/{username}")
    public ResponseEntity<UserVideos> getUserVideos(@PathVariable String username) {
      
		VideoLink videoLink = new VideoLink();
		VideoLink videoLink1 = new VideoLink();
		
		videoLink.setUrl("https://d3s24np0er9fug.cloudfront.net/sample-mp4-file.mp4");
		videoLink.setType("mp4");
		
		videoLink1.setUrl("https://d3s24np0er9fug.cloudfront.net/sample-mp4-file.mp4");
		videoLink1.setType("mp4");
		
		List videoLinks = new  ArrayList();
		videoLinks.add(videoLink);
		videoLinks.add(videoLink1);  
		
		UserVideos userVideos = new UserVideos();
		
		userVideos.setVideos(videoLinks);
		userVideos.setStatus(200);
		userVideos.setMessage("Success");
		userVideos.setUser(username);
			
		HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(userVideos, responseHeaders, HttpStatus.OK);
        
    }
	
	
	@GetMapping("/content/{teamId}")
    public ResponseEntity<TeamCourses> getTeamCourses(@PathVariable String teamId) {
      
		TeamCourses teamCourses = new TeamCourses();
			
		HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(teamCourses, responseHeaders, HttpStatus.OK);
        
    }
	


  
}
  