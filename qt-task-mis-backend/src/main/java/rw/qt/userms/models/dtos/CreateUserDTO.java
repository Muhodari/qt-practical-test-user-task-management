package rw.qt.userms.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import rw.qt.userms.models.enums.EGender;
import rw.qt.userms.security.ValidPassword;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CreateUserDTO {
    @NotBlank
    @Length(min = 2)
    private String firstName;

    @NotBlank
    @Length(min = 2)
    private String lastName;

    @NotNull
    private EGender gender;

    @Email
    private String emailAddress;

    @NotNull
    @Pattern(regexp = "\\+250\\d{9}")
    private String phoneNumber;


    @NotNull
    @ValidPassword
    private String password;

}
