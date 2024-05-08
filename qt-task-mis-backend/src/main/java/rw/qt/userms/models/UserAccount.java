package rw.qt.userms.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import rw.qt.userms.models.dtos.CreateUserDTO;
import rw.qt.userms.models.enums.EGender;
import rw.qt.userms.models.enums.EOTPStatus;
import rw.qt.userms.models.enums.EUserStatus;

import java.time.LocalDateTime;
import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;

    @Column(name="email_address", nullable = false)
    private String emailAddress;


    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="password", nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private EGender gender;


    @ManyToMany(mappedBy = "assignees" ,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> tasks;

    @JsonIgnore
    @Transient
    private Collection<GrantedAuthority> authorities;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private EUserStatus status = EUserStatus.PENDING;

    private String fullName;

    private boolean deletedFlag;


    private String credentialsExpiryDate;

    private boolean isAccountExpired;

    private boolean isCredentialsExpired;

    private boolean isAccountEnabled;

    private boolean isAccountLocked;

    @JsonIgnore
    private String otp;

    @JsonIgnore
    private LocalDateTime otpExpiryDate;

    @Enumerated(EnumType.STRING)
    private EOTPStatus otpStatus;

    @Column(name="last_login")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;

    @Column(name="login_failure_account")
    private Long loginFailureCount;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    @Column()
    private LocalDateTime updatedAt;

    @Column()
    @CreatedBy
    private String createdBy;

    @Column()
    @LastModifiedBy
    private String modifiedBy;


    public UserAccount(String emailAddress, String phoneNumber, String password) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;


    }
    public UserAccount(String emailAddress, String phoneNumber, String password,  EUserStatus status) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
    }

    public UserAccount(CreateUserDTO dto) {
        this.emailAddress = dto.getEmailAddress();
        this.phoneNumber = dto.getPhoneNumber();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.gender = dto.getGender();
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
