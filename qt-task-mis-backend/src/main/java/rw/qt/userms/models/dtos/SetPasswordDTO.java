package rw.qt.userms.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.qt.userms.security.ValidPassword;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SetPasswordDTO {
    @NotBlank
    @ValidPassword
    private String newPassword;
}
