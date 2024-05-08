package rw.qt.userms.controllers;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.qt.userms.models.Project;
import rw.qt.userms.models.dtos.CreateProjectDTO;
import rw.qt.userms.utils.Constants;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.domains.ApiResponse;
import rw.qt.userms.models.enums.EStatus;
import rw.qt.userms.services.IProjectService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController extends BaseController {

    private final IProjectService projectService;


    @GetMapping(value = "/search")
    public ResponseEntity<ApiResponse<Page<Project>>> searchAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "q",required = false) String query,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit)
    {
        Pageable pageable = (Pageable) PageRequest.of(page-1, limit, Sort.Direction.DESC,"id");
        Page<Project> projects = this.projectService.searchAll(query, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(projects, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Project>> getById(@Valid @PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        Project project = this.projectService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(project, localize("responses.getEntitySuccess"), HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> create(@Valid @RequestBody CreateProjectDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            Project project = this.projectService.create(dto);
            return new ResponseEntity<>(new ApiResponse<>(project, localize("responses.saveEntitySuccess"), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Project>> updateById(@PathVariable(value = "id") UUID id, @Valid @RequestBody CreateProjectDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            Project project = this.projectService.updateById(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(project, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PutMapping(value = "/{id}/status/{status}")
    public ResponseEntity<ApiResponse<Project>> changeStatus(
            @PathVariable(value = "id") UUID id,
            @PathVariable(value = "status") EStatus status) throws ResourceNotFoundException {
        Project project = this.projectService.changeStatusById(id, status);
        return ResponseEntity.ok(
                new ApiResponse<>(project, localize("responses.updateEntitySuccess"), HttpStatus.OK)
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        this.projectService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(new Object(), localize("responses.deleteEntitySuccess"), null, HttpStatus.OK));
    }

    @Override
    protected String getEntityName() {
        return "Project";
    }
}

