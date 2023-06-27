package com.projet3.safertogether.services;

import com.projet3.safertogether.exceptions.StorageException;
import com.projet3.safertogether.models.FileEntity;
import com.projet3.safertogether.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void store(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setContent(file.getBytes());
        fileRepository.save(fileEntity);
    }

//public void store(MultipartFile file) {
//    try {
//        if (file.isEmpty()) {
//            throw new StorageException("Failed to store empty file.");
//        }
//
//        FileEntity fileEntity = new FileEntity();
//        fileEntity.setName(file.getOriginalFilename());
//        fileEntity.setData(file.getBytes());
//
//        fileRepository.save(fileEntity);
//    } catch (IOException e) {
//        throw new StorageException("Failed to store file.", e);
//    }
//}


    public FileEntity getFile(Long id) {
        return fileRepository.findById(id).orElse(null);
    }
}
