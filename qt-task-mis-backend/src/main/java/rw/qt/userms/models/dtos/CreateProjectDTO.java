package rw.qt.userms.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
public class CreateProjectDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;
}

