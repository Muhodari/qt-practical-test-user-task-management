package rw.qt.userms.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.qt.userms.models.enums.EPriority;
import rw.qt.userms.models.enums.EStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDTO {
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

    @NotNull
    private EStatus status;
}
