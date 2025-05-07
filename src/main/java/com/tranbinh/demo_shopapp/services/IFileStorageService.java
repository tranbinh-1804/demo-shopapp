package com.tranbinh.demo_shopapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface IFileStorageService {
   String storeFile(MultipartFile file) throws IOException;
   void deleteFile(String fileName) throws IOException;
   Path getUploadPath();
}
