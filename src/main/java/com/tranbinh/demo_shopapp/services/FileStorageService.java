package com.tranbinh.demo_shopapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

/**
 * Service for handling file storage operations including storing, deleting and retrieving upload path.
 * Configured via 'uploads.path' property in application.yml.
 */
@Service
public class FileStorageService implements IFileStorageService {
    private final Path fileStorageLocation;

    /**
     * Initializes the file storage service with the configured upload path.
     * Creates the upload directory if it doesn't exist.
     *
     * @param uploadPath The path where files will be stored, configured via 'uploads.path' property
     */
    public FileStorageService(@Value("${uploads.path:uploads}") String uploadPath) {
        this.fileStorageLocation = Paths.get(uploadPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Stores a file in the configured upload location.
     * Generates a unique filename to prevent conflicts.
     *
     * @param file The MultipartFile to store
     * @return The unique filename of the stored file
     * @throws IOException              If file cannot be stored
     * @throws IllegalArgumentException If file is null or empty
     */
    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        if (originalFileName.contains("..")) {
            throw new IOException(
                    "Sorry! Filename contains invalid path sequence " + originalFileName);
        }
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    /**
     * Deletes a file from the storage location by its filename.
     *
     * @param fileName The name of file to delete
     * @throws IOException If file cannot be deleted
     */
    @Override
    public void deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        Path filePath = this.fileStorageLocation.resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    /**
     * Gets the absolute path of the upload directory.
     *
     * @return Path object representing the upload directory location
     */
    @Override
    public Path getUploadPath() {
        return this.fileStorageLocation;
    }
}
