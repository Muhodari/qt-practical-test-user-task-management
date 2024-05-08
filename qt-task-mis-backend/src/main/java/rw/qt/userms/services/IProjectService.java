package rw.qt.userms.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.Project;
import rw.qt.userms.models.dtos.CreateProjectDTO;

import rw.qt.userms.models.enums.EStatus;


import java.util.UUID;



public interface IProjectService {
    Page<Project> getAllPaginated(Pageable pageable);
    Page<Project> searchAll(String q, Pageable pageable);
    Project create(CreateProjectDTO dto) throws DuplicateRecordException, ResourceNotFoundException;
    Project getById(UUID id) throws ResourceNotFoundException;
    Project updateById(UUID id, CreateProjectDTO dto) throws ResourceNotFoundException, DuplicateRecordException;
    Project changeStatusById(UUID id, EStatus status) throws ResourceNotFoundException;
    void deleteById(UUID id) throws ResourceNotFoundException;

}
