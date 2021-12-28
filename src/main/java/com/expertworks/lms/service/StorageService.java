package com.expertworks.lms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {

    @Value("${cloud.aws.upload.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.bucket.folder.name}")
    private String folderName;

    @Autowired
    private AmazonS3 s3Client;



    public String uploadFile(MultipartFile file) {

        File fileObj = convertMultiPartFileToFile(file);
        String fileNameInS3 = System.currentTimeMillis() + "_" + file.getOriginalFilename();
       	String folderName = "folder1";
	   	PutObjectRequest request = new PutObjectRequest(bucketName, folderName + "/" + fileNameInS3, fileObj);
		s3Client.putObject(request);
		System.out.println("--Uploading file done : " +fileNameInS3 );
        //s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileNameInS3;
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            //log.error("Error converting multipartFile to file", e);
            System.out.println("Error converting multipartFile to file"+ e);
        }
        return convertedFile;
    }
}
