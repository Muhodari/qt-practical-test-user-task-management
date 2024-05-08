package rw.qt.userms.controllers;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.qt.userms.models.Task;
import rw.qt.userms.models.dtos.CreateTaskDTO;
import rw.qt.userms.models.dtos.MultipleIdsDTO;
import rw.qt.userms.models.dtos.UpdateTaskDTO;
import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.services.ITaskService;
import rw.qt.userms.utils.Constants;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;
import rw.qt.userms.models.domains.ApiResponse;
import rw.qt.userms.models.enums.EStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController extends BaseController {


    private final ITaskService taskService;



    @GetMapping
    public ResponseEntity<ApiResponse<Page<Task>>> searchAll(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "q",required = false) String query,
            @RequestParam(value = "status", required = false) EStatus status,
            @RequestParam(value = "priority", required = false) EPriority priority,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws ResourceNotFoundException {
        Pageable pageable = (Pageable) PageRequest.of(page-1, limit, Sort.Direction.DESC,"id");
        Page<Task> tasks = this.taskService.searchAll(query, status,priority, pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(tasks, localize("responses.getListSuccess"), HttpStatus.OK)
        );
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "q",required = false) String query,
            @RequestParam(value = "status", required = false) EStatus status,
            @RequestParam(value = "priority", required = false) EPriority priority,
            @RequestParam(value = "limit", defaultValue = Constants.DEFAULT_PAGE_SIZE) int limit) throws ResourceNotFoundException {
        Pageable pageable = (Pageable) PageRequest.of(page-1, limit, Sort.Direction.DESC,"id");
        ByteArrayResource stream = this.taskService.download(query, status,priority, pageable);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.xlsx");

        return new ResponseEntity<>(stream,
                headers, HttpStatus.CREATED);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Task>> getById(@Valid @PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        Task task = this.taskService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.getEntitySuccess"), HttpStatus.OK));
    }




    @PostMapping
    public ResponseEntity<ApiResponse<Task>> create(@Valid @RequestBody CreateTaskDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            Task task = this.taskService.create(dto);
            return new ResponseEntity<>(new ApiResponse<>(task, localize("responses.saveEntitySuccess"), HttpStatus.CREATED), HttpStatus.CREATED);
    }


    @PutMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Task>> updateById(@PathVariable(value = "id") UUID id, @Valid @RequestBody UpdateTaskDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
            Task task = this.taskService.updateById(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }



    @PutMapping(value = "/{id}/status/{status}")
    public ResponseEntity<ApiResponse<Task>> changeStatus(
            @PathVariable(value = "id") UUID id,
            @PathVariable(value = "status") EStatus status) throws ResourceNotFoundException {
        Task task = this.taskService.changeStatusById(id, status);
        return ResponseEntity.ok(
                new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK)
        );
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable(value = "id") UUID id) throws ResourceNotFoundException {
        this.taskService.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(null, localize("responses.deleteEntitySuccess"), null, HttpStatus.OK));
    }

    @PutMapping(path="/{id}/projects")
    public ResponseEntity<ApiResponse<Task>> addProjects(@PathVariable(value = "id") UUID id, @Valid @RequestBody MultipleIdsDTO projectsId) throws ResourceNotFoundException, DuplicateRecordException {
        Task task = this.taskService.addProjects(projectsId.getIds(), id);
        return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @DeleteMapping(path="/{id}/projects")
    public ResponseEntity<ApiResponse<Task>> removeProjects(@PathVariable(value = "id") UUID id, @Valid @RequestBody MultipleIdsDTO projectsId) throws ResourceNotFoundException {
        Task task = this.taskService.removeProjects(projectsId.getIds(), id);
        return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @PutMapping(path="/{id}/assignees")
    public ResponseEntity<ApiResponse<Task>> addAssignees(@PathVariable(value = "id") UUID id, @Valid @RequestBody MultipleIdsDTO assigneesId) throws ResourceNotFoundException, DuplicateRecordException {
        Task task = this.taskService.addAssignees(assigneesId.getIds(), id);
        return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }

    @DeleteMapping(path="/{id}/assignees")
    public ResponseEntity<ApiResponse<Task>> removeAssignees(@PathVariable(value = "id") UUID id, @Valid @RequestBody MultipleIdsDTO assigneesId) throws ResourceNotFoundException {
        Task task = this.taskService.removeAssignees(assigneesId.getIds(), id);
        return ResponseEntity.ok(new ApiResponse<>(task, localize("responses.updateEntitySuccess"), HttpStatus.OK));
    }


    @Override
    protected String getEntityName() {
        return "Task";
    }
}

