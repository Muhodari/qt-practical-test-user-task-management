package rw.qt.userms.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import rw.qt.userms.models.Task;

import rw.qt.userms.models.dtos.CreateTaskDTO;
import rw.qt.userms.models.dtos.UpdateTaskDTO;

import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.services.IProjectService;
import rw.qt.userms.services.ITaskService;
import rw.qt.userms.exceptions.BadRequestAlertException;
import rw.qt.userms.exceptions.DuplicateRecordException;
import rw.qt.userms.exceptions.ResourceNotFoundException;

import rw.qt.userms.models.enums.EStatus;
import rw.qt.userms.repositories.ITaskRepository;
import rw.qt.userms.services.IUserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class TaskServiceImpl implements ITaskService {

    private final ITaskRepository taskRepository;
    private final IProjectService projectService;
    private final IUserService userService;

    @Override
    public Page<Task> searchAll(String q, EStatus status, EPriority priority, Pageable pageable) throws ResourceNotFoundException {
        log.info("Search all Tasks by query");
       return this.taskRepository.searchWithStatusAndPriority(q,status,priority,pageable);
    }

    private ByteArrayResource generateExcel(List<Task> tasks) {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            Sheet sheet = workbook.createSheet("Entities");

            // Creating header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Task Name");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Status");
            headerRow.createCell(3).setCellValue("Priority");
            headerRow.createCell(4).setCellValue("Start Date");
            headerRow.createCell(5).setCellValue("End Date");

            // inserting data
            int rowIdx = 1;
            for (Task task : tasks) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(task.getName());
                row.createCell(1).setCellValue(task.getDescription());
                row.createCell(2).setCellValue(task.getStatus().toString());
                row.createCell(3).setCellValue(task.getPriority().toString());
                row.createCell(4).setCellValue(task.getStartDate().toString());
                row.createCell(5).setCellValue(task.getEndDate().toString());
            }

            workbook.write(out);
            workbook.close();
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }


    @Override
    public ByteArrayResource download(String q, EStatus status, EPriority priority, Pageable pageable) throws ResourceNotFoundException {
      List<Task> tasksToDownload = searchAll(q,status,priority,pageable).getContent();
        return generateExcel(tasksToDownload);
    }


    @Override
    public Task create(CreateTaskDTO dto) throws DuplicateRecordException, ResourceNotFoundException {
        log.info("Creating Task with details " + dto.toString());

        var task  = new Task(dto);
        this.taskRepository.save(task);

        if (dto.getProjectsId() != null) {
            this.addProjects(dto.getProjectsId(), task.getId());
        }

        if (dto.getAssigneesId() != null) {
            this.addAssignees(dto.getAssigneesId(), task.getId());
        }
        return task;

    }

    @Override
    public Task addProjects(List<UUID> projectsId, UUID taskID) throws ResourceNotFoundException, DuplicateRecordException {
        log.info("Adding Projects to Task with id " + taskID);

        var task = this.getById(taskID);

        // check if the project exists and add them to task
        for (UUID projectId : projectsId) {
            var project = this.projectService.getById(projectId);

            if(this.taskRepository.findByProjectsIdAndId(projectId, taskID).isPresent()){
                throw new DuplicateRecordException("Task","project",project.getName());
            }

            task.getProjects().add(project);
        }

        return this.taskRepository.save(task);
    }

    @Override
    public Task removeProjects(List<UUID> projectsId, UUID taskID) throws ResourceNotFoundException {
        log.info("Removing Projects to Task with id " + taskID);

        var task = this.getById(taskID);

        // check if the project exists and remove them to task
        for (UUID projectId : projectsId) {
            var project = this.projectService.getById(projectId);

            if(this.taskRepository.findByProjectsIdAndId(projectId, taskID).isPresent()){
                task.getProjects().remove(project);
            }

        }

        return this.taskRepository.save(task);
    }

    @Override
    public Task addAssignees(List<UUID> assigneesId, UUID taskID) throws ResourceNotFoundException, DuplicateRecordException {
        log.info("Adding Assignees to Task with id " + taskID);

        var task = this.getById(taskID);

        // check if the assignee exists and add them to task
        for (UUID assigneeId : assigneesId) {
            var assignee = this.userService.getById(assigneeId);

            if(this.taskRepository.findByAssigneesIdAndId(assigneeId, taskID).isPresent()){
                throw new DuplicateRecordException("Task ", "assigne", assignee.getFullName());
            }

            task.getAssignees().add(assignee);
        }

        return this.taskRepository.save(task);
    }


    @Override
    public Task removeAssignees(List<UUID> assigneesId, UUID taskID) throws ResourceNotFoundException {
        log.info("Removing Assignees to Task with id " + taskID);

        var task = this.getById(taskID);

        // check if the assignee exists and remove them to task
        for (UUID assigneeId : assigneesId) {
            var assignee = this.userService.getById(assigneeId);

            if(this.taskRepository.findByAssigneesIdAndId(assigneeId, taskID).isPresent()){
                task.getAssignees().remove(assignee);
            }

        }

        return this.taskRepository.save(task);
    }

    @Override
    public Task getById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching Task by id '" + id.toString() + "'");
        return getTask(id);
    }


    private Task getTask(UUID id) throws ResourceNotFoundException {
        return this.taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task", "id", id.toString()));
    }


    @Override
    public Task updateById(UUID id, UpdateTaskDTO dto) throws ResourceNotFoundException, DuplicateRecordException {
        log.info("Updating Rank by id '" + id.toString() + "'");
        Task task = this.taskRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task", "id", id.toString())
        );

        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task = this.taskRepository.save(task);
        return task;
    }

    @Override
    public Task changeStatusById(UUID id, EStatus status) throws ResourceNotFoundException {
        Task task = this.getById(id);

        if (status == EStatus.DELETED) { throw new  BadRequestAlertException("Status not allowed");}
        task.setStatus(status);
        task = this.taskRepository.save(task);

        return task;
    }

    @Override
    public void deleteById(UUID id) throws ResourceNotFoundException {
        log.info("Deleting Rank by id '" + id.toString() + "'");
        Task task = this.taskRepository.findById(id)  .orElseThrow(
                () -> new ResourceNotFoundException("Task", "id", id.toString())
        );

        task.setStatus(EStatus.DELETED);
        this.taskRepository.save(task);
    }

}
