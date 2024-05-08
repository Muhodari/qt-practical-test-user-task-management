package rw.qt.userms.models.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ForgotPasswordDTO {
    @NotBlank
    @Email
    private String emailAddress;
}
