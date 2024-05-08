package rw.qt.userms.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import rw.qt.userms.exceptions.InternalServerErrorException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.File;

import java.io.IOException;
import java.util.UUID;

public interface IFileService {

    File findById(UUID id) throws ResourceNotFoundException;

    File findByName(String name) throws ResourceNotFoundException;

    void deleteById(UUID id) throws ResourceNotFoundException, InternalServerErrorException, IOException;

    String save(MultipartFile file, String filename) throws Exception;

    Resource load(String path) throws IOException, ResourceNotFoundException;

    File create(MultipartFile document) throws Exception;

    boolean existsById(UUID id);
}
