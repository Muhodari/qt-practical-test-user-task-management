package rw.qt.userms.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rw.qt.userms.exceptions.InternalServerErrorException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.File;
import rw.qt.userms.models.domains.ApiResponse;
import rw.qt.userms.models.dtos.NewFileDTO;
import rw.qt.userms.services.impl.FileServiceImpl;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "Files")
@Validated
public class FileController {
    private final MessageSource messageSource;
    private final FileServiceImpl fileServiceImpl;

    @Autowired
    public FileController(FileServiceImpl fileServiceImpl, MessageSource messageSource) {
        this.fileServiceImpl = fileServiceImpl;
        this.messageSource = messageSource;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<File>> upload(@ModelAttribute NewFileDTO newFileDTO) throws Exception {
        File savedFile = fileServiceImpl.create(newFileDTO.getFile());
        return ResponseEntity.ok(
                new ApiResponse<>(savedFile, localize("responses.saveEntitySuccess"), HttpStatus.CREATED)
        );
    }

    @GetMapping("/raw/{name}")
    @ResponseBody
    public ResponseEntity<Resource> getFileResource(@PathVariable String name) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findByName(name);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }

    @GetMapping("/raw/id/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getFileResourceId(@PathVariable UUID id) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Resource> getFileByIdView(@PathVariable UUID id) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findById(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline;filename=" + file.getName())
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }

    @GetMapping("/view/name/{name}")
    @ResponseBody
    public ResponseEntity<Resource> getFileByIdName(@PathVariable String name) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findByName(name);
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline;filename=" + file.getName())
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }


    @GetMapping("/download/{name}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFileResource(@PathVariable String name) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findByName(name);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename=" + file.getName())
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<File>> deleteFileById(@PathVariable UUID id) throws ResourceNotFoundException, InternalServerErrorException, IOException {
        fileServiceImpl.deleteById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(null,localize("responses.deleteEntitySuccess"), null, HttpStatus.OK)
        );
    }


    @GetMapping("/download/resource/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFileResourceId(@PathVariable UUID id) throws IOException, ResourceNotFoundException {
        File file = fileServiceImpl.findById(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment;filename=" + file.getName())
                .contentType(MediaType.valueOf(file.getType()))
                .body(fileServiceImpl.load(file.getPath()));
    }


    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<File>> getFileById(@PathVariable UUID id) throws ResourceNotFoundException, InternalServerErrorException, IOException {
        File file = fileServiceImpl.findById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(file, localize("responses.getEntitySuccess"), HttpStatus.OK)
        );
    }


    private String localize(String path) {
        return messageSource.getMessage(path, null, LocaleContextHolder.getLocale());
    }
}