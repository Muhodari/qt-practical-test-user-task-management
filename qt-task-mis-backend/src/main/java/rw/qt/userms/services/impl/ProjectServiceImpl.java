package rw.qt.userms.services.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rw.qt.userms.exceptions.BadRequestAlertException;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.Project;
import rw.qt.userms.models.dtos.CreateProjectDTO;
import rw.qt.userms.repositories.IProjectRepository;
import rw.qt.userms.repositories.IUserRepository;
import rw.qt.userms.models.enums.EStatus;
import rw.qt.userms.services.IJwtService;
import rw.qt.userms.services.IProjectService;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final Logger log = (Logger) LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final IProjectRepository projectRepository;


    private final IUserRepository userRepository;

    private final IJwtService jwtService;


    @Override
    public Page<Project> getAllPaginated(Pageable pageable) {
        log.info("Fetching all Projects paginated");
        return this.projectRepository.findAllByStatusNot(EStatus.DELETED, pageable);
    }



    @Override
    public Page<Project> searchAll(String q, Pageable pageable) {
        log.info("Search all Projects by query");

        return this.projectRepository.searchAll(q, pageable);
    }


    @Override
    public Project create(CreateProjectDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
        log.info("Creating Project with details " + dto.toString());

        log.info("Finding duplicate Project by name '" + dto.getName());
        Optional<Project> duplicateName = this.projectRepository.findByName(dto.getName());
        if (duplicateName.isPresent())
            throw new DuplicateRecordException("Project", "name", dto.getName());


        Project project = new Project(dto);
        Project savedProject = this.projectRepository.save(project);

        return savedProject;
    }

    @Override
    public Project getById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching Project by id '" + id.toString() + "'");

        return this.projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project", "id", id.toString())
        );
    }

    @Override
    public Project updateById(UUID id, CreateProjectDTO dto) throws ResourceNotFoundException, DuplicateRecordException {
        log.info("Updating Project by id '" + id.toString() + "'");
        Project project = this.projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project", "id", id.toString())
        );

        log.info("Finding duplicate Project by name '" + dto.getName());
        Optional<Project> duplicateName = this.projectRepository.findByName(dto.getName());
        if (duplicateName.isPresent() && !duplicateName.get().getId().equals(id))
            throw new DuplicateRecordException("Project", "name", dto.getName());


        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project = this.projectRepository.save(project);

        return project;
    }

    @Override
    public Project changeStatusById(UUID id, EStatus status) throws ResourceNotFoundException {
        Project project = this.getById(id);
        if (status == EStatus.DELETED) throw new BadRequestAlertException("exceptions.badRequest.invalidStatus");
         else {
            project.setStatus(status);
        }

        project = this.projectRepository.save(project);

        return project;
    }

    @Override
    public void deleteById(UUID id) throws ResourceNotFoundException {
        log.info("Deleting Project by id '" + id.toString() + "'");
        Project project = this.projectRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Project", "id", id.toString())
        );

            project.setStatus(EStatus.DELETED);
            this.projectRepository.save(project);

    }
}
