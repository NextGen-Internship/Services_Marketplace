package com.service.marketplace.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException (Integer fileId) {
        super("File not found with file id: " + fileId);
    }
}
