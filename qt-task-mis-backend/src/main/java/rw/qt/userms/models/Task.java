package rw.qt.userms.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.qt.userms.models.dtos.CreateTaskDTO;
import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.models.enums.EStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="description", nullable=false)
    private String description;

    @Column(name="priority", nullable=false)
    private EPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private EStatus status = EStatus.ACTIVE;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "task_project",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<>();


    @ManyToMany()
    @JoinTable(
            name = "task_assignee",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserAccount> assignees = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attachement", referencedColumnName = "id")
    private File attachement;

    public Task(CreateTaskDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.priority = dto.getPriority();
        this.startDate = dto.getStartDate();
        this.endDate =  dto.getEndDate();
    }



}
