package rw.qt.userms.security.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rw.qt.userms.models.UserAccount;
import rw.qt.userms.models.enums.EUserStatus;

import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
public class CustomUserDTO {
    private String fullNames;
    private UUID id;
    private String emailAddress;
    private UUID rankId;
    private EUserStatus status;

    public CustomUserDTO(UserAccount userAccount) {
        this.id = userAccount.getId();
        this.emailAddress = userAccount.getEmailAddress();
        this.status = userAccount.getStatus();

    }
}
