package rw.qt.userms.services.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rw.qt.userms.exceptions.InternalServerErrorException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.File;
import rw.qt.userms.models.enums.EFileSizeType;
import rw.qt.userms.models.enums.EFileStatus;
import rw.qt.userms.repositories.IFileRepository;
import rw.qt.userms.services.IFileService;
import rw.qt.userms.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {
    private final IFileRepository fileRepository;
    private final MessageSource messageSource;

    private final Locale locale = LocaleContextHolder.getLocale();

    @Getter
    @Value("${upload.directory}")
    private String root;

    @Autowired
    public FileServiceImpl(IFileRepository fileRepository, MessageSource messageSource) {
        this.fileRepository = fileRepository;
        this.messageSource = messageSource;
    }

    @Override
    public File findById(UUID id) throws ResourceNotFoundException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty())
            throw new ResourceNotFoundException("file_with_id_not_found", messageSource.getMessage("file_with_id_not_found", null, LocaleContextHolder.getLocale()));
        return fileOptional.get();
    }

    @Override
    public File findByName(String name) throws ResourceNotFoundException {
        Optional<File> fileOptional = fileRepository.findByName(name);
        if (fileOptional.isEmpty())
            throw new ResourceNotFoundException(messageSource.getMessage("file_not_found", null, LocaleContextHolder.getLocale()), null);
        return fileOptional.get();
    }

    @Override
    public void deleteById(UUID id) throws ResourceNotFoundException, InternalServerErrorException, IOException {
        File file = this.findById(id);
        delete(file.getPath());
        fileRepository.deleteById(id);
    }

    @Override
    public String save(MultipartFile file, String filename) throws Exception {
        try {
            Path path = Path.of(root);
            Files.copy(file.getInputStream(), path.resolve(Objects.requireNonNull(filename)));
            return path + "/" + filename;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Resource load(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    public void delete(String filePath) throws ResourceNotFoundException, IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path))
            throw new ResourceNotFoundException(messageSource.getMessage("file_not_found", null, locale), null);
        else Files.delete(path);
    }

    @Override
    public File create(MultipartFile document) throws Exception {
        File file = new File();

        String fileName = FileUtil.generateUUID(Objects.requireNonNull(document.getOriginalFilename()));
        String documentSizeType = FileUtil.getFileSizeTypeFromFileSize(document.getSize());
        int documentSize = FileUtil.getFormattedFileSizeFromFileSize(document.getSize(), EFileSizeType.valueOf(documentSizeType));

        file.setName(fileName);
        file.setPath(this.save(document, fileName));
        file.setUrl(file.getPath());
        file.setStatus(EFileStatus.SAVED);
        file.setType(document.getContentType());
        file.setSize(documentSize);
        file.setSizeType(EFileSizeType.valueOf(documentSizeType));

        return this.fileRepository.save(file);
    }

    @Override
    public boolean existsById(UUID id) {
        return fileRepository.existsById(id);
    }
}
