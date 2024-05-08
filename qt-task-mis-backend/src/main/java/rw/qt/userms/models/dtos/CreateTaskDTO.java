package rw.qt.userms.models.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.models.enums.EStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;


    @NotNull
    private EPriority priority;


    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private List<UUID> projectsId;

    private List<UUID> assigneesId;
}
